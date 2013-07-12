/*
 * @(#)PersistentCookieStore.java		       Project:androidkit
 * Date:2013-5-9
 *
 * Copyright (c) 2013 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lurencun.cfuture09.androidkit.http.async;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import com.lurencun.cfuture09.androidkit.utils.lang.StringUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class PersistentCookieStore implements CookieStore {
	private static final String COOKIE_PREFS = "CookiePrefsFile";
	private static final String COOKIE_NAME_STORE = "names";
	private static final String COOKIE_NAME_PREFIX = "cookie_";

	private final ConcurrentHashMap<String, Cookie> cookies;
	private final SharedPreferences cookiePrefs;

	/**
	 * Construct a persistent cookie store.
	 */
	public PersistentCookieStore(Context context) {
		cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
		cookies = new ConcurrentHashMap<String, Cookie>();

		// Load any previously stored cookies into the store
		String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
		if (storedCookieNames != null) {
			String[] cookieNames = TextUtils.split(storedCookieNames, ",");
			for (String name : cookieNames) {
				String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
				if (encodedCookie != null) {
					Cookie decodedCookie = decodeCookie(encodedCookie);
					if (decodedCookie != null) {
						cookies.put(name, decodedCookie);
					}
				}
			}

			// Clear out expired cookies
			clearExpired(new Date());
		}
	}

	@Override
	public void addCookie(Cookie cookie) {
		String name = cookie.getName() + cookie.getDomain();

		// Save cookie into local store, or remove if expired
		if (cookie.isExpired(new Date())) {
			cookies.remove(name);
		} else {
			cookies.put(name, cookie);
		}

		// Save cookie into persistent store
		SharedPreferences.Editor editor = cookiePrefs.edit();
		editor.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
		editor.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
		editor.commit();
	}

	@Override
	public void clear() {
		// Clear cookies from persistent store
		SharedPreferences.Editor editor = cookiePrefs.edit();
		for (String name : cookies.keySet()) {
			editor.remove(COOKIE_NAME_PREFIX + name);
		}
		editor.remove(COOKIE_NAME_STORE);
		editor.commit();

		// Clear cookies from local store
		cookies.clear();
	}

	@Override
	public boolean clearExpired(Date date) {
		boolean clearedAny = false;
		SharedPreferences.Editor editor = cookiePrefs.edit();

		for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			Cookie cookie = entry.getValue();
			if (cookie.isExpired(date)) {
				// Clear cookies from local store
				cookies.remove(name);

				// Clear cookies from persistent store
				editor.remove(COOKIE_NAME_PREFIX + name);

				// We've cleared at least one
				clearedAny = true;
			}
		}

		// Update names in persistent store
		if (clearedAny) {
			editor.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
		}
		editor.commit();

		return clearedAny;
	}

	@Override
	public List<Cookie> getCookies() {
		return new ArrayList<Cookie>(cookies.values());
	}

	//
	// Cookie serialization/deserialization
	//

	protected String encodeCookie(SerializableCookie cookie) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(os);
			outputStream.writeObject(cookie);
		} catch (Exception e) {
			return null;
		}

		return StringUtil.byteArrayToHexString(os.toByteArray());
	}

	protected Cookie decodeCookie(String cookieStr) {
		byte[] bytes = StringUtil.hexStringToByteArray(cookieStr);
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Cookie cookie = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			cookie = ((SerializableCookie) ois.readObject()).getCookie();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cookie;
	}
}
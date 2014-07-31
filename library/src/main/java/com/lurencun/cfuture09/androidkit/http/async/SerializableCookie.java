/*
 * @(#)SerializableCookie.java		       Project:androidkit
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

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class SerializableCookie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7125455124417206207L;
	private transient final Cookie cookie;
	private transient BasicClientCookie clientCookie;

	public SerializableCookie(Cookie cookie) {
		this.cookie = cookie;
	}

	public Cookie getCookie() {
		return (clientCookie == null) ? cookie : clientCookie;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(cookie.getName());
		out.writeObject(cookie.getValue());
		out.writeObject(cookie.getComment());
		out.writeObject(cookie.getDomain());
		out.writeObject(cookie.getExpiryDate());
		out.writeObject(cookie.getPath());
		out.writeInt(cookie.getVersion());
		out.writeBoolean(cookie.isSecure());
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String name = (String) in.readObject();
		String value = (String) in.readObject();
		clientCookie = new BasicClientCookie(name, value);
		clientCookie.setComment((String) in.readObject());
		clientCookie.setDomain((String) in.readObject());
		clientCookie.setExpiryDate((Date) in.readObject());
		clientCookie.setPath((String) in.readObject());
		clientCookie.setVersion(in.readInt());
		clientCookie.setSecure(in.readBoolean());
	}
}
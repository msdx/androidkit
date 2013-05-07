/*
 * @(#)Lg.java		       Project:androidkit
 * Date:2013-5-6
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
package com.lurencun.cfuture09.androidkit.utils.lang;

import java.util.HashMap;

import com.lurencun.cfuture09.androidkit.Version;

import android.util.Log;

/**
 * 打印日志类，该类封装了tag。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XW9vbmxuZG5qamQdLCxzPjIw"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public final class Log4AK {
	public static final int ASSERT = Log.ASSERT;
	public static final int DEBUG = Log.DEBUG;
	public static final int ERROR = Log.ERROR;
	public static final int INFO = Log.INFO;
	public static final int VERBOSE = Log.VERBOSE;
	public static final int WARN = Log.WARN;
	private static final HashMap<Class<?>, Log4AK> logs = new HashMap<Class<?>, Log4AK>();
	private final String tag;

	private Log4AK(Class<?> clazz) {
		tag = String.format("androidkit--v%s: %s", Version.getVersion(), clazz.getSimpleName());
	}

	public static final Log4AK getLog(Class<?> clazz) {
		Log4AK log = logs.get(clazz);
		if (log == null) {
			log = new Log4AK(clazz);
			logs.put(clazz, log);
		}
		return log;
	}

	public void d(String msg) {
		Log.d(tag, msg);
	}

	public void d(String msg, Throwable tr) {
		Log.d(tag, msg, tr);
	}

	public void e(String msg) {
		Log.e(tag, msg);
	}

	public void e(String msg, Throwable tr) {
		Log.e(tag, msg, tr);
	}

	public void i(String msg) {
		Log.i(tag, msg);
	}

	public void i(String msg, Throwable tr) {
		Log.i(tag, msg, tr);
	}

	public void v(String msg) {
		Log.v(tag, msg);
	}

	public void v(String msg, Throwable tr) {
		Log.v(tag, msg, tr);
	}

	public void w(String msg) {
		Log.w(tag, msg);
	}

	public void w(Throwable tr) {
		Log.w(tag, tr);
	}

	public void w(String msg, Throwable tr) {
		Log.w(tag, msg, tr);
	}

	public void getStackTraceString(Throwable tr) {
		Log.getStackTraceString(tr);
	}

	public void isLoggable(int level) {
		Log.isLoggable(tag, level);
	}

	public void println(int priority, String msg) {
		Log.println(priority, tag, msg);
	}

	public static void clearLog() {
		logs.clear();
	}
}

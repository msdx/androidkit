/*
 * @(#)LogAuto.java		       Project:androidkit
 * Date:2013-9-9
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

import android.util.Log;

import com.lurencun.cfuture09.androidkit.Version;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class LogAuto {
	public static final int ASSERT = Log.ASSERT;
	public static final int DEBUG = Log.DEBUG;
	public static final int ERROR = Log.ERROR;
	public static final int INFO = Log.INFO;
	public static final int VERBOSE = Log.VERBOSE;
	public static final int WARN = Log.WARN;
	protected final String TAG;

	protected LogAuto(Class<?> clazz) {
		TAG = initTag(clazz);
	}

	protected String initTag(Class<?> clazz) {
		return String.format("%s: %s", Version.ANDROIDKIT_NAME, clazz.getSimpleName());
	}

	public static  LogAuto getLog(Class<?> clazz) {
		return new LogAuto(clazz);
	}

	public void d(String msg) {
		Log.d(TAG, msg);
	}

	public void d(String msg, Throwable tr) {
		Log.d(TAG, msg, tr);
	}

	public void e(String msg) {
		Log.e(TAG, msg);
	}

	public void e(String msg, Throwable tr) {
		Log.e(TAG, msg, tr);
	}

	public void i(String msg) {
		Log.i(TAG, msg);
	}

	public void i(String msg, Throwable tr) {
		Log.i(TAG, msg, tr);
	}

	public void v(String msg) {
		Log.v(TAG, msg);
	}

	public void v(String msg, Throwable tr) {
		Log.v(TAG, msg, tr);
	}

	public void w(String msg) {
		Log.w(TAG, msg);
	}

	public void w(Throwable tr) {
		Log.w(TAG, tr);
	}

	public void w(String msg, Throwable tr) {
		Log.w(TAG, msg, tr);
	}

	public void getStackTraceString(Throwable tr) {
		Log.getStackTraceString(tr);
	}

	public void isLoggable(int level) {
		Log.isLoggable(TAG, level);
	}

	public void println(int priority, String msg) {
		Log.println(priority, TAG, msg);
	}

}

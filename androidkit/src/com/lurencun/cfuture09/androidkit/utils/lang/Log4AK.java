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

import com.githang.androidkit.Version;

/**
 * 打印日志类，该类封装了tag。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XW9vbmxuZG5qamQdLCxzPjIw"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public final class Log4AK extends LogAuto {

	private Log4AK(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected String initTag(Class<?> clazz) {
		return String.format("%s: %s", Version.ANDROIDKIT_NAME, clazz.getSimpleName());
	}

	public static final Log4AK getLog(Class<?> clazz) {
		return new Log4AK(clazz);
	}
}

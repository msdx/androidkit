/*
 * @(#)AKBind.java		       Project:androidkit
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
package com.lurencun.cfuture09.androidkit;

import android.app.Activity;
import android.view.View;

import com.lurencun.cfuture09.androidkit.uibind.ResBindUtil;
import com.lurencun.cfuture09.androidkit.uibind.UIBindUtil;

/**
 * 进行控件、事件及资源的绑定。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XW9vbmxuZG5qamQdLCxzPjIw"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public final class AKBind {

	/**
	 * 绑定activity的控件、事件及资源。
	 * 
	 * @param activity
	 *            要绑定的activity对象。
	 */

	public static void bind(Activity activity) {
		UIBindUtil.bind(activity);
		ResBindUtil.bindAllRes(activity);
	}

	/**
	 * 绑定activity的控件、事件及资源。
	 * 
	 * @param activity
	 *            要绑定的activity对象
	 * @param layoutResID
	 *            activity的布局id
	 */
	public static void bind(Activity activity, int layoutResID) {
		UIBindUtil.bind(activity, layoutResID);
		ResBindUtil.bindAllRes(activity);
	}

	/**
	 * 绑定view的控件、事件。
	 * 
	 * @param rootView
	 *            要进行绑定的控件所属的view
	 * @param object
	 *            要进行绑定的成员变量所属的对象
	 */
	public static void bindView(View rootView, Object object) {
		UIBindUtil.bind(rootView, object);
	}
}

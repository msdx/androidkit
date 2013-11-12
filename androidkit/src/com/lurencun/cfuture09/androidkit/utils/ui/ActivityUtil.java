/*
 * @(#)ActivityUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-17
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
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
package com.lurencun.cfuture09.androidkit.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Window;
import android.view.WindowManager;

/**
 * Activity工具类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ActivityUtil {

	/**
	 * 返回当前屏幕是否为竖屏。
	 * 
	 * @param context
	 * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 */
	public static boolean isOriatationPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * 隐藏输入法。
	 * 
	 * @param activity
	 */
	public static void hideInputMethod(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 设置全屏
	 * 
	 * @param activity
	 *            要设置全屏的activity
	 */
	public static void setFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		window.setAttributes(params);
		window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 隐藏标题
	 * 
	 * @param activity
	 *            要隐藏标题的activity
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 设置activity屏幕为垂直方向。
	 * 
	 * @param activity
	 *            Activity对象。
	 */
	public static void setScreenPortrait(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 设置activity屏幕为水平方向
	 * 
	 * @param activity
	 *            Activity对象。
	 */
	public static void setScreenLandscape(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}

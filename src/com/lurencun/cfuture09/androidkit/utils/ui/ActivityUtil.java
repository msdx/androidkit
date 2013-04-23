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
import android.content.res.Configuration;
import android.view.inputmethod.InputMethodManager;

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
	public static boolean isScreenOriatationPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * 隐藏输入法。
	 * 
	 * @param activity
	 */
	public static void hideInputMethod(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}

/*
 * @(#)ApplicationUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-13
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
package com.lurencun.cfuture09.androidkit.utils.apk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 应用工具类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApplicationUtil {

	/**
	 * 通过包名获取应用程序的名称。
	 * 
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
					pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取虚拟机的最大的可用内存
	 * 
	 * @return 虚拟机最大的可用内存。
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
}

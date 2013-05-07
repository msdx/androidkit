/*
 * @(#)ResBindUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-11-18
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
package com.lurencun.cfuture09.androidkit.uibind;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;

import com.lurencun.cfuture09.androidkit.uibind.annotation.AndroidRes;
import com.lurencun.cfuture09.androidkit.uibind.annotation.AndroidRes.ResType;
import com.lurencun.cfuture09.androidkit.utils.lang.L;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ResBindUtil {

	/**
	 * 绑定资源。
	 * 
	 * @param context
	 */
	public static void bindAllRes(Context context) {
		Resources reses = context.getResources();
		Class<?> cl = context.getClass();
		Field[] fields = cl.getDeclaredFields();
		try {
			for (Field field : fields) {
				AndroidRes ar = field.getAnnotation(AndroidRes.class);
				if (ar != null) {
					field.setAccessible(true);
					ResType type = ar.type();
					int id = ar.id();
					if (type == ResType.BOOLEAN) {
						field.set(context, reses.getBoolean(id));
					} else if (type == ResType.COLOR) {
						field.set(context, reses.getColor(id));
					} else if (type == ResType.DRAWABLE) {
						field.set(context, reses.getDrawable(id));
					} else if (type == ResType.INT) {
						field.set(context, reses.getInteger(id));
					} else if (type == ResType.INT_ARRAY) {
						field.set(context, reses.getIntArray(id));
					} else if (type == ResType.STRING) {
						field.set(context, reses.getString(id));
					} else if (type == ResType.STRING_ARRAY) {
						field.set(context, reses.getStringArray(id));
					} else if (type == ResType.TEXT) {
						field.set(context, reses.getText(id));
					} else if (type == ResType.TEXT_ARRAY) {
						field.set(context, ResType.TEXT_ARRAY);
					}
				}
			}
		} catch (Exception e) {
			L.getLog(ResBindUtil.class).w(e);
		}
	}
}

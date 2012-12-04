/*
 * @(#)UIBindUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-11-15
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
package com.sinaapp.msdxblog.androidkit.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.sinaapp.msdxblog.androidkit.ui.annotation.AndroidView;
import com.sinaapp.msdxblog.androidkit.ui.annotation.OnClick;
import com.sinaapp.msdxblog.androidkit.ui.annotation.OnItemSelect;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class UIBindUtil {

	/**
	 * 绑定添加了注解的控件，及其事件方法。<br/>
	 * 注意：它必须在activity调用了setContentView之后调用。
	 * 
	 * @param activity
	 */
	public static void bind(Activity activity) {
		Class<?> cl = activity.getClass();
		bindViews(activity, cl);
		bindMethods(activity, cl);
	}

	/**
	 * 根据成员方法的注解进行绑定
	 * 
	 * @param activity
	 * @param cl
	 */
	private static void bindMethods(Activity activity, Class<?> cl) {
		for (Method method : cl.getDeclaredMethods()) {
			OnClick oc = method.getAnnotation(OnClick.class);
			if (oc != null) {
				OnEventListener listener = new OnEventListener(activity)
						.setmClick(method.getName());
				int ids[] = oc.viewId();
				for (int id : ids) {
					activity.findViewById(id).setOnClickListener(listener);
				}
			}
		}
	}

	/**
	 * 根据成员变量的注解进行绑定
	 * 
	 * @param activity
	 * @param cl
	 * @throws IllegalAccessException
	 */
	private static void bindViews(Activity activity, Class<?> cl) {
		try {
			for (Field field : cl.getDeclaredFields()) {
				AndroidView av = field.getAnnotation(AndroidView.class);
				if (av != null) {
					field.setAccessible(true);
					setView(field, activity, av.id());
					setEventListener(activity, field, av);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 该方法用来代替Activity.setContentView(layoutResID)的调用，并自动绑定控件及事件。
	 * 
	 * @param activity
	 *            进行绑定的Activity。
	 * @param layoutResID
	 *            Activity的布局资源的ID。
	 */
	public static void bind(Activity activity, int layoutResID) {
		activity.setContentView(layoutResID);
		bind(activity);
	}

	/**
	 * 设置事件的监听器。
	 * 
	 * @param context
	 *            context对象。
	 * @param field
	 *            要设置的控件。
	 * @param av
	 *            AndroidView注解信息。
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void setEventListener(Context context, Field field,
			AndroidView av) throws IllegalArgumentException,
			IllegalAccessException {
		Object o = field.get(context);

		String click = av.onClick();
		if (!TextUtils.isEmpty(click) && o instanceof View) {
			((View) o).setOnClickListener(new OnEventListener(context)
					.setmClick(click));
		}

		String createContextMenu = av.onCreateContextMenu();
		if (!TextUtils.isEmpty(createContextMenu) && o instanceof View) {
			((View) o).setOnCreateContextMenuListener(new OnEventListener(
					context).setmCreateContextMenu(createContextMenu));
		}

		String focusChange = av.onFocusChange();
		if (!TextUtils.isEmpty(focusChange) && o instanceof View) {
			((View) o).setOnFocusChangeListener(new OnEventListener(context)
					.setmFocusChange(focusChange));
		}

		String itemClick = av.onItemClick();
		if (!TextUtils.isEmpty(itemClick) && o instanceof AdapterView<?>) {
			((AdapterView<?>) o).setOnItemClickListener(new OnEventListener(
					context).setmItemClick(itemClick));
		}

		String itemLongClick = av.onItemLongClick();
		if (!TextUtils.isEmpty(itemLongClick) && o instanceof AdapterView<?>) {
			((AdapterView<?>) o)
					.setOnItemLongClickListener(new OnEventListener(context)
							.setmItemLongClick(itemLongClick));
		}

		OnItemSelect itemSelect = av.onItemSelect();
		if (!TextUtils.isEmpty(itemSelect.onItemSelected())
				&& o instanceof AdapterView<?>) {
			((AdapterView<?>) o).setOnItemSelectedListener(new OnEventListener(
					context).setmItemSelected(itemSelect.onItemSelected())
					.setmNothingSelected(itemSelect.onNothingSelected()));
		}

		String key = av.onKey();
		if (!TextUtils.isEmpty(key) && o instanceof View) {
			((View) o).setOnKeyListener(new OnEventListener(context)
					.setmKey(key));
		}

		String longClick = av.onLongClick();
		if (!TextUtils.isEmpty(longClick) && o instanceof View) {
			((View) o).setOnLongClickListener(new OnEventListener(context)
					.setmLongClick(longClick));
		}

		String touch = av.onTouch();
		if (!TextUtils.isEmpty(touch) && o instanceof View) {
			((View) o).setOnTouchListener(new OnEventListener(context)
					.setmTouth(touch));
		}
	}

	/**
	 * 对控件赋值。
	 * 
	 * @param view
	 *            要进行赋值的控件。
	 * @param activity
	 *            activity对象。
	 * @param id
	 *            控件的ID。
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static void setView(Field view, Activity activity, int id)
			throws IllegalArgumentException, IllegalAccessException {
		view.set(activity, activity.findViewById(id));
	}
}

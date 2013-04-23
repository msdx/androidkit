/*
 * @(#)AndroidView.java		       Project:com.sinaapp.msdxblog.androidkit
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
package com.lurencun.cfuture09.androidkit.uibind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * android控件
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AndroidView {

	/**
	 * @return 控件ID
	 */
	public int id();

	/**
	 * @return onClickListener的回调方法名
	 */
	public String onClick() default "";

	/**
	 * @return onCreateContextMenu的回调方法名
	 */
	public String onCreateContextMenu() default "";

	/**
	 * @return onFocusChange的回调方法名
	 */
	public String onFocusChange() default "";

	/**
	 * @return onKey的回调方法名
	 */
	public String onKey() default "";

	/**
	 * @return onLongClick的回调方法名
	 */
	public String onLongClick() default "";

	/**
	 * @return onTouch的回调方法名
	 */
	public String onTouch() default "";

	/**
	 * @return onItemClick的回调方法名
	 */
	public String onItemClick() default "";

	/**
	 * @return onItemLongClick的回调方法名
	 */
	public String onItemLongClick() default "";

	/**
	 * @return onItemSelected的回调方法名
	 */
	public OnItemSelect onItemSelect() default @OnItemSelect(onItemSelected = "");
}

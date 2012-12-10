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
package com.lurencun.cfuture09.androidkit.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AndroidView {

	public int id();
	public String onClick() default "";
	public String onCreateContextMenu() default "";
	public String onFocusChange() default "";
	public String onKey() default "";
	public String onLongClick() default "";
	public String onTouch() default "";
	public String onItemClick() default "";
	public String onItemLongClick() default "";
	public OnItemSelect onItemSelect() default @OnItemSelect(onItemSelected="");
}

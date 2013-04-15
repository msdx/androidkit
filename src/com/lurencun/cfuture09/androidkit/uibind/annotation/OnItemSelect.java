/*
 * @(#)OnItemSelected.java		       Project:com.sinaapp.msdxblog.androidkit
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

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public @interface OnItemSelect {

	public String onItemSelected() ;
	public String onNothingSelected() default "";
}

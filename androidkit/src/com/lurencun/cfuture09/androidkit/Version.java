/*
 * @(#)Version.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-7-12
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
package com.lurencun.cfuture09.androidkit;


/**
 * 版本号。该类记录本框架发布的版本号。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public interface Version {
	/**
	 * 主版本号。
	 */
	int MAJOR_VERSION = 1;
	/**
	 * 次版本号。
	 */
	int MINOR_VERSION = 2;
	/**
	 * 修正版本号。
	 */
	int REVISION_VERSION = 1;
	/**
	 * 版本后缀。
	 */
	String VERSION_SUFFIX = " build-66";
	/**
	 * Androdi的版本号。
	 */
	String ANDROIDKIT_VERSION = String.format("%d.%d.%d%s", MAJOR_VERSION, MINOR_VERSION,
			REVISION_VERSION, VERSION_SUFFIX);
	/**
	 * 框架名及版本。
	 */
	String ANDROIDKIT_NAME = "androidkit v" + ANDROIDKIT_VERSION;
}

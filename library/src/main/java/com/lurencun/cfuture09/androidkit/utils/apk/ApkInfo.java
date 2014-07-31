/*
 * @(#)ApkInfo.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-4-1
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.utils.apk;

import android.graphics.drawable.Drawable;

/**
 * apk信息实体类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApkInfo {
	/**
	 * 包名
	 */
	public String packageName;
	/**
	 * 图标的资源ID
	 */
	public int iconId;
	/**
	 * 图标
	 */
	public Drawable iconDrawable;
	/**
	 * 程序名
	 */
	public String programName;
	/**
	 * 版本号
	 */
	public int versionCode;
	/**
	 * 版本名
	 */
	public String versionName;

	@Override
	public String toString() {
		return "ApkInfo [packageName=" + packageName + ", iconId=" + iconId + ", iconDrawable="
				+ iconDrawable + ", programName=" + programName + ", versionCode=" + versionCode
				+ ", versionName=" + versionName + "]";
	}
}

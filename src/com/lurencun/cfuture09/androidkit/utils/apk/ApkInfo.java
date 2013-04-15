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
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApkInfo {
	public String packageName;
	public int iconId;
	public Drawable iconDrawable;
	public String programName;
	public int versionCode;
	public String versionName;

	@Override
	public String toString() {
		return "ApkInfo [packageName=" + packageName + ", iconId=" + iconId
				+ ", iconDrawable=" + iconDrawable + ", programName="
				+ programName + ", versionCode=" + versionCode
				+ ", versionName=" + versionName + "]";
	}
}

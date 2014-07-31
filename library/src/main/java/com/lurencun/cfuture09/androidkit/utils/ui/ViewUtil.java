/*
 * @(#)ViewUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-3-17
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.utils.ui;

import android.graphics.Bitmap;
import android.view.View;

/**
 * View工具类
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ViewUtil {
	/**
	 * 截图
	 * 
	 * @param v
	 *            需要进行截图的控件
	 * @return 该控件截图的Bitmap对象。
	 */
	public static Bitmap captureView(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}
}

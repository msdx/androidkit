/*
 * @(#)ViewUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-3-17
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.utils.ui;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.view.View;

/**
 * View工具类
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ViewUtil {
	/**
	 * 对控件截图。
	 * 
	 * @param v
	 *            需要进行截图的控件。
	 * @param quality
	 *            图片的质量
	 * @return 该控件截图的byte数组对象。
	 */
	public static byte[] printScreen(View v, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);
		Bitmap bitmap = v.getDrawingCache();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * 截图
	 * 
	 * @param v
	 *            需要进行截图的控件
	 * @return 该控件截图的Bitmap对象。
	 */
	public static Bitmap printScreen(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}
}

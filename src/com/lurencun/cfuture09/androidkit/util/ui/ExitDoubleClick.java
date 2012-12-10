/*
 * @(#)ExitDoubleClick.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-3-20
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.util.ui;

import android.app.Activity;
import android.content.Context;

/**
 * 双击退出Activity的类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ExitDoubleClick extends DoubleClick {

	private static ExitDoubleClick exitDoubleClick;

	private ExitDoubleClick(Context context) {
		super(context);
	}

	/**
	 * 返回一个双击退出的实例。
	 * 
	 * @param context
	 * @return ExitDoubleClick
	 */
	public static synchronized ExitDoubleClick getInstance(Context context) {
		if (exitDoubleClick == null) {
			exitDoubleClick = new ExitDoubleClick(context);
		}
		return exitDoubleClick;
	}

	/**
	 * 双击之后退出。
	 */
	@Override
	protected void doOnDoubleClick() {
		((Activity) mContext).finish();
		destroy();
	}

	/**
	 * 双击退出Activity，如果msg为null，而默认显示的提示语为"再按一次退出"。
	 */
	@Override
	public void doDoubleClick(int delayTime, String msg) {
		if (msg == null || msg.equals("")) {
			msg = "再按一次退出";
		}
		super.doDoubleClick(delayTime, msg);
	}

	private static void destroy() {
		exitDoubleClick = null;
	}
}

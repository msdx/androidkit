/*
 * @(#)HandlerFactory.java		       version: 0.1 
 * Date:2012-2-11
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class HandlerFactory {

	private HandlerFactory() {

	}

	/**
	 * 获取在新线程运行的Handler对象。
	 * 
	 * @param name
	 *            线程名字。
	 * @return 在新线程运行的Handler对象。
	 */
	public static Handler getNewHandlerInOtherThread(String name) {
		HandlerThread thread = new HandlerThread(name);
		thread.start();
		return new Handler(thread.getLooper());
	}

	/**
	 * 获取在新线程中运行的Looper对象。
	 * 
	 * @param name
	 *            线程名字。
	 * @return 在新线程中运行的Looper对象。
	 */
	public static Looper getHandlerLooperInOtherThread(String name) {
		HandlerThread thread = new HandlerThread(name);
		thread.start();
		return thread.getLooper();
	}
}

/*
 * @(#)HandlerFactory.java		       version: 0.1 
 * Date:2012-2-11
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.utils.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.lurencun.cfuture09.androidkit.utils.lang.IdGenerator;
import com.lurencun.cfuture09.androidkit.utils.lang.IncreaseIntId;

/**
 * 线程工厂类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class HandlerFactory {
	private static IdGenerator<Integer> id = new IncreaseIntId();

	private HandlerFactory() {

	}

	/**
	 * 获取新的后台Handler对象。
	 * 
	 * @param name
	 *            线程名字。
	 * @return 在新线程运行的Handler对象。
	 */
	public static Handler newBackgroundHandler(String name) {
		HandlerThread thread = new HandlerThread(name);
		thread.start();
		return new Handler(thread.getLooper());
	}

	/**
	 * 获取新的后台运行的Handler对象。
	 * 
	 * @return 新的后台运行的Handler对象。
	 */
	public static Handler newBackgroundHandler() {
		return newBackgroundHandler(Integer.toString(id.next()));
	}

	/**
	 * 获取新的后台Looper对象。
	 * 
	 * @param name
	 *            线程名字。
	 * @return 在新线程中运行的Looper对象。
	 */
	public static Looper newBackgroundLooper(String name) {
		HandlerThread thread = new HandlerThread(name);
		thread.start();
		return thread.getLooper();
	}

	/**
	 * 获取新的后台运行的Looper对象。
	 * 
	 * @return 新的后台运行的Looper对象。
	 */
	public static Looper newBackgroundLooper() {
		return newBackgroundLooper(Integer.toString(id.next()));
	}
}

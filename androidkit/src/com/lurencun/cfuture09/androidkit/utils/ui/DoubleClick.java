/*
 * @(#)DoubleClick.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-3-20
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.utils.ui;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 双击抽象类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public abstract class DoubleClick {
	protected Context mContext;
	/**
	 * 开始任务的时间。
	 */
	private long mStartTime;

	private DoubleClickListener doubleClickListener;
	private Toast mToast;

	/**
	 * 构造方法，初始化Context对象及开始的任务时间。
	 * 
	 * @param context
	 */
	public DoubleClick(Context context) {
		mContext = context;
		mToast = new Toast(context); 

		LinearLayout layout = new LinearLayout(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setBackgroundResource(android.R.drawable.toast_frame);
		layout.setLayoutParams(layoutParams);
        TextView tv = new TextView(context);
        tv.setId(android.R.id.message);
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.weight = 1;
        tv.setShadowLayer(2.75f, 1.3f, 1.3f, 0xBB000000);
        layout.addView(tv);
		mToast.setView(layout);
		
		mStartTime = -1;
	}

	protected void resetStartTime() {
		mStartTime = -1;
	}

	/**
	 * 当某个动作要双击才执行时，调用此方法。
	 * 
	 * @param delayTime
	 *            判断双击的时间。
	 * @param msg
	 *            当第一次点击时，弹出的提示信息。如果为null，则不作提示。
	 */
	public void doDoubleClick(int delayTime, String msg) {
		if (!doInDelayTime(delayTime)) {
			mToast.setDuration(delayTime);
			mToast.setText(msg);
			mToast.show();
		}
	}

	/**
	 * 如果是在指定的时间内则执行doOnDoubleClick，否则返回false。
	 * 
	 * @param delayTime
	 *            指定的延迟时间。
	 * @return 当且仅当在指定的时间内时返回true,否则返回false。
	 */
	protected boolean doInDelayTime(int delayTime) {
		long nowTime = System.currentTimeMillis();
		if (nowTime - mStartTime <= delayTime) {
			if (doubleClickListener != null) {
				doubleClickListener.afteDoubleClick();
			}
			mStartTime = -1;
			return true;
		}
		mStartTime = nowTime;
		return false;
	}

	/**
	 * 当某个动作要双击才执行时，调用此方法。
	 * 
	 * @param delayTime
	 *            判断双击的时间。
	 * @param msgResid
	 *            当第一次点击时，弹出的提示信息的资源ID。
	 */
	public void doDoubleClick(int delayTime, int msgResid) {
		if (!doInDelayTime(delayTime)) {
			mToast.setDuration(delayTime);
            mToast.setText(msgResid);
			mToast.show();
		}
	}

	public interface DoubleClickListener {
		public void afteDoubleClick();
	}

	public DoubleClickListener getDoubleClickListener() {
		return doubleClickListener;
	}

	public void setDoubleClickListener(DoubleClickListener doubleClickListener) {
		this.doubleClickListener = doubleClickListener;
	}
}

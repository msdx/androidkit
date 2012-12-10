/*
 * @(#)ColorPickerDialog.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-17
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
package com.lurencun.cfuture09.androidkit.graphics;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.lurencun.cfuture09.androidkit.util.ui.ActivityUtil;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ColorPickerDialog extends Dialog {
	private Context mContext;
	/**
	 * 标题。
	 */
	private String mTitle;
	/**
	 * 初始颜色。
	 */
	private int mInitialColor;
	/**
	 * 颜色选择后的回调接口。
	 */
	private OnColorChangedListener mListener;

	/**
	 * 构造方法。
	 * 
	 * @param context
	 * @param title
	 *            对话框标题。
	 * @param l
	 *            颜色选择后的回调接口。
	 */
	public ColorPickerDialog(Context context, String title,
			OnColorChangedListener l) {
		this(context, title, Color.GRAY, l);

	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 * @param title
	 *            对话框的标题。
	 * @param initalColor
	 *            初始化颜色。
	 * @param l
	 *            颜色选择后的回调接口。
	 */
	public ColorPickerDialog(Context context, String title, int initalColor,
			OnColorChangedListener l) {
		super(context);
		mContext = context;
		mTitle = title;
		mListener = l;
		mInitialColor = initalColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isPortrait = ActivityUtil.isScreenOriatationPortrait(mContext);
		if (isPortrait) {
			setContentView(new PortraitColorPickerView(mContext, mListener));
		} else {
			setContentView(new LandscapeColorPickerView(mContext, mListener));
		}
		setTitle(mTitle);
	}

	public interface OnColorChangedListener {
		void colorChanged(int color);
	}

	protected abstract class ColorPickerView extends View {
		protected OnColorChangedListener mListener;
		protected Paint mCirclePaint;// 渐变色环画笔
		protected Paint mCenterPaint;// 中间圆画笔
		protected Paint mLinePaint;// 分隔线画笔
		protected Paint mRectPaint;// 渐变方块画笔

		protected Shader mRectShader;// 渐变方块渐变图像
		protected float mRectLeft;// 渐变方块左顶点x坐标
		protected float mRectTop;// 渐变方块左顶点y坐标
		protected float mRectRight;// 渐变方块右底点x坐标
		protected float mRectBottom;// 渐变方块右底点y坐标

		protected int mHeight;// View高
		protected int mWidth;// View宽
		protected float mCircleRadius;// 色环半径(paint中部)
		protected float mCenterRadius;// 中心圆半径

		protected boolean mDownInCircle = true;// 按在渐变环上
		protected boolean mDownInRect;// 按在渐变方块上
		protected boolean mHighlightCenter;// 高亮
		protected boolean mlittleLightCenter;// 微亮

		protected final int[] mCircleColors;// 渐变色环颜色
		protected final int[] mRectColors;// 渐变方块颜色

		public ColorPickerView(Context context, OnColorChangedListener l) {
			super(context);
			this.mListener = l;
			mCircleColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
					0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
			mRectColors = new int[] { 0xFF000000, mInitialColor, 0xFFFFFFFF };
		}

		protected int ave(int s, int d, float p) {
			return s + Math.round(p * (d - s));
		}

		/**
		 * 坐标是否在色环上
		 * 
		 * @param x
		 *            坐标
		 * @param y
		 *            坐标
		 * @param outRadius
		 *            色环外半径
		 * @param inRadius
		 *            色环内半径
		 * @return
		 */
		protected boolean inColorCircle(float x, float y, float outRadius,
				float inRadius) {
			double outCircle = outRadius * outRadius;
			double inCircle = inRadius * inRadius;
			double fingerCircle = x * x + y * y;
			return (fingerCircle < outCircle && fingerCircle > inCircle);
		}

		/**
		 * 坐标是否在中心圆上
		 * 
		 * @param x
		 *            坐标
		 * @param y
		 *            坐标
		 * @param centerRadius
		 *            圆半径
		 * @return
		 */
		protected boolean inCenter(float x, float y, float centerRadius) {
			double centerCircle = centerRadius * centerRadius;
			double fingerCircle = x * x + y * y;
			return (fingerCircle < centerCircle);
		}

		/**
		 * 坐标是否在渐变色中
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		protected boolean inRect(float x, float y) {
			return (x <= mRectRight && x >= mRectLeft && y <= mRectBottom && y >= mRectTop);
		}

		/**
		 * 获取圆环上颜色
		 * 
		 * @param colors
		 * @param unit
		 * @return
		 */
		protected int interpCircleColor(int colors[], float unit) {
			if (unit <= 0) {
				return colors[0];
			}
			if (unit >= 1) {
				return colors[colors.length - 1];
			}

			float p = unit * (colors.length - 1);
			int i = (int) p;
			p -= i;

			// now p is just the fractional part [0...1) and i is the index
			int c0 = colors[i];
			int c1 = colors[i + 1];
			int a = ave(Color.alpha(c0), Color.alpha(c1), p);
			int r = ave(Color.red(c0), Color.red(c1), p);
			int g = ave(Color.green(c0), Color.green(c1), p);
			int b = ave(Color.blue(c0), Color.blue(c1), p);

			return Color.argb(a, r, g, b);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(mWidth, mHeight);
		}

		/**
		 * 当手指按下时。
		 * 
		 * @param inCircle
		 *            是否在色环上。
		 * @param inCenter
		 *            是否在圆圈中心上。
		 * @param inRect
		 *            是否在渐变矩形上。
		 */
		protected void onActionDown(boolean inCircle, boolean inCenter,
				boolean inRect) {
			mDownInCircle = inCircle;
			mDownInRect = inRect;
			mHighlightCenter = inCenter;
		}

		/**
		 * 获取渐变块上颜色
		 * 
		 * @param colors
		 * @param x
		 *            手指触摸到的相对x坐标
		 * @param y
		 *            手指触摸到的相对y坐标
		 * @return
		 */
		abstract int interpRectColor(int[] colors, float x, float y);

		/**
		 * 当手指移动时。
		 * 
		 * @param x
		 *            手指位置与圆心横坐标的相对距离。
		 * @param y
		 *            手指位置与圆心纵坐标的相对距离。
		 * @param inCircle
		 *            是否在色环中。
		 * @param inCenter
		 *            是否在圆圈中。
		 * @param inRect
		 *            是否在渐变矩形中。
		 */
		protected void onActionMove(float x, float y, boolean inCircle,
				boolean inCenter, boolean inRect) {
			if (mDownInCircle && inCircle) {// down按在渐变色环内, 且move也在渐变色环内
				final float angle = (float) Math.atan2(y, x);
				float unit = (float) (angle / (2 * Math.PI));
				if (unit < 0) {
					unit += 1;
				}
				mCenterPaint.setColor(interpCircleColor(mCircleColors, unit));
			} else if (mDownInRect && inRect) {// down在渐变方块内, 且move也在渐变方块内
				mCenterPaint.setColor(interpRectColor(mRectColors, x, y));
			}
			if ((mHighlightCenter && inCenter)
					|| (mlittleLightCenter && inCenter)) {// 点击中心圆,
															// 当前移动在中心圆
				mHighlightCenter = true;
				mlittleLightCenter = false;
			} else if (mHighlightCenter || mlittleLightCenter) {// 点击在中心圆,
																// 当前移出中心圆
				mHighlightCenter = false;
				mlittleLightCenter = true;
			} else {
				mHighlightCenter = false;
				mlittleLightCenter = false;
			}
			invalidate();
		}

		/**
		 * 当手指松开时。
		 * 
		 * @param inCenter
		 *            是否在圆圈中。
		 */
		protected void onActionUp(boolean inCenter) {
			if (mHighlightCenter && inCenter) {// 点击在中心圆, 且当前启动在中心圆
				if (this.mListener != null) {
					this.mListener.colorChanged(mCenterPaint.getColor());
					ColorPickerDialog.this.dismiss();
				}
			}
			if (mDownInCircle) {
				mDownInCircle = false;
			}
			if (mDownInRect) {
				mDownInRect = false;
			}
			if (mHighlightCenter) {
				mHighlightCenter = false;
			}
			if (mlittleLightCenter) {
				mlittleLightCenter = false;
			}
			invalidate();
		}
	}

	/**
	 * 竖屏时的颜色选择器的view.
	 * 
	 * @author msdx
	 * 
	 */
	private class PortraitColorPickerView extends ColorPickerView {

		public PortraitColorPickerView(Context context, OnColorChangedListener l) {
			super(context, l);
			Display display = ColorPickerDialog.this.getWindow()
					.getWindowManager().getDefaultDisplay();
			int height = (int) (display.getHeight() * 0.5f) - 36;
			int width = (int) (display.getWidth() * 0.7f);
			this.mHeight = height;
			this.mWidth = width;
			setMinimumHeight(height);
			setMinimumWidth(width);

			// 渐变色环参数

			Shader s = new SweepGradient(0, 0, mCircleColors, null);
			mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCirclePaint.setShader(s);
			mCirclePaint.setStyle(Paint.Style.STROKE);
			mCirclePaint.setStrokeWidth(50);
			mCircleRadius = width / 2 * 0.7f - mCirclePaint.getStrokeWidth()
					* 0.5f;

			// 中心圆参数
			mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCenterPaint.setColor(mInitialColor);
			mCenterPaint.setStrokeWidth(5);
			mCenterRadius = (mCircleRadius - mCirclePaint.getStrokeWidth() / 2) * 0.7f;

			// 边框参数
			mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mLinePaint.setColor(Color.parseColor("#72A1D1"));
			mLinePaint.setStrokeWidth(4);

			// 黑白渐变参数

			mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mRectPaint.setStrokeWidth(5);
			mRectLeft = -mCircleRadius - mCirclePaint.getStrokeWidth() * 0.5f;
			mRectTop = mCircleRadius + mCirclePaint.getStrokeWidth() * 0.5f
					+ mLinePaint.getStrokeMiter() * 0.5f + 15;
			mRectRight = mCircleRadius + mCirclePaint.getStrokeWidth() * 0.5f;
			mRectBottom = mRectTop + 50;
		}

		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			// 移动中心
			canvas.translate(mWidth / 2, mHeight / 2 - 50);
			// 画中心圆
			canvas.drawCircle(0, 0, mCenterRadius, mCenterPaint);
			// 是否显示中心圆外的小圆环
			if (mHighlightCenter || mlittleLightCenter) {
				int c = mCenterPaint.getColor();
				mCenterPaint.setStyle(Paint.Style.STROKE);
				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else if (mlittleLightCenter) {
					mCenterPaint.setAlpha(0x90);
				}
				canvas.drawCircle(0, 0,
						mCenterRadius + mCenterPaint.getStrokeWidth(),
						mCenterPaint);

				mCenterPaint.setStyle(Paint.Style.FILL);
				mCenterPaint.setColor(c);
			}
			// 画色环
			canvas.drawOval(new RectF(-mCircleRadius, -mCircleRadius,
					mCircleRadius, mCircleRadius), mCirclePaint);
			// 画黑白渐变块
			if (mDownInCircle) {
				mRectColors[1] = mCenterPaint.getColor();
			}
			mRectShader = new LinearGradient(mRectLeft, 0, mRectRight, 0,
					mRectColors, null, Shader.TileMode.MIRROR);
			mRectPaint.setShader(mRectShader);
			canvas.drawRect(mRectLeft, mRectTop, mRectRight, mRectBottom,
					mRectPaint);
			float offset = mLinePaint.getStrokeWidth() / 2;
			canvas.drawLine(mRectLeft - offset, mRectTop - offset * 2,
					mRectLeft - offset, mRectBottom + offset * 2, mLinePaint);// 左
			canvas.drawLine(mRectLeft - offset * 2, mRectTop - offset,
					mRectRight + offset * 2, mRectTop - offset, mLinePaint);// 上
			canvas.drawLine(mRectRight + offset, mRectTop - offset * 2,
					mRectRight + offset, mRectBottom + offset * 2, mLinePaint);// 右
			canvas.drawLine(mRectLeft - offset * 2, mRectBottom + offset,
					mRectRight + offset * 2, mRectBottom + offset, mLinePaint);// 下
			super.onDraw(canvas);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX() - mWidth / 2;
			float y = event.getY() - mHeight / 2 + 50;
			boolean inCircle = inColorCircle(x, y,
					mCircleRadius + mCirclePaint.getStrokeWidth() / 2,
					mCircleRadius - mCirclePaint.getStrokeWidth() / 2);
			boolean inCenter = inCenter(x, y, mCenterRadius);
			boolean inRect = inRect(x, y);
			System.out.println(x + "..." + y);
			System.out.println(mRectLeft + "..." + mRectRight + "..."
					+ mRectTop + "..." + mRectBottom);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				onActionDown(inCircle, inCenter, inRect);
			case MotionEvent.ACTION_MOVE:
				onActionMove(x, y, inCircle, inCenter, inRect);
				break;
			case MotionEvent.ACTION_UP:
				onActionUp(inCenter);
				break;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.sinaapp.msdxblog.androidkit.graphics.ColorPickerDialog.
		 * ColorPickerView#interpRectColor(int[], float, float)
		 */
		protected int interpRectColor(int colors[], float x, float y) {
			int a, r, g, b, c0, c1;
			float p;
			if (x < 0) {
				c0 = colors[0];
				c1 = colors[1];
				p = (x + mRectRight) / mRectRight;
			} else {
				c0 = colors[1];
				c1 = colors[2];
				p = x / mRectRight;
			}
			a = ave(Color.alpha(c0), Color.alpha(c1), p);
			r = ave(Color.red(c0), Color.red(c1), p);
			g = ave(Color.green(c0), Color.green(c1), p);
			b = ave(Color.blue(c0), Color.blue(c1), p);
			return Color.argb(a, r, g, b);
		}
	}

	/**
	 * 横屏时的颜色选择器的view.
	 * 
	 * @author msdx
	 * 
	 */
	private class LandscapeColorPickerView extends ColorPickerView {

		public LandscapeColorPickerView(Context context,
				OnColorChangedListener l) {
			super(context, l);
			Display display = ColorPickerDialog.this.getWindow()
					.getWindowManager().getDefaultDisplay();
			int height = (int) (display.getHeight() * 0.8f) - 36;
			int width = (int) (display.getWidth() * 0.5f);
			this.mHeight = height;
			this.mWidth = width;
			setMinimumHeight(height);
			setMinimumWidth(width);

			// 渐变色环参数
			Shader s = new SweepGradient(0, 0, mCircleColors, null);
			mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCirclePaint.setShader(s);
			mCirclePaint.setStyle(Paint.Style.STROKE);
			mCirclePaint.setStrokeWidth(50);
			mCircleRadius = mHeight / 2 * 0.7f - mCirclePaint.getStrokeWidth()
					* 0.5f;

			// 中心圆参数
			mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCenterPaint.setColor(mInitialColor);
			mCenterPaint.setStrokeWidth(5);
			mCenterRadius = (mCircleRadius - mCirclePaint.getStrokeWidth() / 2) * 0.7f;

			// 边框参数
			mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mLinePaint.setColor(Color.parseColor("#72A1D1"));
			mLinePaint.setStrokeWidth(4);

			// 黑白渐变参数
			mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mRectPaint.setStrokeWidth(5);
			mRectLeft = mCircleRadius + mCirclePaint.getStrokeWidth() * 0.5f
					+ mLinePaint.getStrokeMiter() * 0.5f + 15;
			mRectTop = -mCircleRadius - mCirclePaint.getStrokeWidth() * 0.5f;
			mRectRight = mRectLeft + 50;
			mRectBottom = mCircleRadius + mCirclePaint.getStrokeWidth() * 0.5f;
		}

		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			// 移动中心
			canvas.translate(mWidth / 2 - 50, mHeight / 2);
			// 画中心圆
			canvas.drawCircle(0, 0, mCenterRadius, mCenterPaint);
			// 是否显示中心圆外的小圆环
			if (mHighlightCenter || mlittleLightCenter) {
				int c = mCenterPaint.getColor();
				mCenterPaint.setStyle(Paint.Style.STROKE);
				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else if (mlittleLightCenter) {
					mCenterPaint.setAlpha(0x90);
				}
				canvas.drawCircle(0, 0,
						mCenterRadius + mCenterPaint.getStrokeWidth(),
						mCenterPaint);

				mCenterPaint.setStyle(Paint.Style.FILL);
				mCenterPaint.setColor(c);
			}
			// 画色环
			canvas.drawOval(new RectF(-mCircleRadius, -mCircleRadius,
					mCircleRadius, mCircleRadius), mCirclePaint);
			// 画黑白渐变块
			if (mDownInCircle) {
				mRectColors[1] = mCenterPaint.getColor();
			}
			mRectShader = new LinearGradient(0, mRectTop, 0, mRectBottom,
					mRectColors, null, Shader.TileMode.MIRROR);
			mRectPaint.setShader(mRectShader);
			canvas.drawRect(mRectLeft, mRectTop, mRectRight, mRectBottom,
					mRectPaint);
			float offset = mLinePaint.getStrokeWidth() / 2;
			canvas.drawLine(mRectLeft - offset, mRectTop - offset * 2,
					mRectLeft - offset, mRectBottom + offset * 2, mLinePaint);// 左
			canvas.drawLine(mRectLeft - offset * 2, mRectTop - offset,
					mRectRight + offset * 2, mRectTop - offset, mLinePaint);// 上
			canvas.drawLine(mRectRight + offset, mRectTop - offset * 2,
					mRectRight + offset, mRectBottom + offset * 2, mLinePaint);// 右
			canvas.drawLine(mRectLeft - offset * 2, mRectBottom + offset,
					mRectRight + offset * 2, mRectBottom + offset, mLinePaint);// 下
			super.onDraw(canvas);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX() - mWidth / 2 + 50;
			float y = event.getY() - mHeight / 2;
			boolean inCircle = inColorCircle(x, y,
					mCircleRadius + mCirclePaint.getStrokeWidth() / 2,
					mCircleRadius - mCirclePaint.getStrokeWidth() / 2);
			boolean inCenter = inCenter(x, y, mCenterRadius);
			boolean inRect = inRect(x, y);
			System.out.println(x + "..." + y);
			System.out.println(mRectLeft + "..." + mRectRight + "..."
					+ mRectTop + "..." + mRectBottom);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				onActionDown(inCircle, inCenter, inRect);
			case MotionEvent.ACTION_MOVE:
				onActionMove(x, y, inCircle, inCenter, inRect);
				break;
			case MotionEvent.ACTION_UP:
				onActionUp(inCenter);
				break;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.sinaapp.msdxblog.androidkit.graphics.ColorPickerDialog.
		 * ColorPickerView#interpRectColor(int[], float, float)
		 */
		protected int interpRectColor(int colors[], float x, float y) {
			int a, r, g, b, c0, c1;
			float p;
			float referLine = mRectBottom;
			if (y < 0) {
				c0 = colors[0];
				c1 = colors[1];
				p = (y + referLine) / referLine;
			} else {
				c0 = colors[1];
				c1 = colors[2];
				p = y / referLine;
			}
			a = ave(Color.alpha(c0), Color.alpha(c1), p);
			r = ave(Color.red(c0), Color.red(c1), p);
			g = ave(Color.green(c0), Color.green(c1), p);
			b = ave(Color.blue(c0), Color.blue(c1), p);
			return Color.argb(a, r, g, b);
		}
	}
}

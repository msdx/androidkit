/*
 * @(#)IntroActivity.java		       Project:androidkit
 * Date:2013-11-10
 *
 * Copyright (c) 2013 CFuture09, Institute of Software, 
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
package com.lurencun.cfuture09.androidkit.uilibs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;
import com.lurencun.cfuture09.androidkit.utils.ui.ActivityUtil;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public abstract class IntroActivity extends Activity implements OnClickListener,
		OnPageChangeListener {

	private static final Log4AK log = Log4AK.getLog(IntroActivity.class);
	private static final String AK_PREF = "akconfig.pref";
	private static final String INTRO_KEY = "IntroVersion";

	private IntroResource mIntroResource;
	private ViewPager mViewPager;
	private List<ImageView> mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFullScreen()) {
			ActivityUtil.setFullScreen(this);
		}
		init();
	}

	/**
	 * 初始化。
	 */
	private void init() {
		if (showOnlyAtUpdate() && !isAfterUpdate()) {
			finish();
			return;
		}
		createContentView();
		showIntro();
		updateIntroVersion();
	}

	/**
	 * 是否为刚升级或安装。如果选择了只在第一次安装或升级后显示，则
	 * 
	 * @return
	 */
	private boolean isAfterUpdate() {
		try {
			int versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
			int introVersion = this.getSharedPreferences(AK_PREF, MODE_PRIVATE).getInt(INTRO_KEY,
					-1);
			return versionCode > introVersion;
		} catch (NameNotFoundException e) {
			log.e(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 更新配置文件中保存的程序版本，它用于在启动程序时判断是否显示Activity。
	 */
	private void updateIntroVersion() {
		try {
			int versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
			this.getSharedPreferences(AK_PREF, MODE_PRIVATE).edit().putInt(INTRO_KEY, versionCode)
					.commit();
		} catch (NameNotFoundException e) {
			log.e(e.getMessage(), e);
		}
	}

	/**
	 * 显示引导页面。
	 */
	private void showIntro() {
		IntroPagerAdapter adapter = new IntroPagerAdapter(mIntroResource.views);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);
		mIndicator.get(0).setImageResource(mIntroResource.indicatorSelectedId);
	}

	/**
	 * 创建布局的内容。
	 */
	private void createContentView() {
		// 设置资源
		mIntroResource = new IntroResource();
		setIntroViews(mIntroResource);

		// 根布局
		RelativeLayout rootView = new RelativeLayout(this);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		// 滑动页
		mViewPager = new ViewPager(this);
		RelativeLayout.LayoutParams pagerParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		rootView.addView(mViewPager, pagerParams);

		// 指示器布局
		LinearLayout indicatorLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams indicatorParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		indicatorParams.bottomMargin = mIntroResource.indicatorMarginBottom;
		indicatorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		indicatorParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		indicatorLayout.setLayoutParams(indicatorParams);
		indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);

		// 指示器内容
		int indicatorCount = mIntroResource.views.size();
		int padding = mIntroResource.indicatorImagePadding;
		mIndicator = new ArrayList<ImageView>();
		for (int i = 0; i < indicatorCount; i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setClickable(true);
			imageView.setOnClickListener(this);
			imageView.setImageResource(mIntroResource.indicatorNoSelectedId);
			imageView.setTag(i);
			mIndicator.add(imageView);
			indicatorLayout.addView(imageView, imageParams);
		}

		rootView.addView(indicatorLayout);

		setContentView(rootView);
	}

	/**
	 * 是否全屏显示。
	 * 
	 * @return
	 */
	protected boolean isFullScreen() {
		return false;
	}

	/**
	 * 是否仅在第一次安装或升级时显示。
	 * 
	 * @return
	 */
	protected boolean showOnlyAtUpdate() {
		return true;
	}

	/**
	 * 更新指示器。
	 * 
	 * @param position
	 */
	private void updateIndicator(int position) {
		int size = mIndicator.size();
		if (position >= 0 && position < size) {
			for (int i = 0; i < size; i++) {
				mIndicator.get(i).setImageResource(mIntroResource.indicatorNoSelectedId);
			}
			mIndicator.get(position).setImageResource(mIntroResource.indicatorSelectedId);
		}
	}

	/**
	 * 设置当前要显示的视图页。
	 * 
	 * @param position
	 */
	private void setCurrentView(int position) {
		if (position >= 0 && position < mIntroResource.views.size()) {
			mViewPager.setCurrentItem(position);
		}

	}

	protected abstract void setIntroViews(IntroResource resource);

	@Override
	public void onClick(View v) {
		if (v instanceof ImageView) {
			int position = (Integer) v.getTag();
			setCurrentView(position);
			updateIndicator(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int pos) {
		updateIndicator(pos);
	}

	/**
	 * 引导界面的资源类，用于设置需要显示的引导界面、底部指示器的格式。
	 * 
	 * @author Geek_Soledad <a target="_blank" href=
	 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
	 *         style="text-decoration:none;"><img src=
	 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
	 *         /></a>
	 */
	public static class IntroResource {
		/**
		 * 显示在view pager的界面
		 */
		public List<? extends View> views;
		/**
		 * 底部指示器中被选择的图片id
		 */
		public int indicatorSelectedId;
		/**
		 * 底部指示器中没被选择的图片id
		 */
		public int indicatorNoSelectedId;

		/**
		 * 指示器的外延边矩。默认为24.
		 */
		public int indicatorMarginBottom = 24;

		/**
		 * 指示器的各个图片外延边矩。
		 */
		public int indicatorImagePadding = 15;

		public IntroResource() {
			super();
		}

		/**
		 * 引导界面资源。
		 * 
		 * @param views
		 *            显示在view pager的界面
		 * @param indicatorSelectedId
		 *            底部指示器中被选择的图片id
		 * @param indicatorNoSelectedId
		 *            底部指示器中没被选择的图片id
		 */
		public IntroResource(List<? extends View> views, int indicatorSelectedId,
				int indicatorNoSelectedId) {
			super();
			this.views = views;
			this.indicatorSelectedId = indicatorSelectedId;
			this.indicatorNoSelectedId = indicatorNoSelectedId;
		}

	}

	/**
	 * 引导页面的适配器。
	 * 
	 * @author Geek_Soledad <a target="_blank" href=
	 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
	 *         style="text-decoration:none;"><img src=
	 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
	 *         /></a>
	 */
	public static class IntroPagerAdapter extends PagerAdapter {

		private List<? extends View> mListViews;

		public IntroPagerAdapter(List<? extends View> listviews) {
			mListViews = listviews;
		}

		@Override
		public int getCount() {
			return mListViews != null ? mListViews.size() : 0;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mListViews.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}

/*
 * @(#)IntroActivitySample.java		       Project:example
 * Date:2013-11-12
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
package com.lurencun.androidkit.sample.uilibs;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lurencun.androidkit.sample.R;
import com.lurencun.cfuture09.androidkit.uilibs.IntroActivity;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class IntroActivitySample extends IntroActivity {

	@Override
	protected void setIntroViews(IntroResource resource) {
		// TODO Auto-generated method stub
		List<ImageView> views = new ArrayList<ImageView>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		// 初始化引导图片列表
		int[] pics = { R.drawable.login_1, R.drawable.login_2, R.drawable.login_3,
				R.drawable.login_4 };
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(params);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}

		resource.views = views;
		resource.indicatorNoSelectedId = R.drawable.gray_point;
		resource.indicatorSelectedId = R.drawable.green_point;
	}
	
	@Override
	protected boolean showOnlyAtUpdate() {
		return false;
	}

}

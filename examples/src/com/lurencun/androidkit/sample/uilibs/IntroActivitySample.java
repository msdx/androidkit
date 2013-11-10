/*
 * @(#)IntroActivityTest.java		       Project:androidkitTest
 * Date:2013-2-7
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

import java.util.List;

import android.widget.Toast;

import com.lurencun.androidkit.sample.R;
import com.lurencun.cfuture09.androidkit.uilibs.SplashActivity;

/**
 * @Author Geek_Soledad (66704238@51uc.com)
 * @Function
 */
public class IntroActivitySample extends SplashActivity {

	@Override
	protected void setSplashResources(List<SplashImgResource> resources) {
		resources.add(new SplashImgResource(R.drawable.ic_launcher, 1500, 0.3f, false));
		resources.add(new SplashImgResource(R.drawable.ic_launcher, 1500, 0.5f, true));
	}

	@Override
	protected Class<?> nextActivity() {
		return AfterIntroActivity.class;
	}

	// 这里的代码将在非ui线程中执行。
	@Override
	protected void runOnBackground() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void runOnMainThread() {
		super.runOnMainThread();
		Toast.makeText(this, "模拟加载5秒钟的数据", Toast.LENGTH_SHORT).show();
	}
}

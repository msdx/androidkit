/*
 * @(#)LruCacheSample.java		       Project:androidkit.sample
 * Date:2013-5-29
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
package com.githang.androidkit.sample.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.githang.androidkit.cache.BitmapLruCache;
import com.githang.androidkit.sample.R;


/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class LruCacheSample extends Activity {

//	private IdGenerator<Integer> id = new IncreaseIntId();
	private BitmapLruCache cache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sample);
		 cache = new BitmapLruCache(new BitmapLruCache.BitmapCacheParams(this, "bitmapcache"));
	}
	
	protected void putInCache(String key, Bitmap bitmap) {
		cache.addBitmapToCache(key, bitmap);
	}
}

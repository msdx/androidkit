/*
 * @(#)BitmapCacheSample.java		       Project:androidkit.sample
 * Date:2013-5-30
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
package com.lurencun.androidkit.sample.cache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lurencun.androidkit.sample.R;
import com.lurencun.cfuture09.androidkit.cache.BitmapLruCache;
import com.lurencun.cfuture09.androidkit.cache.BitmapLruCache.BitmapCacheParams;
import com.lurencun.cfuture09.androidkit.http.Http;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class BitmapCacheSample extends Activity{

	private ImageView httpImage;
	private ImageView cacheImage;
	private BitmapLruCache cache;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bitmap_cache);
		httpImage = (ImageView) findViewById(R.id.http_image);
		cacheImage = (ImageView) findViewById(R.id.cache_image);
		cache = new BitmapLruCache(new BitmapCacheParams(this, "bitmapcache"));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				bitmap = Http.getBitmap("http://static.oschina.net/uploads/user/113/227618_50.jpg");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						httpImage.setImageBitmap(bitmap);
					}
				});
			}
		}).start();
	}
	
	public void onClicked(View v) {
		switch (v.getId()) {
		case R.id.cache_putImage:
			cache.addBitmapToCache("bitmap",bitmap);
			break;
		case R.id.cache_getImage:
			cacheImage.setImageBitmap(cache.getBitmapFromMemoryCache("bitmap"));
			break;
		default:
			break;
		}
	}
}

/*
 * @(#)BitmapLruCache.java		       Project:androidkit
 * Date:2013-6-14
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
package com.lurencun.cfuture09.androidkit.cache;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * 代码参考自：http://developer.android.com/training/displaying-bitmaps/cache-bitmap.
 * html
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class BitmapLruCache {

	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;

	private final CacheParams mParams;

	public BitmapLruCache(CacheParams params) {
		if (params == null) {
			throw new IllegalArgumentException("the argument could not be null");
		}
		mParams = params;

	}

	/**
	 * 图片缓存参数,使缓存的文件夹默认名为ak_thumbnails.
	 * 
	 * @author Geek_Soledad <a target="_blank" href=
	 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
	 *         style="text-decoration:none;"><img src=
	 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
	 *         /></a>
	 */
	public class BitmapCacheParams extends CacheParams {
		private static final String DISK_CACHE_SUBDIR = "ak_thumbnails";

		/**
		 * 构造BitmapCacheParams对象，并使用默认缓存路径。
		 * 
		 * @param context
		 */
		public BitmapCacheParams(Context context) {
			super(CacheCommonUtil.getDiskAppCacheDir(context, DISK_CACHE_SUBDIR));
		}

		/**
		 * 构造BitmapCacheParams对象，并指定缓存子目录名。
		 * 
		 * @param context
		 * @param bitmapCacheDir
		 *            指定的图片缓存子目录名。
		 */
		public BitmapCacheParams(Context context, String bitmapCacheDir) {
			super(CacheCommonUtil.getDiskAppCacheDir(context, bitmapCacheDir));
		}
	}
}

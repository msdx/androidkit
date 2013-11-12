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
import java.io.IOException;

import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Bitmap Lru缓存类。
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

	private static final int APP_VERSION = 1;
	private static final Log4AK log = Log4AK.getLog(BitmapLruCache.class);
	private DiskLruCache mDiskLruCache;
	private MemoryLruCache<String, Bitmap> mMemoryCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;

	private final CacheParams mParams;

	public BitmapLruCache(CacheParams params) {
		if (params == null) {
			throw new IllegalArgumentException("the argument could not be null");
		}
		mParams = params;

		initCache();
	}

	/**
	 * 初始化缓存。
	 */
	private void initCache() {
		mMemoryCache = new MemoryLruCache<String, Bitmap>(mParams.getMemCacheSize()) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		if (mParams.initDiskCacheOnCreate) {
			new InitDiskCacheTask().execute(mParams);
		}
	}

	/**
	 * 该类用于执行磁盘缓存的初始化。
	 * 
	 * @author Geek_Soledad <a target="_blank" href=
	 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
	 *         style="text-decoration:none;"><img src=
	 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
	 *         /></a>
	 */
	class InitDiskCacheTask extends AsyncTask<CacheParams, Void, Void> {

		@Override
		protected Void doInBackground(CacheParams... params) {
			CacheParams cacheParams = params[0];
			if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
				File cacheDir = cacheParams.getDiskCacheDir();
				if (cacheDir != null && cacheParams.diskCacheEnabled) {
					if (!cacheDir.exists()) {
						cacheDir.mkdirs();
						//TODO
					}
					try {
						mDiskLruCache = DiskLruCache.open(cacheParams.getDiskCacheDir(),
								APP_VERSION, 1, cacheParams.getDiskCacheSize());
					} catch (IOException e) {
						cacheParams.setDiskCacheDir(null);
						log.e(e.getMessage(), e);
					}
				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
			return null;
		}

	}

	public void addBitmapToCache(String key, Bitmap bitmap) {
		addBitmapToMemoryCache(key, bitmap);

	}

	/**
	 * 将Bitamp加入内存缓存。如果内存中已存在键值为{@code key}的Bitmap对象，则不再重复添加。
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public void addBitmapToDiskCache(String key, Bitmap bitmap) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && mDiskLruCache.getDirectory() != null) {
				// TODO
				// mDiskLruCache.
			}
		}
	}

	/**
	 * 从内存缓存中获取键值为{@code key}的Bitmap对象。如果没有，则返回null。
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * Bitmap缓存参数,使缓存的文件夹默认名为ak_thumbnails.
	 * 
	 * @author Geek_Soledad <a target="_blank" href=
	 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
	 *         style="text-decoration:none;"><img src=
	 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
	 *         /></a>
	 */
	public static class BitmapCacheParams extends CacheParams {
		/**
		 * Bitmap缓存默认子目录名。
		 */
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

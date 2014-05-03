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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.lurencun.cfuture09.androidkit.BuildConfig;
import com.lurencun.cfuture09.androidkit.utils.io.IOUtils;
import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;
import com.lurencun.cfuture09.androidkit.utils.security.DigestUtil;

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
	private static final int DISK_CACHE_INDEX = 0;

	private DiskLruCache mDiskLruCache;
	private MemoryLruCache<String, Bitmap> mMemoryCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;

	private final BitmapCacheParams mParams;

	public BitmapLruCache(BitmapCacheParams params) {
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
		if (mParams.isMemCacheEnabled()) {
			if (BuildConfig.DEBUG) {
				log.d("Memory cache created (size = " + mParams.getMemCacheSize() + "B)");
			}
			mMemoryCache = new MemoryLruCache<String, Bitmap>(mParams.getMemCacheSize()) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}
		if (mParams.initDiskCacheOnCreate) {
			initDiskCache();
		}
	}

	/**
	 * 初始化磁盘缓存。
	 */
	private void initDiskCache() {
		new InitDiskCacheTask().execute(mParams);
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
					}
					if (CacheCommonUtil.getUsableSpace(cacheDir) < cacheParams.diskCacheSize) {
						log.e("Disk Cache will be created, but the usable space("
								+ CacheCommonUtil.getUsableSpace(cacheDir)
								+ ") is smaller than the disk cache need ("
								+ cacheParams.diskCacheSize + ")");
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
		if (key == null || bitmap == null) {
			return;
		}
		addBitmapToMemoryCache(key, bitmap);
		addBitmapToDiskCache(key, bitmap);
	}

	/**
	 * 将Bitamp加入内存缓存。
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (mMemoryCache != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 将Bitmap加入磁盘缓存。
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToDiskCache(String key, Bitmap bitmap) {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null && mDiskLruCache.getDirectory() != null) {
				synchronized (mDiskCacheLock) {
					while (mDiskCacheStarting) {
						try {
							mDiskCacheLock.wait();
						} catch (InterruptedException e) {
							log.e(e.getMessage(), e);
						}
					}
					if (mDiskLruCache != null) {
						OutputStream os = null;
						try {
							String hashKey = hashKeyForDisk(key);
							final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKey);
							if (snapshot != null) {
								final DiskLruCache.Editor editor = snapshot.edit();
								if (editor != null) {
									os = editor.newOutputStream(DISK_CACHE_INDEX);
									bitmap.compress(mParams.compressFormat,
											mParams.compressQuality, os);
								}
							}
						} catch (IOException e) {
							log.e(e.getMessage(), e);
						} finally {
							IOUtils.closeQuietly(os);
						}
					}
				}
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
		return mMemoryCache == null ? null : mMemoryCache.get(key);
	}

	/**
	 * 从磁盘缓存中获取键值为{@code key}的Bitmap对象，如果没有，则返回null。
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromDiskCache(String key) {
		synchronized (mDiskCacheLock) {
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
					log.e(e.getMessage(), e);
				}
			}
			if (mDiskLruCache != null) {
				InputStream is = null;
				try {
					String hashKey = hashKeyForDisk(key);
					final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKey);
					if (snapshot != null) {
						is = snapshot.getInputStream(DISK_CACHE_INDEX);
						if (is != null) {
							FileDescriptor fd = ((FileInputStream) is).getFD();
							return BitmapFactory.decodeFileDescriptor(fd);
						}
					}
				} catch (IOException e) {
					log.e(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		}
		return null;
	}

	/**
	 * 清空缓存。如果选择同时清空磁盘缓存，它将会在删除磁盘缓存文件之后重建磁盘缓存。
	 * 
	 * @param deleteDiskCache
	 *            是否清空磁盘缓存。
	 */
	public void clearCache(boolean deleteDiskCache) {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}

		if (deleteDiskCache) {
			synchronized (mDiskCacheLock) {
				mDiskCacheStarting = true;
				if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
					try {
						mDiskLruCache.delete();
					} catch (IOException e) {
						log.e(e.getMessage(), e);
					}
					mDiskLruCache = null;
					initDiskCache();
				}
			}
		}
	}

	/**
	 * 强行输出磁盘缓存中缓冲区的数据。
	 */
	public void flush() {
		synchronized (mDiskCacheLock) {
			if (mDiskLruCache != null) {
				try {
					mDiskLruCache.flush();
				} catch (IOException e) {
					log.e(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 关闭缓存。它将会关闭磁盘缓存。
	 */
	public void close() {
		synchronized (mDiskCacheLock) {
			if ((mDiskLruCache != null)) {
				if (!mDiskLruCache.isClosed()) {
					try {
						mDiskLruCache.close();
						mDiskLruCache = null;
					} catch (IOException e) {
						log.e(e.getMessage(), e);
					}
				}
			}
		}
	}

	/**
	 * 将字符串转换为hash值的方法，以使其更适合用于文件名字。
	 * 
	 * @param key
	 * @return
	 */
	public static String hashKeyForDisk(String key) {
		String hashKey = DigestUtil.doDigest("MD5", key);
		if (hashKey == null) {
			hashKey = String.valueOf(key.hashCode());
		}
		return hashKey;
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
		private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
		private static final int DEFAULT_COMPRESS_QUALITY = 100;

		protected CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

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

		/**
		 * 返回图片的压缩格式。
		 * 
		 * @return
		 */
		public CompressFormat getCompressFormat() {
			return compressFormat;
		}

		/**
		 * 设置图片的压缩格式。
		 * 
		 * @param compressFormat
		 */
		public void setCompressFormat(CompressFormat compressFormat) {
			this.compressFormat = compressFormat;
		}

		/**
		 * 返回图片的压缩质量。
		 * 
		 * @return
		 */
		public int getCompressQuality() {
			return compressQuality;
		}

		/**
		 * 设置图片的压缩质量。
		 * 
		 * @param compressQuality
		 */
		public void setCompressQuality(int compressQuality) {
			this.compressQuality = compressQuality;
		}
	}
}

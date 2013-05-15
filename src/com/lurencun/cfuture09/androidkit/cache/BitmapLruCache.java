/*
 * @(#)BitmapLruCache.java		       Project:androidkit
 * Date:2013-5-14
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
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.lurencun.cfuture09.androidkit.utils.io.IOUtils;
import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;

/**
 * 图片缓存类，使用内存缓存及磁盘缓存。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class BitmapLruCache extends AbstractLruCache<Bitmap> {
	private static final Log4AK log = Log4AK.getLog(BitmapLruCache.class);

	// 写入磁盘的压缩设置。
	/**
	 * 默认的压缩格式
	 */
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	/**
	 * 默认压缩质量。
	 */
	private static final int DEFAULT_COMPRESS_QUALITY = 70;
	/**
	 * 磁盘缓存索引。
	 */
	private static final int DISK_CACHE_INDEX = 0;

	public BitmapLruCache(ImageCacheParam cacheParam) {
		super(cacheParam);
	}

	protected void initMemoryCache() {
		mMemCache = new MemoryLruCache<String, Bitmap>(mCacheParam.memCacheSize) {
			protected int sizeOf(String key, Bitmap value) {
				return BitmapUtils.getByteCount(value);
			};
		};
	}

	public void put(String key, Bitmap bitmap) {
		if (key == null || bitmap == null) {
			throw new NullPointerException("key == null || bitmap == null");
		}

		if (mMemCache != null && mMemCache.get(key) == null) {
			mMemCache.put(key, bitmap);
		}

		synchronized (mDiskCacheLock) {
			if (mDiskCache == null) {
				return;
			}
			if (mDiskCache != null && mDiskCache.getDirectory() != null) {
				if (!mDiskCache.getDirectory().exists()) {
					mDiskCache.getDirectory().mkdirs();
				}
			}
			OutputStream out = null;
			try {
				DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
				if (snapshot == null) {
					final DiskLruCache.Editor editor = mDiskCache.edit(key);
					if (editor != null) {
						out = editor.newOutputStream(DISK_CACHE_INDEX);
						bitmap.compress(((ImageCacheParam) mCacheParam).compressFormat,
								((ImageCacheParam) mCacheParam).compressQuality, out);
						editor.commit();
						out.close();
					}
				} else {
					snapshot.getInputStream(DISK_CACHE_INDEX).close();
				}
			} catch (final IOException e) {
				log.e(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}

	/**
	 * Get bitmap from disk cache
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getFromDiskCache(String key) {
		synchronized (mDiskCacheLock) {
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mDiskCache != null) {
				InputStream inputStream = null;
				try {
					final DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
					if (snapshot != null) {
						inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
						if (inputStream != null) {
							final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
							return bitmap;
						}
					}
				} catch (final IOException e) {
					log.e(e.getMessage(), e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
					}
				}
			}
			return null;
		}
	}

	/**
	 * 设置压缩格式。
	 * 
	 * @param format
	 */
	public void setCompressFormat(CompressFormat format) {
		((ImageCacheParam) mCacheParam).setCompressFormat(format);
	}

	/**
	 * 图片缓存参数。
	 * 
	 * @author msdx
	 */
	public static class ImageCacheParam extends CacheParam {
		protected CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

		public ImageCacheParam(File diskCacheDir) {
			super(diskCacheDir);
		}

		public ImageCacheParam(String diskCacheDir) {
			super(diskCacheDir);
		}

		/**
		 * 设置磁盘缓存大小。
		 * 
		 * @param diskCacheSize
		 *            待设置的磁盘缓存大小，单位为字节。
		 */
		public void setDiskCacheSize(int diskCacheSize) {
			this.diskCacheSize = diskCacheSize;
		}

		/**
		 * 设置图片压缩格式。
		 * 
		 * @param format
		 *            被指定的图片压缩格式。
		 */
		public void setCompressFormat(CompressFormat format) {
			this.compressFormat = format;
		}
	}
}

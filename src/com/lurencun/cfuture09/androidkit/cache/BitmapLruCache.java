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

import com.lurencun.cfuture09.androidkit.utils.apk.ApplicationUtil;
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
public class BitmapLruCache {
	private static final Log4AK log = Log4AK.getLog(BitmapLruCache.class);

	/**
	 * 虚拟机最大内存
	 */
	private static final int VM_MAX_MEMORY = (int) ApplicationUtil.getMaxMemory();

	/**
	 * 默认内存缓存大小为虚拟机最大内存的6分之一(G14测试为6M)。
	 */
	private static final int DEFAULT_MEM_CACHE_SIZE = (int) (VM_MAX_MEMORY / 6);
	/**
	 * 默认磁盘缓存大小
	 */
	private static final int DEFAULT_DISK_CACHE_SIZE = 20 * 1024 * 1024;

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

	// 以下4个常量为了更容易地切换各个缓存。
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;
	private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;

	private DiskLruCache mDiskCache;
	private LruCache<String, Bitmap> mMemCache;
	private ImageCacheParam mCacheParam;
	private boolean mDiskCacheStarting = true;
	private final Object mDiskCacheLock = new Object();

	public BitmapLruCache(ImageCacheParam cacheParam) {
		mCacheParam = cacheParam;
		if (mCacheParam.memCacheEnabled) {
			mMemCache = new LruCache<String, Bitmap>(mCacheParam.memCacheSize) {
				protected int sizeOf(String key, Bitmap value) {
					return BitmapUtils.getByteCount(value);
				};
			};
		}
		if (mCacheParam.initDiskCacheOnCreate) {
			initDiskCache();
		}
	}

	private void initDiskCache() {
		synchronized (mDiskCacheLock) {
			if (mDiskCache == null || mDiskCache.isClosed()) {
				File diskCacheDir = mCacheParam.diskCacheDir;
				if (mCacheParam.diskCacheEnabled && diskCacheDir != null) {
					try {
						if (!diskCacheDir.exists()) {
							if (!diskCacheDir.mkdirs()) {
								throw new IOException("create disk cache directory failed");
							}
						}
					} catch (IOException e) {
						log.w(e);
					}

					if (IOUtils.getUsableSpace(diskCacheDir) > mCacheParam.diskCacheSize) {
						try {
							mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1,
									mCacheParam.diskCacheSize);
						} catch (IOException e) {
							mCacheParam.diskCacheDir = null;
							log.e("init disk cache directory failed", e);
						}
					}

				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
		}
	}

	public void put(String key, Bitmap bitmap) {
		if (key == null || bitmap == null) {
			throw new NullPointerException("key == null || bitmap == null");
		}

		if (mMemCache != null && mMemCache.get(key) == null) {
			mMemCache.put(key, bitmap);
		}

		synchronized (mDiskCacheLock) {
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
						bitmap.compress(mCacheParam.compressFormat, mCacheParam.compressQuality,
								out);
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
	 * Get from memory cache.
	 * 
	 * @param key
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return mMemCache != null ? mMemCache.get(key) : null;
	}

	/**
	 * Get bitmap from disk cache
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
	 * 回收所有内存及磁盘缓存。该操作应该在后台执行。
	 */
	public void evictAllCache() {
		evictAllMemCache();
		eviceAllDiskCache();
	}

	/**
	 * 回收所有的磁盘缓存。
	 */
	public void eviceAllDiskCache() {
		synchronized (mDiskCacheLock) {
			mDiskCacheStarting = true;
			if (mDiskCache != null && !mDiskCache.isClosed()) {
				try {
					mDiskCache.delete();
				} catch (IOException e) {
					log.e(e.getMessage(), e);
				}
				mDiskCache = null;
				initDiskCache();
			}
		}
	}

	/**
	 * 回收所有的内存缓存。
	 */
	public void evictAllMemCache() {
		if (mMemCache != null) {
			mMemCache.evictAll();
		}
	}

	/**
	 * 回收缓存，包括内存缓存及磁盘缓存。
	 * 
	 * @param key
	 */
	public void evictCache(String key) {
		evictMemCache(key);
		evictDiskCache(key);
	}

	/**
	 * 回收磁盘缓存。
	 * 
	 * @param key
	 */
	public void evictDiskCache(String key) {
		synchronized (mDiskCacheLock) {
			if (mDiskCache != null && !mDiskCache.isClosed()) {
				try {
					mDiskCache.remove(key);
				} catch (IOException e) {
					log.e(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 回收内存缓存。
	 * 
	 * @param key
	 */
	public void evictMemCache(String key) {
		if (mMemCache != null) {
			mMemCache.remove(key);
		}
	}

	/**
	 * 清空缓冲区以输出图片对象。
	 */
	public void flush() {
		synchronized (mDiskCacheLock) {
			if (mDiskCache != null) {
				try {
					mDiskCache.flush();
				} catch (IOException e) {
					log.e(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 关闭缓存。
	 */
	public void close() {
		synchronized (mDiskCacheLock) {
			IOUtils.closeQuietly(mDiskCache);
			mDiskCache = null;
		}
	}

	/**
	 * 设置压缩格式。
	 * 
	 * @param format
	 */
	public void setCompressFormat(CompressFormat format) {
		mCacheParam.setCompressFormat(format);
	}

	/**
	 * 图片缓存参数。
	 * 
	 * @author msdx
	 */
	public static class ImageCacheParam {
		/**
		 * 最小的内存缓存大小。
		 */
		private static final int MIN_MEM_CACHE_SIZE = 2 * 1024 * 1024;
		protected int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		protected int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		protected CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		protected int compressQuality = DEFAULT_COMPRESS_QUALITY;
		protected boolean memCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		protected boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		protected boolean cleanDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		protected boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;
		protected File diskCacheDir;

		public ImageCacheParam(File diskCacheDir) {
			this.diskCacheDir = diskCacheDir;
		}

		public ImageCacheParam(String diskCacheDir) {
			this.diskCacheDir = new File(diskCacheDir);
		}

		/**
		 * 设置内存缓存大小。
		 * 
		 * @param memCacheSize
		 *            待设置的内存缓存大小，单位为字节。
		 */
		public void setMemCacheSize(int memCacheSize) {
			if (memCacheSize < MIN_MEM_CACHE_SIZE || memCacheSize > VM_MAX_MEMORY * 0.8) {
				throw new IllegalArgumentException("the memCacheSize " + memCacheSize
						+ " is too small or large, it must be between " + MIN_MEM_CACHE_SIZE
						+ " and " + VM_MAX_MEMORY * 0.8);
			}
			this.memCacheSize = memCacheSize;
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

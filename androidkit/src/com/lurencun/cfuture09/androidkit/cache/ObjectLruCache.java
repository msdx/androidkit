/*
 * @(#)ObjectLruCache.java		       Project:androidkit
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.support.v4.util.LruCache;

import com.lurencun.cfuture09.androidkit.utils.io.IOUtils;
import com.lurencun.cfuture09.androidkit.utils.io.ObjectFileUtils;
import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class ObjectLruCache<T extends Serializable> {
    protected static final Log4AK log = Log4AK.getLog(ObjectLruCache.class);
    private static final int APP_VERSION = 1;
    /**
     * 磁盘缓存索引。
     */
    protected static final int DISK_CACHE_INDEX = 0;

    protected DiskLruCache mDiskCache;
    protected LruCache<String, T> mMemoryCache;
    protected CacheParams mCacheParams;
    protected boolean mDiskCacheStarting = true;
    protected final Object mDiskCacheLock = new Object();

    public ObjectLruCache(CacheParams params) {
        mCacheParams = params;
        initCache();
    }

    private void initCache() {
        if (mCacheParams.isMemCacheEnabled()) {
            initMemoryCache();
        }

        initDiskCache();
    }

    /**
     * 初始化内存缓存。如果需要得写计算内存缓存的大小，需重写此方法并对mMemCache初始化。
     */
    protected void initMemoryCache() {
        mMemoryCache = new LruCache<String, T>(mCacheParams.getMemCacheSize()) {
            /**
             * Measure item size in bytes rather than units which is more
             * practical for a bitmap cache
             */
            @Override
            protected int sizeOf(String key, T t) {
                return 1;
            }
        };
    }

    /**
     * 初始化磁盘缓存。
     */
    protected void initDiskCache() {
        synchronized (mDiskCacheLock) {
            if (mDiskCache == null || mDiskCache.isClosed()) {
                File diskCacheDir = mCacheParams.getDiskCacheDir();
                if (mCacheParams.isDiskCacheEnabled() && diskCacheDir != null) {
                    try {
                        mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, 1,
                                mCacheParams.getDiskCacheSize());
                    } catch (final IOException e) {
                        mCacheParams.setDiskCacheDir(null);
                        log.e("initDiskCache - " + e.getMessage(), e);
                    }
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    protected void addToMemoryCache(String key, T t) {
        // 添加到内存缓存。
        if (mMemoryCache != null && mMemoryCache.get(key) == null) {
            mMemoryCache.put(key, t);
        }
    }

    /**
     * 保存对象到缓存。如果{@code key}或{@code t}为空，则不存入。
     * 
     * @param key
     * @param t
     */
    public void add(String key, T t) {
        if (key == null || t == null) {
            return;
        }

        addToMemoryCache(key, t);

        synchronized (mDiskCacheLock) {
            // 添加到硬盘缓存
            if (mDiskCache != null && mDiskCache.getDirectory() != null) {
                OutputStream out = null;
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                    if (snapshot == null) {
                        final DiskLruCache.Editor editor = mDiskCache.edit(key);
                        if (editor != null) {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            ObjectFileUtils.writeObject(t, out);
                            editor.commit();
                        }
                    } else {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                } catch (Exception e) {
                    log.e("addBitmapToCache - " + e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
        }
    }

    /**
     * 从内存中获取缓存。
     * 
     * @param key
     *            存入缓存的key
     * @return 返回获取的对象。
     */
    public T getFromMemoryCache(String key) {
        return mMemoryCache == null ? null : mMemoryCache.get(key);
    }

    /**
     * 获取硬盘缓存
     * 
     * @param key
     *            存入缓存的key
     * @return 返回获取的缓存对象。
     */
    @SuppressWarnings("unchecked")
    public T getFromDiskCache(String key) {
        if (mDiskCache == null) {
            return null;
        }
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
                        return (T) (ObjectFileUtils.readObject(inputStream));
                    }
                } catch (final Exception e) {
                    log.e("getBitmapFromDiskCache - " + e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
            return null;
        }
    }

    /**
     * 从内存或磁盘中获取缓存。
     * 
     * @param key
     * @return
     */
    public T get(String key) {
        T t = getFromMemoryCache(key);
        if (t == null) {
            t = getFromDiskCache(key);
            if (t == null) {
                return null;
            }
            addToMemoryCache(key, t);
        }
        return t;
    }

    /**
     * 清空缓存。
     */
    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    /**
     * 清空内存缓存。
     */
    public void clearMemoryCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }

    /**
     * 清空磁盘缓存。
     */
    public void clearDiskCache() {
        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskCache != null && !mDiskCache.isClosed()) {
                try {
                    mDiskCache.delete();
                } catch (IOException e) {
                    log.e("clearCache - " + e.getMessage(), e);
                }
                mDiskCache = null;
                initDiskCache();
            }
        }
    }

    /**
     * 清除某个缓存对象。
     * 
     * @param key
     */
    public void clearCache(String key) {
        clearMemoryCache(key);
        clearDiskCache(key);
    }

    /**
     * 清除某个对象在磁盘的缓存。
     * 
     * @param key
     */
    public void clearDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null && !mDiskCache.isClosed()) {
                try {
                    mDiskCache.remove(key);
                } catch (IOException e) {
                    log.e("clearCache - " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 清除某个对象在内存的缓存。
     * 
     * @param key
     */
    public void clearMemoryCache(String key) {
        if (mMemoryCache != null) {
            mMemoryCache.remove(key);
        }
    }

    /**
     * Flushes the disk cache associated with this ImageCache object. Note that
     * this includes disk access so this should not be executed on the main/UI
     * thread.
     */
    public void flush() {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    mDiskCache.flush();
                } catch (IOException e) {
                    log.e("flush - " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 关闭缓存。
     */
    public void close() {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    if (!mDiskCache.isClosed()) {
                        mDiskCache.close();
                        mDiskCache = null;
                    }
                } catch (IOException e) {
                    log.e("close - " + e.getMessage(), e);
                }
            }
        }
    }
}

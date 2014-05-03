/*
 * @(#)CacheParams.java		       Project:androidkit
 * Date:2013-6-15
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

import com.lurencun.cfuture09.androidkit.utils.apk.ApplicationUtil;

/**
 * 缓存参数类，用于配置缓存的相关设置。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class CacheParams {
	/**
	 * 虚拟机最大内存(单位B)
	 */
	protected static final int VM_MAX_MEMORY = (int) ApplicationUtil.getMaxMemory();

	/**
	 * 默认内存缓存大小为虚拟机最大内存的六分之一。
	 */
	protected static final int DEFAULT_MEM_CACHE_SIZE = (int) (VM_MAX_MEMORY / 6);
	/**
	 * 默认磁盘缓存大小(字节）
	 */
	protected static final int DEFAULT_DISK_CACHE_SIZE = 20 * 1024 * 1024;
	// 以下4个常量为了更容易地切换各个缓存。
	protected static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	protected static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	protected static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;
	protected static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;
	/**
	 * 最小的内存缓存大小（字节）。
	 */
	private static final int MIN_MEM_CACHE_SIZE = 2 * 1024 * 1024;
	/**
	 * 内存缓存大小
	 */
	protected int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
	/**
	 * 磁盘缓存大小
	 */
	protected int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
	/**
	 * 是否启用内存缓存
	 */
	protected boolean memCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
	/**
	 * 是否启用磁盘缓存
	 */
	protected boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
	/**
	 * 是否在使用磁盘缓存前清空缓存
	 */
	protected boolean cleanDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
	/**
	 * 在创建时初始化磁盘缓存
	 */
	protected boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;
	/**
	 * 磁盘缓存路径
	 */
	protected File diskCacheDir;

	public CacheParams(File diskCacheDir) {
		this.diskCacheDir = diskCacheDir;
	}

	public CacheParams(String diskCacheDir) {
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
					+ " is too small or large, it must be between " + MIN_MEM_CACHE_SIZE + " and "
					+ VM_MAX_MEMORY * 0.8);
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
	 * 是否启用内存缓存。
	 * 
	 * @return 当且仅当启用内存缓存时返回true,否则返回false。
	 */
	public boolean isMemCacheEnabled() {
		return memCacheEnabled;
	}

	/**
	 * 设置是否启用内存缓存。
	 * 
	 * @param memCacheEnabled
	 *            是否启用内存缓存。
	 */
	public void setMemCacheEnabled(boolean memCacheEnabled) {
		this.memCacheEnabled = memCacheEnabled;
	}

	/**
	 * 返回是否启用磁盘缓存。
	 * 
	 * @return 当且仅当启用磁盘缓存时返回true,否则返回false.
	 */
	public boolean isDiskCacheEnabled() {
		return diskCacheEnabled;
	}

	/**
	 * 设置是否启用磁盘缓存。
	 * 
	 * @param diskCacheEnabled
	 *            是否启用磁盘缓存。
	 */
	public void setDiskCacheEnabled(boolean diskCacheEnabled) {
		this.diskCacheEnabled = diskCacheEnabled;
	}

	/**
	 * 返回是否在开始时清空磁盘缓存。
	 * 
	 * @return 如果设置为在开始时清空缓存则返回true，否则返回false。
	 */
	public boolean isCleanDiskCacheOnStart() {
		return cleanDiskCacheOnStart;
	}

	/**
	 * 设置是否在开始时清空磁盘缓存。
	 * 
	 * @param cleanDiskCacheOnStart
	 *            true if clean disk cache on start.
	 */
	public void setCleanDiskCacheOnStart(boolean cleanDiskCacheOnStart) {
		this.cleanDiskCacheOnStart = cleanDiskCacheOnStart;
	}

	/**
	 * 返回是否在创建缓存时进行磁盘缓存初始化。
	 * 
	 * @return true if initial disk cache on create.
	 */
	public boolean isInitDiskCacheOnCreate() {
		return initDiskCacheOnCreate;
	}

	/**
	 * 设置是否需要在创建缓存时进行磁盘缓存初始化
	 * 
	 * @param initDiskCacheOnCreate
	 *            true if need initial disk cache on create.
	 */
	public void setInitDiskCacheOnCreate(boolean initDiskCacheOnCreate) {
		this.initDiskCacheOnCreate = initDiskCacheOnCreate;
	}

	/**
	 * 返回磁盘缓存的路径。
	 * 
	 * @return 磁盘缓存的路径。
	 */
	public File getDiskCacheDir() {
		return diskCacheDir;
	}

	/**
	 * 设置磁盘缓存的路径。
	 * 
	 * @param diskCacheDir
	 *            要设置的磁盘缓存的路径。
	 */
	public void setDiskCacheDir(File diskCacheDir) {
		this.diskCacheDir = diskCacheDir;
	}

	/**
	 * 返回内存缓存的大小。
	 * 
	 * @return 内存缓存的大小。
	 */
	public int getMemCacheSize() {
		return memCacheSize;
	}

	/**
	 * 返回磁盘缓存的大小。
	 * 
	 * @return 磁盘缓存的大小。
	 */
	public int getDiskCacheSize() {
		return diskCacheSize;
	}
}
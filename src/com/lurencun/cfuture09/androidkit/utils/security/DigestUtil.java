/*
 * @(#)DigestUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-12-18
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
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
package com.lurencun.cfuture09.androidkit.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class DigestUtil {
	/**
	 * 获取一个字符串加密后的16进制值
	 * 
	 * @param algorithm
	 *            摘要算法
	 * @param message
	 *            进行加密的String对象
	 * 
	 * @return String 计算后的结果
	 */
	public static String doDigest(String algorithm, String message) {
		return doDigest(algorithm, message.getBytes());
	}

	/**
	 * 获取一个字符串加密后的16进制值
	 * 
	 * @param algorithm
	 *            摘要算法
	 * @param message
	 *            进行加密的byte数组
	 * 
	 * @return String 计算后的结果
	 */
	public static String doDigest(String algorithm, byte[] message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (message != null) {
			byte[] result = md.digest(message);
			return StringUtil.bytesToHexString(result);
		}
		return null;
	}

	/**
	 * 获取一个字符串加密后的16进制值
	 * 
	 * @param algorithm
	 *            摘要算法名称
	 * @param message
	 *            进行加密的byte数组
	 * 
	 * @return String 计算后的结果
	 */
	public static String doDigest(String algorithm, char[] message) {
		return doDigest(algorithm, new String(message));
	}
}

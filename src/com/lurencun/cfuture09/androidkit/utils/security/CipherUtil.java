/*
 * @(#)CipherUtil.java		       Project:androidkit
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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;
import com.lurencun.cfuture09.androidkit.utils.net.DownloadUtil;

/**
 * 可逆加解密类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class CipherUtil {
	private static final Log4AK log = Log4AK.getLog(DownloadUtil.class);
	public static final String ALGORITHM_DES = "DES";

	/**
	 * 返回可逆算法DES的密钥
	 * 
	 * @param key
	 *            前8字节将被用来生成密钥。
	 * @return 生成的密钥
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key getDESKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		DESKeySpec des = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
		return keyFactory.generateSecret(des);
	}

	/**
	 * 根据指定的密钥及算法，将字符串进行解密。
	 * 
	 * @param data
	 *            要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
	 * @param key
	 *            密钥。
	 * @param algorithm
	 *            算法。
	 * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
	 */
	public static String decrypt(String data, Key key, String algorithm) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key);
			String result = new String(cipher.doFinal(StringUtil.hexStringToBytes(data)), "utf8");
			return result;
		} catch (Exception e) {
			log.w(e);
		}
		return null;
	}

	/**
	 * 根据指定的密钥及算法对指定字符串进行可逆加密。
	 * 
	 * @param data
	 *            要进行加密的字符串。
	 * @param key
	 *            密钥。
	 * @param algorithm
	 *            算法。
	 * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
	 */
	public static String encrypt(String data, Key key, String algorithm) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return StringUtil.bytesToHexString(cipher.doFinal(data.getBytes("utf8")));
		} catch (Exception e) {
			log.w(e);
		}
		return null;
	}
}

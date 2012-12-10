/*
 * @(#)ObjectFileUtils.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-8-23
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
package com.lurencun.cfuture09.androidkit.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ObjectFileUtils {
	/**
	 * 写入对象。
	 * 
	 * @param object
	 * @param path
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void writeObject(final Serializable object, final String path)
			throws FileNotFoundException, IOException {
		writeObject(object, new FileOutputStream(path));
	}

	/**
	 * 写入对象。
	 * 
	 * @param object
	 * @param path
	 * @throws IOException
	 */
	public static void writeObject(final Serializable object, final OutputStream os)
			throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(os);
			oos.writeObject(object);
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * 读出对象
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws StreamCorruptedException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(String path)
			throws StreamCorruptedException, FileNotFoundException,
			IOException, ClassNotFoundException {
		return readObject(new FileInputStream(path));
	}

	/**
	 * 读出对象。
	 * 
	 * @param is
	 * @return
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(InputStream is)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(is);
			return ois.readObject();
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(is);
		}
	}
}

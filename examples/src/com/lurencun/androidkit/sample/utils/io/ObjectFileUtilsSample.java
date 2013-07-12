/*
 * @(#)ObjectFileUtilsSample.java		       Project:com.sinaapp.msdxblog.andoridkit.sample
 * Date:2012-9-12
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
package com.lurencun.androidkit.sample.utils.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import com.lurencun.cfuture09.androidkit.utils.io.ObjectFileUtils;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ObjectFileUtilsSample {

	/**
	 * 将对象写入文件。此对象必须实现Serializable接口。
	 * 
	 * @param object
	 *            对象。
	 * @param path
	 *            文件路径。
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeObject(final Serializable object, final String path)
			throws FileNotFoundException, IOException {
		ObjectFileUtils.writeObject(object, new FileOutputStream(path));
	}

	/**
	 * 从文件中读取对象。
	 * 
	 * @param object
	 *            对象。
	 * @param path
	 *            文件路径。
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws StreamCorruptedException
	 */
	public static Object readObject(final String path)
			throws StreamCorruptedException, FileNotFoundException,
			IOException, ClassNotFoundException {
		Object object = ObjectFileUtils.readObject(path);
		return object;
	}

}

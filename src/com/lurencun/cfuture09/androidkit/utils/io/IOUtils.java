/*
 * @(#)IOUtils.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-5-6
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * 此文件代码完全抽取自apache开源项目commons中的commons-io包。
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
package com.lurencun.cfuture09.androidkit.utils.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * 此文件代码完全抽取自apache开源项目commons中的commons-io包。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class IOUtils {
	private static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.closeQuietly(closeable);
	 * }
	 * </pre>
	 * 
	 * @param closeable
	 *            the object to close, may be null or already closed
	 * @since Commons IO 2.0
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ioe) {
				// ignore
			}
		}
	}

	/**
	 * 将输入流转换为字符串。默认采用UTF-8编码。
	 * 
	 * @param in
	 *            输入流
	 * @return 转换之后的字符串。
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream in) throws IOException {
		return readFully(new InputStreamReader(in, UTF_8));
	}

	/**
	 * 将输入流转换为字符串。
	 * 
	 * @param in
	 *            输入流
	 * @param charset
	 *            字符编码。
	 * @return 转换之后的字符串。
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream in, Charset charset) throws IOException {
		return readFully(new InputStreamReader(in, charset));
	}

	/**
	 * 以字符串类型返回{@code reader}的剩下的内容。
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String readFully(Reader reader) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			int count;
			while ((count = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, count);
			}
			return writer.toString();
		} finally {
			reader.close();
		}
	}
}

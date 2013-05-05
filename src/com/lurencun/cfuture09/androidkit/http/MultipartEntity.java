/*  
 * @(#)MultipartEntity.java              Project:androidkit  
 * Date:2013-5-5  
 *  
 * Copyright (c) 2013 Geek_Soledad.  
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

package com.lurencun.cfuture09.androidkit.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import android.util.Log;

import com.lurencun.cfuture09.androidkit.utils.io.IOUtils;
import com.lurencun.cfuture09.androidkit.utils.lang.LogTag;

/**
 * 以下代码参考自文章：http://blog.rafaelsanches.com/2011/01/29/upload
 * -using-multipart-post-using-httpclient-in-android/<br/>
 * Multipart/form coded HTTP entity consisting of multiple body parts.
 * 
 */
public class MultipartEntity implements HttpEntity {

	/**
	 * ASCII的字符池，用于生成分界线。
	 */
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private ByteArrayOutputStream out;
	/**
	 * 是否设置了开头的分界线
	 */
	boolean isSetFirst = false;
	/**
	 * 是否设置了最后的分界线
	 */
	boolean isSetLast = false;

	/**
	 * 分界线
	 */
	private String boundary;

	/**
	 * Creates an instance using mode {@link HttpMultipartMode#STRICT}
	 */
	public MultipartEntity() {
		super();
		out = new ByteArrayOutputStream();
		boundary = generateBoundary();
	}

	protected String generateContentType(final String boundary, final Charset charset) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("multipart/form-data; boundary=");
		buffer.append(boundary);
		if (charset != null) {
			buffer.append("; charset=");
			buffer.append(charset.name());
		}
		return buffer.toString();
	}

	/**
	 * 生成分界线
	 * 
	 * @return 返回生成的分界线
	 */
	protected String generateBoundary() {
		StringBuilder buffer = new StringBuilder();
		Random rand = new Random();
		int count = rand.nextInt(11) + 30; // a random size from 30 to 40
		for (int i = 0; i < count; i++) {
			buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		return buffer.toString();
	}

	public void writeFirstBoundaryIfNeeds() {
		if (!isSetFirst) {
			try {
				out.write(("--" + boundary + "\r\n").getBytes());
			} catch (final IOException e) {
				Log.e(LogTag.tag(this), e.getMessage(), e);
			}
		}
		isSetFirst = true;
	}

	public void writeLastBoundaryIfNeeds() {
		if (!isSetLast) {
			try {
				out.write(("\r\n--" + boundary + "--\r\n").getBytes());
			} catch (final IOException e) {
				Log.e(LogTag.tag(this), e.getMessage(), e);
			}
			isSetLast = true;
		}
	}

	public void addPart(final String key, final String value) {
		writeFirstBoundaryIfNeeds();
		try {
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n").getBytes());
			out.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
			out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
			out.write(value.getBytes());
			out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (final IOException e) {
			Log.e(LogTag.tag(this), e.getMessage(), e);
		}
	}

	public void addPart(final String key, final String fileName, final InputStream fin) {
		addPart(key, fileName, fin, "application/octet-stream");
	}

	public void addPart(final String key, final String fileName, final InputStream fin, String type) {
		writeFirstBoundaryIfNeeds();
		try {
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
					+ fileName + "\"\r\n").getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			final byte[] tmp = new byte[4096];
			int l = 0;
			while ((l = fin.read(tmp)) != -1) {
				out.write(tmp, 0, l);
			}
			out.flush();
		} catch (final IOException e) {
			Log.e(LogTag.tag(this), e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fin);
		}
	}

	public void addPart(final String key, final File value) {
		try {
			addPart(key, value.getName(), new FileInputStream(value));
		} catch (final FileNotFoundException e) {
			Log.e(LogTag.tag(this), e.getMessage(), e);
		}
	}

	@Override
	public long getContentLength() {
		writeLastBoundaryIfNeeds();
		return out.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		outstream.write(out.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	public void consumeContent() throws IOException, UnsupportedOperationException {
		if (isStreaming()) {
			throw new UnsupportedOperationException(
					"Streaming entity does not implement #consumeContent()");
		}
	}

	public InputStream getContent() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"Multipart form entity does not implement #getContent()");
	}

}

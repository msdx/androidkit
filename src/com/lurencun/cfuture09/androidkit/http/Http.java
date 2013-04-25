/*
 * @(#)Http.java		       Project:androidkit
 * Date:2013-4-21
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
package com.lurencun.cfuture09.androidkit.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lurencun.cfuture09.androidkit.utils.lang.IdIntGenerator;
import com.lurencun.cfuture09.androidkit.utils.thread.HandlerFactory;

/**
 * 封装简单的HTTP请求，包括GET,POST,PUT,DELETE。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class Http {
	private static IdIntGenerator idGenerator = new IdIntGenerator();

	/**
	 * 向指定的URI发起一个GET请求并以String类型返回数据。
	 * 
	 * @param uri
	 *            指定的URI
	 * @return 返回的String对象内容。
	 * @throws IOException
	 */
	public static String get(String uri) throws IOException {
		HttpGet request = new HttpGet(uri);
		return sendRequest(request);
	}

	/**
	 * 向指定的URI发起一个GET请求并以String类型返回数据，并且不抛出其可能发生的异常。
	 * 
	 * @param uri
	 *            指定的URI。
	 * @return 返回结果的String对象。当发生IO异常时返回null。
	 */
	public static String getIgnoreException(String uri) {
		HttpGet request = new HttpGet(uri);
		return sendRequestIgnoreException(request);
	}

	/**
	 * 异步发起get请求。
	 * 
	 * @param uri
	 *            请求的URI。
	 * @param l
	 *            请求完成的回调接口。
	 */
	public static void getOnAsyn(final String uri, final HttpListener l) {
		HttpGet request = new HttpGet(uri);
		sendRequestAsyn(request, l);
	}

	/**
	 * 向指定的URI发起一个POST请求并以String类型返回数据。
	 * 
	 * @param uri
	 *            指定的URI
	 * @return 返回的String对象内容。
	 * @throws IOException
	 */
	public static String post(String uri) throws IOException {
		HttpPost request = new HttpPost(uri);
		return sendRequest(request);
	}

	/**
	 * 向指定 的URI发起一个POST请求并以String类型返回数据，并且不抛出其可能发生的异常。
	 * 
	 * @param uri
	 *            指定的URI
	 * @return 以String对象返回请求的结果。如果发生IO异常，则返回null。
	 */
	public static String postIgnoreException(String uri) {
		HttpPost request = new HttpPost(uri);
		return sendRequestIgnoreException(request);
	}

	/**
	 * 发起一个POST请求并以String类型返回数据。
	 * 
	 * @param uri
	 *            指定的URI
	 * @param params
	 *            请求参数
	 * @return 以String对象返回请求的结果。
	 * @throws IOException
	 */
	public static String post(String uri, BasicParams params) throws IOException {
		HttpPost request = new HttpPost(uri);
		if (params != null) {
			request.setEntity(new UrlEncodedFormEntity(params.getPairs()));
		}
		return sendRequest(request);
	}

	/**
	 * 异步发起一个post请求。
	 * 
	 * @param uri
	 *            指定的URI
	 * @param params
	 *            请求参数。
	 * @param l
	 *            请求成功或失败的回调接口。
	 */
	public static void postOnAsyn(String uri, BasicParams params, HttpListener l) {
		HttpPost request = new HttpPost(uri);
		sendRequestAsyn(request, params, l);
	}

	/**
	 * 异步发起post请求。
	 * 
	 * @param uri
	 *            请求的URI
	 * @param l
	 *            请求结果的回调接口。
	 */
	public static void postOnAsyn(final String uri, final HttpListener l) {
		HttpPost request = new HttpPost(uri);
		sendRequestAsyn(request, l);
	}

	/**
	 * 向指定的URI发起一个PUT请求。
	 * 
	 * @param uri
	 *            指定的URI。
	 * @return 返回请求的结果。
	 * @throws IOException
	 */
	public static String put(String uri) throws IOException {
		HttpPut request = new HttpPut(uri);
		return sendRequest(request);
	}

	/**
	 * 发起一个PUT请求。
	 * 
	 * @param uri
	 *            指定的URI
	 * @param params
	 *            请求参数
	 * @return 返回请求的结果。
	 * @throws IOException
	 */
	public static String put(String uri, BasicParams params) throws IOException {
		HttpPut request = new HttpPut(uri);
		if (params != null) {
			request.setEntity(new UrlEncodedFormEntity(params.getPairs()));
		}
		return sendRequest(request);
	}

	/**
	 * 向指定的URI发起一个PUT请求，且不抛出可能发生的异常。
	 * 
	 * @param uri
	 *            指定的URI
	 * @return 返回请求的结果，如果发生异常则返回null。
	 */
	public static String putIgnoreException(String uri) {
		HttpPut request = new HttpPut(uri);
		return sendRequestIgnoreException(request);
	}

	/**
	 * 异步向一个指定的URI发起一个PUT请求。
	 * 
	 * @param uri
	 *            指定的URI.
	 * @param l
	 *            请求完成后的回调接口。
	 */
	public static void putOnAsyn(final String uri, final HttpListener l) {
		HttpPut put = new HttpPut(uri);
		sendRequestAsyn(put, l);
	}

	/**
	 * 异步发起一个PUT请求。
	 * 
	 * @param uri
	 *            指定的URI
	 * @param params
	 *            请求参数
	 * @param l
	 *            请求完成后的回调接口。
	 */
	public static void putOnAsyn(String uri, BasicParams params, HttpListener l) {
		HttpPut request = new HttpPut(uri);
		sendRequestAsyn(request, params, l);
	}

	/**
	 * 向指定的URI发起一个DELETE请求。
	 * 
	 * @param uri
	 *            指定的URI.
	 * @return 返回请求的结果。
	 * @throws IOException
	 */
	public static String delete(String uri) throws IOException {
		HttpDelete request = new HttpDelete(uri);
		return sendRequest(request);
	}

	/**
	 * 向指定的URI发起一个DELETE请求，且不抛出可能发生的异常。
	 * 
	 * @param uri
	 *            指定的URI.
	 * @return 返回请求的结果。如果发生了异常，则返回null。
	 */
	public static String deleteIgnoreException(String uri) {
		HttpDelete request = new HttpDelete(uri);
		return sendRequestIgnoreException(request);
	}

	/**
	 * 异步向指定的URI发起一个DELETE请求。
	 * 
	 * @param uri
	 *            指定的URI.
	 * @param l
	 *            请求完成的回调接口。
	 */
	public static void deleteOnAsyn(String uri, HttpListener l) {
		HttpDelete request = new HttpDelete(uri);
		sendRequestAsyn(request, l);
	}

	/**
	 * 执行HTTP URI请求，并以String对象返回内容。
	 * 
	 * @param request
	 *            HTTP URI请求
	 * @return 返回内容的String对象。
	 * @throws IOException
	 *             请求或转换过程发生的IO异常。
	 */
	private static String sendRequest(HttpUriRequest request) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		return EntityUtils.toString(response.getEntity());
	}

	/**
	 * 执行HTTP请求，并以String对象返回内容，且不抛出其可能发生的异常。
	 * 
	 * @param request
	 *            HTTP URI 请求对象。
	 * @return 以String对象返回请求的结果。如果发生异常，则返回null。
	 */
	private static String sendRequestIgnoreException(HttpUriRequest request) {
		try {
			return sendRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 异步执行HTTP 请求。
	 * 
	 * @param request
	 *            要执行的请求。
	 */
	private static void sendRequestAsyn(HttpUriRequest request, HttpListener l) {
		HandlerFactory.newBackgroundHandler(idGenerator.nextId() + "").post(
				new RequestRunnable(request, l));
	}

	/**
	 * 异步执行HTTP请求。
	 * 
	 * @param request
	 *            要执行的请求
	 * @param params
	 *            请求参数
	 * @param l
	 *            请求成功或失败的回调接口。
	 */
	private static void sendRequestAsyn(HttpUriRequest request, BasicParams params, HttpListener l) {
		HandlerFactory.newBackgroundHandler(idGenerator.nextId() + "").post(
				new RequestRunnable(request, params.getPairs(), l));
	}

	/**
	 * @author msdx
	 * @Function 执行Http请求的Runnable对象。
	 */
	static class RequestRunnable implements Runnable {
		private HttpUriRequest mRequest;
		private HttpListener mListener;
		private List<NameValuePair> mParams;

		RequestRunnable(HttpUriRequest request, HttpListener l) {
			mRequest = request;
			mListener = l;
		}

		public RequestRunnable(HttpUriRequest request, List<NameValuePair> params, HttpListener l) {
			mRequest = request;
			mParams = params;
			mListener = l;
		}

		@Override
		public void run() {
			try {
				if (mParams != null && mRequest instanceof HttpEntityEnclosingRequestBase) {
					((HttpEntityEnclosingRequestBase) mRequest).setEntity(new UrlEncodedFormEntity(
							mParams));
				}
				String result = sendRequest(mRequest);
				mListener.onFinish(result);
			} catch (IOException e) {
				e.printStackTrace();
				mListener.onFailed(e.getMessage());
			}
		}
	}

}

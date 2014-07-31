/*
 * @(#)AsyncHttpRequest.java		       Project:androidkit
 * Date:2013-5-8
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
package com.lurencun.cfuture09.androidkit.http.async;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
class AsyncHttpRequest implements Runnable {
	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final AsyncHttpResponseHandler responseHandler;
	private boolean isBinaryRequest;
	private int executionCount;

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request,
			AsyncHttpResponseHandler responseHandler) {
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
		if (responseHandler instanceof BinaryHttpResponseHandler) {
			this.isBinaryRequest = true;
		}
	}

	@Override
	public void run() {
		try {
			if (responseHandler != null) {
				responseHandler.sendStartMessage();
			}

			makeRequestWithRetries();

			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
			}
		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
				if (isBinaryRequest) {
					responseHandler.sendFailureMessage(e, (byte[]) null);
				} else {
					responseHandler.sendFailureMessage(e, (String) null);
				}
			}
		}
	}

	private void makeRequest() throws IOException {
		if (!Thread.currentThread().isInterrupted()) {
			try {
				HttpResponse response = client.execute(request, context);
				if (!Thread.currentThread().isInterrupted()) {
					if (responseHandler != null) {
						responseHandler.sendResponseMessage(response);
					}
				} else {
					// TODO: should raise InterruptedException? this block is
					// reached whenever the request is cancelled before its
					// response is received
				}
			} catch (IOException e) {
				if (!Thread.currentThread().isInterrupted()) {
					throw e;
				}
			}
		}
	}

	private void makeRequestWithRetries() throws ConnectException {
		// This is an additional layer of retry logic lifted from droid-fu
		// See:
		// https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
		while (retry) {
			try {
				makeRequest();
				return;
			} catch (UnknownHostException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketException e) {
				// Added to detect host unreachable
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketTimeoutException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "socket time out");
				}
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount, context);
			} catch (NullPointerException e) {
				// there's a bug in HttpClient 4.0.x that on some occasions
				// causes
				// DefaultRequestExecutor to throw an NPE, see
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount, context);
			}
		}

		// no retries left, crap out with exception
		ConnectException ex = new ConnectException();
		ex.initCause(cause);
		throw ex;
	}
}

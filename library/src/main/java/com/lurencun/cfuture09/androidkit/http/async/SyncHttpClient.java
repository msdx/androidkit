/*
 * @(#)SyncHttpClient.java		       Project:androidkit
 * Date:2013-5-9
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

import android.content.Context;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public abstract class SyncHttpClient extends AsyncHttp {
	private int responseCode;
	/*
	 * as this is a synchronous request this is just a helping mechanism to pass
	 * the result back to this method. Therefore the result object has to be a
	 * field to be accessible
	 */
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

		@Override
		void sendResponseMessage(HttpResponse response) {
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response);
		};

		@Override
		protected void sendMessage(Message msg) {
			/*
			 * Dont use the handler and send it directly to the analysis
			 * (because its all the same thread)
			 */
			handleMessage(msg);
		}

		@Override
		public void onSuccess(String content) {
			result = content;
		}

		@Override
		public void onFailure(Throwable error, String content) {
			result = onRequestFailed(error, content);
		}
	};

	/**
	 * @return the response code for the last request, might be usefull
	 *         sometimes
	 */
	public int getResponseCode() {
		return responseCode;
	}

	// Private stuff
	@Override
	protected void sendRequest(DefaultHttpClient client, HttpContext httpContext,
			HttpUriRequest uriRequest, String contentType,
			AsyncHttpResponseHandler responseHandler, Context context) {
		if (contentType != null) {
			uriRequest.addHeader("Content-Type", contentType);
		}

		/*
		 * will execute the request directly
		 */
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler).run();
	}

	public abstract String onRequestFailed(Throwable error, String content);

	public void delete(String url, RequestParams queryParams,
			AsyncHttpResponseHandler responseHandler) {
		// TODO what about query params??
		delete(url, responseHandler);
	}

	public String get(String url, RequestParams params) {
		this.get(url, params, responseHandler);
		/*
		 * the response handler will have set the result when this line is
		 * reached
		 */
		return result;
	}

	public String get(String url) {
		this.get(url, null, responseHandler);
		return result;
	}

	public String put(String url, RequestParams params) {
		this.put(url, params, responseHandler);
		return result;
	}

	public String put(String url) {
		this.put(url, null, responseHandler);
		return result;
	}

	public String post(String url, RequestParams params) {
		this.post(url, params, responseHandler);
		return result;
	}

	public String post(String url) {
		this.post(url, null, responseHandler);
		return result;
	}

	public String delete(String url, RequestParams params) {
		this.delete(url, params, responseHandler);
		return result;
	}

	public String delete(String url) {
		this.delete(url, null, responseHandler);
		return result;
	}

}

/*
 * @(#)HttpCallback.java		       Project:androidkit
 * Date:2013-5-7
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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 异步Http的回调接口。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class AsyncHttpResponseHandler {
	protected static final int SUCCESS_MESSAGE = 0;
	protected static final int FAILURE_MESSAGE = 1;
	protected static final int START_MESSAGE = 2;
	protected static final int FINISH_MESSAGE = 3;

	private Handler handler;

	public AsyncHttpResponseHandler() {
		// 该handler用于发送事件到当前线程
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					AsyncHttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}

	/**
	 * 该方法将于request开始时被调用。在你的代码当中重写该方法以处理。
	 */
	public void onStart() {
	}

	/**
	 * 该方法将在request完成后被调用，不管请求是成功还是失败。在你的代码当中重写该方法以处理。
	 */
	public void onFinish() {
	}

	/**
	 * 当request成功到达时被调用。在你的代码当中重写该方法以处理。
	 * 
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(String content) {
	}

	/**
	 * 当request成功到达时被调用。在你的代码当中重写该方法以处理。
	 * 
	 * @param status
	 *            response的状态码
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int status, String content) {
		onSuccess(content);
	}

	/**
	 * 当request成功到达时被调用。在你的代码当中重写该方法以处理。
	 * 
	 * @param status
	 *            response的状态码。
	 * @param headers
	 *            Http the headers of the HTTP response
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int status, Header[] headers, String content) {
		onSuccess(status, content);
	}

	/**
	 * 该方法在请求失败时被调用。在你的代码当中重写该方法以进行处理。
	 * 
	 * @param e
	 *            可能引起失败的异常。
	 */
	public void onFailure(Throwable e) {
	}

	/**
	 * 该方法在request失败时被调用。在你的代码当中重写该方法以处理。
	 * 
	 * @param e
	 *            可能引起失败的异常。
	 * @param content
	 *            response的主体，如果有的话。
	 */
	public void onFailure(Throwable e, String content) {
		onFailure(e);
	}

	// 在后台线程池中的线程中执行
	protected void sendSuccessMessage(int status, Header[] headers, String responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] { Integer.valueOf(status), headers,
				responseBody }));
	}

	protected void sendFailureMessage(Throwable e, String responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e, responseBody }));
	}

	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e, responseBody }));
	}

	protected void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	protected void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}

	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	protected void handleMessage(Message msg) {
		Object[] response;
		switch (msg.what) {
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(), (Header[]) response[1],
					(String) response[2]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0], (String) response[1]);
			break;
		default:
		}
	}

	protected void handleSuccessMessage(int statusCode, Header[] headers, String responseBody) {
		onSuccess(statusCode, headers, responseBody);
	}

	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
	}

	// 异步HTTP请求的接口。
	void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		String responseBody = null;
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
				responseBody = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			sendFailureMessage(e, (String) null);
		}

		if (status.getStatusCode() >= 300) {
			sendFailureMessage(
					new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),
					responseBody);
		} else {
			sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
		}
	}

}

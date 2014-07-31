/*
 * @(#)EntityHttpResponseHandler.java		       Project:androidkit
 * Date:2013-5-28
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
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class EntityHttpResponseHandler extends AsyncHttpResponseHandler {

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
			sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(),
					response.getEntity());
		}
	}

	protected void sendSuccessMessage(int status, Header[] headers, HttpEntity entity) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] { Integer.valueOf(status), headers,
				entity }));
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
					(HttpEntity) response[2]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0], (String) response[1]);
			break;
		default:
		}
	}

	protected void handleSuccessMessage(int statusCode, Header[] headers, HttpEntity responseEntity) {
		onSuccess(statusCode, headers, responseEntity);
	}

	public void onSuccess(int status, Header[] headers, HttpEntity responseEntity) {
		onSuccess(status, responseEntity);
	}

	public void onSuccess(int status, HttpEntity responseEntity) {
		onSuccess(responseEntity);
	}

	public void onSuccess(HttpEntity responseEntity) {
	}

}

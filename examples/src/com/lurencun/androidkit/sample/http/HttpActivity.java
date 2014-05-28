/*
 * @(#)HttpActivity.java		       Project:androidkit.sample
 * Date:2013-5-6
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
package com.lurencun.androidkit.sample.http;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githang.androidkit.uibind.annotation.AndroidView;
import com.lurencun.androidkit.sample.R;
import com.lurencun.cfuture09.androidkit.AKBind;
import com.lurencun.cfuture09.androidkit.http.async.AsyncHttp;
import com.lurencun.cfuture09.androidkit.http.async.AsyncHttpResponseHandler;
import com.lurencun.cfuture09.androidkit.utils.lang.Log4AK;
import com.lurencun.cfuture09.androidkit.utils.thread.HandlerFactory;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XW9vbmxuZG5qamQdLCxzPjIw"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class HttpActivity extends Activity {
	private static final int UPDATE_IMAGE = 9999;
	private static final int UPDATE_TEXT = 8888;
	private Log4AK log = Log4AK.getLog(this.getClass());
	@AndroidView(id = R.id.http_image)
	private ImageView imageView;
	@AndroidView(id = R.id.http_text)
	private TextView textView;

	private Handler handler;
	private AsyncHttp http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AKBind.bind(this, R.layout.activity_http);
		http = new AsyncHttp();
		handler = new UIHandler();
	}

	public void onClicked(View v) {
		switch (v.getId()) {
		case R.id.http_upload:
			HttpSample.uploadFile();
			break;
		case R.id.http_getImage:
			HandlerFactory.newBackgroundHandler().post(new Runnable() {
				@Override
				public void run() {
					log.d("start download image");
					Bitmap bitmap = HttpSample
							.getBitmap("http://static.oschina.net/uploads/user/113/227618_50.jpg");
					log.d("download image finish");
					handler.sendMessage(handler.obtainMessage(UPDATE_IMAGE, bitmap));
				}
			});
			break;
		case R.id.http_async_getImage:
			http.get("http://code.google.com/p/cfuture-androidkit/", new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String content) {
					handler.sendMessage(handler.obtainMessage(UPDATE_TEXT, content));
				}
			});
			break;
		default:
			
		}
		
	}

	class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == UPDATE_IMAGE && msg.obj instanceof Bitmap) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
				return;
			}
			if(msg.what == UPDATE_TEXT && msg.obj instanceof String) {
				textView.setText((String)msg.obj);
			}
		}
	}
}

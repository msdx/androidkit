/*
 * @(#)Http.java		       Project:androidkit.sample
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
package com.githang.androidkit.sample.http;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.lurencun.cfuture09.androidkit.http.Http;
import com.lurencun.cfuture09.androidkit.http.HttpSimpleListener;

import java.io.File;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XW9vbmxuZG5qamQdLCxzPjIw"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class HttpSample {

	public static void uploadFile() {
		String uri = "http://192.168.1.115:8080/web1/Upload.jsp";
		File uploadFile = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/tuding/20130207083329.jpg");
		Http.uploadAsync(uri, "pic", uploadFile, new HttpSimpleListener() {

			@Override
			public void onFinish(String arg0) {
				System.out.println(arg0);
			}

			@Override
			public void onFailed(String arg0) {
				Log.e("Sample", arg0);
			}
		});
	}

	public static Bitmap getBitmap(String url) {
		return Http.getBitmap(url);
	}
}

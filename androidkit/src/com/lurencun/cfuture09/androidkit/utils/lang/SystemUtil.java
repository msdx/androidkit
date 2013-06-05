/*
 * @(#)AndroidUtil.java		       Project:androidkit
 * Date:2013-5-25
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
package com.lurencun.cfuture09.androidkit.utils.lang;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class SystemUtil {

	/**
	 * 发送短信。
	 * 
	 * @param context
	 *            Context对象
	 * @param number
	 *            收件人号码
	 * @param content
	 *            短信内容。
	 */
	public static void sendSMS(Context context, String number, String content) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sendIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		smsManager.sendTextMessage(number, null, content, sendIntent, null);
	}
}

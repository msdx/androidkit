/*
 * @(#)SMSUtil.java		       Project:androidkit.sample
 * Date:2013-5-30
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
package com.lurencun.cfuture09.androidkit.utils.osutil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 这是短信工具类，通过它可以对短信进行增删改查。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class SMSUtil {

	// 系统中短信相关的协议
	/**
	 * 全部短信
	 */
	public static final String SMS_ALL = "content://sms";
	/**
	 * 收件箱
	 */
	public static final String SMS_INBOX = "content://sms/inbox";
	/**
	 * 已发送
	 */
	public static final String SMS_SEND = "content://sms/send";
	/**
	 * 草稿箱
	 */
	public static final String SMS_DRAFT = "content://sms/draft";
	/**
	 * 发件箱
	 */
	public static final String SMS_OUTBOX = "content://sms/outbox";
	/**
	 * 发送失败
	 */
	public static final String SMS_FAILED = "content://sms/failed";
	/**
	 * 发送列表
	 */
	public static final String SMS_QUEUED = "content://sms/queued";

	public static final Uri URI_ALL = Uri.parse(SMS_ALL);
	public static final Uri URI_INBOX = Uri.parse(SMS_INBOX);
	public static final Uri URI_SEND = Uri.parse(SMS_SEND);
	public static final Uri URI_DRAFT = Uri.parse(SMS_DRAFT);
	public static final Uri URI_OUTBOX = Uri.parse(SMS_OUTBOX);
	public static final Uri URI_FAILED = Uri.parse(SMS_FAILED);
	public static final Uri URI_QUEUED = Uri.parse(SMS_QUEUED);

	// 短信相关的属性
	public static final String COL_ID = "_id";
	public static final String COL_THREAD_ID = "thread_id";
	public static final String COL_ADDRESS = "address";
	public static final String COL_PERSON = "person";
	public static final String COL_DATE = "date";
	public static final String COL_PROTOCOL = "protocol";
	public static final String COL_READ = "read";
	public static final String COL_STATUS = "status";
	public static final String COL_TYPE = "type";
	public static final String COL_BODY = "body";
	public static final String COL_SERVICE_CENTER = "service_center";

	// 短信的相关属性值
	public static final int PROTO_SMS = 0;
	public static final int PROTO_MMS = 1;
	public static final int READ_NO = 0;
	public static final int READ_YES = 1;
	public static final int STATUS_RECEIVED = -1;
	public static final int STATUS_COMPLETE = 0;
	public static final int STATUS_PENDING = 64;
	public static final int STATUS_FAILED = 128;
	public static final int TYPE_ALL = 0;
	public static final int TYPE_INBOX = 1;
	public static final int TYPE_SEND = 2;
	public static final int TYPE_DRAFT = 3;
	public static final int TYPE_OUTBOX = 4;
	public static final int TYPE_FAILED = 5;
	public static final int TYPE_QUEUED = 6;

	// 排序方式
	public static final String ORDER_DATE_DESC = "date desc";
	public static final String ORDER_DATE_ASC = "date asc";

	/**
	 * 按日期倒序排序，获取的内容包括号码，发件人，短信内容，日期，类型（如发送出去的，接收到的，草稿箱的等等）
	 * 
	 * @param context
	 * @param uri
	 *            获取地址
	 * @return
	 */
	public static List<SMSEntity> query(Context context, Uri uri) {
		return query(context, uri, ORDER_DATE_DESC);
	}

	/**
	 * 获取的内容包括号码，发件人，短信内容，日期，类型（如发送出去的，接收到的，草稿箱的等等）
	 * 
	 * @param context
	 * @param uri
	 *            获取短信的地址
	 * @param sortOrder
	 *            排序方式
	 * @return
	 */
	public static List<SMSEntity> query(Context context, Uri uri, String sortOrder) {
		ContentResolver cr = context.getContentResolver();
		final String[] projection = new String[] { COL_ID, COL_ADDRESS, COL_PERSON, COL_BODY,
				COL_DATE, COL_TYPE };
		Cursor cur = cr.query(uri, projection, null, null, sortOrder);
		if (cur.moveToFirst()) {
			List<SMSEntity> smsEntities = new ArrayList<SMSEntity>();
			final int idIndex = cur.getColumnIndex(COL_ID);
			final int addressIndex = cur.getColumnIndex(COL_ADDRESS);
			final int personIndex = cur.getColumnIndex(COL_PERSON);
			final int bodyIndex = cur.getColumnIndex(COL_BODY);
			final int dateIndex = cur.getColumnIndex(COL_DATE);
			final int typeIndex = cur.getColumnIndex(COL_TYPE);
			do {
				SMSEntity sms = new SMSEntity();
				sms.set_id(cur.getInt(idIndex));
				sms.setAddress(cur.getString(addressIndex));
				sms.setPerson(cur.getInt(personIndex));
				sms.setBody(cur.getString(bodyIndex));
				sms.setDate(cur.getLong(dateIndex));
				sms.setType(cur.getInt(typeIndex));
				smsEntities.add(sms);
			} while (cur.moveToNext());
			return smsEntities;
		}
		return null;
	}

	/**
	 * 根据短信id删除短信。
	 * 
	 * @param context
	 * @param uri
	 * @param id
	 *            短信id。
	 * @return 返回删除的条数。
	 */
	public static int deleteById(Context context, Uri uri, int id) {
		return context.getContentResolver().delete(uri, "id=?",
				new String[] { Integer.toString(id) });
	}

	/**
	 * 根据条件删除短信。
	 * 
	 * @param context
	 * @param uri
	 * @param where
	 *            条件
	 * @param selectionArgs
	 *            条件的值
	 * @return 返回删除的条数
	 */
	public static int delete(Context context, Uri uri, String where, String[] selectionArgs) {
		return context.getContentResolver().delete(uri, where, selectionArgs);
	}

	/**
	 * 根据某一属性删除信息。
	 * 
	 * @param context
	 * @param uri
	 * @param colName
	 *            属性名
	 * @param colValue
	 *            属性值
	 * @return 返回删除的条数
	 */
	public static int deleteByCol(Context context, Uri uri, String colName, String colValue) {
		return context.getContentResolver().delete(uri, colName + "=?", new String[] { colValue });
	}

	/**
	 * 插入短信。
	 * 
	 * @param context
	 * @param uri
	 * @param values
	 * @return 创建的短信的uri
	 */
	public static Uri insert(Context context, Uri uri, ContentValues values) {
		return context.getContentResolver().insert(uri, values);
	}

}

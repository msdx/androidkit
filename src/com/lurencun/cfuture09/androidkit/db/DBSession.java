/*
 * @(#)DBSession.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-11-16
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
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
package com.lurencun.cfuture09.androidkit.db;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class DBSession {

	private List<String> sqls;
	private SQLiteDatabase mDB;

	public DBSession(SQLiteDatabase db) {
		sqls = new ArrayList<String>();
		mDB = db;
	}

	/**
	 * 保存但不提交。
	 * 
	 * @param o
	 */
	public void save(Object o) {
		// TODO
	}

	/**
	 * 提交到数据库当中。
	 */
	public void commit() {
		for (String sql : sqls) {
			mDB.execSQL(sql);
		}
		sqls.clear();
	}

	/**
	 * 开启事务。
	 */
	public void beginTransaction() {
		mDB.beginTransaction();
	}

	/**
	 * 结束事务。
	 */
	public void endTransaction() {
		mDB.setTransactionSuccessful();
		mDB.endTransaction();
	}
	
	public void close() {
		if (mDB != null) {
			mDB.close();
		}
	}
}

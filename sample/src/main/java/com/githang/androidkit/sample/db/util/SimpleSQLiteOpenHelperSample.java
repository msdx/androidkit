/*
 * @(#)SimpleSQLiteOpenHelper.java		       Project:com.sinaapp.msdxblog.andoridkit.sample
 * Date:2012-9-12
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
package com.githang.androidkit.sample.db.util;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.lurencun.cfuture09.androidkit.db.util.SimpleSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class SimpleSQLiteOpenHelperSample {

	/**
	 * 第一次使用数据库，需要创建。
	 */
	public SQLiteOpenHelper openSQLiteHelperFirstTime(Context context,
			String dbName, List<String> sqlCreateStatement) {
		// List<String> sqlCreateStatement = new ArrayList<String>();
		// sqlCreateStatement
		// .add("create table t_1(id int not null, name varchar(40) not null)");
		// sqlCreateStatement是创建时要执行的SQL语句。
		SimpleSQLiteOpenHelper helper = new SimpleSQLiteOpenHelper(context,
				dbName, sqlCreateStatement);
		return helper;
	}

	/**
	 * 第一次使用数据库，或升级数据库时的方法 。这里指定创建时的版本号。
	 */
	public SQLiteOpenHelper openSQLiteHelperFirstTime(Context context,
			int version, String dbName) {
		List<String> sqlCreateStatement = new ArrayList<String>();
		sqlCreateStatement
				.add("create table t_1(id int not null, name varchar(40) not null)");
		// sqlCreateStatement是创建或升级时要执行的SQL语句。如果数据库已经创建，或新版本号没有大于旧版本号，则这些语句不会被执行。
		SimpleSQLiteOpenHelper helper = new SimpleSQLiteOpenHelper(context,
				dbName, version, sqlCreateStatement);
		return helper;
	}

	/**
	 * 不指定版本号，也不更新或创建数据库。
	 */
	public SQLiteOpenHelper openSQLiteHelperFirstTime(Context context,
			String dbName) {
		SimpleSQLiteOpenHelper helper = new SimpleSQLiteOpenHelper(context,
				dbName);
		return helper;
	}
}

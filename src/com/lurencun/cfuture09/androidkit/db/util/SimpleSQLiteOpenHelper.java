/*
 * @(#)SQLiteOpen.java		       version: 0.1 
 * Date:2012-2-11
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.lurencun.cfuture09.androidkit.db.util;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class SimpleSQLiteOpenHelper extends SQLiteOpenHelper {
	private static final int INIT_VERSION = 1;

	/**
	 * 创建或升级数据库时执行的语句。
	 */
	private List<String> sqlStatementExed;

	/**
	 * 如果是创建或升级数据库，请使用带List参数的构造方法。
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 * @param factory
	 *            to use for creating cursor objects, or null for the default
	 * @param version
	 *            number of the database (starting at 1); if the database is
	 *            older, onUpgrade(SQLiteDatabase, int, int) will be used to
	 *            upgrade the database; if the database is newer,
	 *            onDowngrade(SQLiteDatabase, int, int) will be used to
	 *            downgrade the database
	 */
	public SimpleSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		sqlStatementExed = null;
	}

	/**
	 * 带SQL语句的构造方法。此SQL语句将在数据库创建或升级时执行。
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 * @param factory
	 *            to use for creating cursor objects, or null for the default
	 * @param version
	 *            number of the database (starting at 1); if the database is
	 *            older, onUpgrade(SQLiteDatabase, int, int) will be used to
	 *            upgrade the database; if the database is newer,
	 *            onDowngrade(SQLiteDatabase, int, int) will be used to
	 *            downgrade the database
	 * @param sqlStatementExed
	 *            在数据库创建或升级的时候将执行的语句。
	 */
	public SimpleSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version, List<String> sqlStatementExed) {
		super(context, name, factory, version);
		this.sqlStatementExed = sqlStatementExed;
	}

	/**
	 * 如果是创建或升级数据库，请使用带List参数的构造方法。
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 * @param version
	 *            number of the database (starting at 1); if the database is
	 *            older, onUpgrade(SQLiteDatabase, int, int) will be used to
	 *            upgrade the database; if the database is newer,
	 *            onDowngrade(SQLiteDatabase, int, int) will be used to
	 *            downgrade the database
	 */
	public SimpleSQLiteOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
		sqlStatementExed = null;
	}

	/**
	 * 如果是创建或升级数据库，请使用带List参数的构造方法。
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 */
	public SimpleSQLiteOpenHelper(Context context, String name) {
		super(context, name, null, INIT_VERSION);
		sqlStatementExed = null;
	}

	/**
	 * 如果是创建或升级数据库，请使用带List参数的构造方法。
	 * 
	 * @param context
	 *            to use to open or create the database
	 * @param name
	 *            of the database file, or null for an in-memory database
	 * @param version
	 *            number of the database (starting at 1); if the database is
	 *            older, onUpgrade(SQLiteDatabase, int, int) will be used to
	 *            upgrade the database; if the database is newer,
	 *            onDowngrade(SQLiteDatabase, int, int) will be used to
	 *            downgrade the database
	 * @param sqlCreateStatement
	 *            在创建或升级数据库时要执行的语句。
	 */
	public SimpleSQLiteOpenHelper(Context context, String name, int version,
			List<String> sqlCreateStatement) {
		super(context, name, null, version);
		this.sqlStatementExed = sqlCreateStatement;
	}

	/**
	 * @param context
	 * @param name
	 * @param sqlCreateStatement
	 *            在创建或升级数据库时要执行的语句。
	 */
	public SimpleSQLiteOpenHelper(Context context, String name,
			List<String> sqlCreateStatement) {
		super(context, name, null, INIT_VERSION);
		this.sqlStatementExed = sqlCreateStatement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	@Deprecated
	public void onCreate(SQLiteDatabase db) {
		exeSqlStatementExed(db);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	@Deprecated
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			exeSqlStatementExed(db);
		}
	}

	/**
	 * 初始化或升级数据库时执行的SQL语句。。
	 */
	private void exeSqlStatementExed(SQLiteDatabase db) {
		if (sqlStatementExed != null) {
			for (String statement : sqlStatementExed) {
				db.execSQL(statement);
			}
		}
	}
}
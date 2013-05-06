/*
 * @(#)IdGenerator.java		       Project:androidkit
 * Date:2013-5-2
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


/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class IdGenerators {
	/**
	 * 返回int类型id生成类
	 * 
	 * @param threadSafe
	 *            是否为线程安全。
	 * @return int类型id生成类。
	 */
	public static IdIntGenerator getIdIntGenerator(boolean threadSafe) {
		return threadSafe ? new IncreaseIntIdThreadSafe() : new IncreaseIntId();
	}

	/**
	 * 返回指定初始值的int类型id生成类
	 * 
	 * @param threadSafe
	 *            是否为线程安全。
	 * @return int类型id生成类。
	 */
	public static IdIntGenerator getIdIntGenerator(boolean threadSafe, int initialId) {
		return threadSafe ? new IncreaseIntIdThreadSafe(initialId) : new IncreaseIntId(initialId);
	}

	/**
	 * 返回longt类型id生成类
	 * 
	 * @param threadSafe
	 *            是否为线程安全。
	 * @return long类型id生成类。
	 */
	public static IdLongGenerator getIdLongGenerator(boolean threadSafe) {
		return threadSafe ? new IncreaseLongIdThreadSafe() : new IncreaseLongId();
	}

	/**
	 * 返回指定初始值的long类型id生成类
	 * 
	 * @param threadSafe
	 *            是否为线程安全。
	 * @return long类型id生成类。
	 */
	public static IdLongGenerator getIdLongGenerator(boolean threadSafe, long initialId) {
		return threadSafe ? new IncreaseLongIdThreadSafe(initialId) : new IncreaseLongId(initialId);
	}

}

/*
 * @(#)DBSessionManager.java		       Project:androidkit
 * Date:2013-4-19
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
package com.lurencun.cfuture09.androidkit.db;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class DBSessionManager {

	private static HashMap<String, DBSession> sessionMap;
	static {
		sessionMap = new HashMap<String, DBSession>();
	}

	public static void register(String name, DBSession session) {
		sessionMap.put(name, session);
	}

	public static void closeSession(String name) {
		DBSession session = sessionMap.get(name);
		if (session != null) {
			session.close();
			sessionMap.remove(name);
		}
	}

	public static void closeAll() {
		Set<Entry<String, DBSession>> entries = sessionMap.entrySet();
		for (Entry<String, DBSession> entry : entries) {
			DBSession session = entry.getValue();
			if (session != null) {
				session.close();
				sessionMap.remove(entry.getKey());
			}
		}
	}
}

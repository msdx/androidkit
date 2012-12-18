/*
 * @(#)DateUtil.java		       Project:ProgramList
 * Date:2012-9-4
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
package com.lurencun.cfuture09.androidkit.util;

import java.util.Calendar;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class DateUtil {
	/**
	 * 得到当前日期是星期几。
	 * 
	 * @return 当为周日时，返回0，当为周一至周六时，则返回对应的1-6。
	 */
	public static final int getCurrentDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
	}
}

/*
 * @(#)RoundListAdapter.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-12-6
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
package com.lurencun.cfuture09.androidkit.uilibs.widget;

import android.widget.ListAdapter;

/**
 * 圆角风格ListAdapter的子接口。在这里定义了圆角风格背景的参数。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public interface RoundListAdapter extends ListAdapter {
	/**
	 * 圆角背景风格ListView的参数类。定义了顶部背景，底部背景，中间背景及只有一个时的背景。
	 * 
	 * @author msdx
	 * 
	 */
	public static class RoundParams {
		/**
		 * 位置在顶部的ITEM的背景资源ID
		 */
		public int topResid;
		/**
		 * 位置在中间的ITEM的背景资源ID
		 */
		public int middleResid;
		/**
		 * 位置在底部的ITEM的背景资源ID
		 */
		public int bottomResid;
		/**
		 * ListView只有一项时的背景资源ID
		 */
		public int lonelyResid;

		/**
		 * 圆角listview的背景参数构造方法。
		 * 
		 * @param topResid
		 *            位置在顶部的ITEM的背景资源ID
		 * @param middleReside
		 *            位置在中间的ITEM的背景资源ID
		 * @param bottomResid
		 *            位置在底部的ITEM的背景资源ID
		 * @param lonelyResid
		 *            ListView只有一项时的背景资源ID
		 */
		public RoundParams(int topResid, int middleReside, int bottomResid,
				int lonelyResid) {
			this.topResid = topResid;
			this.middleResid = middleReside;
			this.bottomResid = bottomResid;
			this.lonelyResid = lonelyResid;
		}
	}
}

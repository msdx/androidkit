/*
 * @(#)RoundArrayAdapter.java		       Project:com.sinaapp.msdxblog.androidkit
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

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lurencun.cfuture09.androidkit.uilibs.widget.RoundListAdapter.RoundParams;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class RoundArrayAdapter<T> extends ArrayAdapter<Object> {
	/**
	 * 圆角背景的参数
	 */
	private RoundParams mParams;

	public RoundArrayAdapter(Context context, int resource,
			int textViewResourceId, List<Object> objects, RoundParams params) {
		super(context, resource, textViewResourceId, objects);
		mParams = params;
	}

	public RoundArrayAdapter(Context context, int resource,
			int textViewResourceId, Object[] objects, RoundParams params) {
		super(context, resource, textViewResourceId, objects);
		mParams = params;
	}

	public RoundArrayAdapter(Context context, int resource,
			int textViewResourceId, RoundParams params) {
		super(context, resource, textViewResourceId);
		mParams = params;
	}

	public RoundArrayAdapter(Context context, int textViewResourceId,
			List<Object> objects, RoundParams params) {
		super(context, textViewResourceId, objects);
		mParams = params;
	}

	public RoundArrayAdapter(Context context, int textViewResourceId,
			Object[] objects, RoundParams params) {
		super(context, textViewResourceId, objects);
		mParams = params;
	}

	public RoundArrayAdapter(Context context, int textViewResourceId,
			RoundParams params) {
		super(context, textViewResourceId);
		mParams = params;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		setItemBackground(position, view);
		return view;
	}

	/**
	 * 设置圆角风格的ListView
	 * 
	 * @param position
	 *            item在ListView中的位置
	 * @param item
	 *            要设置背景的item
	 */
	public void setItemBackground(int position, View item) {
		if (getCount() == 1) {
			item.setBackgroundResource(mParams.lonelyResid);
		} else if (position == 0) {
			item.setBackgroundResource(mParams.topResid);
		} else if (position == getCount() - 1) {
			item.setBackgroundResource(mParams.bottomResid);
		} else {
			item.setBackgroundResource(mParams.middleResid);
		}
	}
}

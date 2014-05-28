/*
 * @(#)SampleActivity.java		       Project:androidkitTest
 * Date:2013-2-7
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
package com.lurencun.androidkit.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.githang.androidkit.uibind.annotation.AndroidView;
import com.lurencun.androidkit.sample.cache.CacheDemo;
import com.lurencun.androidkit.sample.db.DbDemo;
import com.lurencun.androidkit.sample.http.HttpDemo;
import com.lurencun.androidkit.sample.uibind.UIBindDemo;
import com.lurencun.androidkit.sample.uilibs.UILibsDemo;
import com.lurencun.androidkit.sample.utils.UtilsDemo;
import com.lurencun.cfuture09.androidkit.AKBind;

/**
 * @Author Geek_Soledad (66704238@51uc.com)
 */
public class SampleActivity extends Activity {
	@AndroidView(id = R.id.sample_list, onItemClick = "onItemClick")
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AKBind.bind(this, R.layout.activity_sample);
		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();
		sampleBeans.add(new SampleBean(CacheDemo.class));
		sampleBeans.add(new SampleBean(DbDemo.class));
		sampleBeans.add(new SampleBean(HttpDemo.class));
		sampleBeans.add(new SampleBean(UIBindDemo.class));
		sampleBeans.add(new SampleBean(UILibsDemo.class));
		sampleBeans.add(new SampleBean(UtilsDemo.class));
		listView.setAdapter(new ArrayAdapter<SampleBean>(this, android.R.layout.simple_list_item_1,
				sampleBeans));
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		startActivity(new Intent(this,
				((SampleBean) listView.getAdapter().getItem(position)).getClazz()));
	}
}

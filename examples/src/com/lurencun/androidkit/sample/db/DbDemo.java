/*
 * @(#)Db.java		       Project:androidkit.sample
 * Date:2013-6-7
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
package com.lurencun.androidkit.sample.db;

import java.util.ArrayList;
import java.util.List;

import com.lurencun.androidkit.sample.R;
import com.lurencun.androidkit.sample.SampleBean;
import com.lurencun.cfuture09.androidkit.AKBind;
import com.lurencun.cfuture09.androidkit.uibind.annotation.AndroidView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class DbDemo extends Activity {
	@AndroidView(id = R.id.sample_list, onItemClick = "onItemClick")
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AKBind.bind(this, R.layout.activity_sample);
		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();

		listView.setAdapter(new ArrayAdapter<SampleBean>(this, android.R.layout.simple_list_item_1,
				sampleBeans));
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		startActivity(new Intent(this,
				((SampleBean) listView.getAdapter().getItem(position)).getClazz()));
	}
}

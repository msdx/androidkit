/*
 * @(#)UIBindSample.java		       Project:androidkit.sample
 * Date:2013-5-14
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
package com.lurencun.androidkit.sample.uibind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.androidkit.uibind.UIBindUtil;
import com.githang.androidkit.uibind.annotation.AndroidView;
import com.githang.androidkit.uibind.annotation.OnClick;
import com.lurencun.androidkit.sample.R;

/**
 * 该类为UIBind及ResBind的示例。使用传统的方法，对于控件，您可能需要这样写：<br />
 * 
 * <pre>
 * private Button buttonLeft;
 * private Button buttonRight;
 * private TextView textView;
 * private ListView listView;
 * 
 * private Toast toast;
 * 
 * &#064;Override
 * protected void onCreate(Bundle savedInstanceState) {
 * 	super.onCreate(savedInstanceState);
 * 	toast = Toast.makeText(this, &quot;&quot;, Toast.LENGTH_LONG);
 * 	setContentView(R.layout.activity_bind);
 * 
 * 	buttonLeft = (Button) findViewById(R.id.bind_buttonLeft);
 * 	buttonRight = (Button) findViewById(R.id.bind_buttonRight);
 * 	textView = (TextView) findViewById(R.id.bind_textview);
 * 	listView = (TextView) findViewById(R.id.bind_listview);
 * 
 * 	buttonLeft.setOnClickListener(new OnClickListener() {
 * 		&#064;Override
 * 		public void onClick(View v) {
 * 			showToast(&quot;click left button&quot;);
 * 		}
 * 	});
 * 	buttonRight.setOnClickListener(new OnClickListener() {
 * 		&#064;Override
 * 		public void onClick(View v) {
 * 			showToast(&quot;click right button&quot;);
 * 		}
 * 	});
 * 	textView.setText(R.string.text);
 * 
 * 	String[] array = getResources().getStringArray(R.array.listview);
 * 	ListAdapter adapter = new ArrayAdapter&lt;String&gt;(this, android.R.layout.simple_list_item_1, array);
 * 	listView.setAdapter(adapter);
 * 	listView.setOnItemClickListener(new OnItemClickListener() {
 * 		&#064;Override
 * 		public void onItemClick(AdapterView&lt;?&gt; arg0, View arg1, int arg2, long arg3) {
 * 			showToast(listView.getAdapter().getItem(arg2).toString());
 * 
 * 		}
 * 	});
 * }
 * 
 * private void showToast(String msg) {
 * 	toast.setText(msg);
 * 	toast.show();
 * }
 * </pre>
 * 
 * 而如果使用本框架的uibind功能模块，初始化的代码将简化如下。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class UIBindSample extends Activity {
	@AndroidView(id = R.id.bind_textview)
	private TextView textView;
	@AndroidView(id = R.id.bind_listview, onItemClick = "onItemClick")
	private ListView listView;

	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		UIBindUtil.bind(this, R.layout.activity_bind);
		textView.setText(R.string.text);

		String[] array = getResources().getStringArray(R.array.listview);
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				array);
		listView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		showToast(listView.getAdapter().getItem(arg2).toString());
	}

	@OnClick(viewId = { R.id.bind_buttonLeft, R.id.bind_buttonRight })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bind_buttonLeft:
			showToast("click left button");
			break;
		case R.id.bind_buttonRight:
			showToast("click right button");
			break;
		default:
			break;
		}
	}

	private void showToast(String msg) {
		toast.setText(msg);
		toast.show();
	}
}

/*  
 * @(#)BasicParams.java              Project:androidkit  
 * Date:2013-1-9  
 *  
 * Copyright (c) 2013 Geek_Soledad.  
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
package com.lurencun.cfuture09.androidkit.http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP请求的参数。
 * 
 * @author Geek_Soledad (msdx.android@tom.com)
 */
public class BaseParams {
	private List<NameValuePair> pairs;

	public BaseParams() {
		pairs = new ArrayList<NameValuePair>();
	}

	public BaseParams(List<NameValuePair> pairs) {
		this.pairs = pairs;
	}

	/**
	 * 添加参数。
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void put(String name, String value) {
		pairs.add(new BasicNameValuePair(name, value));
	}

	/**
	 * 添加多个参数。
	 * 
	 * @param pairs
	 *            参数列表。
	 */
	public void put(List<NameValuePair> pairs) {
		pairs.addAll(pairs);
	}

	public List<NameValuePair> getPairs() {
		return pairs;
	}

	public void setPairs(List<NameValuePair> pairs) {
		this.pairs = pairs;
	}
}

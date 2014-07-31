/*
 * @(#)NetworkUtilSample.java		       Project:com.sinaapp.msdxblog.andoridkit.sample
 * Date:2012-9-12
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
package com.githang.androidkit.sample.utils.net;

import android.content.Context;

import com.lurencun.cfuture09.androidkit.utils.osutil.DeviceUtil;

/**
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class NetworkUtilSample {

	public void isNetworkActived(Context context) {
		if(DeviceUtil.isNetworkActived(context)) {
			System.out.println("网络状态良好");
		} else {
			System.out.println("无法连接网络。");
		}
	}
}

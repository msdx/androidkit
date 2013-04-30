/*  
 * @(#)IntId.java              Project:androidkit  
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
package com.lurencun.cfuture09.androidkit.utils.lang;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * int类型的自增长ID生成。
 * 
 * @author msdx
 */
public class IdIntGenerator {
	private AtomicInteger id;

	public IdIntGenerator() {
		id = new AtomicInteger();
	}

	public IdIntGenerator(int initialId) {
		id = new AtomicInteger(initialId);
	}

	/**
	 * 生成一个新的id并返回。
	 * 
	 * @return 返回新的id.
	 */
	public int newId() {
		return id.incrementAndGet();
	}

	/**
	 * 获取当前ID.
	 * 
	 * @return 当前ID。
	 */
	public int currentId() {
		return id.get();
	}

	/**
	 * 设置ID的值。
	 * 
	 * @param id
	 *            要设置成的值。
	 */
	public void setId(int id) {
		this.id.set(id);
	}

}

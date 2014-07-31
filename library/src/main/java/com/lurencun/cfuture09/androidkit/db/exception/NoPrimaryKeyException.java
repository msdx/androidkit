/*  
 * @(#)NoPrimaryKeyException.java              Project:androidkit  
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
package com.lurencun.cfuture09.androidkit.db.exception;

/**
 * @author Geek_Soledad (msdx.android@tom.com)
 */
public class NoPrimaryKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4932999357301716877L;

	public NoPrimaryKeyException() {
		super();
	}

	public NoPrimaryKeyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoPrimaryKeyException(String detailMessage) {
		super(detailMessage);
	}

	public NoPrimaryKeyException(Throwable throwable) {
		super(throwable);
	}

}

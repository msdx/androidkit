/*
 * @(#)Id.java		       Project:androidkit
 * Date:2013-4-19
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
package com.lurencun.cfuture09.androidkit.db.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表的主键。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
	/**
	 * id生成模式为自增长
	 */
	public static final int GENERATE_AUTO = 0;
	/**
	 * id生成模式为手动设置
	 */
	public static final int GENERATE_MANUAL = 1;

	/**
	 * @return 主键名称。如果不配置，默认为_id或id的成员变量，并以变量名作为列名。如果配置为""，则为该变量名。
	 */
	public String value() default "";

	/**
	 * @return 主键生成类型。当为自增长时，仅当id为int类型时有效。
	 */
	public int generatedType();

}

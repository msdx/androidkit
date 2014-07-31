/*
 * @(#)ColorPickerSampl.java		       Project:androidkit.sample
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
package com.githang.androidkit.sample.uilibs.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.githang.androidkit.sample.R;
import com.githang.androidkit.uibind.UIBindUtil;
import com.githang.androidkit.uibind.annotation.AndroidView;
import com.githang.androidkit.uibind.annotation.OnClick;
import com.githang.androidkit.widget.ColorPickerDialog;

/**
 * @Author Geek_Soledad (66704238@51uc.com)
 * @Function
 */
public class ColorPickerDialogSample extends Activity{
	private static final int DIALOG_COLOR_PICKER = 1;
	@AndroidView(id= R.id.color_picker_button)
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIBindUtil.bind(this, R.layout.activity_colorpickerdialogsample);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_COLOR_PICKER:
			return new ColorPickerDialog(this, "ColorPicker", new ColorPickerDialog.OnColorChangedListener() {
				@Override
				public void colorChanged(int arg0) {
					button.setTextColor(arg0);
				}
			});
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	@OnClick(viewId={R.id.color_picker_button})
	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.color_picker_button:
			showDialog(DIALOG_COLOR_PICKER);
			break;

		default:
			break;
		}
	}
}

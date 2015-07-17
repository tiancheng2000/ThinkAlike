/**
* Copyright 2013-2015 Tiancheng Hu
* 
* Licensed under the GNU Lesser General Public License, version 3.0 (LGPL-3.0, the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://opensource.org/licenses/lgpl-3.0.html
*     
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.aqiservice.android.domain;

import android.content.Context;

import com.aqiservice.android.AQIServiceApp;
import com.thinkalike.generic.concurrent.Executor;
import com.thinkalike.android.concurrent.AsyncExecutor;
import com.thinkalike.android.control.ImageNodeView;
import com.thinkalike.android.dal.AssetManagerLocal;
import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;

public class Factory implements com.thinkalike.generic.domain.Factory{
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//SmartClient Framework Domain ----------
	@Override
	public IAssetManagerLocal createAssetManagerLocal(Object context){
		if (context == null)
			context = AQIServiceApp.getInstance().getApplicationContext();
		
		if (context instanceof Context)
			return new AssetManagerLocal((Context) context);
		return null;
	}

	@Override
	public <Result> Executor<Result> createAsyncExecutor(Result dumb){
		return new AsyncExecutor<Result>();
	}
	
	//Business Domain ----------
	//  XxxNode ---
	@Override
	public IImageNodeView createImageNodeView(Object uiContext) {
		return new ImageNodeView((Context) uiContext);
	}
	
	//  Layout ---

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

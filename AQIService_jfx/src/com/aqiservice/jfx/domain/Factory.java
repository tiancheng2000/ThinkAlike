/**
* Copyright 2013-2014 Tiancheng Hu
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

package com.aqiservice.jfx.domain;

import com.thinkalike.generic.concurrent.Executor;
import com.thinkalike.generic.dal.AssetManagerLocal;
import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.jfx.assets.Assets;
import com.thinkalike.jfx.control.ImageNodeView;
//import com.thinkalike.generic.viewmodel.control.ITextResourceView;
//import com.thinkalike.jfx.control.TextResourceView;


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
		return new AssetManagerLocal(Assets.class.getResource(""));
	}

	@Override
	public <Result> Executor<Result> createAsyncExecutor(Result dumb){
		// TODO Auto-generated method stub
		return null;
	}
	
	//Business Domain ----------
	//  XxxNode ---
	@Override
	public IImageNodeView createImageNodeView(Object uiContext) {
		//IMPROVE: do some instanceof check. 
		return new ImageNodeView();  //ignore uiContext..
	}

	//  Layout ---
	

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

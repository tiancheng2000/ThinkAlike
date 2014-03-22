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

package com.thinkalike.generic;

import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.dal.AssetManager;


public class Loader {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	public interface OnLoaderEventListener{
		public void onReloaded();
		//public void onConfigChanged();
	}
	
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static Loader _this;
	private Platform _platform;
	@SuppressWarnings("unused")
	private OnLoaderEventListener _onLoaderEventListener;
	
	//-- Properties --------------------------------------------
	public Platform getPlatform(){return _platform;}
	
	//-- Constructors ------------------------------------------
	private Loader(Platform platform, OnLoaderEventListener l)
	{
		_platform = platform;
		_onLoaderEventListener = l;
		
		//...load generic.View, which load platform-dependent Views 
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static Loader createInstance(Platform platform, OnLoaderEventListener l){
		_this = new Loader(platform, l);
		return _this;
	}
	
	public static Loader getInstance(){
		if (_this == null)
			throw new UnsupportedOperationException("Loader: must be instantiated by a platform-relative Applicaiton");
		else if (_this.getPlatform() == null)
			throw new UnsupportedOperationException("Loader: must be initialized with a valid platform instance");
		return _this;
	}
	
	//IMPROVE: implement standard interface methods 
	//UncaughtExceptionHandler()
	//isInitialized()
	//execute()
	//finalize()
	//onConfigReloaded()

	public void initialize(){
		//IMPROVE: 1.use AssetLoader thread  2.ignore Asset copy if already exists
		AssetManager.copyAssets(Config.PATH_ROOT, Util.getAbsolutePath(Config.PATH_ROOT));
		if(!Config.PATH_TYPE_A.startsWith(Config.PATH_ROOT))
			AssetManager.copyAssets(Config.PATH_TYPE_A, Util.getAbsolutePath(Config.PATH_TYPE_A));
	}

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------

}

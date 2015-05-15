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

import java.util.ArrayList;

import com.thinkalike.generic.common.Config.Key;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.dal.AssetManager;


public class Loader {
	//-- Constants and Enums -----------------------------------
	public static final int E_OK = 0;
	public static final int E_INVALID_PLATFORM = 1;
	
	//-- Inner Classes and Structures --------------------------
	public interface OnLoaderEventListener{
		public void onInitialized();
		public void onReloaded();
		//public void onConfigChanged();
		public void onFinalized();
	}
	
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	protected static Loader _this;
	private Platform _platform;
	private ArrayList<OnLoaderEventListener> _onLoaderEventListeners; 
	
	//-- Properties --------------------------------------------
	public Platform getPlatform(){return _platform;}
	public void addLoaderEventListener(OnLoaderEventListener l){_onLoaderEventListeners.add(l);}
	public void removeLoaderEventListener(OnLoaderEventListener l){_onLoaderEventListeners.remove(l);}
	
	//-- Constructors ------------------------------------------
	protected Loader(Platform platform, OnLoaderEventListener l)
	{
		_platform = platform;
		_onLoaderEventListeners = new ArrayList<OnLoaderEventListener>();
		addLoaderEventListener(l);
		
		//IMPROVE: ...load generic.View, which in turn load platform-specific Views 
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static Loader createInstance(Platform platform, OnLoaderEventListener l){
		_this = new Loader(platform, l);
		return _this;
	}
	
	public static Loader getInstance(){
		if (_this == null){
			//throw new UnsupportedOperationException("Loader: must be instantiated by a platform-relative Application");
			//Util.warn(TAG, "Loader: should be instantiated by a platform-relative Application");
		}
		else if (_this.getPlatform() == null){
			//throw new UnsupportedOperationException("Loader: must be initialized with a valid platform instance");
			//Util.warn(TAG, "Loader: must be initialized with a valid platform instance");
		}
		return _this;
	}
	
	//IMPROVE: implement standard interface methods 
	//UncaughtExceptionHandler()
	//isInitialized()
	//execute()
	//finalize()
	//onConfigReloaded()

	public int initialize(){
		if(_platform == null)
			return E_INVALID_PLATFORM;
		
		//IMPROVE: 1.use AssetLoader thread  2.ignore Asset copy if already exists and fresh
		String path_root = (String)_platform.getConfig(Key.PATH_ROOT);
		AssetManager.copyAssets(path_root, Util.getAbsolutePath(path_root));

		for(OnLoaderEventListener l : _onLoaderEventListeners){
			l.onInitialized();
		}
		
		return E_OK;
	}

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------

}

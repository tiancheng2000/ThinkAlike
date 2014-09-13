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

package com.hscardref.android;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import com.thinkalike.android.common.Util;
import com.thinkalike.generic.Loader;
import com.thinkalike.generic.Platform;
import com.thinkalike.generic.common.Config.Key;
import com.thinkalike.generic.domain.Factory;
import com.hscardref.generic.common.Config;
import com.hscardref.generic.common.Constant;

public class HSCardRefApp extends android.app.Application implements Platform, Loader.OnLoaderEventListener {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = HSCardRefApp.class.getSimpleName();
	private final static Object DUMB_OBJECT = new Object();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static HSCardRefApp _this;
	private Factory _factory;
	private Context _currentContext;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public HSCardRefApp() {
		super();
		//NOTE: Platform-dependent Application is created by the application framework, only once.
		_this = this; 
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void onCreate() {
		super.onCreate();
		//FD: 1.(new generic.Loader()).initialize(this, eventListener)
		//    2.initialize platform-dependent objects: application-context(?), LogFile, Factory 
		
		//0.Initialize generic.Loader
		Loader loader = Loader.createInstance(this, this); //instantiate a Loader

		//1.Initialize platform-dependent objects
		Config.STORAGE_BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath(); //platform-dependent
		
		//2.Initialize Log scheme (File IO)
		@SuppressWarnings("unused")
		Util dumb = Util.getInstance(); //initialize
		Util.trace(null, TAG, "---------- " +Constant.APP_SHORTNAME+ " get launched [" +com.thinkalike.generic.common.Util.getTimeStamp()+ "] ----------");
		Util.trace(null, TAG, String.format("resolution = %s", getResources().getDisplayMetrics().toString()));
		//Util.trace(null, TAG, String.format("resolution=%d*%d, density=%d", System.getProperty("")));
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		if(am!=null)
			Util.trace(null, TAG, String.format("memory per process = %dMB", am.getMemoryClass()));
		
		//3.Initialize other platform-dependent elements 
		//IMPROVE: use API which can directly locate to "/DCIM/Camera". 
		//String path_photoResource = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
		//if (path_photoResource.startsWith(Config.STORAGE_BASEPATH))
		//	Config.PATH_PHOTORESOURCE = path_photoResource.substring(Config.STORAGE_BASEPATH.length());
		//else
		//	Config.PATH_PHOTORESOURCE = "/DCIM/Camera";
		_factory = (Factory)new com.hscardref.android.domain.Factory();
		
		//IMPROVE: show initialization error/warning in GUI
		loader.initialize();
		loader.addLoaderEventListener(this);
	}

	//----- generic.Platform -----
//IMPROVE: Application has its own getString() and cannot be override, should hold a Platform inner class instead.
//	@Override
//	public String getString(int id) {
//		//IMPROVE: id shall be translated to platform-dependent id
//		return this.getString(id);
//	}
	
	@Override
	public Context getUIContext() {
		//NOTE: remember call setUIContext() in every Activity.onResume().
		return _currentContext;
		//return getApplicationContext(); //TEMP 
	}

	@Override
	public Factory getFactory() {return _factory;}

	@Override
	public Object getConfig(String key) {
		Object value = DUMB_OBJECT;
		if (key!=null)
			value = com.thinkalike.generic.common.Util.getFieldValue(Config.class, key);
		return value;
	}
	@Override
	public Object getConfig(Key key) {
		Object value = DUMB_OBJECT;
		if (key!=null)
			value = Config.get(key);
		return value;
	}

	@Override
	public Object getConstant(String key) {
		Object value = DUMB_OBJECT;
		if (key!=null)
			value = com.thinkalike.generic.common.Util.getFieldValue(Constant.class, key);
		return value;
	}

	@Override
	public void logSystem(String tag, String message, int level) {
		//Translate generic.common.Config.LogLevel to platform-specific log level? Done in Util class. 
		Util.logSystem(tag, message, level);
	}

	@Override
	public void logFile(String tag, String message, int level) {
		Util.logFile(tag, message, level);
	}

	@Override
	public void logGUI(String tag, String message, int level) {
		Util.logGUI(getUIContext(), tag, message, level);
	}
	
	//-- Public and internal Methods ---------------------------
	public static HSCardRefApp getInstance() {
		if(_this == null)
			_this = new HSCardRefApp();
		return _this;
	}

	/**
	 * IMPROVE: visibility scope cannot be constrained to sub-package level in Java. work-around = proxy sub-class in sub-package? 
	 * NOTE: call registerUIContext() in onResume() method of each Activity
	 */
	public void registerUIContext(Context context) { 
//		if (context instanceof Context)
//			_currentContext = (Context)context;
//		else
//			throw new IllegalArgumentException("platform-dependent Context type mismatched");
		_currentContext = (Context)context;
	}
	/**
	 * Context/Activity may have short LifeCycle. Prevent referencing it from Objects with longer LifeCycle (otherwise it will note be GC-ed)   
	 */
	public void unregisterUIContext(Context context) {
		if (_currentContext == context)
			_currentContext = null;  //otherwise, ignore the request 
	}

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
	//----- generic.Loader.OnLoaderEventListener -----
	@Override
	public void onInitialized() {
		//if(!Config.PATH_TYPE_A.startsWith(Config.PATH_ROOT))
		//	AssetManager.copyAssets(Config.PATH_TYPE_A, Util.getAbsolutePath(Config.PATH_TYPE_A));
	}

	@Override
	public void onReloaded() {
	}

	@Override
	public void onFinalized() {
	}
}


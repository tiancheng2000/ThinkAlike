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
	public Loader(Platform platform, OnLoaderEventListener l)
	{
		_this = this;
		_platform = platform;
		_onLoaderEventListener = l;
		
		//...load generic.View, which load platform-dependent Views 
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
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

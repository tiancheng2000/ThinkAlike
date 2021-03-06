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

package com.hscardref.jfx;

import java.io.InputStream;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.hscardref.generic.common.Config;
import com.hscardref.generic.common.Constant;
import com.hscardref.jfx.res.Res;
import com.hscardref.jfx.view.MainScene;
import com.thinkalike.generic.Loader;
import com.thinkalike.generic.Platform;
import com.thinkalike.generic.common.Config.Key;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.dal.AssetManager;
import com.thinkalike.generic.domain.Factory;

public class HSCardRefApp extends Application implements Platform, Loader.OnLoaderEventListener {

	//-- Constants and Enums --------------------------
	private final static String TAG = HSCardRefApp.class.getSimpleName();
	private final static Object DUMB_OBJECT = new Object();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------	
	private static HSCardRefApp _this;
	private Factory _factory;
	private Stage _primaryStage;
	private Scene _currentScene;

	//-- Properties --------------------------
	//-- Constructors --------------------------
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		//FD: 1.(new generic.Loader()).initialize(this, eventListener)
		//    2.initialize platform-dependent objects: application-context(?), LogFile, Factory
		
		Locale.setDefault(new Locale("en", "US"));  //TEMP
		
		//0.Initialize generic.Loader
		Loader loader = Loader.createInstance(this, this); //instantiate a Loader

		//1.Initialize platform-dependent objects
		com.hscardref.jfx.common.Constant.initialize();
		Config.STORAGE_BASEPATH = "C:\\Test\\"; //platform-dependent
		
		//2.Initialize Log scheme (File IO)
		//Util dumb = Util.getInstance(); //initialization is not required for JRE platform
		Util.trace(getClass().getSimpleName(), "---------- " +Constant.APP_SHORTNAME+ " get launched [" +com.thinkalike.generic.common.Util.getTimeStamp()+ "] ----------");
		//IMPROVE: retrieve system display metrics for JRE platform
		//Util.trace(getClass().getSimpleName(), String.format("resolution = %s", getResources().getDisplayMetrics().toString()));
		//Util.trace(getClass().getSimpleName(), String.format("resolution=%d*%d, density=%d", System.getProperty("")));
		
		//3.Initialize other platform-dependent elements 
		//Config.PATH_PHOTORESOURCE = "";
		_factory = (Factory)new com.hscardref.jfx.domain.Factory();

		//IMPROVE: show initialization error/warning in GUI
		loader.initialize();  //ignore ret...
		loader.addLoaderEventListener(this);

		_primaryStage = primaryStage;
		_primaryStage.setTitle(Constant.APP_SHORTNAME + " JavaFX");
	    gotoMainScene();
	    _primaryStage.show();
	    
	}

	//----- generic.Platform -----
	@Override
	public String getString(int id) {
		//TODO: Platform.getString() for JRE platform
		return null;
	}

	@Override
	public Object getUIContext() {
		//NOTE: remember call setUIContext() in every Stage.onShow().
		return _currentScene;
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
		Util.logSystem(tag, message, level);
	}

	@Override
	public void logFile(String tag, String message, int level) {
		com.thinkalike.jfx.common.Util.logFile(tag, message, level);
	}

	@Override
	public void logGUI(String tag, String message, int level) {
		Util.logGUI(getUIContext(), tag, message, level); //TODO
	}

	//-- Public and internal Methods --------------------------
	public static HSCardRefApp getInstance() {
		if(_this == null)
			_this = new HSCardRefApp();
		return _this;
	}

	/**
	 * IMPROVE: visibility scope cannot be constrained to sub-package level in Java. work-around = proxy sub-class in sub-package? 
	 * NOTE: call registerUIContext() in onResume() method of each Activity
	 */
	public void registerUIContext(Scene context) { 
//		if (context instanceof Scene)
//			_currentContext = (Scene)context;
//		else
//			throw new IllegalArgumentException("platform-dependent Context type mismatched");
		_currentScene = (Scene)context;
	}
	/**
	 * Context/Activity may have short LifeCycle. Prevent referencing it from Objects with longer LifeCycle (otherwise it will note be GC-ed)   
	 */
	public void unregisterUIContext(Scene context) {
		if (_currentScene == context)
			_currentScene = null;  //otherwise, ignore the request 
	}

	//-- Private and Protected Methods --------------------------
	private Initializable replacePrimarySceneContent(String layout_fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream is_layout = getClass().getClassLoader().getResourceAsStream(layout_fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getClassLoader().getResource(layout_fxml));
        Parent root;
        try {
        	root = (Parent)loader.load(is_layout);
        } finally {
            is_layout.close();
        }
        _currentScene = new Scene(root, com.hscardref.jfx.common.Constant.Screen.WIDTH, com.hscardref.jfx.common.Constant.Screen.HEIGHT);
        //NOTE: JavaFX bug fix. Otherwise Popup window will not inherit global style setting, though it also belongs to the Scene. ref:http://stackoverflow.com/questions/17551774/javafx-styling-pop-up-windows
        _currentScene.getStylesheets().addAll(root.getStylesheets());
        _primaryStage.setScene(_currentScene);
        _primaryStage.sizeToScene();
        return (Initializable) loader.getController();
    }
	
    private void gotoMainScene() {
        try {
        	@SuppressWarnings("unused")
			MainScene mainScene = (MainScene) replacePrimarySceneContent(Res.getLayoutUrl("scene_main.fxml"));
        } catch (Exception ex) {
        	//use Util from generic package
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        	Util.error(getClass().getSimpleName(), String.format("Failed to load MainScene: %s", ex.getMessage()));
        }
    }

	//-- Event Handlers --------------------------
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

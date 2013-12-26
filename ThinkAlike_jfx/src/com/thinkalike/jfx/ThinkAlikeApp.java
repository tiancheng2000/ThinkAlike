package com.thinkalike.jfx;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.Platform;
import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.Factory;
import com.thinkalike.jfx.res.Res;
import com.thinkalike.jfx.view.MainScene;

public class ThinkAlikeApp extends Application implements Platform, Loader.OnLoaderEventListener {

	//-- Constants and Enums --------------------------
	private final static String TAG = ThinkAlikeApp.class.getSimpleName();
	private final static Object DUMB_OBJECT = new Object();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------	
	private static ThinkAlikeApp _this;
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
		
		//0.Initialize generic.Loader
		Loader loader = new Loader(this, this); //instantiate a Loader

		//1.Initialize platform-dependent objects
		Config.STORAGE_BASEPATH = "C:\\Test\\"; //platform-dependent
		
		//2.Initialize Log scheme (File IO)
		//Util dumb = Util.getInstance(); //initialization is not required for JRE platform
		Util.trace(getClass().getSimpleName(), "---------- " +Constant.APP_NAME+ " get launched [" +com.thinkalike.generic.common.Util.getTimeStamp()+ "] ----------");
		//IMPROVE: retrieve system display metrics for JRE platform
		//Util.trace(getClass().getSimpleName(), String.format("resolution = %s", getResources().getDisplayMetrics().toString()));
		//Util.trace(getClass().getSimpleName(), String.format("resolution=%d*%d, density=%d", System.getProperty("")));
		
		//3.Initialize other platform-dependent elements 
		//Config.PATH_PHOTORESOURCE = "";
		_factory = (Factory)new com.thinkalike.jfx.domain.Factory();

		//IMPROVE: show initialization error/warning in GUI
		loader.initialize();

		_primaryStage = primaryStage;
		_primaryStage.setTitle(Constant.APP_NAME + " JavaFX");
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
	public static ThinkAlikeApp getInstance() {
		if(_this == null)
			_this = new ThinkAlikeApp();
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
        _currentScene = new Scene(root, com.thinkalike.jfx.common.Constant.Screen.WIDTH, com.thinkalike.jfx.common.Constant.Screen.HEIGHT);
        //NOTE: JavaFX bug fix. Otherwise Popup window will not inherit global style setting, though it also belongs to the Scene. ref:http://stackoverflow.com/questions/17551774/javafx-styling-pop-up-windows
        _currentScene.getStylesheets().addAll(root.getStylesheets());
        _primaryStage.setScene(_currentScene);
        _primaryStage.sizeToScene();
        return (Initializable) loader.getController();
    }
	
    private void gotoMainScene() {
        try {
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
	public void onReloaded() {
	}

}

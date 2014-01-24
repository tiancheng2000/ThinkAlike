package com.thinkalike.generic.common;

/**
 * Manage properties/constants which can be configured by admins or users (1.xml/properties file 2.runtime param)
 */
public class Config {
	//1.Message Output / Logging related
    public static class LogLevel {
    	private static String[] messages = new String[]{"TRACE","WARN","ERROR"};
    	public static final int TRACE = 1; 
    	public static final int WARN = 2; 
    	public static final int ERROR = 3;
    	public static String toString(int level){
    		return messages[level-LogLevel.TRACE];
    	}
    };
	public static final boolean LOGGING_USING_LOG = true;
	public static final boolean LOGGING_USING_LOGFILE = true;
	public static int LogLevel_UsingLogFile = LogLevel.TRACE;
	public static final String LOGFILE_NAME = Constant.APP_NAME + ".log.txt";
	public static final boolean LOGGING_USING_GUI = true; //include: Notification(Toast, StatusBar, Dialog), View(setText())
	public static int LogLevel_UsingGUI = LogLevel.WARN;
	
	//2.Path related
	//public static final String DOWNLOAD_PATH = "/sdcard/";
	public static String STORAGE_BASEPATH = Util.appendPath(System.getProperty("user.dir"), Constant.FILEPATH_SEPARATOR);
	//relative paths
	public static final String PATH_ROOT = Constant.APP_NAME;
	public static final String PATH_TYPE_A = Util.appendPath(Constant.APP_NAME, "Druid");
    public static final String PATH_TYPE_B = Util.appendPath(Constant.APP_NAME, "Hunter");
    public static final String PATH_TYPE_C = Util.appendPath(Constant.APP_NAME, "Mage");
    public static final String PATH_TYPE_D = Util.appendPath(Constant.APP_NAME, "Paladin");
    public static final String PATH_TYPE_E = Util.appendPath(Constant.APP_NAME, "Priest");
    public static final String PATH_TYPE_F = Util.appendPath(Constant.APP_NAME, "Rogue");
    public static final String PATH_TYPE_G = Util.appendPath(Constant.APP_NAME, "Shaman");
    public static final String PATH_TYPE_H = Util.appendPath(Constant.APP_NAME, "Warlock");
    public static final String PATH_TYPE_I = Util.appendPath(Constant.APP_NAME, "Warrior");
    public static final String PATH_TYPE_J = Util.appendPath(Constant.APP_NAME, "Neutral");
    public static final String PATH_TYPE_K = Util.appendPath(Constant.APP_NAME, "Customize");
    
	//3.Text Encoding
    public static final String DEFAULT_TEXT_ENCODING = "UTF-8";
    
	//IMPROVE: Implement Config load/reload/save related schemes
    public static final boolean TESTCODE_ON = true;
    
}

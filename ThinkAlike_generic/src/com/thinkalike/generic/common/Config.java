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

package com.thinkalike.generic.common;

/**
 * Manage properties/constants which can be configured by admins or users (1.xml/properties file 2.runtime param)
 * Generally contains <i>project-specific</i> contents.
 */
public class Config {

	//0.for Key-value mapping
	//Key-value pairs that could be configured in project base.
	public enum Key { 
		LOGGING_USING_LOG, LOGGING_USING_LOGFILE, LogLevel_UsingLogFile, LOGFILE_NAME, LOGGING_USING_GUI, LogLevel_UsingGUI, 
		STORAGE_BASEPATH, PATH_ROOT, BUFFER_READ_SIZE, BUFFER_ZIP_SIZE,
		DEFAULT_TEXT_ENCODING,
		TESTCODE_ON
		};
	private final static Object DUMB_OBJECT = new Object();

	//1.Message Output / Logging related
    public static class LogLevel {
    	private static final String[] messages = new String[]{"TRACE","WARN","ERROR"};
    	public static final int TRACE = 1; 
    	public static final int WARN = 2; 
    	public static final int ERROR = 3;
    	public static String toString(int level){
    		return messages[level-LogLevel.TRACE];
    	}
    };
	public static boolean LOGGING_USING_LOG = true;
	public static boolean LOGGING_USING_LOGFILE = true;
	public static int LogLevel_UsingLogFile = LogLevel.TRACE;
	public static String LOGFILE_NAME = Constant.FRAMEWORK_SHORTNAME + ".log.txt";
	public static boolean LOGGING_USING_GUI = true; //include: Notification(Toast, StatusBar, Dialog), View(setText())
	public static int LogLevel_UsingGUI = LogLevel.WARN;
	
	//2.Path related
	//public static final String DOWNLOAD_PATH = "/sdcard/";
	public static String STORAGE_BASEPATH = Util.appendPath(System.getProperty("user.dir"), 
											com.thinkalike.generic.common.Constant.FILEPATH_SEPARATOR);
	public static String PATH_ROOT = Constant.FRAMEWORK_SHORTNAME;
    public static int BUFFER_READ_SIZE = 1 * 1024; //1K
    public static int BUFFER_ZIP_SIZE = 1 * 1024;
    
	//3.Text Encoding
    public static String DEFAULT_TEXT_ENCODING = "UTF-8";
    
	//IMPROVE: Implement Config load/reload/save related schemes
    public static boolean TESTCODE_ON = true;
    

	//-- Public and internal Methods ---------------------------
    public static Object get(Key key){
		Object value = DUMB_OBJECT;
		switch(key){
			case LOGGING_USING_LOG: value = LOGGING_USING_LOG; break;
			case LOGGING_USING_LOGFILE: value = LOGGING_USING_LOGFILE; break;
			case LogLevel_UsingLogFile: value = LogLevel_UsingLogFile; break;
			case LOGFILE_NAME: value = LOGFILE_NAME; break;
			case LOGGING_USING_GUI: value = LOGGING_USING_GUI; break;
			case LogLevel_UsingGUI: value = LogLevel_UsingGUI; break;
			case STORAGE_BASEPATH: value = STORAGE_BASEPATH; break;
			case PATH_ROOT: value = PATH_ROOT; break;
			case BUFFER_READ_SIZE: value = BUFFER_READ_SIZE; break;
			case BUFFER_ZIP_SIZE: value = BUFFER_ZIP_SIZE; break;
			case DEFAULT_TEXT_ENCODING: value = DEFAULT_TEXT_ENCODING; break;
			case TESTCODE_ON: value = TESTCODE_ON; break;
			default: value = Util.getFieldValue(Config.class, key.toString()); if(value==null)value=DUMB_OBJECT; break;
		}
		return value;
    }
}

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

package com.hscardref.generic.common;

import com.thinkalike.generic.common.Util;

/**
 * Manage properties/constants which can be configured by admins or users (1.xml/properties file 2.runtime param)
 * Generally contains <i>project-specific</i> contents.
 */
public class Config extends com.thinkalike.generic.common.Config{
	
	//kw: static blocks will be executed before static methods being called, so as to overwrite static variables of base class 
	//1.Message Output / Logging related
	static {
		LogLevel_UsingLogFile = LogLevel.TRACE;
		LOGFILE_NAME = Constant.APP_SHORTNAME + ".log.txt";
		LogLevel_UsingGUI = LogLevel.WARN;
	}
	
	//2.Path related, File Operation
	//relative paths
	static {
		PATH_ROOT = Constant.APP_SHORTNAME;
	}
	public static final String PATH_TYPE_A = Util.appendPath(Constant.APP_SHORTNAME, "Druid");
    public static final String PATH_TYPE_B = Util.appendPath(Constant.APP_SHORTNAME, "Hunter");
    public static final String PATH_TYPE_C = Util.appendPath(Constant.APP_SHORTNAME, "Mage");
    public static final String PATH_TYPE_D = Util.appendPath(Constant.APP_SHORTNAME, "Paladin");
    public static final String PATH_TYPE_E = Util.appendPath(Constant.APP_SHORTNAME, "Priest");
    public static final String PATH_TYPE_F = Util.appendPath(Constant.APP_SHORTNAME, "Rogue");
    public static final String PATH_TYPE_G = Util.appendPath(Constant.APP_SHORTNAME, "Shaman");
    public static final String PATH_TYPE_H = Util.appendPath(Constant.APP_SHORTNAME, "Warlock");
    public static final String PATH_TYPE_I = Util.appendPath(Constant.APP_SHORTNAME, "Warrior");
    public static final String PATH_TYPE_J = Util.appendPath(Constant.APP_SHORTNAME, "Neutral");
    public static final String PATH_TYPE_K = Util.appendPath(Constant.APP_SHORTNAME, "Customize");
    static {
    	BUFFER_READ_SIZE = 1 * 1024; //1K
    	BUFFER_ZIP_SIZE = 1 * 1024;
    }
    
	//3.Text Encoding


    //NOTE: override static methods of base class, so as to execute static statements of the subclass when being called.
    //Only called by generic modules. For domain-specific Configs, directly access them using Config.xxx
    public static Object get(Key key){
		return com.thinkalike.generic.common.Config.get(key);
    }
    
}

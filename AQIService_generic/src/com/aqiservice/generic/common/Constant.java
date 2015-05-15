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

package com.aqiservice.generic.common;

//IMPROVE: project-specific Constant, platform-specific Constant, and framework Constant should be distinguished
public class Constant {
	//--- Application related -----------------------------
	public static final String APP_FULLNAME = "AQIService";
	public static final String APP_SHORTNAME = "AQIService";
	public static final String UNCAUGHTEXCEPTION_RECEIVER_MAIL = "tchu_2000@sina.com.cn";
		
	//--- System dependent --------------------------------
	//--- File Operation ----------------------------------
	//--- Data Access -------------------------------------
	//--- Data Representation -----------------------------
    //--- Screen Layout -----------------------------------

	//--- Drag & Drop -------------------------------------

	//--- [Project-specific] Property Change Event, Event Observer/Handler -----------------------------------
    //FD: Notify the property change of XxxViewModel (by using ViewModelBase's pcs scheme). Subscribers(XxxView/Control) will judge acceptance based on Event.source,.propertyName.
    public static class PropertyName{
    	public static final String AQIData = "aqidata";
    }
    
    //--- Log related -------------------------------------
    //Log Tags -- reference to LogTag.java  
    //Log Messages: internally used only!!
    
}

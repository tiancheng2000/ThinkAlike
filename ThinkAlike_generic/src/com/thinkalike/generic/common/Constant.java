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
 * Manage properties/constants which can be tuned by developers.
 * <i>Project-specific</i> contents should be moved to project-scope generic.common.Constant class.
 */
public class Constant {
		
	//--- Application related -----------------------------
	public static final String FRAMEWORK_FULLNAME = "Think Alike";
	public static final String FRAMEWORK_SHORTNAME = "ThinkAlike";
		
	//--- System specific --------------------------------
    public static enum OsType {Android, JRE7, JRE8};
	public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String FILEPATH_SEPARATOR = System.getProperty("file.separator"); //"\\";
    public static final String APP_BASEPATH = Util.appendPath(System.getProperty("user.dir"), FILEPATH_SEPARATOR);
    public static final String URLPATH_SEPARATOR = "/";
    public static final char URLPATH_SEPARATOR_CHAR = URLPATH_SEPARATOR.charAt(0);
    public static final char NAMESPACE_SEPARATOR_CHAR = '.';

	//--- File Operation ----------------------------------
    public static enum SortType {Name, Modified, Type, Nothing};
    public static enum SortOrder {Ascend, Descend};
    
	//--- Data Access -------------------------------------
	//--- Data Representation -----------------------------

    //--- Screen Layout -----------------------------------
    public static class Layout{
        public class Orientation{
        	public static final int HORIZONTAL = 1;
        	public static final int VERTICAL = 2;
        }
        public class Dimension{
        	public static final int MATCH_PARENT = -1;
        	public static final int WRAP_CONTENT = -2;
        }
    }

	//--- [Project-independent] Property Change Event, Event Observer/Handler -----------------------------------
    //FD: Notify the property change of XxxViewModel (by using ViewModelBase's pcs scheme). Subscribers(XxxView/Control) will judge acceptance based on Event.source,.propertyName.
    public static class PropertyName{
    	public static final String IsBusy = "isBusy";
    }

    //--- Log related -------------------------------------
    //Log Tags -- reference to LogTag.java  
    //Log Messages: internally used only!!
    
}

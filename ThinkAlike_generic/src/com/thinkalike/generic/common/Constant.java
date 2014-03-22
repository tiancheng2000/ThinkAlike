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

//IMPROVE: a part of values should be moved to generic package
public class Constant {
	//--- Application related -----------------------------
	public static final String APP_NAME = "ThinkAlike";
	public static final String UNCAUGHTEXCEPTION_RECEIVER_MAIL = "tchu@psh.com.cn";
		
	//--- System dependent --------------------------------
	public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String FILEPATH_SEPARATOR = System.getProperty("file.separator"); //"\\";
    public static final String APP_BASEPATH = Util.appendPath(System.getProperty("user.dir"), FILEPATH_SEPARATOR);
    public static final String URLPATH_SEPARATOR = "/";
    public static final char URLPATH_SEPARATOR_CHAR = URLPATH_SEPARATOR.charAt(0);
    public static final char NAMESPACE_SEPARATOR_CHAR = '.';

	//--- File Operation ----------------------------------
    public static final int BUFFER_READ_SIZE = 1 * 1024; //1K
    public static final int BUFFER_ZIP_SIZE = 1 * 1024;
    public static enum SortType {Name, Modified, Type, Nothing};
    public static enum SortOrder {Ascend, Descend};
    
	//--- Data Access -------------------------------------
	//--- Data Representation -----------------------------

    //--- Screen Layout -----------------------------------
    public class Layout{
        public class Orientation{
        	public static final int HORIZONTAL = 1;
        	public static final int VERTICAL = 2;
        }
        public class Dimension{
        	public static final int MATCH_PARENT = -1;
        	public static final int WRAP_CONTENT = -2;
        }
    }

    //--- Drag & Drop -------------------------------------

	//--- Property Change Event, Event Observer/Handler -----------------------------------
    //FD: 1.Notify the property change of XxxViewModel (by using ViewModelBase's pcs scheme). Subscribers(XxxView/Control) will judge acceptance based on Event.source,.propertyName.
    //    2.
    public class PropertyName{
    	public static final String ID = "id";
    	public static final String Name = "name";
    	public static final String Content = "content";
    	public static final String IsBusy = "isBusy";
    	//public static final String NodeType = "nodeType";
    	public static final String NodeList = "nodeList";
    	public static final String Node = "node";
    }
    
    //--- NodeSelector ------------------------------------
    public static final String[] EXTENSIONS_IMAGE = new String[]{"jpg", "png", "gif", "jpeg", "bmp"};
    public static final String[] EXTENSIONS_TEXT = new String[]{"txt"};
    public static final String[] EXTENSIONS_VIDEO = new String[]{"3gp", "mp4", "wmv", "avi"};
    public static final String[] EXTENSIONS_AUDIO = new String[]{"mp3", "wma", "mid"};
    
    //--- Drag & Drop -------------------------------------
	public class DragLabel{
		//public static final String Resource_Text = "Resource_Text";
		public static final String Nothing = "Nothing";
	}
	public static String getDragLabel(Object object){
		//if(object instanceof TextResource){
		//	return DragLabel.Resource_Text;
		//}
		return DragLabel.Nothing;
	}
    
    //--- Log related -------------------------------------
    //Log Tags -- reference to LogTag.java  
    //Log Messages: internally used only!!
    
}

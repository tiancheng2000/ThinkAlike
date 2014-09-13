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

package com.hscardref.android.common;

//IMPROVE: a part of values should be moved to generic package
public class Constant {
	//--- Application related -----------------------------
		
	//--- System dependent --------------------------------

	//--- File Operation ----------------------------------
    public static final int BUFFER_READ_SIZE = 1 * 1024; //1K
    public static final int BUFFER_ZIP_SIZE = 1 * 1024;
    
	//--- Screen Layout -----------------------------------
   
    //--- Drag & Drop -------------------------------------

    //--- NodeSelector UI ---------------------------------
    public static class NodeSelector{
    	public static final int NUM_CELLS_IN_NODELIST_VERT = 4;
    }
    
    public static class WorkArea{
    	public static final int TEXT_FONTSIZE_DEFAULT = 16; //px
    	public static final int TEXT_FONTSIZE = 20; //px
    }
    
    //--- Drag & Drop -------------------------------------
    //ref: generic.common
        
    //--- Log related -------------------------------------
    //Log Tags -- for more, reference to generic.common.LogTag.java  
	
}

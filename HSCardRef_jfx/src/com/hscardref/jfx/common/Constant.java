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

package com.hscardref.jfx.common;

import com.hscardref.jfx.assets.Assets;
import com.hscardref.jfx.res.Res;
import com.thinkalike.jfx.common.Util;

//IMPROVE: a part of values should be moved to generic package
public class Constant extends com.thinkalike.jfx.common.Constant {
	//--- Application related -----------------------------
		
	//--- System dependent --------------------------------

	//--- File Operation ----------------------------------
    
	//--- Resource, Asset files ---------------------------
	static {
		URL_RESOURCE_BASE = Util.namespaceToPath(Res.class.getPackage().getName()); //"{project_ns}/jfx/res/";
		URL_RESOURCE_IMAGES = Util.appendUrl(URL_RESOURCE_BASE, "images");
		URL_RESOURCE_LAYOUT = Util.appendUrl(URL_RESOURCE_BASE, "layout");
		URL_ASSET_BASE = Util.namespaceToPath(Assets.class.getPackage().getName()); //"{project_ns}/jfx/assets/";
	}
    
	//--- Screen Layout -----------------------------------
    public static class Screen{
    	public static final int WIDTH = 1024;
    	public static final int HEIGHT = 780;
    }
   
    //--- Drag & Drop -------------------------------------
    //ref: generic.common

    //--- NodeSelector UI ---------------------------------
    public static class NodeSelector{
    	public static final int DEFAULT_NODELIST_WIDTH = 120;
    	public static final int DEFAULT_NODELIST_HEIGHT = 166;
    }
    
    public static class WorkArea{
    	public static final int TEXT_FONTSIZE_DEFAULT = 16;  //px
    	public static final int TEXT_FONTSIZE = 20;  //px
    }

    //--- Log related -------------------------------------
    //Log Tags -- for more, reference to generic.common.LogTag.java  
	

	//--- Initialization -----------------------------
	//kw: must be called so as to make sure the static statements be executed before being referenced. 
	public static void initialize(){}
	
}

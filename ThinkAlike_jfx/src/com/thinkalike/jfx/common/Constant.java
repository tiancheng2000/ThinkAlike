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

package com.thinkalike.jfx.common;

import com.thinkalike.jfx.assets.Assets;
import com.thinkalike.jfx.res.Res;

//IMPROVE: a part of values should be moved to generic package
public class Constant {
	//--- Application related -----------------------------
		
	//--- System dependent --------------------------------

	//--- File Operation ----------------------------------
    
	//--- Resource, Asset files ---------------------------
	//IMPROVE: resource/assets url is project-specific and should be handled in code base (Res class, Assets class) 
	//NOTE: url path SHOULD end with "/", but not start with one.
	public static String URL_RESOURCE_BASE = Util.namespaceToPath(Res.class.getPackage().getName()); //"{framework_ns}/jfx/res/";
	public static String URL_RESOURCE_IMAGES = Util.appendUrl(URL_RESOURCE_BASE, "images");
	public static String URL_RESOURCE_LAYOUT = Util.appendUrl(URL_RESOURCE_BASE, "layout");
	public static String URL_ASSET_BASE = Util.namespaceToPath(Assets.class.getPackage().getName()); //"{framework_ns}/jfx/assets/";

	//--- Screen Layout -----------------------------------
   
    //--- Drag & Drop -------------------------------------

    //--- Generic UI ---------------------------------
	public static final int TEXT_FONTSIZE_DEFAULT = 16; //px
    
    //--- Log related -------------------------------------
    //Log Tags -- for more, reference to generic.common.LogTag.java  
	
}

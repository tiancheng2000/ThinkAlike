package com.thinkalike.jfx.common;

import com.thinkalike.generic.common.Util;
import com.thinkalike.jfx.assets.Assets;
import com.thinkalike.jfx.res.Res;

//IMPROVE: a part of values should be moved to generic package
public class Constant {
	//--- Application related -----------------------------
		
	//--- System dependent --------------------------------

	//--- File Operation ----------------------------------
    
	//--- Resource, Asset files ---------------------------
	//NOTE: url path SHOULD end with "/", but not start with one.
	public static final String URL_RESOURCE_BASE = Util.namespaceToPath(Res.class.getPackage().getName()); //"{project_ns}/jfx/res/";
	public static final String URL_RESOURCE_IMAGES = Util.appendUrl(URL_RESOURCE_BASE, "images");
	public static final String URL_RESOURCE_LAYOUT = Util.appendUrl(URL_RESOURCE_BASE, "layout");
	public static final String URL_ASSET_BASE = Util.namespaceToPath(Assets.class.getPackage().getName()); //"{project_ns}/jfx/assets/";
    
	//--- Screen Layout -----------------------------------
    public class Screen{
    	public static final int WIDTH = 1024;
    	public static final int HEIGHT = 780;
    }
   
    //--- Drag & Drop -------------------------------------

    //--- NodeSelector UI ---------------------------------
    public class NodeSelector{
    	public static final int DEFAULT_NODELIST_WIDTH = 160;
    	public static final int DEFAULT_NODELIST_HEIGHT = 166;
    }
    
    public class WorkArea{
    	public static final int TEXT_FONTSIZE_DEFAULT = 16;  //px
    	public static final int TEXT_FONTSIZE = 20;  //px
    }

    //--- Drag & Drop -------------------------------------
    //ref: generic.common
        
    //--- Log related -------------------------------------
    //Log Tags -- for more, reference to generic.common.LogTag.java  
	
}

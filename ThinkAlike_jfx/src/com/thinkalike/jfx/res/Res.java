package com.thinkalike.jfx.res;

import com.thinkalike.generic.common.Util;
import com.thinkalike.jfx.common.Constant;

/**
 * Resources helper class
 */
public class Res {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static String getImageUrl(String url){
		return Util.appendUrl(Constant.URL_RESOURCE_IMAGES, url);
	}
	public static String getLayoutUrl(String url){
		return Util.appendUrl(Constant.URL_RESOURCE_LAYOUT, url);
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

package com.thinkalike.jfx.domain;

import com.thinkalike.generic.dal.AssetManagerLocal;
import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.jfx.assets.Assets;
import com.thinkalike.jfx.control.ImageNodeView;
//import com.thinkalike.generic.viewmodel.control.ITextResourceView;
//import com.thinkalike.jfx.control.TextResourceView;


public class Factory implements com.thinkalike.generic.domain.Factory{
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//SmartClient Framework Domain ----------
	@Override
	public IAssetManagerLocal createAssetManagerLocal(Object context){
		return new AssetManagerLocal(Assets.class.getResource(""));
	}

	//Business Domain ----------
	//  XxxNode ---
	@Override
	public IImageNodeView createImageNodeView(Object uiContext) {
		//IMPROVE: do some instanceof check. 
		return new ImageNodeView();  //ignore uiContext..
	}
	
	//  Layout ---
	

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

package com.thinkalike.android.domain;

import android.content.Context;

import com.thinkalike.android.ThinkAlikeApp;
import com.thinkalike.android.control.ImageNodeView;
import com.thinkalike.android.dal.AssetManagerLocal;
import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;

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
		if (context == null)
			context = ThinkAlikeApp.getInstance().getApplicationContext();
		
		if (context instanceof Context)
			return new AssetManagerLocal((Context) context);
		return null;
	}

	//Business Domain ----------
	//  XxxNode ---
	@Override
	public IImageNodeView createImageNodeView(Object uiContext) {
		return new ImageNodeView((Context) uiContext);
	}
	
	//  Layout ---

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

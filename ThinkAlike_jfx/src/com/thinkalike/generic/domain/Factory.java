package com.thinkalike.generic.domain;

import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;

public interface Factory {
	//SmartClient Framework Domain ----------
	public IAssetManagerLocal createAssetManagerLocal(Object context);

	//Business Domain ----------
	//  XxxNode ---
	public IImageNodeView createImageNodeView(Object uiContext);
	
	//  Layout ---
	
}

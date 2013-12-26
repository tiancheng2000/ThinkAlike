package com.thinkalike.generic.viewmodel.control;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.domain.Node;

public class UIImageNode extends UINode{
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	public String getRelativePath() {return ((ImageNode)getData()).getRelativePath();}
	
	//-- Constructors ------------------------------------------
	public UIImageNode(Object context, Node data) {
		this(context, data, false);
	}
	public UIImageNode(Object context, Node data, boolean createViewFlag) {
		super(context, data, createViewFlag);
		assert(data instanceof ImageNode);
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public INodeView createView() {
		//TODO[V1.0]: (for All kinds of UIResource) if uiContext is not initialized correctly on createView(), should allow caller accept exception and reset a valid uiContext. 
		_view = Loader.getInstance().getPlatform().getFactory().createImageNodeView(_uiContext);
		if(_view == null){
			Util.error(TAG, "View creation failed");
		}
		return (INodeView)super.createView();
	}
	public void attachView(INodeView view) {
		super.attachView(view);
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

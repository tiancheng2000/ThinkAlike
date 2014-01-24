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
	private int _width, _height;
	
	//-- Properties --------------------------------------------
	/**
	 * By default: 1.Image auto-fits(center-fit) ImageView 2.keep aspect ratio 3.background loading 4.smooth
	 * 0-value for any one of the dimensions means calculating the other dimension according to aspect ratio.
	 * 0-value for both dimensions means Image will auto-fit ImageView and ImageView auto-fit its container.
	 * percentage value means Image will auto-fit ImageView and ImageView center-fit its container with 
	 * 
	 */
	public void setDimension(int width, int height) {_width=width; _height=height;}
	public int getWidth() {return _width;}
	public int getHeight() {return _height;}
	public String getRelativePath() {return ((ImageNode)getData()).getRelativePath();}
	
	//-- Constructors ------------------------------------------
	public UIImageNode(Object uiContext, Node data) {
		this(uiContext, data, false);
	}
	public UIImageNode(Object uiContext, Node data, boolean createViewFlag) {
		super(uiContext, data, createViewFlag);
		assert(data instanceof ImageNode);
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public INodeView createView() {
		//TODO[V1.0]: (for All kinds of UINode) if uiContext is not initialized correctly on createView(), should allow caller accept exception and reset a valid uiContext. 
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

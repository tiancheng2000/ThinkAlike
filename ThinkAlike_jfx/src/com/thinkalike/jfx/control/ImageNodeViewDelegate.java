package com.thinkalike.jfx.control;

import javafx.scene.image.ImageView;

import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * For existed platform-specific View/Controls, use this class as an adapter.
 * If derived INodeView compatible View/Controls can be directly used, then this class is not necessary. 
 */
public class ImageNodeViewDelegate implements IImageNodeView {
	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNodeViewDelegate.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	ImageView _rawView;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public ImageNodeViewDelegate(ImageView rawView) {
		_rawView = rawView;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void update(UINode uiData) {
		ImageNodeView.update(uiData, _rawView);
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

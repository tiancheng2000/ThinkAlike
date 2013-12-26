package com.thinkalike.generic.domain;

public class ImageNode implements Node {

	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNode.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private String _relativePath; //relative to Config.STORAGE_BASEPATH
	
	//-- Properties --------------------------------------------
	public String getRelativePath() {return _relativePath;}

	//-- Constructors ------------------------------------------
	public ImageNode(String relativePath) {
		_relativePath = relativePath;
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
	
}

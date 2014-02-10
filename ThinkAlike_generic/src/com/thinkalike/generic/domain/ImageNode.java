package com.thinkalike.generic.domain;

public class ImageNode implements Node, IReadOnlyProvider {

	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNode.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	public class RO implements INodeRO{  //forward pattern
		public String getRelativePath() {return ImageNode.this.getRelativePath();}
	}
	
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private final RO _readonly = new RO(); 
	private String _relativePath; //relative to Config.STORAGE_BASEPATH
	
	//-- Properties --------------------------------------------
	public INodeRO getIReadOnlyEntrace() {return _readonly;}
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

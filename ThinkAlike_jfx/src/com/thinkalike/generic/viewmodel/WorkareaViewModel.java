package com.thinkalike.generic.viewmodel;

import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.viewmodel.control.UINode;

public class WorkareaViewModel extends ViewModelBase{
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private static WorkareaViewModel _this;
	private UINode _uiNode;

	//-- Properties --------------------------
	public void setUINode(UINode uiNode){
		UINode oldUINode = _uiNode;
		_uiNode = uiNode; 
		this.firePropertyChange(Constant.PropertyName.NodeList, oldUINode, _uiNode);
	}
	public UINode getUINode(){return _uiNode;}
	
	//-- Constructors --------------------------
	private WorkareaViewModel()
	{
	}
	
	//-- Destructors --------------------------
	public void dispose()
	{
	}
	
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
	public static WorkareaViewModel getInstance() {
		if(_this == null)
			_this = new WorkareaViewModel();
		return _this;
	}
	
	//-- Private and Protected Methods --------------------------
	//-- Event Handlers --------------------------

}

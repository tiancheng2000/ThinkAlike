package com.thinkalike.generic.viewmodel.control;

public interface INodeView {

	/**
	 * apply UI data to Native view.<br>
	 * 
	 * @param uiData UI data to be applied to Native view instance.
	 */
	void update(UINode uiData);

	//IMPROVE: [Style] UI data Attributes + Style Rules => Presentation
	//void updateStyle(Style style);
	
	/**
	 * View may solve platform-specific operations here, on parent view having changed.<br>
	 * 
	 * @param newParentView new parent view. NOT necessarily to be an IParentNodeView instance.
	 */
	//void onParentViewChanged(Object newParentView);
}

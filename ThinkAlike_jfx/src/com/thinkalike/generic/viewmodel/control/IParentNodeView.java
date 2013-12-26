package com.thinkalike.generic.viewmodel.control;

public interface IParentNodeView extends INodeView{

	/**
	 * add a Native view as a child view.<br>
	 * 
	 * @param view platform-related view, e.g.android::TextView.
	 * @param index index of a child view in child view list, after which the new child view should be appended.
	 */
	void addView(Object view);
	void addView(Object view, int index);
	
	/**
	 * remove a Native view from child views.<br>
	 * 
	 * @param view platform-related view, e.g.android::TextView.
	 */
	void removeView(Object view);
	
	/**
	 * remove a Native view from child views.<br>
	 * 
	 * @param oldView child view to be replaced.
	 * @param newView child view which will replace the old one.
	 */
	void replaceView(Object oldView, Object newView);
}

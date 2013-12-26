package com.thinkalike.generic.gesture;

public interface IDragSource{
	
	public interface OnDragListener {
		//may degenerate to private methods in real implementation, called by default system event handler(in constructor)
		boolean onStartDrag(Object view);  
	}

	void setDragObject(Object dragObject);
	void setDragMode(int mode);
}

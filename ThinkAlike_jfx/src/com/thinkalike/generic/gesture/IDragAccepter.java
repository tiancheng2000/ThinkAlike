package com.thinkalike.generic.gesture;

public interface IDragAccepter{

	public interface OnDragListener {
		//may degenerate to private methods in real implementation, called by default system event handler(in constructor)
		boolean onDragStarted(Object view, Object event);
		boolean onDragEntered(Object view, Object event);
		boolean onDrop(Object view, Object event);
		boolean onDragExited(Object view, Object event);
		boolean onDragEnded(Object view, Object event);
	}
	
	void setHandlableDragObjectLabels(String[] labels);
	
}

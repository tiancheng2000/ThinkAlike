package com.thinkalike.generic.domain;

/**
 * base interface for parent-child relationship manipulation
 */
public interface Node {
	
	/*
	//Event Handler
	public interface OnChildrenChangeListener {
		public boolean onChildNodeReplaced(Node oldNode, Node newNode);
	}
	public interface OnParentChangeListener {
		public boolean onRemovedFrom(Node oldParent);
		public boolean onInsertedTo(Node newParent);
	}

	//Hierarchy operations
	public Node appendChild(Node newChild);
	public boolean hasChildNodes(); 
	public int indexOfChild(Node child); 
	public Node insertBefore(Node newChild, Node refChild);
	public Node removeChild(Node oldChild); 
	public void removeChildNodes(); 
	public Node replaceChild(Node newChild, Node refChild);
	public ArrayList<Node> getChildNodes();
	public Node getFirstChild();
	public Node getLastChild();
	public Node getChild(int index);
	public Node getParentNode();
	
	//Event Submittor
	public void setOnAsAChildChangedListener(OnChildrenChangeListener l); //when the node, as a child node, changed, it will notify relevant listeners
	//public void setOnParentChangeListener(OnParentChangeListener l); //not required. handled by self.
	public void fireNodeReplaced(Node newNode);
	public void fireRemovedFrom(Node oldParent);
	public void fireInsertedTo(Node newParent);
	*/
	
}

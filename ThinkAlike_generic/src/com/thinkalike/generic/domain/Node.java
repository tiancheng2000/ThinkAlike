/**
* Copyright 2013-2014 Tiancheng Hu
* 
* Licensed under the GNU Lesser General Public License, version 3.0 (LGPL-3.0, the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://opensource.org/licenses/lgpl-3.0.html
*     
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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

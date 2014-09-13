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

import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * ***NOT USED*** Domain utility class. Should be domain-specific.
 */
public class DomainUtil{
	//-- Constants and Enums -----------------------------------
	public static final String TAG = DomainUtil.class.getSimpleName();
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static DomainUtil _this;
	
	//-- Properties --------------------------------------------
	
	//-- Constructors ------------------------------------------
	private DomainUtil() {
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static DomainUtil getInstance() {
		if(_this == null)
			_this = new DomainUtil();
		return _this;
	}
	
	//Node/Element ---
	//public static void dumpNode(String tag, Node baseNode, int level){}
	//public static UINode getUINode(Node node){}
	public static INodeView getNodeView(UINode uiNode){
		return (uiNode==null)? null : uiNode.getView();
	}

	//IView ---
	//public static void linkView(Node parentNode, Node childNode){}
	//public static void linkView(Node parentNode, Node childNode, int index){}
	//public static void unlinkView(Node parentNode, Node childNode){}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}
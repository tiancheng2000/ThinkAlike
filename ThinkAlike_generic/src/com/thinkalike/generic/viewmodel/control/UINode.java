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

package com.thinkalike.generic.viewmodel.control;

import com.thinkalike.generic.domain.INodeRO;
import com.thinkalike.generic.domain.IReadOnlyProvider;
import com.thinkalike.generic.domain.Node;


public abstract class UINode {
	//-- Constants and Enums -----------------------------------
	public static final String TAG = UINode.class.getSimpleName();
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	protected Object _uiContext; 
	protected Node _data;
	//IMPROVE: for resource-consuming Native views (esp. image controls), consider using WeakReferenece instead.
	protected INodeView _view;
	
	//-- Properties --------------------------------------------
	//NOTE: VOs can manage DOs inside of them, but only have right to provide read-only interface to outside. 
	public INodeRO getDataRO() {return (_data instanceof IReadOnlyProvider)?(INodeRO)((IReadOnlyProvider)_data).getIReadOnlyEntrace():null;}
	protected Node getData() {return _data;}
	public INodeView getView() {return _view;}
	//NOTE: (inherited class) should expose DO's properties here, so as to be referenced(read-only) by View layer.
	
	//-- Constructors ------------------------------------------
	public UINode(Object uiContext, Node data, boolean createViewFlag) {
		_uiContext = uiContext;
		_data = data;
		if(createViewFlag)
			createView();
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public INodeView createView() {
		//1.NOTE: (inherited class) SHOULD instantiate a platform-related IXxxNodeView by using Factory class.
		//e.g.INodeView _view = Loader.getInstance().getPlatform().getFactory()
		// 							.createXxxNodeView(context, this);
		
		if(_view != null){
			/*
			//2.build up View-level link 
			//view-level link must be built up before applying Style (fraction size requires parent's size to apply)
			Node parentNode = (_data==null)? null : _data.getParentNode();
			//_data.setUI() might not be executed yet. _data still has no link to this UINode instance (which is on creation).
			INodeView parentView = DomainUtil.getNodeView(parentNode);
			if(parentView instanceof IParentNodeView){
				Util.trace(LogTag.ViewModel, String.format("View-level linkView: <%s,%s> +++",parentView.getClass().getSimpleName(),_view.getClass().getSimpleName()));
				((IParentNodeView) parentView).addView(_view);
			}
			else
				Util.trace(LogTag.ViewModel, String.format("View-level linkView NOT completed: <%s,%s>",(parentView==null)?null:parentView.getClass().getSimpleName(),_view.getClass().getSimpleName()));
			
			//IMPROVE: Check childNodes. createView() can be called in any sequences. (currently in breadth-first traversal sequence)
			 */

			//3.Update VO to View
			_view.update(this);
		}
		
		return _view;
	}
	public INodeView attachView(INodeView view) {
		_view = view;
		_view.update(this);
		return _view;
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}


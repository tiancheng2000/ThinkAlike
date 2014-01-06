package com.thinkalike.generic.viewmodel;

import java.util.ArrayList;
import java.util.List;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.dal.NodeLoader;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.domain.NodeType;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;

public class NodeSelectorViewModel extends ViewModelBase{
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//IMPROVE: if the following factors can be retrieved dynamically (and efficiently),
	//  we can make this class an independent helper class(e.g. generic.event.VMListenerAdapter<L>):
	//  1.onXxxChanged() method name  2.xxx returned value type  
//	public class VMListenerAdapter implements PropertyChangeListener{
//		
//		private List<VMListener> _listenerList = new ArrayList<VMListener>();
//		public void addVMListener(VMListener l){_listenerList.add(l);}
//		public void removeVMListener(VMListener l){_listenerList.remove(l);}
//
//		@Override
//		public void onPropertyChanged(PropertyChangeEvent event) {
//			assert(event.getPropertyName().equals(Constant.PropertyName.NodeType));
//			NodeType nodeType = (NodeType)event.getNewValue(); 
//			for (VMListener l : _listenerList){
//				l.onCurrentResoruceTypeChanged(nodeType);
//			}
//		}
//	}
//	public interface VMListener{
//		public void onCurrentNodeTypeChanged(NodeType nodeType);
//	}

	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private static NodeSelectorViewModel _this;
	private NodeType _currentNodeType = NodeType.TypeA;
	//IMPROVE: confirm if this variable is necessary
	//private String _activatedItemId = ""; 
	private List<UINode> _uiNodeList = null;

	//-- Properties --------------------------
	public NodeType getCurrentNodeType(){return _currentNodeType;}
	
	//-- Constructors --------------------------
	private NodeSelectorViewModel()
	{
	}
	
	//-- Destructors --------------------------
	public void dispose()
	{
	}
	
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
	public static NodeSelectorViewModel getInstance() {
		if(_this == null)
			_this = new NodeSelectorViewModel();
		return _this;
	}
	

	//-- Private and Protected Methods --------------------------
	private void initData() {
		
	}
	
	private void initLayout() {
		
	}

	private List<UINode> getUINodeList(boolean isForcedReload)
	{
		//MVVM::DataSource: list need to be fetched from somewhere 
		if(_uiNodeList == null || isForcedReload){
			//IMPROVE: 1.consider the UIContext could still be invalid by now -- UINode's createView() should be modified
			//          2.consider the case that one ViewModel can serve several Views -- a.multiple uiContext b.multiple view?
			Object uiContext = Loader.getInstance().getPlatform().getUIContext();
			Util.trace(LogTag.ViewModel, String.format("Load DO: uiContext=%s", (uiContext==null)? "null" : uiContext.getClass().getSimpleName()));

			ImageNode[] imageNodes = NodeLoader.loadImageNodes(_currentNodeType);
			_uiNodeList = new ArrayList<UINode>();
			for (int i=0; i<imageNodes.length; i++){
				_uiNodeList.add(new UIImageNode(uiContext, imageNodes[i], false)); //It will be more efficient to create platform-dependent ImageView after UI dimension(width & height) can be calculated. 
			}           
			Util.trace(LogTag.ViewModel, String.format("Load DO: _uiNodeList.size=%s", (_uiNodeList==null) ? "n/a" : _uiNodeList.size()));
		}
		return _uiNodeList;
	}

	//-- Event Handlers --------------------------
	public void onNodeTypeChanged(){
		onNodeTypeChanged(_currentNodeType);
	}
	public void onNodeTypeChanged(NodeType nodeType){
		Util.trace(LogTag.ViewModel, "[ICommand]::VM onNodeTypeChanged()");
		_currentNodeType = nodeType; //no matter equal or not
		onRefreshNodeList();
	}

	public void onRefreshNodeList(){
		Util.trace(LogTag.ViewModel, "[ICommand]::VM onRefreshNodeList()");
		//Object oldValue = _uiNodeList;
		_uiNodeList = getUINodeList(true);
		this.firePropertyChange(Constant.PropertyName.NodeList, null, _uiNodeList); //force refresh
	}

}

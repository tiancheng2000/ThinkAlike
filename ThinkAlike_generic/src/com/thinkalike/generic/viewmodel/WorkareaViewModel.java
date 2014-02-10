package com.thinkalike.generic.viewmodel;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.INodeRO;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * managed Constant.PropertyName: Node
 */
public class WorkareaViewModel extends ViewModelBase{
	//-- Constants and Enums --------------------------
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private static WorkareaViewModel _this;
	private INodeRO _nodeRO;
	private UINode _uiNode;
	//MVVM
	//NOTE: it's possible a.one VM concerns another b.all VMs concerns a central EventBus
	private NodeSelectorViewModel _vm_nodeSelector = null;
	private PropertyChangeListener _listenToVM_nodeSelector = null; 

	//-- Properties --------------------------
	private void setUINode(UINode uiNode){
		UINode oldValue = _uiNode; 
		_uiNode = uiNode; 
		this.firePropertyChange(Constant.PropertyName.Node, oldValue, _uiNode);
	}
	//public UINode getUINode(){return _uiNode;} //not required yet
	
	//-- Constructors --------------------------
	private WorkareaViewModel()
	{
		final WorkareaViewModel thisInstance = this;
		_vm_nodeSelector = NodeSelectorViewModel.getInstance();
		_listenToVM_nodeSelector = new PropertyChangeListener(){
			@Override
			public void onPropertyChanged(PropertyChangeEvent event) {
				Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
				assert(event.getPropertyName().equals(Constant.PropertyName.Node));
				thisInstance.setNodeRO((INodeRO)event.getNewValue());
			}
		};
		_vm_nodeSelector.addPropertyChangeListener(Constant.PropertyName.Node, _listenToVM_nodeSelector);
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
	private void setNodeRO(INodeRO nodeRO){
		if(_nodeRO == nodeRO)
			return;
		
		//IMPROVE: DTO->DO mechanism will be helpful (with access privilege check at DO side)
		Object uiContext = Loader.getInstance().getPlatform().getUIContext();
		if(nodeRO instanceof ImageNode.RO){
			ImageNode node = new ImageNode(((ImageNode.RO)nodeRO).getRelativePath()); 
			UIImageNode uiNode = new UIImageNode(uiContext, node, false); 
			setUINode(uiNode);
		}
		else if(nodeRO == null){
			setUINode(null);
		}
		_nodeRO = nodeRO;
	}
	
	//-- Event Handlers --------------------------

}

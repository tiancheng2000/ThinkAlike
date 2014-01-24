package com.thinkalike.jfx.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.DomainUtil;
import com.thinkalike.generic.domain.NodeType;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.NodeSelectorViewModel;
import com.thinkalike.generic.viewmodel.WorkareaViewModel;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;
import com.thinkalike.jfx.res.Res;

public class MainScene extends AnchorPane implements Initializable {

	//-- Constants and Enums --------------------------
	public static final String TAG = MainScene.class.getSimpleName();
	//NodeSelector ---
	private static final NodeType[] NodeTypes = new NodeType[]{
		NodeType.TypeA,
		NodeType.TypeB,
		NodeType.TypeC,
		NodeType.TypeD,
		NodeType.TypeE,
		NodeType.TypeF,
		NodeType.TypeG,
		NodeType.TypeH,
		NodeType.TypeI,
		NodeType.TypeJ,
		NodeType.TypeK,
	};
	//sorted in the same order of NodeType definition
	private static final String[] CellImageUrls = new String[]{ 
		"btn_typeA.png",
		"btn_typeB.png",
		"btn_typeC.png",
		"btn_typeD.png",
		"btn_typeE.png",
		"btn_typeF.png",
		"btn_typeG.png",
		"btn_typeH.png",
		"btn_typeI.png",
		"btn_typeJ.png",
		"btn_typeK.png",
	};

	//-- Inner Classes and Structures --------------------------
	private static class NodeTypeCell extends ListCell<NodeType> {
		private final ImageView _iv_cell;
		
		{
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY); //TEXT_ONLY
			_iv_cell = new ImageView();
		}
 		
		@Override
		public void updateItem(NodeType item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null || empty){
				setGraphic(null);
				return;
			}

			String cellImageUrl = CellImageUrls[item.ordinal()];
			_iv_cell.setImage(new Image(Res.getImageUrl(cellImageUrl)));
			setGraphic(_iv_cell);
		}

		//IMPROVE: how to set hover style to the button cell dynamically (the following is useless)
//		public void setCellStyle(String style) {
//			Node node = _iv_cell;
//			node.styleProperty().bind(
//					Bindings.when(node.hoverProperty())
//							.then(new SimpleStringProperty(style))
//							.otherwise(new SimpleStringProperty("")));
//		}
	}
	private static class NodeCell extends ListCell<UINode> {
		//private NodeType _nodeType = null;
		//NodeType getNodeType(){return _nodeType;}
		
		@Override
		public void updateItem(UINode item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null || empty){
				setGraphic(null);
				return;
			}

			//refine context-related UINode attributes
			if(item instanceof UIImageNode){
				
			}
				
			if(item.getView() == null)
				item.createView();
			if(item.getView() instanceof Node)
				setGraphic((Node)item.getView());
			else
				setText(String.format("<Invalid ListCell>: %s", (item.getView()==null)?"null":item.getView().getClass().getSimpleName()));
		}
	}
	
	//-- Delegates and Events --------------------------
	//-- Instance and Shared Fields --------------------------
	//kw: it seems inconvenient to keep the "_" convention for FXML field variables, since the names must be same with those in FXML files (fx:id).
	@FXML
	StackPane sp_nodeSelector;
	@FXML
	ComboBox<NodeType> cb_nodeType;
	@FXML
	ListView<UINode> lv_nodeList;
	@FXML
	StackPane sp_workarea;
	@FXML
	Pane p_work;
	@FXML
	Text txt_nodeContent;
	@FXML
	ImageView iv_nodeContent;

	//MVVM
	private NodeSelectorViewModel _vm_nodeSelector = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_nodeSelector = null; 

	private WorkareaViewModel _vm_workarea = null;
	private PropertyChangeListener _listenToVM_workarea = null; 

	//Sample of another kind of data-binding (by using JavaFX's ObservableList)
	//private ItemListViewModel _vm_itemList = new ItemListViewModel();
    //private ItemDetailViewModel _vm_itemDetail = new ItemDetailViewModel();
    //final ObservableList<Item> _listItems = FXCollections.observableArrayList(_vm_nodeSelector.getInstance().getItemList());        

	//-- Properties --------------------------
	//-- Constructors --------------------------
	public MainScene(){
		//NOTE: UI elements cannot be manipulated here. They are not mapped and initialized yet.
	}
	
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: initialize", getClass().getSimpleName()));

		//0.UI Controls initialization
		//NodeType ComboBox
		Callback<ListView<NodeType>, ListCell<NodeType>> nodeCellFactory = 
				new Callback<ListView<NodeType>, ListCell<NodeType>>() {
					@Override
					public ListCell<NodeType> call(ListView<NodeType> list) {
						NodeTypeCell cell = new NodeTypeCell();
						if(list == null){
							//FD: return the button cell of ComboBox
							//needn't special handling. NodeTypeCell will deal with it.
							//cell.setCellStyle("-fx-opacity: .8;");
						}
						return cell;
					}
				};
		this.cb_nodeType.getItems().addAll(NodeTypes);
		this.cb_nodeType.setButtonCell(nodeCellFactory.call(null));
		this.cb_nodeType.setCellFactory(nodeCellFactory);
		this.cb_nodeType.valueProperty().addListener(new ChangeListener<NodeType>(){
			@Override
			public void changed(ObservableValue<? extends NodeType> ov,
					NodeType oldValue, NodeType newValue) {
				_vm_nodeSelector.onNodeTypeChanged(newValue);
			}
		});
		//NodeList ListView
		this.lv_nodeList
		.setCellFactory(new Callback<ListView<UINode>, ListCell<UINode>>() {
			@Override
			public ListCell<UINode> call(ListView<UINode> list) {
				return new NodeCell();
			}
		});
		this.lv_nodeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lv_nodeList.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<UINode>() {
	                public void changed(ObservableValue<? extends UINode> ov, 
	                		UINode old_val, UINode new_val) {
	                	if(_vm_workarea!=null){
	                		_vm_workarea.setUINode(new_val);
	                	}
	                }
	            });

		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		//FD: 1.通过ICommand模式，实现onNodeTypeChanged()
		//    2.getUINodeList() 通过IProperty模式取得
		final MainScene thisInstance = this;
		if(_vm_nodeSelector == null){
			_vm_nodeSelector = NodeSelectorViewModel.getInstance();
			//IMPROVE: if it's possible that parent activity changes, then the inner NodeAdapter should be 
			//  recreated or get notified (e.g. implement a NodePanelVMListenerBase managing uiContext and nodeType). 
			//listen to relative ViewModel.
			_listenToVM_nodeSelector = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					assert(event.getPropertyName().equals(Constant.PropertyName.NodeList));
					List<UINode> nodeList = (List<UINode>)event.getNewValue();
					thisInstance.updateNodeList(nodeList);
				}
			};
			_vm_nodeSelector.addPropertyChangeListener(Constant.PropertyName.NodeList, _listenToVM_nodeSelector);
		}
		_vm_nodeSelector.onRefreshNodeList();
		
		if(_vm_workarea == null){
			_vm_workarea = WorkareaViewModel.getInstance();
			//IMPROVE: listener of "isBusy" should also be an instance variable, to prevent itself from being recycled. 
			_listenToVM_workarea = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					if (event.getPropertyName().equals(Constant.PropertyName.Node)){
						Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
						updateWorkarea((UINode)event.getNewValue());
					}
				}
			};
			_vm_workarea.addPropertyChangeListener(Constant.PropertyName.Node, _listenToVM_workarea);
		}
		_vm_workarea.setUINode(null);
		
		//2.node type selector
		this.cb_nodeType.setValue(_vm_nodeSelector.getCurrentNodeType()); //will activate PropertyChanged event
		
		//3.UINode --> ListCell 
		//ImageNodeView + TextNodeView, ref:KT:\Study\Java\JavaFX\GUI\ListView\#readme.rtf
//    	txt_nodeContent.setText(new_val.getProperty("content").toString());
//    	Image imageContent = new Image(String.format("file:%s", id)); //path = id
//    	iv_nodeContent.setImage(imageContent); 
		
	}

	//-- Public and internal Methods --------------------------
    //-- Private and Protected Methods --------------------------
	//-- Event Handlers --------------------------
	private void updateNodeList(List<UINode> uiNodeList){
		//IMPROVE: consider using JavaFX::ObservableArrayList or merely PropertyChangeListener for data-binding(UI Update)
		this.lv_nodeList.setItems(FXCollections.observableArrayList(uiNodeList));
		//this.lv_nodeList.getItems().setAll(uiNodeList);  //NOTE: if you don't want to use JavaFX::ObservableArrayList to notify any changes in data list.	
	}

	private void updateWorkarea(UINode uiNode){
		INodeView nodeView = DomainUtil.getNodeView(uiNode); 
		if(nodeView==null){
			Util.error(TAG, "updateWorkarea: cannot get view from work.");
			return;
		}
		else if(!(nodeView instanceof Node)){
			Util.error(TAG, "updateWorkarea: workView is not a valid JavaFX Node object");
			return;
		}
		//RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		sp_workarea.getChildren().clear();
		sp_workarea.getChildren().add((Node)nodeView);
		//nodeView.onParentViewChanged(sp_workarea);
		//Otherwise, use IWorkView.attachedToView(sp_workarea) instead.
	}
}

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

package com.hscardref.jfx.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import com.hscardref.generic.common.Constant;
import com.hscardref.generic.viewmodel.NodeSelectorViewModel;
import com.hscardref.generic.viewmodel.WorkareaViewModel;
import com.hscardref.jfx.res.Res;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.NodeType;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;
import com.thinkalike.jfx.control.ImageNodeView;

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
	//kw: must use static inner class, otherwise every instance of the class will be accompanied with an instance of the enclosing class...  
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

			//TODO: refine context-related UINode attributes
			if(item instanceof UIImageNode){
				
			}
				
			if(item.getView() == null)
				item.createView();
			if(item.getView() instanceof javafx.scene.Node)
				setGraphic((javafx.scene.Node)item.getView());
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
	TilePane tp_work;
	@FXML
	VBox vb_work;
	@FXML
	Label lb_nodeContent;
	@FXML
	//ImageView iv_nodeContent;
	//ImageNodeViewDelegate _inv_nodeContent;
	ImageNodeView inv_nodeContent;
	@FXML
	Button btn_OK;
	@FXML
	HBox hb_nodefilters;
	@FXML
	Button btn_nodefilter_type;
	@FXML
	Button btn_nodefilter_all;
	@FXML
	Button btn_nodefilter_0;
	@FXML
	Button btn_nodefilter_1;
	@FXML
	Button btn_nodefilter_2;
	@FXML
	Button btn_nodefilter_3;
	@FXML
	Button btn_nodefilter_4;
	@FXML
	Button btn_nodefilter_5;
	@FXML
	Button btn_nodefilter_6;
	@FXML
	Button btn_nodefilter_7;
	@FXML
	Button btn_nodefilter_custom;

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
               	if(_vm_nodeSelector!=null){
					_vm_nodeSelector.onNodeTypeChanged(newValue);
				}
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
	                	if(_vm_nodeSelector!=null){
	                		_vm_nodeSelector.onNodeSelected(new_val);
	                	}
	                }
	            });
		//WorkArea
		this.btn_OK.setVisible(false);

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
					@SuppressWarnings("unchecked")
					List<UINode> nodeList = (List<UINode>)event.getNewValue();
					//update platform-specific VO attributes here
					for (UINode uiNode : nodeList){
						if (uiNode instanceof UIImageNode){
							//IMPROVE: JavaFX doesn't have onPreDraw() event as that is in Android.. We must be able to get the proper list cell dimension before updating ImageNodeView.
							((UIImageNode)uiNode).setFitDimension(com.hscardref.jfx.common.Constant.NodeSelector.DEFAULT_NODELIST_WIDTH, 
									com.hscardref.jfx.common.Constant.NodeSelector.DEFAULT_NODELIST_HEIGHT);
						}
					}
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
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					if (event.getPropertyName().equals(Constant.PropertyName.Node)){
						updateWorkarea((UINode)event.getNewValue());
					}
				}
			};
			_vm_workarea.addPropertyChangeListener(Constant.PropertyName.Node, _listenToVM_workarea);
		}
		
		//2.node type selector
		this.cb_nodeType.setValue(_vm_nodeSelector.getCurrentNodeType()); //will activate PropertyChanged event
		
		//3.UINode --> ListCell 
		//ImageNodeView + TextNodeView, ref:KT:\Study\Java\JavaFX\GUI\ListView\#readme.rtf
//    	this.txt_nodeContent.setText(new_val.getProperty("content").toString());
//    	Image imageContent = new Image(String.format("file:%s", id)); //path = id
//    	this.iv_nodeContent.setImage(imageContent); 
//		_inv_nodeContent = new ImageNodeViewDelegate(this.iv_nodeContent);
				
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
		if (uiNode == null){
			this.inv_nodeContent.setImage(null);
			this.btn_OK.setVisible(false);
			return;
		}
		
		INodeView nodeView = uiNode.getView(); 
		if(nodeView==null){
			nodeView = uiNode.attachView(inv_nodeContent); //attach and update uiData to an existed raw ImageView 
			this.btn_OK.setVisible(true);
		}
		else if(nodeView instanceof javafx.scene.Node){
			sp_workarea.getChildren().clear();
			sp_workarea.getChildren().add((javafx.scene.Node)nodeView);
			//nodeView.onParentViewChanged(sp_workarea);
			//Otherwise, use IWorkView.attachedToView(sp_workarea) instead.
		}
		else{
			Util.error(TAG, "updateWorkarea: workView is not a valid JavaFX Node object");
			return;
		}
	}
}

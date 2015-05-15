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

package com.helloworld.jfx.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import com.helloworld.generic.common.Constant;
import com.helloworld.generic.viewmodel.HelloViewModel;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.jfx.control.ImageNodeView;

public class MainController implements Initializable {

	//-- Constants and Enums --------------------------
	public static final String TAG = MainController.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------
	//-- Instance and Shared Fields --------------------------
	//kw: it seems inconvenient to keep the "_" convention for FXML field variables, since the names must be same with those in FXML files (fx:id).
	@FXML
	Label l_message;
	@FXML
	ImageNodeView inv_image;

	//MVVM
	private HelloViewModel _vm_hello = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_hello = null; 

	//Sample of another kind of data-binding (by using JavaFX's ObservableList)
	//private ItemListViewModel _vm_itemList = new ItemListViewModel();
    //private ItemDetailViewModel _vm_itemDetail = new ItemDetailViewModel();
    //final ObservableList<Item> _listItems = FXCollections.observableArrayList(_vm_hello.getInstance().getItemList());        
	
	//-- Properties --------------------------
	//-- Constructors --------------------------
	public MainController(){
		//NOTE: UI elements cannot be manipulated here. They are not mapped and initialized yet.
	}
	
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: initialize", getClass().getSimpleName()));

		//0.UI Controls initialization

		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		final MainController thisInstance = this;
		if(_vm_hello == null){
			_vm_hello = HelloViewModel.getInstance();
			//listen to relative ViewModel
			_listenToVM_hello = new PropertyChangeListener(){
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					Util.trace(LogTag.ViewModel, String.format("[IProperty] PropertyChanged(name=%s, value=%s, listener=%s)", event.getPropertyName(), event.getNewValue(), thisInstance.getClass().getSimpleName()));
					if(event.getPropertyName().equals(Constant.PropertyName.HelloMessage)){
						assert(event.getNewValue() instanceof String);
						final String value  = (String) event.getNewValue();
						//thisInstance.updateView(); //it's not safe to directly update UI in callback
						Platform.runLater(new Runnable() {
		                     @Override public void run() {
		                    	 thisInstance.updateView(value);
		                     }
		                });
					}
					else if(event.getPropertyName().equals(Constant.PropertyName.HelloImage)){
						assert(event.getNewValue() instanceof UIImageNode);
						final UIImageNode value  = (UIImageNode) event.getNewValue();
						Platform.runLater(new Runnable() {
		                     @Override public void run() {
		                    	 thisInstance.updateView(value);
		                     }
		                });
					}
				}
			};
			_vm_hello.addPropertyChangeListener(Constant.PropertyName.HelloMessage, _listenToVM_hello);
			_vm_hello.addPropertyChangeListener(Constant.PropertyName.HelloImage, _listenToVM_hello);
		}
		l_message.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				_vm_hello.onRefresh(); //ICommand
			}
		});
		
		//2.Activate the first rendering
		//_vm_hello.onRefresh();
	}

	//-- Public and internal Methods --------------------------
    //-- Private and Protected Methods --------------------------
	//-- Event Handlers --------------------------
	private void updateView(String helloMessage){
		l_message.setText(helloMessage);
	}
	private void updateView(UIImageNode helloImage){
		INodeView nodeView = helloImage.getView(); 
		if(nodeView==null){
			nodeView = helloImage.attachView(inv_image); //attach and update uiData to an existed raw ImageView 
		}
		else
			inv_image = (ImageNodeView)nodeView;
	}
}

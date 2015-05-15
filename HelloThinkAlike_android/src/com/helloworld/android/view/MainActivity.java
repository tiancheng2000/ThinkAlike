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

package com.helloworld.android.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.helloworld.R;
import com.helloworld.android.HelloThinkAlikeApp;
import com.helloworld.generic.common.Constant;
import com.helloworld.generic.viewmodel.HelloViewModel;
import com.thinkalike.android.common.UncaughtExceptionHandler;
import com.thinkalike.android.control.ImageNodeView;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.event.PropertyChangeEvent;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.viewmodel.control.INodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;

/**
 * This activity has different presentations for handset and tablet-size devices.
 * <p>
 * The activity makes heavy use of fragments. The list of resource is a
 * {@link NodeSelectorFragment} and the work area is a {@link WorkareaFragment}.
 * <p>
 * This activity also implements the required {@link NodeSelectorFragment.FragmentCallbacks}
 * interface to listen for resource drag&drop.
 */
public class MainActivity extends Activity {

	//-- Constants and Enums ----------------------------------------------
	//-- Inner Classes and Structures -------------------------------------
	//-- Delegates and Events ---------------------------------------------
	//-- Instance and Shared Fields ---------------------------------------
	protected UncaughtExceptionHandler _ueh = new UncaughtExceptionHandler(this, Constant.APP_SHORTNAME, Constant.UNCAUGHTEXCEPTION_RECEIVER_MAIL);
	//MVVM
	private HelloViewModel _vm_hello = null;
	/**
	 * listen to relative ViewModel. SHOULD be an instance variable.<br>
	 * Registration side (ViewModel) will only keep listener's WeakReference, so that View can be safely released.
	 */
	private PropertyChangeListener _listenToVM_hello = null; 
	private TextView _l_message;
	private ImageNodeView _inv_image;

	//-- Properties -------------------------------------------------------
	//-- Constructors -----------------------------------------------------
	//-- Destructors ------------------------------------------------------
	//-- Base Class Overrides ---------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onCreate", getClass().getSimpleName()));
		HelloThinkAlikeApp.getInstance().registerUIContext(this); //Application.getUIContext() will be used as uiContext by ViewModel in some case, which happens before onResume(). 
		super.onCreate(savedInstanceState);
		_ueh.initialize();
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//0.UI Controls initialization
		_l_message = (TextView)findViewById(R.id.l_message);
		_inv_image = (ImageNodeView)findViewById(R.id.inv_image);
		
		//1.ViewModel related: 1.IProperty event for UI Update 2.ICommand for dispatching UI Command to ViewModel
		final MainActivity thisInstance = this;
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
						thisInstance.updateView(value);
					}
					else if(event.getPropertyName().equals(Constant.PropertyName.HelloImage)){
						assert(event.getNewValue() instanceof UIImageNode);
						final UIImageNode value  = (UIImageNode) event.getNewValue();
						thisInstance.updateView(value);
					}
				}
			};
			_vm_hello.addPropertyChangeListener(Constant.PropertyName.HelloMessage, _listenToVM_hello);
			_vm_hello.addPropertyChangeListener(Constant.PropertyName.HelloImage, _listenToVM_hello);
		}
		_l_message.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				_vm_hello.onRefresh(); //ICommand
			}
		});
	}

	@Override
	protected void onResume() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onResume", getClass().getSimpleName()));
		HelloThinkAlikeApp.getInstance().registerUIContext(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Util.trace(LogTag.LifeCycleManagement, String.format("%s: onDestroy", getClass().getSimpleName()));
		_ueh.restoreOriginalHandler();
		_ueh = null;
		HelloThinkAlikeApp.getInstance().unregisterUIContext(this);
		super.onDestroy();
	}

	//-- Public and internal Methods --------------------------------------
	//-- Private and Protected Methods ------------------------------------
	//-- Event Handlers ---------------------------------------------------
	private void updateView(String helloMessage){
		_l_message.setText(helloMessage);
	}
	private void updateView(UIImageNode helloImage){
		INodeView nodeView = helloImage.getView(); 
		if(nodeView==null){
			nodeView = helloImage.attachView(_inv_image); //attach and update uiData to an existed raw ImageView 
		}
		else
			_inv_image = (ImageNodeView)nodeView;
	}

}

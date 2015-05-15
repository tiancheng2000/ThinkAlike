/**
* Copyright 2013-2015 Tiancheng Hu
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

package com.helloworld.generic.viewmodel;

import com.helloworld.generic.common.Constant;
import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Constant.OsType;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.viewmodel.ViewModelBase;
import com.thinkalike.generic.viewmodel.control.UIImageNode;

/**
 * managed Constant.PropertyName: HelloMessage
 */
public class HelloViewModel extends ViewModelBase{
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private static HelloViewModel _this;
	private static String _helloMessage = new String("Hello, ThinkAlike!");
	private static UIImageNode _helloImage;

	//-- Properties --------------------------
	public String getHelloMessage(){return _helloMessage;}
	
	//-- Constructors --------------------------
	//-- Destructors --------------------------
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
	public static HelloViewModel getInstance() {
		if(_this == null)
			_this = new HelloViewModel();
		return _this;
	}

	//-- Private and Protected Methods --------------------------
	private void updateHelloMessage(){
		
		//1.hello message
		this.firePropertyChange(Constant.PropertyName.HelloMessage, null, _helloMessage);

		//2.hello image
		if(_helloImage==null){
			OsType osType = Loader.getInstance().getPlatform().getOsType();
			ImageNode node = new ImageNode((osType==OsType.Android) ? Constant.PATH_LOGO_ANDROID : Constant.PATH_LOGO_JAVAFX);
			_helloImage = new UIImageNode(null, node, false); 
		}
		this.firePropertyChange(Constant.PropertyName.HelloImage, null, _helloImage);
		
	}
	
	//-- Event Handlers --------------------------
	public void onRefresh(){
		updateHelloMessage();
	}

}


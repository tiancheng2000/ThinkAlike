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

package com.thinkalike.jfx.control;

import javafx.scene.image.ImageView;

import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.generic.viewmodel.control.UINode;

/**
 * For existed platform-specific View/Controls, use this class as an adapter.
 * If derived INodeView compatible View/Controls can be directly used, then this class is not necessary. 
 */
public class ImageNodeViewDelegate implements IImageNodeView {
	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNodeViewDelegate.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	ImageView _rawView;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public ImageNodeViewDelegate(ImageView rawView) {
		_rawView = rawView;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void update(UINode uiData) {
		ImageNodeView.update(uiData, _rawView);
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

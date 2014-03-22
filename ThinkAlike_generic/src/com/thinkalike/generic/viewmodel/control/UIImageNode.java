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

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.domain.Node;

public class UIImageNode extends UINode{
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private int _fitWidth, _fitHeight;
	
	//-- Properties --------------------------------------------
	/**
	 * fitWidth/fitHeight affects image loading efficiency, and should be tunable at VO level.
	 * By default: 1.Image auto-fits(center-fit) ImageView 2.keep aspect ratio 3.background loading 4.smooth
	 * 0-value for any one of the dimensions means calculating the other dimension according to aspect ratio.
	 * 0-value for both dimensions means Image will auto-fit ImageView and ImageView auto-fit its container.
	 */
	public void setFitDimension(int fitWidth, int fitHeight) {_fitWidth=fitWidth; _fitHeight=fitHeight;}
	public int getFitWidth() {return _fitWidth;}
	public int getFitHeight() {return _fitHeight;}
	public String getRelativePath() {return ((ImageNode)getData()).getRelativePath();}
	
	//-- Constructors ------------------------------------------
	public UIImageNode(Object uiContext, Node data) {
		this(uiContext, data, false);
	}
	public UIImageNode(Object uiContext, Node data, boolean createViewFlag) {
		super(uiContext, data, createViewFlag);
		assert(data instanceof ImageNode);
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public INodeView createView() {
		//TODO[V1.0]: (for All kinds of UINode) if uiContext is not initialized correctly on createView(), should allow caller accept exception and reset a valid uiContext. 
		_view = Loader.getInstance().getPlatform().getFactory().createImageNodeView(_uiContext);
		if(_view == null){
			Util.error(TAG, "View creation failed");
		}
		return (INodeView)super.createView();
	}
	public INodeView attachView(INodeView view) {
		return super.attachView(view);
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

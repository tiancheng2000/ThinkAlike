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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.thinkalike.jfx.res.Res;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;
import com.thinkalike.jfx.common.Util;

public class ImageNodeView extends ImageView implements IImageNodeView {
	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNodeView.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void update(UINode uiData) {
		//core platform-related implementation: DO -> View
		
		if(uiData instanceof UIImageNode){
			
			//0.initialize according to context.
			int width_limit, height_limit;
			width_limit = ((UIImageNode)uiData).getFitWidth();
			height_limit = ((UIImageNode)uiData).getFitHeight();
			if(width_limit==0 && height_limit==0){
				//Image fit ImageView: ImageView should already have its own fitWidth/fitHeight set 
			}
			else{
				//Image has its own fit dimensions
				this.setPreserveRatio(true);
				this.setFitWidth(width_limit);
				this.setFitHeight(height_limit);
			}
			
			//1.call static routine for actual rendering, which is shared with ImageNodeViewDelegate class.
			ImageNodeView.update(uiData, this);
		}
	}

	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	protected static void update(UINode uiData, ImageView rawView) {
		//core platform-related implementation: DO -> View
		if(rawView == null)
			return;
		
		if(uiData instanceof UIImageNode){
			
			//0.initialize according to context.
			int width_limit, height_limit;
			width_limit = (int)rawView.getFitWidth();
			height_limit = (int)rawView.getFitHeight();
			
			//IMPROVE: raise a timeout thread, only set default image when timeout 
			//1.set the default image: 
			rawView.setImage(new Image(Res.getImageUrl("default_image.gif")));

			//2.Async load Image: 
			//IMPROVE: if url contains html-encoded substring (e.g."%20"), it'll be decoded and image will not be correctly loaded.
			final String imageUrl = Util.getAbsoluteUrl(((UIImageNode)uiData).getRelativePath());
			
			//Method 1. Synchronized image loading, with fixed size
			Image image = Util.decodeThumbFromFile(imageUrl, width_limit, height_limit);
			rawView.setImage(image);

			//Method 2. Asynchronized image loading, with fixed size
//			MediaAsyncLoader.asyncLoadImageFile(imagePath,
//												Constant.NodeSelector.DEFAULT_NODELIST_WIDTH,
//												Constant.NodeSelector.DEFAULT_NODELIST_HEIGHT,
//								_onMediaLoadListener);

			//Method 3. Asynchronized image loading, with proper size
//			final ImageNodeView thisInstance = rawView;
//			rawView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
//					@Override
//					public boolean onPreDraw() {
//						thisInstance.getViewTreeObserver().removeOnPreDrawListener(this);  //<-- this is a must
//						int width_image = Util.getActualLayoutWidth(thisInstance);
//						int height_image = Util.getActualLayoutHeight(thisInstance);
//						Util.trace(null, LogTag.ResourceThread, "ImageNodeView.setData, (width_image,height_image)=("+width_image+","+height_image+")");
//						MediaAsyncLoader.asyncLoadImageFile(imagePath, width_image, height_image, _onMediaLoadListener);
// 						return true;
//					}
//			});

		}
	}
	
	//-- Event Handlers ----------------------------------------
}

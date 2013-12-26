package com.thinkalike.jfx.control;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;
import com.thinkalike.jfx.common.Constant;
import com.thinkalike.jfx.common.Util;
import com.thinkalike.jfx.res.Res;

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
			
			//1.set the default image: 
			setImage(new Image(Res.getImageUrl("default_image.gif")));

			//2.Async load Image: 
			//IMPROVE: if url contains html-encoded substring (e.g."%20"), the image will not be correctly loaded.
			final String imageUrl = Util.getAbsoluteUrl(((UIImageNode)uiData).getRelativePath());
			
			//Method 1. Synchronized image loading, with fixed size
			Image thumb = Util.decodeThumbFromFile(imageUrl
													, Constant.NodeSelector.DEFAULT_NODELIST_WIDTH
													, Constant.NodeSelector.DEFAULT_NODELIST_HEIGHT);
			
			setImage(thumb);

			//Method 2. Asynchronized image loading, with fixed size
//			MediaAsyncLoader.asyncLoadImageFile(imagePath,
//												Constant.NodeSelector.DEFAULT_NODELIST_WIDTH,
//												Constant.NodeSelector.DEFAULT_NODELIST_HEIGHT,
//								_onResourceLoadListener);

			//Method 3. Asynchronized image loading, with proper size
//			final ImageNodeView thisInstance = this;
//			this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
//					@Override
//					public boolean onPreDraw() {
//						thisInstance.getViewTreeObserver().removeOnPreDrawListener(this);  //<-- this is a must
//						int width_image = Util.getActualLayoutWidth(thisInstance);
//						int height_image = Util.getActualLayoutHeight(thisInstance);
//						Util.trace(null, LogTag.ResourceThread, "ImageNodeView.setData, (width_image,height_image)=("+width_image+","+height_image+")");
//						MediaAsyncLoader.asyncLoadImageFile(imagePath, width_image, height_image, _onResourceLoadListener);
// 						return true;
//					}
//			});

		}
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

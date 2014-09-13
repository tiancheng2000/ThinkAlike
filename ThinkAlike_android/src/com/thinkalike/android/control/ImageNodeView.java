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

package com.thinkalike.android.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.thinkalike.R;
import com.thinkalike.android.common.Util;
import com.thinkalike.android.dal.MediaAsyncLoader;
import com.thinkalike.android.dal.MediaAsyncLoader.OnMediaLoadListener;
import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.viewmodel.control.IImageNodeView;
import com.thinkalike.generic.viewmodel.control.UIImageNode;
import com.thinkalike.generic.viewmodel.control.UINode;

public class ImageNodeView extends ImageView implements IImageNodeView {
	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNodeView.class.getSimpleName();
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	
	//-- Instance and Shared Fields ----------------------------
	private Context _context;
	//private DragSourceImpl _dragNodeDelegate;
	private OnMediaLoadListener _onMediaLoadListener;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public ImageNodeView(Context context) {
		this(context, null);
	}
	public ImageNodeView(Context context, AttributeSet attrs) {
		super(context, attrs); //NOTE: if you call "super(context)", findViewById() will return null! 
		//_dragNodeDelegate = new DragSourceImpl(this);
		_context = context;
		_onMediaLoadListener = new OnMediaLoadListener() {
			@Override
			public void onMediaLoaded(Object media, Object tag) {
				if(media instanceof Bitmap){
					Util.trace(null, LogTag.ResourceThread, "ImageNodeView onMediaLoaded: image="+tag.toString());
					setImageBitmap((Bitmap)media);
				}
			}

			@Override
			public void onError(int errCode) {
				Util.error(null, LogTag.ResourceThread, "ImageNodeView onMediaLoadListener failed: errCode=" + errCode);
			}
		};
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public void update(UINode uiData) {
		//core platform-related implementation: DO -> View
		
		if(uiData instanceof UIImageNode){
			
			//1.set the default image
			setImageDrawable(_context.getResources().getDrawable(R.drawable.talib_default_image));
			//Util.trace(null, LogTag.MemoryManagement, "totalMemory ===== " + Runtime.getRuntime().totalMemory() + " at getDrawable");

			//2.Async load Image
			final String imagePath = com.thinkalike.generic.common.Util.appendPath(
									Config.STORAGE_BASEPATH, ((UIImageNode)uiData).getRelativePath());
			
			//Method 1. Synchronized image loading
//			Bitmap thumb = Util.createThumbFromFile(imagePath
//													, Constant.NodeView.DEFAULT_NODELIST_WIDTH
//													, Constant.NodeView.DEFAULT_NODELIST_HEIGHT);
//			setImageBitmap(thumb);
			
			//Method 2. Asynchronized image loading, with fixed size
//			MediaAsyncLoader.asyncLoadImageFile(imagePath,
//												Constant.NodeView.DEFAULT_NODELIST_WIDTH,
//												Constant.NodeView.DEFAULT_NODELIST_HEIGHT,
//								_onMediaLoadListener);
			
			//Method 3. Asynchronized image loading, with hard-fix size
			final ImageNodeView thisInstance = this;
			this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
					@Override
					public boolean onPreDraw() {
						thisInstance.getViewTreeObserver().removeOnPreDrawListener(this); //no longer handle this event
						int width_image = Util.getActualLayoutWidth(thisInstance);
						int height_image = Util.getActualLayoutHeight(thisInstance);
						Util.trace(null, LogTag.ResourceThread, "ImageNodeView.update, (width_image,height_image,image)=("+width_image+","+height_image+","+imagePath+")");
						MediaAsyncLoader.asyncLoadImageFile(imagePath, width_image, height_image, _onMediaLoadListener);
 						return true;
					}
			});

		}
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

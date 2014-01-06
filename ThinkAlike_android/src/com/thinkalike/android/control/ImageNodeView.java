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
		super(context);
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
			setImageDrawable(_context.getResources().getDrawable(R.drawable.default_image));
			//Util.trace(null, LogTag.MemoryManagement, "totalMemory ===== " + Runtime.getRuntime().totalMemory() + " at getDrawable");

			//2.Async load Image
			final String imagePath = com.thinkalike.generic.common.Util.appendPath(
									Config.STORAGE_BASEPATH, ((UIImageNode)uiData).getRelativePath());
			
			//Method 1. Synchronized image loading
//			Bitmap thumb = Util.createThumbFromFile(imagePath
//													, Constant.NodeView.DEFAULT_RESOURCELIST_WIDTH
//													, Constant.NodeView.DEFAULT_RESOURCELIST_HEIGHT);
//			setImageBitmap(thumb);
			
			//Method 2. Asynchronized image loading, with hard-fix size
//			MediaAsyncLoader.asyncLoadImageFile(imagePath,
//												Constant.NodeView.DEFAULT_RESOURCELIST_WIDTH,
//												Constant.NodeView.DEFAULT_RESOURCELIST_HEIGHT,
//								_onNodeLoadListener);
			
			//Method 3. Asynchronized image loading, with hard-fix size
			final ImageNodeView thisInstance = this;
			this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
					@Override
					public boolean onPreDraw() {
						thisInstance.getViewTreeObserver().removeOnPreDrawListener(this);  //<-- this is a must
						int width_image = Util.getActualLayoutWidth(thisInstance);
						int height_image = Util.getActualLayoutHeight(thisInstance);
						Util.trace(null, LogTag.ResourceThread, "ImageNodeView.setData, (width_image,height_image,image)=("+width_image+","+height_image+","+imagePath+")");
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

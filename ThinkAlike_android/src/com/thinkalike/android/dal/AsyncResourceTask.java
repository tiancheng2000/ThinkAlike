package com.thinkalike.android.dal;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.thinkalike.android.ThinkAlikeApp;
import com.thinkalike.android.common.Util;
import com.thinkalike.android.dal.MediaAsyncLoader.OnMediaLoadListener;
import com.thinkalike.generic.common.LogTag;

class AsyncResourceTask extends AsyncTask<Object, Integer, Boolean>{
	//-- Constants and Enums -----------------------------------
	public static final String TAG = AsyncResourceTask.class.getSimpleName();

	public enum WorkType{ Text, Image, Video, Audio };
	//{WorkType = Image}
	public static final int PARAM_PATH = 0;
	public static final int PARAM_MAXWIDTH = 1;
	public static final int PARAM_MAXHEIGHT = 2;
	//Error Code
	public static final int ERR_SUCCESS = 0;
	public static final int ERR_OOM = 1;
	public static final int ERR_CANCELLED = 98;
	public static final int ERR_UNKNOWN = 99;
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private WorkType _workType;
	private OnMediaLoadListener _mediaLoadListener;
	private WeakReference<Object> _resultRef;
	private Object _tag;
	private int _errCode;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public AsyncResourceTask(WorkType workType, OnMediaLoadListener mediaLoadListener) {
		_workType = workType;
		_mediaLoadListener = mediaLoadListener;
		_resultRef = new WeakReference<Object>(new String(""));
		_errCode = ERR_SUCCESS;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	protected Boolean doInBackground(Object... params) {
		if(isCancelled()){
			_errCode = ERR_CANCELLED;
			return false;
		}
		
		switch (_workType) {
		case Image:
			try {
				long freeMemBeforeWork = 0, freeMemAfterWork = 0; //for trace memory
				freeMemBeforeWork = Runtime.getRuntime().freeMemory();
				Util.trace(null, LogTag.MemoryManagement, TAG + ": freeMemory ===== " + freeMemBeforeWork + " before decodeThumbFromFile");
				Bitmap result = Util.decodeThumbFromFile((String) params[PARAM_PATH],
						((Integer) params[PARAM_MAXWIDTH]).intValue(),
						((Integer) params[PARAM_MAXHEIGHT]).intValue());
				_resultRef = new WeakReference<Object>(result);
				_tag = params[PARAM_PATH];
				freeMemAfterWork = Runtime.getRuntime().freeMemory();
				Util.trace(null, LogTag.MemoryManagement, TAG + ": freeMemory ===== " + freeMemAfterWork + " after decodeThumbFromFile" +
						" used: " + (freeMemBeforeWork-freeMemAfterWork));
				return true;
			} catch (OutOfMemoryError e) {
				//Util.trace(null, LogTag.UIThread, "Before calling Toast from a non-UI thread."); 
				Util.error(ThinkAlikeApp.getInstance().getUIContext(), LogTag.MemoryManagement, String.format("AsyncWorker[WorkType::%s] OOM: path=%s w=%d h=%d", 
							_workType.toString(), params[PARAM_PATH], ((Integer) params[PARAM_MAXWIDTH]).intValue(), ((Integer) params[PARAM_MAXHEIGHT]).intValue()));
				//Util.trace(null, LogTag.UIThread, "After calling Toast from a non-UI thread."); 
				_errCode = ERR_OOM;
				return false;
			}
		default:
			break;
		}
		_errCode = ERR_UNKNOWN;
		return false;
	}

	@Override
	protected void onCancelled() {
		Util.warn(LogTag.ResourceThread, this.getClass().getSimpleName() + ": call OnMediaLoadListener::onCancelled(), error="+_errCode+",tag="+_tag);
		if(_resultRef!=null){ //IMPROVE: lock it, otherwise it may be released after check
			if(_resultRef.get() instanceof Bitmap){
				((Bitmap) _resultRef.get()).recycle();
			}
		}
		//super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(Boolean succeed) {
		if(_mediaLoadListener != null){
			if (succeed)
				_mediaLoadListener.onMediaLoaded(_resultRef.get(), _tag);
			else{
				Util.warn(LogTag.ResourceThread, this.getClass().getSimpleName() + ": call OnMediaLoadListener::onError()");
				_mediaLoadListener.onError(_errCode);
			}
		}
			
	}
	
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

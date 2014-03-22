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

package com.thinkalike.android.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkalike.android.ThinkAlikeApp;
import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.Config.LogLevel;
import com.thinkalike.generic.common.LogTag;

public class Util extends com.thinkalike.generic.common.Util {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = Util.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static Util _this;
	private static BufferedWriter _logFile; //include: Notification(Toast, StatusBar, Dialog), View(setText())
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public static Util getInstance(){
		if (_this == null)
			_this = new Util();
		return _this;
	}
    private Util() {
        super();
    	if (Config.LOGGING_USING_LOGFILE){
    		String logFilePath = "";
    		try {
    			logFilePath = com.thinkalike.generic.common.Util.appendPath(Config.STORAGE_BASEPATH, Config.LOGFILE_NAME);
				_logFile = new BufferedWriter(new FileWriter(logFilePath, true));
				_logFile.flush(); //Skill: make sure sdcard get mounted
			} catch (IOException e) {
				Util.error(null, TAG, String.format("********** LogFile open failed: %s **********", logFilePath));  
			}
    	}
    }

	//-- Destructors -------------------------------------------
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(_logFile!=null){
			_logFile.flush();
			_logFile.close();
		}
	}
    
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
    //-- 1. Message Output / Logging (Android Implementation) related ------
    //NOTE: Android Log levels : Verbose, Debug, Info, Warning, Error, Fatal
	//NOTE: context is used for GUI output only, can be null or set to current Activity instance
    public static void trace(Context context, String tag, String format, Object... args){
    	trace(context, tag, String.format(format, args));
    }
    public static void trace(Context context, String tag, String message)
    {
    	logFacade(context, tag, message, LogLevel.TRACE);  //trace --> INFO
    }
    public static void warn(Context context, String tag, String format, Object... args){
    	warn(context, tag, String.format(format, args));
    }
    public static void warn(Context context, String tag, String message)
    {
    	logFacade(context, tag, message, LogLevel.WARN);  //warn --> WARN
    }
    public static void error(Context context, String tag, String format, Object... args){
    	error(context, tag, String.format(format, args));
    }
    public static void error(Context context, String tag, String message)
    {
    	logFacade(context, tag, message, LogLevel.ERROR);  //error --> ERROR
    }

    //generally called by logFacade() only
	public static void logSystem(String tag, String message, int level) {
    	if (Config.LOGGING_USING_LOG){
    		int logcatLevel = translateLogLevel(level);
    		//only use tag canonicalization in necessary circumstances. ONLY android.systemIO need this.
        	//NOTE: TAG should be shorter than 23 chars in Android. use LogTag.canonicalize().
        	tag = LogTag.canonicalize(tag, Constant.MAX_LENGTH_LOGTAG);
        	//NOTE: Android LOG's default template already contains time stamp
        	if (Log.isLoggable(tag, logcatLevel)){
        		switch (logcatLevel){
	        		case Log.INFO:
	            		Log.i(tag, message);
	        			break;
	        		case Log.WARN:
	            		Log.w(tag, message);
	        			break;
	        		case Log.ERROR:
	            		Log.e(tag, message);
	        			break;
	        		default:
	        			break;
        		}
        	}
    	}
	}
	public static void logFile(String tag, String message, int level) {
    	if (Config.LOGGING_USING_LOGFILE){
    		if (Config.LogLevel_UsingLogFile <= level && _logFile!=null){
	    		String logLevel = "";
	    		switch (level){
	    		case LogLevel.TRACE:
	    			logLevel = "[INFO]";
	    			break;
	    		case LogLevel.WARN:
	    			logLevel = "[WARN]";
	    			break;
	    		case LogLevel.ERROR:
	    			logLevel = "[ERROR]";
	    			break;
	    		default:
	    			break;
	    		}
	    		try {
	    			_logFile.write(String.format("%s,%s,%s%s", logLevel, tag, message, com.thinkalike.generic.common.Constant.NEW_LINE));
					_logFile.flush(); //Skill: if not call flush() every time, log will not be output at all..
				} catch (IOException e) {}
    		}
    	}
	}
	public static void logGUI(Context context, String tag, String message, int level) {
    	if (Config.LOGGING_USING_GUI){
    		if (Config.LogLevel_UsingGUI <= level && context!=null){
    			//kw: Toast cannot be called from a thread that has no Looper (for creating Handler)
    			//check if Looper already exists
    			if (Looper.myLooper()!=null)
        			Toast.makeText(context, message, Toast.LENGTH_LONG).show(); //Toast.LENGTH_SHORT
    			else{
    				//if there is an active Activity, show the message, otherwise, ignore.
    				if (ThinkAlikeApp.getInstance().getUIContext() instanceof Activity){
    					final Activity currentActivity = (Activity)ThinkAlikeApp.getInstance().getUIContext();
    					final String messageToShow = message;
    					currentActivity.runOnUiThread(new Runnable(){
							@Override
							public void run() {
			    				Toast.makeText(currentActivity, messageToShow, Toast.LENGTH_LONG).show(); //Toast.LENGTH_SHORT
							}
    					});
    				}
    				//kw: the following will block the thread, because Toast.makeText() will never call Looper.myLooper().quit() inside of the Loop
        			//Looper.prepare();
    				//Toast.makeText(context, message, Toast.LENGTH_LONG).show(); //Toast.LENGTH_SHORT
    				//Looper.loop();
    			}
    		}
    	}
	}

    //-- 2. File/Folder/Full Path related ----------------------------------------------------

	//-- 3. GUI: Image transaction related ----------------------------------------------------
    public static BitmapFactory.Options getImageSampleInfo(String imagePath, int width_limit, int height_limit){
//		if(width_limit<=0 && height_limit<=0){
//			Util.error(null, TAG, "width_limit and height_limit cannot both be smaller than 0.");
//			return null;
//		}
		boolean isBothDimensionFree = (width_limit<=0 && height_limit<=0);
		boolean isOneDimensionFree = (width_limit<=0 || height_limit<=0);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		@SuppressWarnings("unused")
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		if(options.outWidth==0 || options.outHeight==0){
			Util.error(null, TAG, "invalid options.outWidth or options.outHeight (=0).");
			return null;
		}
		
		int ratio = 1; //default = keep origin size
		if (!isBothDimensionFree){
			boolean isOriginWider = !isOneDimensionFree && (options.outWidth/options.outHeight) > (width_limit/height_limit);
			ratio = !isOneDimensionFree ? (isOriginWider ? (int)(options.outWidth/width_limit) : (int)(options.outHeight/height_limit))
						: (width_limit<=0 ? (int)(options.outHeight/height_limit) : (int)(options.outWidth/width_limit));
			if (ratio < 1)
				ratio = 1; //NOTE: if origin image is smaller than specified size, it will keep origin size
		}
		options.inSampleSize = ratio;
		options.inJustDecodeBounds = false;
		return options;
    }
    public static int getBytePerPixcel(Bitmap.Config bitmapConfig){
    	switch (bitmapConfig){
			case ALPHA_8:
				return 1;
    		case ARGB_4444:
    		case RGB_565:
    			return 2;
    		case ARGB_8888:
    			return 4;
    		default:
    	    	//Util.error(null, TAG, "Invalid bitmapConfig is used.");
    			return 4; //IMPROVE: it will cost more than 4-byte per pixcel for higher resolution, in near future.
    	}
    }
    public static long calcImageSize(String imagePath, Bitmap.Config bitmapConfig){
    	BitmapFactory.Options options = getImageSampleInfo(imagePath, 0, 0);
    	if (options == null)
    		return 0;
    	return calcImageSize((int) options.outWidth/options.inSampleSize, (int) options.outHeight/options.inSampleSize, bitmapConfig);
    }
    public static long calcImageSize(int width, int height, Bitmap.Config bitmapConfig){
    	return calcImageSize(width, height, getBytePerPixcel(bitmapConfig));
    }
    public static long calcImageSize(int width, int height, int bytePerPixcel){
    	return width * height * bytePerPixcel;
    }

    //IMPROVE: add param: 1.specify ARGB8888 or RGB_565 2.specify whether use mmap file or not
    public static Bitmap decodeThumbFromFile(String imagePath, int width_limit, int height_limit){
    	return decodeThumbFromFile(imagePath, width_limit, height_limit, null);
    }
    public static Bitmap decodeThumbFromFile(String imagePath, int width_limit, int height_limit, Bitmap.Config preferredConfig){
		BitmapFactory.Options options = getImageSampleInfo(imagePath, width_limit, height_limit);
		if(options == null){
			Util.error(null, TAG, "failed to get SampleInfo.");
			return null;
		}
		
		options.inPreferredConfig = preferredConfig;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		return bitmap;
	}
    
    public static void saveImage(Bitmap bitmap, String imagePath) {
    	File file = new File(imagePath);
    	try {
    		file.createNewFile();
    		FileOutputStream fOut = new FileOutputStream(file);
    		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); //PNG will ignore quality setting (2nd param)
    		fOut.flush();
    		fOut.close();
    	} catch (IOException e) {
    		Util.error(null, TAG, "saveImage failed: " + e.getMessage());
    	}
    }
    
    public static void recycleViewBackground(View view){
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            recycleBitmapDrawable(bd);
        }
    }
    public static void recycleImageView(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            imageView.setImageDrawable(null);
            recycleBitmapDrawable(bd);
        }
    }
    public static void recycleBitmapDrawable(BitmapDrawable bd) {
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            recycleBitmap(bitmap);
        }
        bd = null;
    }
    public static Bitmap getBitmapFromDrawable(Drawable d) {
        if (d instanceof BitmapDrawable) {
        	BitmapDrawable bd = (BitmapDrawable)d;
            return bd.getBitmap();
        }
        return null;
    }
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
	
    //-- 4. GUI: Layout related ----------------------------------------------------
    public static int getActualLayoutWidth(View view) {
    	return getActualLayoutSize(view, LinearLayout.HORIZONTAL);
   	}
    public static int getActualLayoutHeight(View view) {
    	return getActualLayoutSize(view, LinearLayout.VERTICAL);
   	}
    public static int getActualLayoutSize(View view, int orientation) {
    	assert(view!=null);
    	int nRet = (orientation==LinearLayout.HORIZONTAL) ? view.getWidth() : view.getHeight();
    	if (nRet==0
    		&& view.getLayoutParams()!=null){ //so we can get w/h from parent-views recursively. 
    		nRet = (orientation==LinearLayout.HORIZONTAL) ? view.getLayoutParams().width : view.getLayoutParams().height;
    		if(nRet<0){
    			if(nRet==LayoutParams.MATCH_PARENT){
    				View parent = (View)view.getParent();
    				if(parent==null)
        				nRet = 0;
    				else
    					nRet = Util.getActualLayoutSize(parent, orientation);
    			}
    			else if(nRet==LayoutParams.WRAP_CONTENT)
    				nRet = 0;
    			else
    				nRet = 0;
    		}
    	}
    	return nRet;
   	}
    
    public static float calcRawSize(View view, String unit_s, Number size, int orientation) {
    	assert(view!=null && view.getParent()!=null && view.getContext()!=null && unit_s!=null && size!=null);
    	int unit = unit2Type(unit_s);
    	switch(unit){
    	case TypedValue.COMPLEX_UNIT_FRACTION_PARENT:
    		if(size.intValue()==100){
    			return LayoutParams.MATCH_PARENT;
    		}
    		else{
        		View parent = (View)view.getParent();
        		//IMPROVE: it's still possible to be called with parentView==null..: DragAccepterImpl.onDrop(), new XxxFigure()->initUI(newFigure)->oldFigure.fireNodeReplaced()
        		if (parent == null){
        			Util.warn(null, TAG, "calcRawSize() called without parentView. Should be called again when parentView get linked: view="+view.getClass().getSimpleName());
    				return LayoutParams.MATCH_PARENT; 
        		}
        		else if (orientation==LinearLayout.HORIZONTAL)
    				return Util.getActualLayoutWidth(parent) * size.floatValue() / 100; 
    			else
    				return Util.getActualLayoutHeight(parent) * size.floatValue() / 100; 
    		}
    	case TypedValue.COMPLEX_UNIT_SP: //"em" comes here
    		float fontsize = (view instanceof TextView) ? ((TextView)view).getTextSize() 
    				: Constant.WorkArea.TEXT_FONTSIZE; //px
    		fontsize *= 1.8; //TEMP: title's height is incorrect if using default value.  
    		//return TypedValue.applyDimension(unit, size.floatValue()*fontsize,  
    		//		view.getContext().getResources().getDisplayMetrics());
    		return size.floatValue()*fontsize; //needn't density-based conversion
    	case TypedValue.COMPLEX_UNIT_PX:
    	default:
    		return size.floatValue();
    	}
    }

    public static int unit2Type(String unit_s) {
    	assert(unit_s != null);
    	int unit;
    	if(unit_s.equals("%"))
    		unit = TypedValue.COMPLEX_UNIT_FRACTION_PARENT;
    	else if(unit_s.equals("em"))
    		unit = TypedValue.COMPLEX_UNIT_SP;
    	else 
    		unit = TypedValue.COMPLEX_UNIT_PX;
    	return unit;
    }
	
    //-- Private and Protected Methods -------------------------
	private static int translateLogLevel(int level){  //generic.Config.LogLevel -> Android::Log
		switch (level){
		case LogLevel.TRACE:
    		return Log.INFO;
		case LogLevel.WARN:
    		return Log.WARN;
		case LogLevel.ERROR:
    		return Log.ERROR;
		default:
    		return Log.INFO;
		}
	}
	private static void logFacade(Context context, String tag, String message, int level)
    {    
    	if(!LogTag.showRulesAllowed(tag))
    		return;
		
    	logSystem(tag, message, level);
    	logFile(tag, message, level);
    	logGUI(context, tag, message, level);
    }
    
	
	//-- Event Handlers ----------------------------------------
}

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

package com.thinkalike.jfx.common;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.thinkalike.jfx.common.Constant;
import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.Config.LogLevel;
import com.thinkalike.generic.common.Constant.Layout;
import com.thinkalike.generic.common.TypedValue;

public class Util extends com.thinkalike.generic.common.Util {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = Util.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(Util.class);

	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private static Util _this;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public static Util getInstance(){
		if (_this == null)
			_this = new Util();
		return _this;
	}
    private Util() {
        super();
    }

	//-- Destructors -------------------------------------------
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
    
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
    //-- 1. Message Output / Logging (Android Implementation) related ------
	@SuppressWarnings("deprecation")
	public static void logFile(String tag, String message, int level) {
    	if (Config.LOGGING_USING_LOGFILE){
    		if (Config.LogLevel_UsingLogFile <= level){
    			int log4JLevel = Priority.INFO_INT;
	    		switch (level){
	    		case LogLevel.TRACE:
	    			log4JLevel = Priority.INFO_INT;
	    			break;
	    		case LogLevel.WARN:
	    			log4JLevel = Priority.WARN_INT;
	    			break;
	    		case LogLevel.ERROR:
	    			log4JLevel = Priority.ERROR_INT;
	    			break;
	    		default:
	    			break;
	    		}
    			//IMPROVE: directly upgrade to the new version of Log4J (2.x) 
	    		logger.log(Priority.toPriority(log4JLevel), String.format("%s, %s", tag, message));
    		}
    	}
	}
	
	
    //-- 2. File/Folder/Full Path related ----------------------------------------------------
	//-- 3. GUI: Image transaction related ----------------------------------------------------
    public static long calcImageSize(int width, int height, int bytePerPixcel){
    	return width * height * bytePerPixcel;
    }

    public static Dimension getImageSize(String imageUrl){
    	ImageInputStream in = null;
		try {
			URL url = new URL(imageUrl);
			in = ImageIO.createImageInputStream(url.openStream());
			if(in == null){
				Util.error(TAG, "getImageSize() failed: createImageInputStream failed");
				return null;
			}
    	    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
    	    if (readers.hasNext()) {
    	        ImageReader reader = readers.next();
    	        try {
    	            reader.setInput(in);
    	            return new Dimension(reader.getWidth(0), reader.getHeight(0));
    	        } finally {
    	            reader.dispose();
    	        }
    	    }
		} catch (IOException e) {
			Util.error(TAG, "getImageSize() failed: " + e.getMessage());
    	} finally {
    	    if (in != null)
				try {in.close();} catch (IOException e) {}
    	}
    	return null;
    }
    
    //IMPROVE: add param: 1.specify ARGB8888 or RGB_565 2.specify whether use mmap file or not
    public static Image decodeThumbFromFile(String imageUrl, int width_limit, int height_limit){
    	return decodeThumbFromFile(imageUrl, width_limit, height_limit, null);
    }
    public static Image decodeThumbFromFile(String imageUrl, int width_limit, int height_limit, Object preferredConfig){
		// BitmapFactory.Options options = getImageSampleInfo(imagePath, width_limit, height_limit);
		// if(options == null){
			// Util.error(null, TAG, "failed to get SampleInfo.");
			// return null;
		// }
		

    	//1.get image size without loading it
    	int width=0, height=0;
    	Dimension imageSize = getImageSize(imageUrl);
    	if(imageSize!=null){
    		width = imageSize.width;
    		height = imageSize.height;
    	}
		if(width>0 && height>0){
			width = Math.min(width, width_limit);
			height = Math.min(height, height_limit);
		}
		else{
			width = width_limit;
			height = height_limit;
		}

    	//2.load image in preferred size
    	// options.inPreferredConfig = preferredConfig;
		Image image = new Image(imageUrl, width, height, true, true, true);
		//image = BitmapFactory.decodeFile(imagePath, options);
		
		return image;
	}
    
    public static void saveImage(Image image, String imagePath) {
    	File file = new File(imagePath);
    	try {
    		file.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    	} catch (Exception e) {
    		Util.error(null, TAG, "saveImage failed: " + e.getMessage());
    	}
    }
	
    //-- 4. GUI: Layout related ----------------------------------------------------
    public static void setPrefSize(javafx.scene.Node node, TypedValue width, TypedValue height) {
    	assert(node instanceof Region || node instanceof Control);
    	TypedValue[] tvalues = new TypedValue[]{width, height};
    	final int IDX_WIDTH = 0, IDX_HEIGHT = 1, IDX_MAX = tvalues.length-1;
    	int[] units = new int[IDX_MAX+1];
    	Number[] values = new Number[IDX_MAX+1];
    	boolean[] isValid = new boolean[IDX_MAX+1];

    	//0.initialize
    	for (int idxDimension=IDX_WIDTH; idxDimension<=IDX_MAX; idxDimension++){
    		isValid[idxDimension] = (tvalues[idxDimension]!=null);
        	if (isValid[idxDimension]){
        		units[idxDimension] = com.thinkalike.generic.common.Util.unit2Type(tvalues[idxDimension].getUnit());
        		values[idxDimension] = (Number)tvalues[idxDimension].getValue(); //no check, check should be done in Parser.
        	}
    	}
    	
    	//1.set preferred size for dimensions: width, height
    	for (int idxDimension=IDX_WIDTH; idxDimension<=IDX_MAX; idxDimension++){
        	if (isValid[idxDimension]){
            	switch(units[idxDimension]){
            	case TypedValue.UNIT_FRACTION_PARENT:
            		Parent parentNode = node.getParent();
            		//IMPROVE: it's still possible to be called with parentView==null..: DragAccepterImpl.onDrop(), new XxxFigure()->initUI(newFigure)->oldFigure.fireNodeReplaced()
            		if (parentNode == null){
            			Util.warn(TAG, "setPrefSize() called without parentNode. Should be called again when parentNode get linked: view="+node.getClass().getSimpleName());
        				continue; //next dimension      
            		}else if (!(parentNode instanceof Region)){
            			Util.warn(TAG, "setPrefSize() called without valid parentNode(must be Region): parentNode="+parentNode.getClass().getSimpleName());
        				continue; //next dimension      
            		}else{
    					bindPrefSize(node, (Region)parentNode, (idxDimension==IDX_WIDTH)?Layout.Orientation.HORIZONTAL:Layout.Orientation.VERTICAL, 
    									values[idxDimension].intValue());
        			}
            		break;
            	case TypedValue.UNIT_SP: //"em" comes here
            		float fontsize = (float) ((node instanceof Labeled) ? ((Labeled)node).fontProperty().getValue().getSize() 
            				: Constant.TEXT_FONTSIZE_DEFAULT); //px
            		fontsize *= 1.8; //TEMP: title's height is incorrect if using default value.  
            		//return TypedValue.applyDimension(unit, size.floatValue()*fontsize,
            		//		view.getContext().getResources().getDisplayMetrics());
            		setPrefSize(node, (idxDimension==IDX_WIDTH)?Layout.Orientation.HORIZONTAL:Layout.Orientation.VERTICAL, 
            							values[idxDimension].floatValue()*fontsize); //needn't density-based conversion
            		break;
            	case TypedValue.UNIT_PX:
            	default:
            		setPrefSize(node, (idxDimension==IDX_WIDTH)?Layout.Orientation.HORIZONTAL:Layout.Orientation.VERTICAL, 
            							values[idxDimension].doubleValue());
            	}
        	}
    	}
    	
    	
    }
    public static void bindPrefSize(javafx.scene.Node node, Region parentNode, int orientation, int percentage) {
    	if (orientation==Layout.Orientation.HORIZONTAL){
    		if(node instanceof Region)
    			((Region)node).prefWidthProperty().bind(parentNode.prefWidthProperty().multiply(percentage/100));
    		else if(node instanceof Control)
				((Control)node).prefWidthProperty().bind(parentNode.prefWidthProperty().multiply(percentage/100));
    	}
    	else{
    		if(node instanceof Region)
    			((Region)node).prefHeightProperty().bind(parentNode.prefHeightProperty().multiply(percentage/100));
    		else if(node instanceof Control)
				((Control)node).prefHeightProperty().bind(parentNode.prefHeightProperty().multiply(percentage/100));
    	}
    }
    public static void setPrefSize(javafx.scene.Node node, int orientation, double size_px) {
    	if (size_px == 0)
    		return; //ignore
    	if (orientation==Layout.Orientation.HORIZONTAL){
    		if(node instanceof Region)
    			((Region)node).setPrefWidth(size_px);
    		else if(node instanceof Control)
				((Control)node).setPrefWidth(size_px);
    	}
    	else{
    		if(node instanceof Region)
    			((Region)node).setPrefHeight(size_px);
    		else if(node instanceof Control)
				((Control)node).setPrefHeight(size_px);
    	}
    }

    
    //-- Private and Protected Methods -------------------------

	//-- Event Handlers ----------------------------------------
}

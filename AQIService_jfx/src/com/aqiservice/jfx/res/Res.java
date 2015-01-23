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

package com.aqiservice.jfx.res;

import java.util.Locale;

import com.aqiservice.jfx.common.Constant;
import com.thinkalike.generic.common.Util;

/**
 * Resources helper class
 */
public class Res extends com.thinkalike.jfx.res.Res{
	//-- Constants and Enums -----------------------------------
	static {
		//LOCALE_SPECIFIC_PATTERN = new String[]{""};
	}
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static String getImageUrl(String url){
		//IMPROVE: for I18N in JavaFX, using ResourceBundle? What's Android's way to avoid unnecessary  
		// file existence check of locale-specific resource item? auto-generated resource item list?
		// According to android.content.res.Resources, the Maps between resIDs and Values can be easily referenced.
		// Before checking file existence using AssetsManager, its Resource bundle and resID will be checked firstly.
		String locale_append = ""; //language part of the locale
		if(isLocaleSpecific(url) && !Locale.getDefault().getLanguage().equals("en"))
			locale_append = "_" + Locale.getDefault().getLanguage();
		return Util.appendUrl(Constant.URL_RESOURCE_IMAGES + locale_append, url);
	}
	public static String getLayoutUrl(String url){
		return Util.appendUrl(Constant.URL_RESOURCE_LAYOUT, url);
	}
	public static String getValues(String bundle){
		return String.format("%s.%s.%s", Res.class.getPackage().getName(), "values", bundle);
	}
	
	//-- Private and Protected Methods -------------------------
	protected static boolean isLocaleSpecific(String resourceName){
		return com.thinkalike.jfx.res.Res.isLocaleSpecific(resourceName);
	}
	//-- Event Handlers ----------------------------------------
}

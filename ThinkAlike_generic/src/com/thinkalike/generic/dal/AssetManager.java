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

package com.thinkalike.generic.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.common.Config.Key;

/**
 * Handle asset files (generally unzip&copy) by using local utility class (AssetManagerLocal). 
 */
public class AssetManager {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
    /**
     * @param assetDirUrl: relative to asset root directory (e.g./com/xxx/xxx/assets/)
     * @param destDirPath: absolute destination directory path
     * @param overwrite
     * @param amLocal: platform-relative AssetManager instance employed to do assets copy
     */
    public static void copyAssets(String assetDirUrl, String destDirPath) {
    	Object uiContext = Loader.getInstance().getPlatform().getUIContext();
    	IAssetManagerLocal amLocal = Loader.getInstance().getPlatform().getFactory().createAssetManagerLocal(uiContext);
    	copyAssets(assetDirUrl, destDirPath, true, amLocal);
    }
    public static void copyAssets(String assetDirUrl, String destDirPath, boolean overwrite, IAssetManagerLocal amLocal) {
    	assert( assetDirUrl!=null && destDirPath!=null && destDirPath.length()>0 && amLocal!=null );
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			String[] assetFileNameList = amLocal.list(assetDirUrl);
			if (assetFileNameList==null || assetFileNameList.length==0){
				Util.warn(LogTag.AssetThread, "copyAssets: assetDirPath has no files: " + assetDirUrl);
				return;
			}
			File destDir = new File(destDirPath);
			if (!destDir.exists()) {
				if (!destDir.mkdirs()){
					Util.error(LogTag.AssetThread, "copyAssets: cannot create directory: " + destDirPath);
					return;
				}
			}
			for (String assetFileName : assetFileNameList) {
				String assetFileUrl = Util.appendUrl(assetDirUrl, assetFileName);
				String destFilePath = Util.appendPath(destDirPath, assetFileName);

				if (amLocal.isDirectory(assetFileUrl)) { 
					//In "DirectDescendants" list mode, we need to recurse further to explore more descendants. 
					if(amLocal.getListMode() == IAssetManagerLocal.EntryListMode.DirectDescendants)
						copyAssets(assetFileUrl, destFilePath, overwrite, amLocal);
					continue;
				}
				else{
					inputStream = amLocal.open(assetFileUrl);
					
					File destFile = new File(destDir, assetFileName);
					if(amLocal.getListMode() == IAssetManagerLocal.EntryListMode.AllEntries){
						//In "AllEntries" list mode, possibly assetFileName will contain sub-folder path, which need to be created
						if(!destFile.getParentFile().exists())
							destFile.getParentFile().mkdirs();
					}
					if (destFile.exists()){
						if (!overwrite){
							//IMPROVE: record which old files are kept without being overwritten
							continue;
						}
					}
					//IMPROVE: overwrite/delete after ensuring inputStream is opened successfully
					//IMPROVE: Exception should be caught for each File.
					outputStream = new FileOutputStream(destFile, false);
					byte[] buffer = new byte[(Integer)Loader.getInstance().getPlatform().getConfig(Key.BUFFER_READ_SIZE)];
					int len;
					while ((len = inputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, len);
					}
					if(amLocal.getListMode() != IAssetManagerLocal.EntryListMode.AllEntries)
						inputStream.close(); //In "AllEntries" list mode, the whole Zip file will be closed when we close an entry stream.
					outputStream.close();
				}
			}
		} catch (FileNotFoundException e) {
			Util.error(LogTag.AssetThread, "AssetManager copyAssets failed: " + e.getMessage());
		} catch (IOException e) {
			Util.error(LogTag.AssetThread, "AssetManager copyAssets failed: " + e.getMessage());
		} finally {
			if(inputStream!=null)
				try {inputStream.close();}	catch (IOException e) {}
			if(outputStream!=null)
				try {outputStream.close();} catch (IOException e) {}
		}
	}

    //-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

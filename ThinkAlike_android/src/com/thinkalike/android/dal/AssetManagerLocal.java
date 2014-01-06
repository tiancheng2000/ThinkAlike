package com.thinkalike.android.dal;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.dal.IAssetManagerLocal;
import com.thinkalike.generic.dal.IAssetManagerLocal.EntryListMode;

public class AssetManagerLocal implements IAssetManagerLocal {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private Context _context;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public AssetManagerLocal(Context context){
		_context = context;
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public EntryListMode getListMode(){
		return EntryListMode.DirectDescendants;
	}

	@Override
	public boolean isDirectory(String path) {
		//IMPROVE: File.isDirectory() cannot be used on Android asset files. 
		//          There's a solution which has performance cost: catch IOException thrown by amLocal.list()(50ms per call)
		String fileName = Util.getFileName(path);
		//NOTE: files under assets folder and have no extension, must be checked here (e.g."mimetype"). Otherwise it will be transacted as a "folder".
		return (!fileName.contains(".") && !fileName.equals("mimetype")); //raw simulation to File.isDirectory()
	}

	@Override
	public String[] list(String path) throws IOException {
//		try {
			return _context.getAssets().list(path);
//		} catch (IOException e) {
//			Util.error(LogTag.AssetThread, "AssetManagerLocal failed to list path:" + path);
//			throw e;
//		}
	}

	@Override
	public InputStream open(String filePath) throws IOException {
//		try {
			return _context.getAssets().open(filePath);
//		} catch (IOException e) {
//			Util.error(LogTag.AssetThread, "AssetManagerLocal failed to open file:" + filePath);
//			throw e;
//		}
	}

	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

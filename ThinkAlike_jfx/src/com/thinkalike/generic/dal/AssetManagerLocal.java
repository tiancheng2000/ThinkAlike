package com.thinkalike.generic.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;

public class AssetManagerLocal implements IAssetManagerLocal {
	//-- Constants and Enums -----------------------------------
	private static final String PROTOCOL_FILE = "file"; 
	private static final String PROTOCOL_JAR = "jar";
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private String _protocol;
	private String _rootUrl;
	//private String _jarFileUrl;
	private JarFile _jarFile;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public AssetManagerLocal(URL assetRoot){
		//_rootUrl = this.getClass().getClassLoader().getResource(baseUrl).toExternalForm();
		_protocol = assetRoot.getProtocol();
		String assetRootUrl = assetRoot.toExternalForm();
		Util.trace(LogTag.AssetThread, String.format("Assets root protocol=%s, url=%s", _protocol, assetRootUrl));
		if (_protocol.equals(PROTOCOL_FILE)){
			_rootUrl = assetRootUrl.substring(_protocol.length()+2); //get rid of protocol part, e.g."file"+":/"
		}
		else if (_protocol.equals(PROTOCOL_JAR)){
			//_jarFileUrl = _rootUrl.substring(0, _rootUrl.indexOf("!"));
			String jarFileUrl = assetRootUrl.substring(assetRootUrl.indexOf(":/")+2, assetRootUrl.indexOf("!"));
			_rootUrl = assetRootUrl.substring(assetRootUrl.indexOf("!")+1);
			if(_rootUrl.startsWith(Constant.URLPATH_SEPARATOR))
				_rootUrl = _rootUrl.substring(1);
			Util.trace(LogTag.AssetThread, String.format("jarFileUrl=%s", jarFileUrl));
			Util.trace(LogTag.AssetThread, String.format("_rootUrl=%s", _rootUrl));
			//URL jar = getClass().getClassLoader().getResource(jarFileUrl);
			//Util.trace(LogTag.AssetThread, String.format("jar=%s", (jar==null)?"null":""));
			//URLConnection urlCon;
			//try {
			//	urlCon = jar.openConnection();
			//	Util.trace(LogTag.AssetThread, String.format("urlCon=%s", (urlCon==null)?"null":urlCon.getClass().getSimpleName()));
			//	_jarFile = null;
			//	if (urlCon instanceof JarURLConnection){
			//		_jarFile = ((JarURLConnection) urlCon).getJarFile();
			//		Util.trace(LogTag.AssetThread, String.format("_jarFile=%s", (_jarFile==null)?"null":_jarFile.getClass().getSimpleName()));
			//	}
			//} catch (IOException e) {}
			_jarFile = null;
			try {
				//_jarFile = new JarFile(URLDecoder.decode(jarFileUrl, "UTF-8"));
				Util.trace(LogTag.AssetThread, "Before new JarFile()");
				_jarFile = new JarFile(jarFileUrl);
				Util.trace(LogTag.AssetThread, String.format("_jarFile=%s", (_jarFile==null)?"null":_jarFile.getClass().getSimpleName()));
			} catch (IOException e) {
				Util.error(LogTag.AssetThread, String.format("Exception occurred: %s", e.getMessage()));
			}
		}
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	@Override
	public EntryListMode getListMode(){
		if (_protocol.equals(PROTOCOL_FILE))
			return EntryListMode.DirectDescendants;
		else if (_protocol.equals(PROTOCOL_JAR))
			return EntryListMode.AllEntries;
		
		return EntryListMode.DirectDescendants; //IMPROVE: unsupported protocol handling
	}
	
	@Override
	public boolean isDirectory(String url) {
		if (_protocol.equals(PROTOCOL_FILE)){
			File file = new File(Util.appendUrl(_rootUrl, url));
			return file.isDirectory();
		}
		else if (_protocol.equals(PROTOCOL_JAR)){
			//throw new UnsupportedOperationException("Jar-style list mode needn't to implement isDirectory().");
			ZipEntry entry = _jarFile.getEntry(Util.appendUrl(_rootUrl, url));
			return (entry==null)? false : entry.isDirectory();
		}
		return false; //IMPROVE: unsupported protocol handling
	}

	@Override
	public String[] list(String url) throws IOException {
//		try {
			String assetUrl = Util.appendUrl(_rootUrl, url);
			Util.trace(LogTag.AssetThread, String.format("assetManLocal.list(), assetUrl=%s", assetUrl));
			if (_protocol.equals(PROTOCOL_FILE)){
				File file = new File(assetUrl);
				return file.list();
			}
			else if (_protocol.equals(PROTOCOL_JAR)){
				Set<String> result = new HashSet<String>();
				Enumeration<JarEntry> entries = _jarFile.entries();
				while (entries.hasMoreElements()) {
	                String entryName = entries.nextElement().getName();
	                if(entryName.startsWith(assetUrl)){
	                	entryName = entryName.substring(assetUrl.length());
	                	result.add(entryName);
						Util.trace(LogTag.AssetThread, String.format("assetManLocal.list(), entryName=%s", entryName));
	                }
	            } 
				return (result.size()==0) ? null : result.toArray(new String[result.size()]);
			}
			return null;
//		} catch (IOException e) {
//			Util.error(LogTag.AssetThread, "AssetManagerLocal failed to list path:" + path);
//			throw e;
//		}
	}

	@Override
	public InputStream open(String fileUrl) throws IOException {
//		try {
			String assetUrl = Util.appendUrl(_rootUrl, fileUrl);
			Util.trace(LogTag.AssetThread, String.format("assetManLocal.open(), assetUrl=%s", assetUrl));
			if (_protocol.equals(PROTOCOL_FILE)){
				File file = new File(assetUrl);
				return new FileInputStream(file);
			}
			else if (_protocol.equals(PROTOCOL_JAR)){
				InputStream is = null;
				//is = getClass().getClassLoader().getResourceAsStream(Util.appendUrl(_rootUrl, fileUrl));
				is = _jarFile.getInputStream(_jarFile.getEntry(assetUrl));
				return is;
			}
			return null;
//		} catch (IOException e) {
//			Util.error(LogTag.AssetThread, "AssetManagerLocal failed to open file:" + filePath);
//			throw e;
//		}
	}

	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

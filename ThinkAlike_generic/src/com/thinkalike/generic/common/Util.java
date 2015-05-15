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

package com.thinkalike.generic.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.thinkalike.generic.Loader;
import com.thinkalike.generic.common.Config.Key;
import com.thinkalike.generic.common.Config.LogLevel;
import com.thinkalike.generic.common.Constant.SortOrder;
import com.thinkalike.generic.common.Constant.SortType;

public class Util {
	//-- Constants and Enums -----------------------------------
	private final static String TAG = Util.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	private static class CustomizedFileFilter implements FileFilter {
		private boolean _includeSubDir;
		private String[] _fileExtList_lowerCase;
		public CustomizedFileFilter(boolean includeSubDir, String[] fileExtList_lowerCase){
			_includeSubDir = includeSubDir;
			_fileExtList_lowerCase = fileExtList_lowerCase;
		}
		@Override
		public boolean accept(File f) {
			
			if (f.isDirectory())
				return _includeSubDir;
			
			if (_fileExtList_lowerCase == null)
				return true;

			String fileExtLowerCase = Util.getFileExtension(f.getName().toLowerCase());
			for (String fileExtFilter : _fileExtList_lowerCase){
				if (fileExtFilter.equals(fileExtLowerCase)) {
					return true;
				}
			}
			return false;
		}
	}

	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------

	//-- Public and internal Methods ---------------------------
	public static String getString(int id){
		return Loader.getInstance().getPlatform().getString(id);
	}
	
	//-- 1. Message Output / Logging (invoke platform-related implementation) related ------
    public static void trace(String tag, String format, Object... args){
    	trace(tag, String.format(format, args));
    }
    public static void trace(String tag, String message)
    {
    	logCore(tag, message, LogLevel.TRACE);
    }
    public static void warn(String tag, String format, Object... args){
    	warn(tag, String.format(format, args));
    }
    public static void warn(String tag, String message)
    {
    	logCore(tag, message, LogLevel.WARN);
    }
    public static void error(String tag, String format, Object... args){
    	error(tag, String.format(format, args));
    }
    public static void error(String tag, String message)
    {
    	logCore(tag, message, LogLevel.ERROR);
    }
    
    public static void logSystem(String tag, String message, int level){
    	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_LOG)){
    		String strMsg = String.format("[%s] %s (%s) %s", LogLevel.toString(level), Util.getTimeStamp(), tag, message);
    		System.out.println(strMsg);
    	}
    }
    public static void logFile(String tag, String message, int level){
    	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_LOGFILE)){
    		//TODO: logFile for JRE platform
    		//String strMsg = String.format("[%s] %s (%s) %s", LogLevel.toString(level), Util.getTimeStamp(), tag, message);
    		//System.out.println(strMsg);
    	}
    }
	public static void logGUI(Object context, String tag, String message, int level) {
    	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_GUI)){
    		//TODO: logGUI for JRE platform
    		//String strMsg = String.format("[%s] %s (%s) %s", LogLevel.toString(level), Util.getTimeStamp(), tag, message);
    	}
	}

    //-- 2. File/Folder/Full Path related ----------------------------------------------------
    public static String appendPath(String strPathPrev, String... strPathPosts)
    {
    	if(!Constant.FILEPATH_SEPARATOR.equals(Constant.URLPATH_SEPARATOR)){
    		strPathPrev = strPathPrev.replace(Constant.URLPATH_SEPARATOR, Constant.FILEPATH_SEPARATOR);
        	for (int i=0; i<strPathPosts.length; i++){
        		strPathPosts[i] = strPathPosts[i].replace(Constant.URLPATH_SEPARATOR, Constant.FILEPATH_SEPARATOR);
        	}
    	}
    	return linkTokens(Constant.FILEPATH_SEPARATOR, strPathPrev, strPathPosts);
    }
    public static String appendUrl(String strUrlPrev, String... strUrlPosts)
    {
    	if(!Constant.FILEPATH_SEPARATOR.equals(Constant.URLPATH_SEPARATOR)){
    		strUrlPrev = strUrlPrev.replace(Constant.FILEPATH_SEPARATOR, Constant.URLPATH_SEPARATOR);
        	for (int i=0; i<strUrlPosts.length; i++){
        		strUrlPosts[i] = strUrlPosts[i].replace(Constant.FILEPATH_SEPARATOR, Constant.URLPATH_SEPARATOR);
        	}
    	}
    	return linkTokens(Constant.URLPATH_SEPARATOR, strUrlPrev, strUrlPosts);
    }
    public static String pathToUrl(String strPath)
    {
    	if(!Constant.FILEPATH_SEPARATOR.equals(Constant.URLPATH_SEPARATOR)){
    		return "file:"+strPath.replace(Constant.FILEPATH_SEPARATOR, Constant.URLPATH_SEPARATOR);
    	}
    	else
    		return "file:"+strPath; 
    }
    public static String namespaceToPath(String namespace)
    {
    	return namespace.replace(Constant.NAMESPACE_SEPARATOR_CHAR, Constant.URLPATH_SEPARATOR_CHAR);
    }
    
    public static String getAbsoluteUrl(String relativePath){
    	return pathToUrl(getAbsolutePath(relativePath));
    }
    public static String getAbsolutePath(String relativePath){ //Config.STORAGE_BASEPATH, defined in Application class.
    	return Util.appendPath((String)Loader.getInstance().getPlatform().getConfig(Key.STORAGE_BASEPATH), relativePath);
	}
    public static String getRelativePath(String absolutePath){
    	//IMPROVE: check if the beginning parts are same
    	return absolutePath.substring(((String)Loader.getInstance().getPlatform().getConfig(Key.STORAGE_BASEPATH)).length());
	}
    public static String getFileName(String filePath){
    	return filePath.substring(filePath.lastIndexOf(Constant.FILEPATH_SEPARATOR)+1);
	}
    public static String getFileExtension(String filePath){
    	return filePath.substring(filePath.lastIndexOf(".")+1); //not include "." mark
	}

    public static Date getFileCreateTime(String filePath){
		Date createDate = null;
		String creatDateString = "";
		//retrieved from http://bbs.csdn.net/topics/330254776
		Process ls_proc;
		try {
			ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir \"" + filePath + "\" /tc");
		    BufferedReader in = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
		    for (int i = 0; i < 5; i++) {
		        in.readLine();
		    }
		    StringTokenizer st = new StringTokenizer(in.readLine()); //6th line
		    in.close();
		    creatDateString = st.nextToken();
		} catch (IOException e) {
			Util.error(TAG, String.format("getFileCreateTime failed: %s", e.getMessage()));
		}
		try {
			createDate = DateFormat.getDateTimeInstance().parse(creatDateString);
		} catch (ParseException e) {
			Util.error(TAG, String.format("getFileCreateTime failed: %s", e.getMessage()));
		}
		return createDate;
    }
    
	/**
	 * @param dirPath: list files/sub-folders under this folder
	 * @param includeSubDir: should sub-folders being included in the result
	 * @param fileExtLowerCase: file extension filter, in lower case
	 * @param sortType
	 * @return
	 */
	public static File[] listSortedFiles(String dirPath, boolean includeSubDir, String fileExt_lowerCase, SortType sortType)	{
		return listSortedFiles(dirPath, includeSubDir, new String[]{fileExt_lowerCase}, sortType);
	}
	public static File[] listSortedFiles(String dirPath, boolean includeSubDir, String[] fileExtList_lowerCase, SortType sortType)	{
		//FD: dirPath -> File[] -> FileItem[] (-> FileItemListView, FileListDetailView)
		File[] result = null;
		File rootDir = new File(dirPath);
		if (rootDir.isDirectory()){
			result = rootDir.listFiles(new CustomizedFileFilter(includeSubDir, fileExtList_lowerCase));
			result = Util.sortFiles(result, sortType, Constant.SortOrder.Ascend); 
		}
		return result;
	}
    public static int fileCount(String fileDirPath, String fileNameInclude, String extensionName){
    	int count = 0;
    	File fileDir = new File(fileDirPath);
    	if (fileDir == null || !fileDir.isDirectory())
    		return 0;
    	
    	File[] files = fileDir.listFiles();
    	if(files == null || files.length == 0)
			return 0;
    	
    	String fileName;
    	for(int i = 0; i < files.length; i++) {
    		fileName = files[i].getName();
    		if (files[i].isFile()
    				&& fileName.indexOf(fileNameInclude) != -1  
    				&& Util.getFileExtension(fileName).equalsIgnoreCase(extensionName)){
    			count++;
    		}
        }
    	return count;
    }
    
    public static File[] sortFiles(File[] files, final SortType sortType, final SortOrder sortOrder){
		final int sortOrderFlag = (sortOrder == SortOrder.Ascend) ? 1 : -1;
    	Arrays.sort(files, new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				final File file1 = (File) obj1;
				final File file2 = (File) obj2;
				int compare = 0;

				switch (sortType) {
				case Name:
					compare = file1.getName().compareTo(file2.getName());
					if (compare < 0)
						return -1*sortOrderFlag; // list ahead
					else if (compare > 0)
						return 1*sortOrderFlag; // list behind
					else
						return 0;
				case Type:
					compare = Util.getFileExtension(file1.getName()).compareTo(Util.getFileExtension(file2.getName()));
					if (compare < 0)
						return -1*sortOrderFlag; // list ahead
					else if (compare > 0)
						return 1*sortOrderFlag; // list behind
					else
						return 0;
				case Modified:
				default:
					compare = Long.signum(file1.lastModified() - file2.lastModified());
					if (compare > 0) {
						return -1*sortOrderFlag; // list ahead
					} else if (compare < 0) {
						return 1*sortOrderFlag; // list behind
					} else {
						return 0;
					}
				}
			}
		});
    	return files;
    }
    
	public static void copyFilesTo(String srcDirPath, String destDirPath) {
		File srcDir = new File(srcDirPath);
		File destDir = new File(destDirPath);
		if (!destDir.exists()) {
			if (!destDir.mkdirs()){
				Util.error(TAG, "copyFilesTo: cannot create directory: " + destDirPath);
				return;
			}
		}
        if (!srcDir.isDirectory() || !destDir.isDirectory())
            return;
        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile())
				copyFileTo(srcFiles[i].getAbsolutePath(), appendPath(destDir.getPath(), srcFiles[i].getName()));
            else if (srcFiles[i].isDirectory())
                copyFilesTo(srcFiles[i].getAbsolutePath(), appendPath(destDir.getPath(), srcFiles[i].getName()));
        }
    }
	public static void copyFileTo(String srcFilePath, String destFilePath) {
		File srcFile = new File(srcFilePath);
        if (srcFile.isDirectory())
            return;
        try {
	        FileInputStream fis = new FileInputStream(srcFile);
	        copyFileTo(fis, destFilePath);
		} catch (IOException e) {
			Util.error(TAG, "copyFileTo: " + e.getMessage());
		}
    }
	public static void copyFileTo(FileInputStream fis, String destFilePath) {
		File destFile = new File(destFilePath);
        if (destFile.isDirectory())
            return;
        try {
	        FileOutputStream fos = new FileOutputStream(destFile);
	        int readLen = 0;
	        byte[] buffer = new byte[(Integer)Loader.getInstance().getPlatform().getConfig(Key.BUFFER_READ_SIZE)];
	        while ((readLen = fis.read(buffer)) != -1) {
	            fos.write(buffer, 0, readLen);
	        }
	        fis.close();
	        fos.flush();
	        fos.close();
		} catch (IOException e) {
			Util.error(TAG, "copyFileTo: " + e.getMessage());
		}
    }
	
	public static void deleteFile(String filePath){
		File file = new File(filePath);
		if(file.exists())
			file.delete();
	}
	public static void deleteDir(String dirPath) {
		File dir = new File(dirPath);
        if (dir == null || !dir.exists() || dir.isFile())
            return;
        
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
            	deleteDir(file.getAbsolutePath());
        }
        dir.delete();
    }  
	
    //IMPROVE: don't compress root folder
    public static void zipFiles(String srcFolderPath, String zipFilePath){
		File srcFolder = new File(srcFolderPath);
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
			zipFile(srcFolder, zipOut, "");
		} catch (FileNotFoundException e) {
			Util.error(TAG, "zipFiles: invalid zipFilePath = " + zipFilePath); 
		} finally {
			try {zipOut.close();}
			catch (IOException e) {}
		}
	}
	public static void upZipFile(String zipFilePath, String destFolderPath) {
		upZipFile(zipFilePath, destFolderPath, null);
	}
	public static void upZipFile(String zipFilePath, String destFolderPath, String appointFileName) {
        File destFolder = new File(destFolderPath);
        if (!destFolder.exists())
            destFolder.mkdir();
        if (!destFolder.isDirectory())
            return;

        ZipFile zipFile = null;
        InputStream in = null;
        OutputStream out = null;
		try {
			zipFile = new ZipFile(new File(zipFilePath));
	        for (Enumeration<?> entries = zipFile.entries(); entries.hasMoreElements();) {
	            ZipEntry entry = ((ZipEntry)entries.nextElement());
	            
	            in = null;
	            File destFile = null;
	            if(appointFileName == null || appointFileName.trim().length() == 0){
	            	in = zipFile.getInputStream(entry);
	                destFile = new File(appendPath(destFolderPath, entry.getName()));
	            }else{
	            	if (entry.getName().contains(appointFileName)) {
	            		//IMPROVE: is it necessary to do appointFileName check?
	            		in = zipFile.getInputStream(entry);
	                    destFile = new File(appendPath(destFolderPath, entry.getName()));
	            	}
	            }
	            if(in == null || destFile == null || destFile.isDirectory())
	            	continue;
	            
	            if (!destFile.exists()) {
	                File fileParentDir = destFile.getParentFile();
	                if (!fileParentDir.exists())
	                	//Skill: use File.mkdirs() to recursively build up ancestor folders if necessary
	                    fileParentDir.mkdirs();
	                destFile.createNewFile();
	            }
	            out = new FileOutputStream(destFile);
	            byte buffer[] = new byte[(Integer)Loader.getInstance().getPlatform().getConfig(Key.BUFFER_ZIP_SIZE)];
	            int realLength;
	            while ((realLength = in.read(buffer)) > 0) {
	                out.write(buffer, 0, realLength);
	            }
	            in.close();
	            out.close();
	        }
		} catch (IOException e) {
			Util.error(TAG, String.format("upZipFile failed: zipFilePath=%s, err=%s",zipFilePath,e.getMessage())); 
		} finally {
			try {if(in!=null)in.close();}
			catch (IOException e) {}
			try {if(out!=null)out.close();}
			catch (IOException e) {}
			try {if(zipFile!=null)zipFile.close();}
			catch (IOException e) {}
		}
        
    }

	
    //-- 9. Other Helpers ----------------------------------------------------
    public static Object getPropertyValue(Object classInstance, String propertyName){
    	Object propertyValue = null;
        //first char of propertyName should be uppercased
    	propertyName = firstCharUpperCase(propertyName);
    	try{
            Class<?> target = classInstance.getClass();
            Method getter = target.getMethod("get"+propertyName, (Class<?>[])null);
            propertyValue = getter.invoke(classInstance, (Object[])null);
            Util.trace(TAG, String.format("getPropertyValue(%s,%s)==>%s", classInstance.getClass().getSimpleName(), propertyName, propertyValue.toString()));
        } catch (Exception e){
        	//ignore
        }
        return propertyValue;
    }
    public static Object getFieldValue(Class<?> target, String fieldName){
    	Object fieldValue = null;
    	try{
            Field field = target.getField(fieldName);
            fieldValue = field.get(null); //for static field, param(object) will be ignored
            Util.trace(TAG, String.format("getFieldValue(%s,%s)==>%s", target.getSimpleName(), fieldName, fieldValue.toString()));
        } catch (Exception e){
        	//ignore
        }
        return fieldValue;
    }
    public static void setPropertyValue(Object classInstance, String propertyName, Object... propertyValues){
        //first char of propertyName should be uppercased
    	propertyName = firstCharUpperCase(propertyName);
        try{
            Class<?> target = classInstance.getClass();
            Class<?>[] propertyValueTypes = (propertyValues==null) ? null : new Class[propertyValues.length];
            for(int i=0; i<((propertyValues==null)?-1:propertyValues.length); i++){
            	if(propertyValues[i]!=null)
            		propertyValueTypes[i] = propertyValues[i].getClass();
            }
            Method setter = target.getMethod("set"+propertyName, propertyValueTypes);
            setter.invoke(classInstance, propertyValues);
            Util.trace(TAG, String.format("setPropertyValue(%s,%s,%s)", classInstance.getClass().getSimpleName(), propertyName, (propertyValues==null)?null:propertyValues[0].toString()));
        } catch (Exception e){
        	//ignore
        }
    }
    public static void setFieldValue(Class<?> target, String fieldName, Object fieldValue){
        try{
            Field field = target.getField(fieldName);
            field.set(null, fieldValue); //for static field, first param(object) will be ignored
            Util.trace(TAG, String.format("setFieldValue(%s,%s,%s)", target.getSimpleName(), fieldName, fieldValue.toString()));
        } catch (Exception e){
        	//ignore
        }
    }
    public static boolean hasPropertyValue(Object classInstance, String propertyName){
        //first char of propertyName should be uppercased
    	propertyName = firstCharUpperCase(propertyName);
        try{
            Class<?> target = classInstance.getClass();
            @SuppressWarnings("unused")
			Method getter = target.getMethod("get"+propertyName, (Class<?>[])null);
        } catch (NoSuchMethodException e){
        	return false;
        }
        return true;
    }
    public static boolean hasFieldValue(Class<?> target, String fieldName){
        try{
            @SuppressWarnings("unused")
            Field field = target.getField(fieldName);
        } catch (NoSuchFieldException e){
        	return false;
        }
        return true;
    }

    public static String linkTokens(String strSeparator, String strTokenFirst, String... strTokenFollowings)
    {
    	StringBuffer result = new StringBuffer(strTokenFirst);
    	int endsWithSeparator = strTokenFirst.endsWith(strSeparator) ? 1 : 0;
    	int startsWithSeparator = 0;
    	for (String strTokenFollowing : strTokenFollowings){
    		startsWithSeparator = strTokenFollowing.startsWith(strSeparator) ? 1 : 0;
    		int countSeparator = endsWithSeparator + startsWithSeparator;
            if (countSeparator == 0){
            	result.append(strSeparator);
            	result.append(strTokenFollowing);
            }
            else if (countSeparator == 1){
            	result.append(strTokenFollowing);
            }
            else if (countSeparator == 2){
            	result.setLength(result.length()-1); //truncate tail separator
            	result.append(strTokenFollowing);
            }
            endsWithSeparator = strTokenFollowing.endsWith(strSeparator) ? 1 : 0;
    	}
        return result.toString();
    }
    /** e.g. "1px" -> {"1","px"} */
    public static String[] splitNumberUnit(String value){
		String[] sRet = new String[2];
		int i = 0;
		for(i = 0; i<value.length(); i++){
			//NOTE: JDK version constraint: Android platform requires JDK6
			//if ( !(Character.isDigit(value.charAt(i)) || Character.compare(value.charAt(i), '.')==0) 
			//		|| (i==0 && Character.compare(value.charAt(i), '-')==0)) {
			if ( !(Character.isDigit(value.charAt(i)) || Character.toString(value.charAt(i)).equals(".")
					|| (i==0 && Character.toString(value.charAt(i)).equals("-"))) ) {
				break;
			}
		}
		sRet[0] = (i>0) ? value.substring(0, i) : "";
		sRet[1] = (i<value.length()) ? value.substring(i, value.length()) : "";
		return sRet;
	}

	public static String compressDisplayableText(String originText, int max_len_inChar, String omit_mark) {
		//IMPROVE: more accurate method to trim multi-byte characters (UTF-8 encoding)
    	if(originText.length() > max_len_inChar)
    		originText = originText.substring(0, max_len_inChar-1) + omit_mark;
    	return originText;
    }
	public static String firstCharUpperCase(String string){
		byte[] items = string.getBytes();
		if ((char)items[0] >= 'a'){
			items[0] = (byte)((char)items[0]-'a'+'A');
			return new String(items);
		}
		else
			return string;
	}
    
	public static String getTimeStamp(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	public static String formatDate(String sourceDate, String targetFormat){
		Date date;
		try {
			date = DateFormat.getDateTimeInstance().parse(sourceDate);
		} catch (ParseException e) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(targetFormat);
		return df.format(date);
	}
    
	public static boolean putKeyValuePairs(Map<Object,Object> map, Object[] keys, Object[] values){
		if(keys.length != values.length){
			Util.error(TAG, "Number of key items must equal to number of value items");
			return false;
		}
		for(int i=0; i<keys.length; i++){
			Object key = keys[i];
			Object value = values[i];
			map.put(key, value);
		}
		return true;
	}
	
    //-- Private and Protected Methods -------------------------
	//-- 1. Message Output / Logging (invoke platform-related implementation) related ------
    private static void logCore(String tag, String message, int level)
    {   
    	//NOTE: should also be checked at platform-relative log facade method.
    	if(!LogTag.showRulesAllowed(tag))
    		return;
    	
    	Loader loader = Loader.getInstance();
       	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_LOG)){
    		if(loader!=null)
    			loader.getPlatform().logSystem(tag, message, level);
    		else
    			logSystem(tag, message, level);
    	}
       	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_LOGFILE)){
    		if(loader!=null)
    			loader.getPlatform().logFile(tag, message, level);
    		else
    			logFile(tag, message, level);
    	}
       	if ((Boolean)Loader.getInstance().getPlatform().getConfig(Key.LOGGING_USING_GUI)){
    		if(loader!=null)
    			loader.getPlatform().logGUI(tag, message, level);
    		else
    			logGUI(null, tag, message, level);
    	}
    }
    //-- 2. File/Folder/Full Path related ----------------------------------------------------
    private static void zipFile(File srcFile, ZipOutputStream zipOut, String rootPath){
		rootPath = rootPath.trim().length() == 0 ? srcFile.getName() : appendPath(rootPath, srcFile.getName());
        if (srcFile.isDirectory()){
            File[] fileList = srcFile.listFiles();
            for(File file : fileList)
                zipFile(file, zipOut, rootPath);
        } else {
            byte buffer[] = new byte[(Integer)Loader.getInstance().getPlatform().getConfig(Key.BUFFER_ZIP_SIZE)];
            BufferedInputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(srcFile), (Integer)Loader.getInstance().getPlatform().getConfig(Key.BUFFER_ZIP_SIZE));
				zipOut.putNextEntry(new ZipEntry(rootPath));
	            int realLength;
	            while ((realLength = inputStream.read(buffer)) != -1) {
	                zipOut.write(buffer, 0, realLength);
	            }
	            zipOut.flush();
	            zipOut.closeEntry();
			} catch (FileNotFoundException e) {
				Util.error(TAG, String.format("zipFile failed: srcFile=%s, %s", srcFile, e.getMessage()));
			} catch (IOException e) {
				Util.error(TAG, String.format("zipFile failed: srcFile=%s, %s", srcFile, e.getMessage()));
			} finally {
				try {inputStream.close();}
				catch (IOException e) {}
			}
        }
    }

	//-- 3. GUI: Image transaction related ----------------------------------------------------

    //-- 4. GUI: Layout related ----------------------------------------------------
    public static int unit2Type(String unit_s) {
    	assert(unit_s != null);
    	int unit;
    	if(unit_s.equals("%"))
    		unit = TypedValue.UNIT_FRACTION_PARENT;
    	else if(unit_s.equals("em"))
    		unit = TypedValue.UNIT_SP;
    	else 
    		unit = TypedValue.UNIT_PX;
    	return unit;
    }

    //-- Event Handlers ----------------------------------------
}

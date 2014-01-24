package com.thinkalike.generic.dal;

import java.io.IOException;
import java.io.InputStream;

public interface IAssetManagerLocal {
	//For Jar-style assets, generally use "AllEntries". For File-style or Android assets, use "DirectDescendants"    
	public enum EntryListMode {
		DirectDescendants, AllDescendants, AllEntries
	}
	public EntryListMode getListMode();
	public boolean isDirectory(String url);
	public String[] list(String url) throws IOException;
	public InputStream open(String fileUrl) throws IOException;
}

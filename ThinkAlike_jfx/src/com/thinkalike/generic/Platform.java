package com.thinkalike.generic;

import com.thinkalike.generic.domain.Factory;

public interface Platform {

	public String getString(int id); //id shall be translated to platform-dependent id
	public Object getConstant(String key); 
	public Object getConfig(String key);

	public Object getUIContext();
	//public void setUIContext(Object context);
	public Factory getFactory(); //generally only used to create View/Controls which require fine-grained control 
	
	public void logSystem(String tag, String message, int level);
	public void logFile(String tag, String message, int level);
	public void logGUI(String tag, String message, int level);
}

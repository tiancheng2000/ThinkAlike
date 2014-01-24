package com.thinkalike.generic.common;

public class LogTag {

	//-- Constants and Enums -----------------------------------
   	private static final int MAX_LENGTH = 50; //generic. Different platforms may have different length limits.

	//NOTE: --- Platform-Specific::Android ---
	//      Every LogTag can have its own output flag(LogLevel) which can be turn on/off 
	//      by modifying /data/local.prop (see Log.isLoggable() for detail)
	//FD: (General UseCase) 
	//1. Trace potential conflicts: e.g. Critical Section Enter/Exit, Resource Lock/Unlock, Multi-Thread, EventHandler Multi-Access(Async Message) 
	public static final String UIThread = "UIThread";
	public static final String ResourceThread = "ResourceThread";
	public static final String AssetThread = "AssetThread";
	public static final String NetworkThread = "NetworkThread";
	public static final String UIControlEvent = "UIControlEvent"; //Message Queue
	public static final String SystemEvent = "SystemEvent";
	public static final String LifeCycleManagement = "LifeCycleManagement"; //Android Activity LifeCycle
	public static final String CriticalSection = "CriticalSection";
	public static final String ResourceLock = "ResourceLock";
	public static final String MemoryManagement = "MemoryManagement"; //Performance analyse, OOM problem etc.
	public static final String ViewModel = "ViewModel";

	//2. Trace Transaction/Functional sequence: e.g. "Encryption", "Decryption", "SendMail"  
	public static final String LoadProfile = "LoadProfile";
	public static final String SaveProfile = "SaveProfile";

	//3. Trace Method Calling flow: directly use Method Name, and "Enter"/"Exit"
	//doDraw() etc.

	//4. Specify the ClassName so as to locate the message origin: directly use TAG defined in the Class
	//MainActivity.TAG etc.

   	private static enum ShowMode {Exclusion, Inclusion};
   	private static final ShowMode showMode = ShowMode.Exclusion; //Inclusion
	//used by showRulesAllowed() w. showMode
   	private static final String[] exclusionList = new String[]{"GestureDetector", "GCImpl::PageBaseView", MemoryManagement, AssetThread}; 
   								//UIControlEvent: "GestureDetector", "GCImpl::PageBaseView"
   	private static final String[] inclusionList = new String[]{};
   	
   	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------

   	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//0. Tag related filter and methods
   	public static String canonicalize(String tag){
   		return canonicalize(tag, MAX_LENGTH);
   	}
   	public static String canonicalize(String tag, int max_length){
   		if (tag.length() > max_length)
   			return tag.substring(0, max_length);
   		return tag;
   	}
   	
   	public static boolean showRulesAllowed(String tag){
   		assert(tag!=null);
   		if(showMode == ShowMode.Exclusion){
   	   		for (String tagInList : exclusionList){
   	   			if (tag.equals(tagInList))
   	   				return false;
   	   		}
   	   		return true;
   		}
   		else if(showMode == ShowMode.Inclusion){
   	   		for (String tagInList : inclusionList){
   	   			if (tag.equals(tagInList))
   	   				return true;
   	   		}
   	   		return false;
   		}
	   	return false;
   	}

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

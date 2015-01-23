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

import java.util.ArrayList;

public class LogTag {

	//-- Constants and Enums -----------------------------------
   	private static final int MAX_LENGTH = 50; //generic. Different platforms may have different length limits.

	//NOTE: --- Platform-Specific::Android ---
	//      Every LogTag can have its own output flag(LogLevel) which can be turn on/off 
	//      by modifying /data/local.prop (see Log.isLoggable() for detail)
	//FD: (General UseCase) 
	//1. Trace potential conflicts: e.g. Critical Section Enter/Exit, Resource Lock/Unlock, Multi-Thread, EventHandler Multi-Access(Async Message) 
	public static final String GenericThread = "GenericThread";
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

	public static enum ShowMode {Exclusion, Inclusion};
   	private static ShowMode showMode = ShowMode.Exclusion; //Inclusion
	//used by showRulesAllowed() w. showMode
   	private static final ArrayList<String> exclusionList = new ArrayList<String>();
   	static {
   		exclusionList.add("GestureDetector");
   		exclusionList.add(MemoryManagement);
   		exclusionList.add(AssetThread);
   	}
   	private static final ArrayList<String> inclusionList = new ArrayList<String>();
   	
   	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
   	//-- Properties --------------------------------------------
   	public static void setShowMode(ShowMode showMode_){showMode = showMode_;}
   	public static ShowMode getShowMode(){return showMode;}
   	public static String listExclusionItems(){return exclusionList.toString();}
   	public static void addExclusionItem(String tag){exclusionList.add(tag);}
   	public static void removeExclusionItem(String tag){exclusionList.remove(tag);}
   	public static String listInclusionItems(){return inclusionList.toString();}
   	public static void addInclusionItem(String tag){inclusionList.add(tag);}
   	public static void removeInclusionItem(String tag){inclusionList.remove(tag);}
   	
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

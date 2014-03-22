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

package com.thinkalike.generic.event;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import com.thinkalike.generic.common.LogTag;
import com.thinkalike.generic.common.Util;


//IMPROVE: package-scope class, not public. but Java doesn't have package-level Class accessibility
//          maybe move it into ViewModelBase class.
public class PropertyChangeListenerAdapter implements java.beans.PropertyChangeListener{
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	//NOTE: do not use WeakReference. so listeners must unregister themselves on finishing their life cycle.
	private WeakReference<PropertyChangeListener> _listenerClientRef;
	private static Map<PropertyChangeListener, PropertyChangeListenerAdapter> _map;
	
	//-- Properties --------------------------	
	//-- Constructors --------------------------
	private PropertyChangeListenerAdapter(PropertyChangeListener listenerClient){
		_listenerClientRef = new WeakReference<PropertyChangeListener>(listenerClient);
	}

	//-- Destructors --------------------------	
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
	public static PropertyChangeListenerAdapter getInstance(PropertyChangeListener listenerClient){
		if (listenerClient == null)
			return null;

		if(_map == null)
			_map = new WeakHashMap<PropertyChangeListener, PropertyChangeListenerAdapter>();
		
		PropertyChangeListenerAdapter instance = _map.get(listenerClient);
		if(instance != null)
			return instance;

		instance = new PropertyChangeListenerAdapter(listenerClient);
		_map.put(listenerClient, instance);
		return instance;
	}

	//NOTE: Use WeakReference to manage adapter's listenerClient. As long as listenerClient is alive, its adapter(and the mapping relationship) should be alive.
//	public static PropertyChangeListenerAdapter removeInstance(PropertyChangeListener listenerClient){
//		if (listenerClient == null || _map == null)
//			return null;
//		
//		//IMPROVE: java.beans.PropertyChangeSupport support multiple subscription. We should simulate the implementation of java.beans.ChangeListenerMap.
//		return _map.remove(listenerClient);
//	}
	
	//-- Private and Protected Methods --------------------------
	//-- Event Handlers --------------------------
	@Override
	public void propertyChange(java.beans.PropertyChangeEvent event) {
		Util.trace(LogTag.ViewModel, String.format("(Low Level)PropertyChangeListenerAdapter.propertyChange() get called. name=%s, listenerClient=%s", event.getPropertyName(), (_listenerClientRef.get()==null)?"null":_listenerClientRef.get().getClass().getSimpleName()));
		if(_listenerClientRef.get() != null)
			_listenerClientRef.get().onPropertyChanged(new PropertyChangeEvent(event));  //adapter
	}

}

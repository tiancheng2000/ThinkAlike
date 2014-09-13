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

package com.thinkalike.generic;

import com.thinkalike.generic.common.Config.Key;
import com.thinkalike.generic.domain.Factory;

public interface Platform {

	public String getString(int id); //id shall be translated to platform-dependent id
	public Object getConstant(String key); 
	public Object getConfig(String key);
	public Object getConfig(Key key);

	public Object getUIContext();
	//public void setUIContext(Object context);
	public Factory getFactory(); //generally only used to create Views/Controls which require fine-grained control 
	
	public void logSystem(String tag, String message, int level);
	public void logFile(String tag, String message, int level);
	public void logGUI(String tag, String message, int level);
}

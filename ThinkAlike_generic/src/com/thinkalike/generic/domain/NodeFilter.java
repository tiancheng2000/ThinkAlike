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

package com.thinkalike.generic.domain;


/**
 * TODO: Filter class for filtering Node list.  
 */
public class NodeFilter {

	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public NodeFilter(){
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public boolean accept(Node node){
		return false;
	}

	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
	
}
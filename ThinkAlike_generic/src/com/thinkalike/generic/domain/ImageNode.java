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

public class ImageNode implements Node, IReadOnlyProvider {

	//-- Constants and Enums -----------------------------------
	public static final String TAG = ImageNode.class.getSimpleName();

	//-- Inner Classes and Structures --------------------------
	public class RO implements INodeRO{  //forward pattern
		public String getRelativePath() {return ImageNode.this.getRelativePath();}
	}
	
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private final RO _readonly = new RO(); 
	private String _relativePath; //relative to Config.STORAGE_BASEPATH
	
	//-- Properties --------------------------------------------
	public INodeRO getIReadOnlyEntrace() {return _readonly;}
	public String getRelativePath() {return _relativePath;}

	//-- Constructors ------------------------------------------
	public ImageNode(String relativePath) {
		_relativePath = relativePath;
	}

	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
	
}

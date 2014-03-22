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

package com.thinkalike.generic.dal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.thinkalike.generic.common.Config;
import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.common.Constant.SortType;
import com.thinkalike.generic.common.Util;
import com.thinkalike.generic.domain.ImageNode;
import com.thinkalike.generic.domain.NodeType;

public class NodeLoader {
	//-- Constants and Enums -----------------------------------
	// NodeType-AssetsPath Map
	private final static Map<NodeType, String> NodeType_AssetsPath_Map = new HashMap<NodeType, String>();
	private final static NodeType[] NodeTypeList = new NodeType[]{NodeType.TypeA, NodeType.TypeB, NodeType.TypeC, NodeType.TypeD, NodeType.TypeE, NodeType.TypeF, NodeType.TypeG, NodeType.TypeH, NodeType.TypeI, NodeType.TypeJ, NodeType.TypeK }; 
	private final static String[] AssetsPathList = new String[]{Config.PATH_TYPE_A, Config.PATH_TYPE_B, Config.PATH_TYPE_C, Config.PATH_TYPE_D, Config.PATH_TYPE_E, Config.PATH_TYPE_F, Config.PATH_TYPE_G, Config.PATH_TYPE_H, Config.PATH_TYPE_I, Config.PATH_TYPE_J, Config.PATH_TYPE_K};
	static{
		assert(NodeTypeList.length == AssetsPathList.length);
		for(int i=0; i<NodeTypeList.length; i++){
			NodeType nodeType = NodeTypeList[i];
			String assetsPath = AssetsPathList[i];
			NodeType_AssetsPath_Map.put(nodeType, assetsPath);
		}
	}
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	public static ImageNode[] loadImageNodes(NodeType nodeType){
		ImageNode[] result = new ImageNode[0];
		
		String relativeFolderPath = (String)NodeType_AssetsPath_Map.get(nodeType);
		File[] files = Util.listSortedFiles(Util.getAbsolutePath(relativeFolderPath), 
						false, Constant.EXTENSIONS_IMAGE, SortType.Modified);
		if (null == files)
			return result;
		int fileNum = files.length;
		result = new ImageNode[fileNum];
		for (int i = 0;i < fileNum;i++) {
			result[i] = new ImageNode(Util.appendPath(relativeFolderPath, files[i].getName()));
		}
		return result;
	}
	
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

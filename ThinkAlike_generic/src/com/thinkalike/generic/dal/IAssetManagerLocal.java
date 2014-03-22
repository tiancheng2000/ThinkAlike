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

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

package com.thinkalike.generic.viewmodel.control;

public interface INodeView {

	/**
	 * apply UI data to Native view.<br>
	 * 
	 * @param uiData UI data to be applied to Native view instance.
	 */
	void update(UINode uiData);

	//IMPROVE: [Style] UI data Attributes + Style Rules => Presentation
	//void updateStyle(Style style);
	
	/**
	 * View may solve platform-specific operations here, on parent view having changed.<br>
	 * 
	 * @param newParentView new parent view. NOT necessarily to be an IParentNodeView instance.
	 */
	//void onParentViewChanged(Object newParentView);
}

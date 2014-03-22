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

public interface IParentNodeView extends INodeView{

	/**
	 * add a Native view as a child view.<br>
	 * 
	 * @param view platform-related view, e.g.android::TextView.
	 * @param index index of a child view in child view list, after which the new child view should be appended.
	 */
	void addView(Object view);
	void addView(Object view, int index);
	
	/**
	 * remove a Native view from child views.<br>
	 * 
	 * @param view platform-related view, e.g.android::TextView.
	 */
	void removeView(Object view);
	
	/**
	 * remove a Native view from child views.<br>
	 * 
	 * @param oldView child view to be replaced.
	 * @param newView child view which will replace the old one.
	 */
	void replaceView(Object oldView, Object newView);
}

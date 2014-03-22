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

package com.thinkalike.generic.gesture;

public interface IDragAccepter{

	public interface OnDragListener {
		//may degenerate to private methods in real implementation, called by default system event handler(in constructor)
		boolean onDragStarted(Object view, Object event);
		boolean onDragEntered(Object view, Object event);
		boolean onDrop(Object view, Object event);
		boolean onDragExited(Object view, Object event);
		boolean onDragEnded(Object view, Object event);
	}
	
	void setHandlableDragObjectLabels(String[] labels);
	
}

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

package com.thinkalike.jfx.view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

public class DraggableWindowDelegate {
	//-- Constants and Enums -----------------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events ----------------------------------
	//-- Instance and Shared Fields ----------------------------
	private Window _clientWindow;
	private Node _draggableNode;
	private double _dragStartX, _dragStartY;
	
	//-- Properties --------------------------------------------
	//-- Constructors ------------------------------------------
	public DraggableWindowDelegate(Window clientWindow, Node draggableNode){
		_clientWindow = clientWindow;
		_draggableNode = draggableNode;
		_draggableNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	_dragStartX = event.getScreenX() - _clientWindow.getX();
            	_dragStartY = event.getScreenY() - _clientWindow.getY();
            }
        });
		_draggableNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	_clientWindow.setX(event.getScreenX() - _dragStartX);
            	_clientWindow.setY(event.getScreenY() - _dragStartY);
            }
        });
	}
	
	//-- Destructors -------------------------------------------
	//-- Base Class Overrides ----------------------------------
	//-- Public and internal Methods ---------------------------
	//-- Private and Protected Methods -------------------------
	//-- Event Handlers ----------------------------------------
}

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

/**
 * For compatibility, this class keeps independent name-space and wraps
 * java.beans.PropertyChangeEvent in background.
 * Document copied from JDK document:
 * A "PropertyChange" event gets delivered whenever a bean changes a "bound"
 * or "constrained" property.  A PropertyChangeEvent object is sent as an
 * argument to the PropertyChangeListener and VetoableChangeListener methods.
 * <P>
 * Null values may be provided for the old and the new values if their
 * true values are not known.
 * <P>
 * An event source may send a null object as the name to indicate that an
 * arbitrary set of if its properties have changed.  In this case the
 * old and new values should also be null.
 */

public class PropertyChangeEvent implements java.io.Serializable {
	//-- Constants and Enums --------------------------
	private static final long serialVersionUID = 1408761280837761110L;
	
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	private java.beans.PropertyChangeEvent _delegate;                                                           
	
	//-- Properties --------------------------	
	//-- Constructors --------------------------	
	/**
     * Constructs a new <code>PropertyChangeEvent</code>.
     *
     * @param source  The bean that fired the event.
     * @param propertyName  The programmatic name of the property
     *          that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     */
    public PropertyChangeEvent(Object source, String propertyName,
                                     Object oldValue, Object newValue) {
    	_delegate = new java.beans.PropertyChangeEvent(source, propertyName, oldValue, newValue);  //forward
    }
    PropertyChangeEvent(java.beans.PropertyChangeEvent event) {
    	_delegate = event;  //wrapping
    }

	//-- Destructors --------------------------	
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------
    /**
     * Gets the programmatic name of the property that was changed.
     *
     * @return  The programmatic name of the property that was changed.
     *          May be null if multiple properties have changed.
     */
    public String getPropertyName() {
        return _delegate.getPropertyName();  //forward
    }

    /**
     * Gets the new value for the property, expressed as an Object.
     *
     * @return  The new value for the property, expressed as an Object.
     *          May be null if multiple properties have changed.
     */
    public Object getNewValue() {
        return _delegate.getNewValue();  //forward
    }

    /**
     * Gets the old value for the property, expressed as an Object.
     *
     * @return  The old value for the property, expressed as an Object.
     *          May be null if multiple properties have changed.
     */
    public Object getOldValue() {
        return _delegate.getOldValue();  //forward
    }
    
    /**
     * Sets the propagationId object for the event.
     *
     * @param propagationId  The propagationId object for the event.
     */
    public void setPropagationId(Object propagationId) {
    	_delegate.setPropagationId(propagationId);  //forward
    }

    /**
     * The "propagationId" field is reserved for future use.  In Beans 1.0
     * the sole requirement is that if a listener catches a PropertyChangeEvent
     * and then fires a PropertyChangeEvent of its own, then it should
     * make sure that it propagates the propagationId field from its
     * incoming event to its outgoing event.
     *
     * @return the propagationId object associated with a bound/constrained
     *          property update.
     */
    public Object getPropagationId() {
        return _delegate.getPropagationId();  //forward
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    public String toString() {
        return _delegate.toString();  //forward　　　　　　　　　　　　　　　　　　　　　                         
    }

    //-- Private and Protected Methods --------------------------	
	//-- Event Handlers --------------------------

}

package com.thinkalike.generic.event;

/**
 * For compatibility, this class keeps independent name-space and "adapts"
 * java.beans.PropertyChangeListener.
 * Document copied from JDK document:
 * A "PropertyChange" event gets fired whenever a bean changes a "bound"
 * property.  You can register a PropertyChangeListener with a source
 * bean so as to be notified of any bound property updates.
 */

public interface PropertyChangeListener extends java.util.EventListener {

    /**
     * This method gets called when a bound property is changed.
     * @param event A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */

    void onPropertyChanged(PropertyChangeEvent event);

}

package com.thinkalike.generic.viewmodel;

import java.beans.PropertyChangeSupport;

import com.thinkalike.generic.common.Constant;
import com.thinkalike.generic.event.PropertyChangeListener;
import com.thinkalike.generic.event.PropertyChangeListenerAdapter;

public class ViewModelBase {
	//-- Constants and Enums --------------------------
	//-- Inner Classes and Structures --------------------------
	//-- Delegates and Events --------------------------	
	//-- Instance and Shared Fields --------------------------
	//kw: PropertyChangeSupport is thread safe. No locking is necessary when subscribing or unsubscribing listeners, or when publishing events.
	//    Callers should be careful when publishing events because listeners may not be thread safe.
	/**
	 * manage PropertyChangeListenerAdapters, which is an adapter between java.beans.PropertyChangeListener and our own PropertyChangeListener.
	 */
	private final PropertyChangeSupport _pcs = new PropertyChangeSupport(this);
	
	//-- Properties --------------------------
	private boolean _isBusy = false;
	public boolean isBusy() {return _isBusy;}
	public void setBusy(boolean value) {
		if (_isBusy != value) {
			boolean oldValue = _isBusy;
			_isBusy = value;
			_pcs.firePropertyChange(Constant.PropertyName.IsBusy, oldValue, value);
        }
	}
	
	//IMPROVE: IsValid + Errors
	//private boolean _isValid = true;
	//private final List<String> _errors = new ArrayList<String>();
	
	//-- Constructors --------------------------	
	//-- Destructors --------------------------	
	//-- Base Class Overrides --------------------------
	//-- Public and internal Methods --------------------------	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		_pcs.addPropertyChangeListener(PropertyChangeListenerAdapter.getInstance(l));
    }
	public void removePropertyChangeListener(PropertyChangeListener l) {
		_pcs.removePropertyChangeListener(PropertyChangeListenerAdapter.getInstance(l));
		//PropertyChangeListenerAdapter.removeInstance(l);  //use WeakReference to manage adapter's listenerClient
    }

	//TODO: IsBusy + Waiting ProgressBar: 借用PropertyChangeSupport(Constant.PropertyName.IsBusy) + 继承PropertyChangeListener 来实现
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
		_pcs.addPropertyChangeListener(propertyName, PropertyChangeListenerAdapter.getInstance(l));
    }
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
		_pcs.removePropertyChangeListener(propertyName, PropertyChangeListenerAdapter.getInstance(l));
		//PropertyChangeListenerAdapter.removeInstance(l);  //use WeakReference to manage adapter's listenerClient
    }
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		_pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

	//-- Private and Protected Methods --------------------------	
	//-- Event Handlers --------------------------	

}

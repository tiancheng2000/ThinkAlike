package com.thinkalike.generic.common.util;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A simple list which holds only weak references to the original objects.
 * @author  Martin Entlicher@Sun Microsystems (an initial non-template version),<BR>
 *           Tiancheng Hu (the template version, and implemented set(), remove(), indexOf() in addition)
 */
public class WeakList<E> extends AbstractList<E> {
    
    private ArrayList<WeakReference<E>> items;

    /** Creates new WeakList */
    public WeakList() {
        items = new ArrayList<WeakReference<E>>();
    }
    
    public WeakList(Collection<E> c) {
        items = new ArrayList<WeakReference<E>>();
        addAll(0, c);
    }
    
    public void add(int index, E element) {
        items.add(index, new WeakReference<E>(element));
    }
    
    public Iterator<E> iterator() {
        return new WeakListIterator();
    }
    
    public int size() {
        removeReleased();
        return items.size();
    }    
    
    public E get(int index) {
        return ((WeakReference<E>) items.get(index)).get();
    }
    
    public E set(int index, E element) {
    	WeakReference<E> refPrev = items.set(index, new WeakReference<E>(element));
        return (refPrev==null)? null : refPrev.get();
    }

    public E remove(int index) {
    	WeakReference<E> ref = items.remove(index);
        return (ref==null)? null : ref.get();
    }

    //NOTE: this implementation didn't use ListIterator (ref:AbstractList.java)
    public int indexOf(Object o) {
    	for (int i=0; i<items.size(); i++){
			WeakReference<E> ref = (WeakReference<E>) items.get(i);
    		if (o==null) {
    			if (ref==null)
    				return i;
    		} else {
    			if (o.equals(ref.get()))
    				return i;
    		}
    	}
        return -1;
    }
    public int lastIndexOf(Object o) {
    	for (int i=items.size()-1; i>=0; i--){
			WeakReference<E> ref = (WeakReference<E>) items.get(i);
    		if (o==null) {
    			if (ref==null)
    				return i;
    		} else {
    			if (o.equals(ref.get()))
    				return i;
    		}
    	}
        return -1;
    }
    
    private void removeReleased() {
        for (Iterator<WeakReference<E>> it = items.iterator(); it.hasNext(); ) {
            WeakReference<E> ref = (WeakReference<E>) it.next();
            if (ref.get() == null) items.remove(ref);
        }
    }
    
    private class WeakListIterator implements Iterator<E> {
        
        private int n;
        private int i;
        
        public WeakListIterator() {
            n = size();
            i = 0;
        }
        
        public boolean hasNext() {
            return i < n;
        }
        
        public E next() {
            return get(i++);
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

}

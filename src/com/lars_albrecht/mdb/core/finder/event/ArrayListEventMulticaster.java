/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.event;

import java.util.Vector;

/**
 * @author albrela
 * 
 */
public class ArrayListEventMulticaster implements ArrayListListener {

	protected Vector<ArrayListListener>	listener	= new Vector<ArrayListListener>();

	public void add(final ArrayListListener a) {
		if (!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void arrayListenerAdd(final ArrayListEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).arrayListenerAdd(e);
		}
	}

	@Override
	public void arrayListenerClear(final ArrayListEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).arrayListenerClear(e);
		}
	}

	@Override
	public void arrayListenerRemove(final ArrayListEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).arrayListenerRemove(e);
		}
	}

	public void remove(final ArrayListListener l) {
		this.listener.remove(l);
	}

}

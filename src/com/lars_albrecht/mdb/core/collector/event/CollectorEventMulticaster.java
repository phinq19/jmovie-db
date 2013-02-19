/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.event;

import java.util.Vector;

/**
 * @author albrela
 * 
 */
public class CollectorEventMulticaster implements CollectorListener {

	protected Vector<CollectorListener>	listener	= new Vector<CollectorListener>();

	public void add(final CollectorListener a) {
		if (!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void finderAddFinish(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAddFinish(e);
		}
	}

	@Override
	public void finderAfterAdd(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAfterAdd(e);
		}
	}

	@Override
	public void finderAfterPersist(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAfterPersist(e);
		}
	}

	@Override
	public void finderFoundDir(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderFoundDir(e);
		}
	}

	@Override
	public void finderFoundFile(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderFoundFile(e);
		}
	}

	@Override
	public void finderPreAdd(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderPreAdd(e);
		}
	}

	public void remove(final CollectorListener l) {
		this.listener.remove(l);
	}

}

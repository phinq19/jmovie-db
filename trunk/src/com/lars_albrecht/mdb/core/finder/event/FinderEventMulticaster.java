/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.event;

import java.util.Vector;

/**
 * @author albrela
 * 
 */
public class FinderEventMulticaster implements FinderListener {

	protected Vector<FinderListener>	listener	= new Vector<FinderListener>();

	public void remove(final FinderListener l) {
		this.listener.remove(l);
	}

	public void add(final FinderListener a) {
		if (!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void finderFoundDir(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderFoundDir(e);
		}
	}

	@Override
	public void finderFoundFile(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderFoundFile(e);
		}
	}

	@Override
	public void finderPreAdd(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderPreAdd(e);
		}
	}

	@Override
	public void finderAfterAdd(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAfterAdd(e);
		}
	}

	@Override
	public void finderAfterPersist(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAfterPersist(e);
		}
	}

	@Override
	public void finderAddFinish(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAddFinish(e);
		}
	}

}

/**
 * 
 */
package com.lars_albrecht.mdb.main.core.finder.event;

import java.util.Vector;

/**
 * @author lalbrecht
 * 
 */
public class FinderEventMulticaster implements IFinderListener {

	protected Vector<IFinderListener>	listener	= new Vector<IFinderListener>();

	public void add(final IFinderListener a) {
		if (!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void finderAddFinish(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAddFinish(e);
		}
	}

	@Override
	public void finderAfterAdd(final FinderEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).finderAfterAdd(e);
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

	public void remove(final IFinderListener l) {
		this.listener.remove(l);
	}

}

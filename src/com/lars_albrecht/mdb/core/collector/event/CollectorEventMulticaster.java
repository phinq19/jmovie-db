/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.event;

import java.util.Vector;

/**
 * @author lalbrecht
 * 
 */
public class CollectorEventMulticaster implements ICollectorListener {

	protected Vector<ICollectorListener>	listener	= new Vector<ICollectorListener>();

	public void add(final ICollectorListener a) {
		if (!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void collectorsEndAll(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).collectorsEndAll(e);
		}
	}

	@Override
	public void collectorsEndSingle(final CollectorEvent e) {
		for (int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).collectorsEndSingle(e);
		}
	}

	public void remove(final ICollectorListener l) {
		this.listener.remove(l);
	}

}

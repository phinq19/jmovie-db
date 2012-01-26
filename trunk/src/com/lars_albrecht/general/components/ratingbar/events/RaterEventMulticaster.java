/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.events;

import java.util.Vector;

/**
 * @author lalbrecht
 * 
 */
public class RaterEventMulticaster implements RaterListener {

	protected Vector<RaterListener> listener = new Vector<RaterListener>();

	public void remove(final RaterListener l) {
		this.listener.remove(l);
	}

	public void add(final RaterListener a) {
		if(!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void raterSelected(final RaterEvent e) {
		for(int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).raterSelected(e);
		}
	}

}

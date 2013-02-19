/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel.events;

import java.util.Vector;

/**
 * @author lalbrecht
 * 
 */
public class ImageEventMulticaster implements ImageListener {

	protected Vector<ImageListener> listener = new Vector<ImageListener>();

	public void remove(final ImageListener l) {
		this.listener.remove(l);
	}

	public void add(final ImageListener a) {
		if(!this.listener.contains(a)) {
			this.listener.addElement(a);
		}
	}

	@Override
	public void imageAdded(final ImageEvent e) {
		for(int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).imageAdded(e);
		}
	}

	@Override
	public void imageRemoved(final ImageEvent e) {
		for(int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).imageRemoved(e);
		}
	}

	@Override
	public void imageGet(final ImageEvent e) {
		for(int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).imageGet(e);
		}
	}

	@Override
	public void imageClear(final ImageEvent e) {
		for(int i = 0; i < this.listener.size(); i++) {
			(this.listener.elementAt(i)).imageClear(e);
		}
	}

}

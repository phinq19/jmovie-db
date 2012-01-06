/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel;

import java.awt.Image;
import java.util.ArrayList;

import com.lars_albrecht.general.components.imagepanel.events.ImageEvent;
import com.lars_albrecht.general.components.imagepanel.events.ImageEventMulticaster;
import com.lars_albrecht.general.components.imagepanel.events.ImageListener;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class ImageList extends ArrayList<Image> {

	private ImageEventMulticaster multicaster = null;

	private JImage parent = null;

	public ImageList(final JImage parent) {
		super();
		this.parent = parent;
		this.multicaster = new ImageEventMulticaster();
	}

	@Override
	public boolean add(final Image e) {
		final boolean resultVal = super.add(e);
		this.multicaster.imageAdded((new ImageEvent(this.parent, ImageEvent.IMAGE_ADDED, e)));
		return resultVal;
	}

	@Override
	public Image remove(final int index) {
		final Image resultVal = super.remove(index);
		this.multicaster.imageRemoved((new ImageEvent(this.parent, ImageEvent.IMAGE_ADDED, resultVal)));
		return resultVal;
	}

	@Override
	public Image get(final int index) {
		final Image resultVal = super.get(index);
		this.multicaster.imageGet((new ImageEvent(this.parent, ImageEvent.IMAGE_GET, resultVal)));
		return resultVal;
	}

	@Override
	public void clear() {
		super.clear();
		this.multicaster.imageClear((new ImageEvent(this.parent, ImageEvent.IMAGE_CLEAR, null)));
	}

	public void addImageListener(final ImageListener l) {
		this.multicaster.add(l);
	}

	public void removeImageListener(final ImageListener l) {
		this.multicaster.remove(l);
	}

}

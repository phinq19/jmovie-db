/**
 * 
 */
package com.lars_albrecht.moviedb.components.imagepanel;

import java.awt.Image;
import java.util.ArrayList;

import com.lars_albrecht.moviedb.components.imagepanel.events.IImageListener;
import com.lars_albrecht.moviedb.components.imagepanel.events.ImageEvent;
import com.lars_albrecht.moviedb.components.imagepanel.events.ImageEventMulticaster;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class ImageList extends ArrayList<Image> {

	private ImageEventMulticaster multicaster = null;

	public ImageList() {
		super();
		this.multicaster = new ImageEventMulticaster();
	}

	@Override
	public boolean add(final Image e) {
		final boolean resultVal = super.add(e);
		this.multicaster.imageAdded((new ImageEvent(this, ImageEvent.IMAGE_ADDED, e)));
		return resultVal;
	}

	@Override
	public Image remove(final int index) {
		final Image resultVal = super.remove(index);
		this.multicaster.imageAdded((new ImageEvent(this, ImageEvent.IMAGE_ADDED, this.get(index))));
		return resultVal;
	}

	@Override
	public Image get(final int index) {
		final Image resultVal = super.get(index);
		this.multicaster.imageGet((new ImageEvent(this, ImageEvent.IMAGE_GET, super.get(index))));
		return resultVal;
	}

	@Override
	public void clear() {
		super.clear();
		this.multicaster.imageClear((new ImageEvent(this, ImageEvent.IMAGE_CLEAR, null)));
	}

	public void addImageListener(final IImageListener l) {
		this.multicaster.add(l);
	}

	public void removeImageListener(final IImageListener l) {
		this.multicaster.remove(l);
	}

}

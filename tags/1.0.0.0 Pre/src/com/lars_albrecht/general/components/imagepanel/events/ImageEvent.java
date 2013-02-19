/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel.events;

import java.awt.Image;
import java.util.EventObject;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class ImageEvent extends EventObject {

	public final static int IMAGE_ADDED = 10001;
	public final static int IMAGE_REMOVED = 10002;
	public final static int IMAGE_GET = 10003;
	public final static int IMAGE_CLEAR = 10004;

	protected int id;
	protected Image image;

	public ImageEvent(final Object source, final int id, final Image image) {
		super(source);
		this.id = id;
		this.image = image;
	}

	public int getID() {
		return this.id;
	}

	public Image getImage() {
		return this.image;
	}
}

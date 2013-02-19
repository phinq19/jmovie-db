/**
 * 
 */
package com.lars_albrecht.general.components.imagepanel.events;

import java.util.EventListener;

/**
 * @author lalbrecht
 * 
 */
public interface ImageListener extends EventListener {

	public void imageAdded(ImageEvent e);

	public void imageRemoved(ImageEvent e);

	public void imageGet(ImageEvent e);

	public void imageClear(ImageEvent e);
}

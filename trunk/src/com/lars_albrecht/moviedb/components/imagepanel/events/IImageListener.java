/**
 * 
 */
package com.lars_albrecht.moviedb.components.imagepanel.events;

import java.util.EventListener;

/**
 * @author lalbrecht
 * 
 */
public interface IImageListener extends EventListener {

	public void imageAdded(ImageEvent e);

	public void imageRemoved(ImageEvent e);

	public void imageGet(ImageEvent e);

	public void imageClear(ImageEvent e);
}

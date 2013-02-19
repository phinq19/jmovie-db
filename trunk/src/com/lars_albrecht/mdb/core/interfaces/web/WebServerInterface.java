/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

import com.lars_albrecht.mdb.core.controller.MainController;

/**
 * @author ibsisini
 * 
 */
public class WebServerInterface implements Runnable {

	private MainController	mainController	= null;

	public WebServerInterface(final MainController mainController) {
		this.mainController = mainController;
	}

	@Override
	public void run() {
		this.start();
	}

	/**
	 * Starts the webserver.
	 */
	public void start() {
		final WebServer ws = new WebServer();
		if (ws != null) {
			ws.start(this.mainController);
		}
	}

}

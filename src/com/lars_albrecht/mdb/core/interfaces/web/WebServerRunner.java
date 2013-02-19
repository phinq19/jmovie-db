/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

import com.lars_albrecht.mdb.core.controller.MainController;

/**
 * @author ibsisini
 * 
 */
public class WebServerRunner implements Runnable {

	private MainController mainController = null;

	public WebServerRunner(final MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Starts the webserver.
	 */
	public void start() {
		final WebServer ws = new WebServer();
		if (ws != null) {
			System.out.println("start");
			ws.start(this.mainController);
		}
	}

	@Override
	public void run() {
		System.out.println("run");
		this.start();
	}

}

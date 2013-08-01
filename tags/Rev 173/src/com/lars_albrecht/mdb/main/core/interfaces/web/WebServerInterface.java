/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces.web;

import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.interfaces.WebInterface;

/**
 * @author lalbrecht
 * 
 */
public class WebServerInterface implements Runnable {

	private MainController	mainController	= null;
	private WebInterface	webInterface	= null;

	public WebServerInterface(final MainController mainController, final WebInterface webInterface) {
		this.mainController = mainController;
		this.webInterface = webInterface;
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
			ws.start(this.mainController, this.webInterface);
		}
	}

}

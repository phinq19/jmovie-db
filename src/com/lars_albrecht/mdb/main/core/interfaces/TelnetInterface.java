/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.interfaces.abstracts.AInterface;
import com.lars_albrecht.mdb.main.core.interfaces.telnet.TelnetRunner;

/**
 * @author lalbrecht
 * 
 */
public class TelnetInterface extends AInterface {

	public TelnetInterface(final MainController mainController, final IController controller) {
		super(mainController, controller);
		this.canOpened = true;
	}

	@Override
	public void startInterface() {
		final int port = 23;
		final int maxConnections = 100;
		// Listen for incoming connections and handle them
		int i = 0;

		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			Socket client;

			while ((i++ < maxConnections) || (maxConnections == 0)) {
				client = server.accept();
				System.out.println("accept");
				final TelnetRunner runner = new TelnetRunner(this.mainController, client);
				final Thread t = new Thread(runner);
				t.start();
			}
		} catch (final IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void openInterface() {
	}

}

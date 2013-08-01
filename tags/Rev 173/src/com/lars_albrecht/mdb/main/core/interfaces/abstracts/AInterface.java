/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces.abstracts;

import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;

/**
 * 
 * @author lalbrecht
 * 
 */
public abstract class AInterface implements Runnable {

	protected MainController	mainController		= null;
	protected IController		controller			= null;
	public boolean				canOpened	= false;

	public AInterface(final MainController mainController, final IController controller) {
		this.mainController = mainController;
		this.controller = controller;
	}

	@Override
	public final void run() {
		this.startInterface();
		this.controller.getThreadList().remove(Thread.currentThread());
	}

	/**
	 * Starts the interface.
	 */
	public abstract void startInterface();

	public abstract void openInterface();

}

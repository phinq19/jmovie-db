/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.abstracts;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;

/**
 * 
 * @author ibsisini
 * 
 */
public abstract class AInterface implements Runnable {

	protected MainController	mainController	= null;
	protected IController		controller		= null;

	public AInterface(final MainController mainController, final IController controller) {
		this.mainController = mainController;
		this.controller = controller;
	}

	@Override
	public final void run() {
		this.starInterface();
		this.controller.getThreadList().remove(Thread.currentThread());
	}

	/**
	 * Starts the interface.
	 */
	public abstract void starInterface();

}

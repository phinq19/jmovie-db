/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.core.interfaces.abstracts.AInterface;

/**
 * @author ibsisini
 * 
 */
public class InterfaceController implements IController {

	private ArrayList<AInterface> interfaces = null;

	private MainController mainController = null;

	public InterfaceController(final MainController mainController) {
		this.mainController = mainController;
		this.interfaces = new ArrayList<AInterface>();
		this.initInterfaces();
	}

	private void initInterfaces() {
		this.interfaces.add(new WebInterface(this.mainController, this));
	}

	private void runInterfaces() {
		for (final AInterface interfaze : this.interfaces) {
			IController.threadList.add(new Thread(interfaze));
			IController.threadList.get(IController.threadList.size() - 1).start();
		}
	}

	@Override
	public void run(final Object... params) {
		this.runInterfaces();
	}

	@Override
	public ArrayList<Thread> getThreadList() {
		return IController.threadList;
	}
}

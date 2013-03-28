/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.interfaces.WebInterface;
import com.lars_albrecht.mdb.core.interfaces.abstracts.AInterface;

/**
 * @author lalbrecht
 * 
 */
public class InterfaceController implements IController {

	final ArrayList<ThreadEx>		threadList		= new ArrayList<ThreadEx>();
	private ArrayList<AInterface>	interfaces		= null;

	private MainController			mainController	= null;

	public InterfaceController(final MainController mainController) {
		this.mainController = mainController;
		this.interfaces = new ArrayList<AInterface>();
		this.initInterfaces();
	}

	@Override
	public ArrayList<ThreadEx> getThreadList() {
		return this.threadList;
	}

	private void initInterfaces() {
		this.interfaces.add(new WebInterface(this.mainController, this));
	}

	@Override
	public void run(final Object... params) {
		this.runInterfaces();
	}

	private void runInterfaces() {
		for (final AInterface interfaze : this.interfaces) {
			this.threadList.add(new ThreadEx(interfaze));
			this.threadList.get(this.threadList.size() - 1).start();
		}
	}

}

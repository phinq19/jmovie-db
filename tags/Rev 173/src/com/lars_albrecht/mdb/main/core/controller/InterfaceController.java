/**
 * 
 */
package com.lars_albrecht.mdb.main.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.main.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.main.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.main.core.interfaces.abstracts.AInterface;

/**
 * @author lalbrecht
 * 
 */
public class InterfaceController implements IController {

	final ArrayList<ThreadEx>		threadList		= new ArrayList<ThreadEx>();
	private ArrayList<AInterface>	interfaces		= null;

	@SuppressWarnings("unused")
	private MainController			mainController	= null;

	public InterfaceController(final MainController mainController) {
		this.mainController = mainController;
		this.interfaces = new ArrayList<AInterface>();
	}

	@Override
	public ArrayList<ThreadEx> getThreadList() {
		return this.threadList;
	}

	@Override
	public void run(final Object... params) throws Exception {
		this.runInterfaces();
	}

	private void runInterfaces() throws Exception {
		if (this.interfaces == null || this.interfaces.size() == 0) {
			throw new Exception("Interface Controller run failed. No interfaces specified");
		}
		for (final AInterface interfaze : this.interfaces) {
			this.threadList.add(new ThreadEx(interfaze));
			this.threadList.get(this.threadList.size() - 1).start();
		}
	}

	/**
	 * @param interfaces
	 *            the interfaces to set
	 */
	public final void setInterfaces(final ArrayList<AInterface> interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * @return the interfaces
	 */
	public final ArrayList<AInterface> getInterfaces() {
		return this.interfaces;
	}

}

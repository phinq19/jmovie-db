/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.finder.Finder;
import com.lars_albrecht.mdb.core.finder.event.FinderEventMulticaster;
import com.lars_albrecht.mdb.core.finder.event.IFinderListener;

/**
 * @author lalbrecht
 * 
 */
public class FinderController implements IController {
	private final ArrayList<Thread>	threadList			= new ArrayList<Thread>();
	private final ArrayList<File>	foundFiles			= new ArrayList<File>();
	private FinderEventMulticaster	finderMulticaster	= null;

	public FinderController(final MainController mainController) {
		this.finderMulticaster = new FinderEventMulticaster();
	}

	public void addFinderEventListener(final IFinderListener listener) {
		this.finderMulticaster.add(listener);
	}

	/**
	 * 
	 * @param files
	 */
	public void findFiles(final ArrayList<File> files) {
		// start a new thread for each given dir
		for (int i = 0; i < files.size(); i++) {
			this.threadList.add(new Thread(new Finder(this, files.get(i))));
			this.threadList.get(this.threadList.size() - 1).start();
		}
	}

	public FinderEventMulticaster getFinderMulticaster() {
		return this.finderMulticaster;
	}

	public ArrayList<File> getFoundFiles() {
		return this.foundFiles;
	}

	@Override
	public ArrayList<Thread> getThreadList() {
		return this.threadList;
	}

	public void removeFinderEventListener(final IFinderListener listener) {
		this.finderMulticaster.remove(listener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(final Object... params) {
		if ((params.length == 1) && (params[0] instanceof ArrayList<?>)) {
			this.findFiles((ArrayList<File>) params[0]);
		}
	}

}

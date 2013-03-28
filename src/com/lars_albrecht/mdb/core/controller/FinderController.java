/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.core.abstracts.ThreadEx;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.finder.Finder;
import com.lars_albrecht.mdb.core.finder.event.FinderEventMulticaster;
import com.lars_albrecht.mdb.core.finder.event.IFinderListener;

/**
 * @author lalbrecht
 * 
 */
public class FinderController implements IController {
	final ArrayList<ThreadEx>		threadList			= new ArrayList<ThreadEx>();
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
		if (files != null && files.size() > 0) {
			final String[] info = {
				"Finder"
			};
			ThreadEx tempThread = null;
			for (int i = 0; i < files.size(); i++) {
				if (files.get(i).exists() && files.get(i).isDirectory() && files.get(i).canRead()) {
					tempThread = new ThreadEx(new Finder(this, files.get(i)), files.get(i).getPath(), info);
					this.threadList.add(tempThread);
					tempThread.start();
				}
			}
		}
	}

	public FinderEventMulticaster getFinderMulticaster() {
		return this.finderMulticaster;
	}

	public ArrayList<File> getFoundFiles() {
		return this.foundFiles;
	}

	@Override
	public ArrayList<ThreadEx> getThreadList() {
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

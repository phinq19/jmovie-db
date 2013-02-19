/**
 * 
 */
package com.lars_albrecht.mdb.controller;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.core.finder.Finder;
import com.lars_albrecht.mdb.core.finder.event.FinderEventMulticaster;
import com.lars_albrecht.mdb.core.finder.event.FinderListener;

/**
 * @author albrela
 * 
 */
public class ThreadController {

	private final ArrayList<Thread>	finderList			= new ArrayList<Thread>();
	private final ArrayList<File>	foundFiles			= new ArrayList<File>();
	private FinderEventMulticaster	finderMulticaster	= null;

	public ThreadController() {
		this.finderMulticaster = new FinderEventMulticaster();
	}

	/**
	 * 
	 * @param files
	 */
	public void findFiles(final ArrayList<File> files) {
		for (int i = 0; i < files.size(); i++) {
			this.finderList.add(new Thread(new Finder(this, files.get(i))));
			this.finderList.get(this.finderList.size() - 1).start();
		}
	}

	public FinderEventMulticaster getFinderMulticaster() {
		return this.finderMulticaster;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Thread> getFinderList() {
		return this.finderList;
	}

	public ArrayList<File> getFoundFiles() {
		return this.foundFiles;
	}

	public void addFinderEventListener(final FinderListener listener) {
		this.finderMulticaster.add(listener);
	}

	public void removeFinderEventListener(final FinderListener listener) {
		this.finderMulticaster.remove(listener);
	}

}

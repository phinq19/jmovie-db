/**
 * 
 */
package com.lars_albrecht.moviedb.thread;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.moviedb.controller.ThreadController;
import com.lars_albrecht.moviedb.filter.MovieFilenameFilter;

/**
 * @author lalbrecht
 * 
 */
public class Finder implements Runnable {

	private ThreadController tc = null;
	private File dir = null;

	private ArrayList<File> dirs = null;

	/**
	 * 
	 * @param tc
	 *            ParserController
	 * @param dir
	 *            File
	 */
	public Finder(final ThreadController tc, final File dir) {
		Debug.log(Debug.LEVEL_DEBUG, "new Parser for " + dir.getAbsolutePath());
		this.tc = tc;
		this.dir = dir;

	}

	/**
	 * 
	 * @param dirs
	 */
	private void getMoviesFromFolders(final ArrayList<File> dirs) {
		final ArrayList<File> tempFileList = new ArrayList<File>();
		for(final File dir : dirs) {
			final File[] files = dir.listFiles(new MovieFilenameFilter());
			for(final File file : files) {
				if(file.exists() && file.isDirectory()) {
					final ArrayList<File> tempList = new ArrayList<File>();
					tempList.add(file);

					// TODO new Thread?
					this.getMoviesFromFolders(tempList);
				}
				if(file.exists() && !file.isDirectory() && file.isFile() && (new MovieFilenameFilter().accept(file))) {
					tempFileList.add(file);
				}
			}
		}
		if(tempFileList.size() > 0) {
			this.tc.startParse(tempFileList);
		}
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		if(this.dir != null) {
			this.dirs = new ArrayList<File>();
			this.dirs.add(this.dir);
			this.tc.getFinderList().remove(Thread.currentThread());
			this.getMoviesFromFolders(this.dirs);
		} else {
			this.tc.getFinderList().remove(Thread.currentThread());
		}
	}

}

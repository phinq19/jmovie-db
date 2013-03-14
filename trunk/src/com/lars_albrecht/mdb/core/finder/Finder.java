/**
 * 
 */
package com.lars_albrecht.mdb.core.finder;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.core.controller.FinderController;
import com.lars_albrecht.mdb.core.finder.event.FinderEvent;
import com.lars_albrecht.mdb.core.finder.event.FinderEventMulticaster;
import com.lars_albrecht.mdb.filter.VideoFileFilter;

/**
 * @author lalbrecht
 * 
 */
public class Finder implements Runnable {

	private FinderController		controller	= null;
	private File					dir			= null;

	private FinderEventMulticaster	multicaster	= null;

	private ArrayList<File>			dirs		= null;

	/**
	 * 
	 * @param controller
	 * @param dir
	 */
	public Finder(final FinderController controller, final File dir) {
		this.controller = controller;
		this.dir = dir;
		this.multicaster = controller.getFinderMulticaster();
	}

	/**
	 * 
	 * @param dirs
	 * @return ArrayList<File>
	 */
	public ArrayList<File> find(final ArrayList<File> dirs) {
		final ArrayList<File> tempList = new ArrayList<File>();
		// for each dir in dirs
		for (final File dir : dirs) {
			// if given dir exists and is a directory
			if (dir.exists() && dir.isDirectory()) {
				// list files
				final File[] files = dir.listFiles(new VideoFileFilter());
				// if files were found
				if ((files != null) && (files.length > 0)) {
					// for each file in files
					for (final File file : files) {
						// is file is directory ...
						if (file.exists()) {
							if (file.isDirectory()) {
								final ArrayList<File> tempDirList = new ArrayList<File>();
								tempDirList.add(file);
								this.multicaster.finderFoundDir((new FinderEvent(this, FinderEvent.FINDER_FOUNDDIR, tempDirList)));

								// start new thread to find new files in
								// subfolder
								this.controller.findFiles(tempDirList);
							} else if (file.isFile()) {
								final ArrayList<File> tempFileList = new ArrayList<File>();
								tempFileList.add(file);
								this.multicaster.finderFoundFile((new FinderEvent(this, FinderEvent.FINDER_FOUNDFILE, tempFileList)));
								tempList.add(file);
							}
						}
					}
				}
			}
		}
		return tempList;

	}

	@Override
	public void run() {
		if (this.dir != null) {
			this.dirs = new ArrayList<File>();
			this.dirs.add(this.dir);
			final ArrayList<File> foundFiles = this.find(this.dirs);

			this.multicaster.finderPreAdd((new FinderEvent(this, FinderEvent.FINDER_PREADD, foundFiles)));
			this.controller.getFoundFiles().addAll(foundFiles);
			this.multicaster.finderAfterAdd((new FinderEvent(this, FinderEvent.FINDER_AFTERADD, this.controller.getFoundFiles())));
		}
		this.controller.getThreadList().remove(Thread.currentThread());
		if (this.controller.getThreadList().size() == 0) {
			this.multicaster.finderAddFinish((new FinderEvent(this, FinderEvent.FINDER_ADDFINISH, this.controller.getFoundFiles())));
		}
	}
}

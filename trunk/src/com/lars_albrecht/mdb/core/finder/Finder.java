/**
 * 
 */
package com.lars_albrecht.mdb.core.finder;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.mdb.controller.ThreadController;
import com.lars_albrecht.mdb.core.finder.event.FinderEvent;
import com.lars_albrecht.mdb.core.finder.event.FinderEventMulticaster;
import com.lars_albrecht.mdb.filter.VideoFileFilter;

/**
 * @author albrela
 * 
 */
public class Finder implements Runnable {

	private ThreadController		threadController	= null;
	private File					dir					= null;

	private FinderEventMulticaster	multicaster			= null;

	private ArrayList<File>			dirs				= null;

	public Finder(final ThreadController threadController, final File dir) {
		this.threadController = threadController;
		this.dir = dir;
		this.multicaster = threadController.getFinderMulticaster();

	}

	public ArrayList<File> find(final ArrayList<File> dirs) {
		final ArrayList<File> tempFileList = new ArrayList<File>();
		// for each dir in dirs
		for (final File dir : dirs) {
			// list files
			final File[] files = dir.listFiles(new VideoFileFilter());
			// for each file in files
			for (final File file : files) {
				// is file is directory ...
				if (file.exists()) {
					final ArrayList<File> tempList = new ArrayList<File>();
					tempList.add(file);
					if (file.isDirectory()) {
						this.multicaster.finderFoundDir((new FinderEvent(this,
								FinderEvent.FINDER_FOUNDDIR, tempList)));

						// start new thread to find new files
						this.threadController.findFiles(tempList);
					} else if (file.isFile()) {
						this.multicaster.finderFoundFile((new FinderEvent(this,
								FinderEvent.FINDER_FOUNDFILE, tempList)));
						tempFileList.add(file);
					}
				}
			}
		}

		return tempFileList;

	}

	@Override
	public void run() {
		if (this.dir != null) {
			this.dirs = new ArrayList<File>();
			this.dirs.add(this.dir);
			final ArrayList<File> foundFiles = this.find(this.dirs);

			this.multicaster.finderPreAdd((new FinderEvent(this,
					FinderEvent.FINDER_PREADD, foundFiles)));
			this.threadController.getFoundFiles().addAll(foundFiles);
			this.multicaster.finderAfterAdd((new FinderEvent(this,
					FinderEvent.FINDER_AFTERADD, this.threadController
							.getFoundFiles())));
		}
		this.threadController.getFinderList().remove(Thread.currentThread());
		if (this.threadController.getFinderList().size() == 0) {
			this.multicaster.finderAddFinish((new FinderEvent(this,
					FinderEvent.FINDER_ADDFINISH, this.threadController
							.getFoundFiles())));
		}
	}

}

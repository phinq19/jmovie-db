/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author lalbrecht
 * 
 *         This class can help to find a file that can be in different paths.
 * 
 */
public class FileFinder {

	private static FileFinder	instance	= new FileFinder();

	private ArrayList<File>		pathList	= null;

	private FileFinder() {
	}

	/**
	 * 
	 * @return FileFinder
	 */
	public static FileFinder getInstance() {
		return FileFinder.instance;
	}

	public void addToPathList(final File path, final int position) {
		if (path.exists() && path.isDirectory()) {

			if (this.pathList == null) {
				this.pathList = new ArrayList<File>();
			}
			switch (position) {
				case -1:
					this.pathList.add(path);
					break;
				default:
					this.pathList.add(position, path);
			}
		}
	}

	public ArrayList<File> getPathList() {
		return this.pathList;
	}

	public File findFile(final File fileToFind) throws IOException {
		File returnFile = null;
		if (this.pathList != null) {
			for (final File path : this.pathList) {
				if ((returnFile = new File(path.getCanonicalPath() + File.separator + fileToFind.getName())) != null && returnFile.exists()
						&& returnFile.isFile()) {
					return returnFile;
				}
			}
		}
		return null;
	}
}

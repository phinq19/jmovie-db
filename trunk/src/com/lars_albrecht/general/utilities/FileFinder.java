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
			if (!this.pathList.contains(path)) {
				switch (position) {
					case -1:
						this.pathList.add(path);
						break;
					default:
						this.pathList.add(position, path);
				}
			}
		}
	}

	public ArrayList<File> getPathList() {
		return this.pathList;
	}

	public File findFile(final File fileToFind, final boolean recursive) throws IOException {
		File returnFile = null;
		if (this.pathList != null) {
			for (final File path : this.pathList) {
				final File[] files = path.listFiles();
				if ((returnFile = this.findFileInDir(files, fileToFind, recursive)) != null) {
					break;
				}
			}
		}
		return returnFile;
	}

	/**
	 * Find file in directory. TODO speed up recursive find.
	 * 
	 * @param files
	 * @param fileToFind
	 * @param recursive
	 * @return File
	 * @throws IOException
	 */
	private File findFileInDir(final File[] files, File fileToFind, final boolean recursive) throws IOException {
		fileToFind = new File(fileToFind.getName());
		File returnFile = null;
		for (final File file : files) {
			Debug.log(Debug.LEVEL_DEBUG, "Search file " + fileToFind.getName() + " in " + file.getAbsolutePath());
			if ((file.isDirectory() && (returnFile = new File(file.getCanonicalPath() + File.separator + fileToFind.getName())) != null
					&& returnFile.exists() && returnFile.isFile())
					|| (file.isFile() && file.getName().equalsIgnoreCase(fileToFind.getName()) && (returnFile = file) != null)
					&& returnFile.exists()) {
				Debug.log(Debug.LEVEL_INFO, "Found File " + fileToFind.getName() + " in " + file.getAbsolutePath());
				return new File(returnFile.getAbsolutePath());
			} else if (file.isDirectory() && file.exists() && recursive) {
				returnFile = this.findFileInDir(file.listFiles(), fileToFind, recursive);
				if (returnFile != null) {
					return returnFile;
				}
			}
		}
		return null;
	}
}
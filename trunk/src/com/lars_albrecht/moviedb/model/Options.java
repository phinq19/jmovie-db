/**
 * 
 */
package com.lars_albrecht.moviedb.model;

import java.io.File;
import java.util.ArrayList;

/**
 * @author lalbrecht
 * 
 */
public class Options {

	private ArrayList<File> paths = null;
	private String filenameSeperator = null;
	private Boolean showStatusAfterRefresh = null;

	// x y height / width / pos

	public Options() {
	}

	/**
	 * @return the paths
	 */
	public synchronized final ArrayList<File> getPaths() {
		if (this.paths == null) {
			this.paths = new ArrayList<File>();
		}
		return this.paths;
	}

	/**
	 * @param paths
	 *            the paths to set
	 */
	public synchronized final void setPaths(final ArrayList<File> paths) {
		this.paths = paths;
	}

	/**
	 * @return the filenameSeperator
	 */
	public synchronized final String getFilenameSeperator() {
		if (this.filenameSeperator == null) {
			this.filenameSeperator = new String();
		}
		return this.filenameSeperator;
	}

	/**
	 * @param filenameSeperator
	 *            the filenameSeperator to set
	 */
	public synchronized final void setFilenameSeperator(final String filenameSeperator) {
		this.filenameSeperator = filenameSeperator;
	}

	/**
	 * @return the showStatusAfterRefresh
	 */
	public synchronized final Boolean getShowStatusAfterRefresh() {
		return this.showStatusAfterRefresh;
	}

	/**
	 * @param showStatusAfterRefresh
	 *            the showStatusAfterRefresh to set
	 */
	public synchronized final void setShowStatusAfterRefresh(Boolean showStatusAfterRefresh) {
		this.showStatusAfterRefresh = showStatusAfterRefresh;
	}

}

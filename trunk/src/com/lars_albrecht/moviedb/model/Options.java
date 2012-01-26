/**
 * 
 */
package com.lars_albrecht.moviedb.model;

import java.awt.Point;
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

	private Point widthHeightMainWindow = null;
	private Point xYMainWindow = null;

	private Integer sliderBottomPos = null;

	private Boolean refreshOnStartup = null;

	public Options() {
	}

	/**
	 * @return the paths
	 */
	public synchronized final ArrayList<File> getPaths() {
		if(this.paths == null) {
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
		if(this.filenameSeperator == null) {
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
	public synchronized final void setShowStatusAfterRefresh(final Boolean showStatusAfterRefresh) {
		this.showStatusAfterRefresh = showStatusAfterRefresh;
	}

	/**
	 * @return the widthHeightMainWindow
	 */
	public synchronized final Point getWidthHeightMainWindow() {
		return this.widthHeightMainWindow;
	}

	/**
	 * @param widthHeightMainWindow
	 *            the widthHeightMainWindow to set
	 */
	public synchronized final void setWidthHeightMainWindow(final Point widthHeightMainWindow) {
		this.widthHeightMainWindow = widthHeightMainWindow;
	}

	/**
	 * @return the xYMainWindow
	 */
	public synchronized final Point getxYMainWindow() {
		return this.xYMainWindow;
	}

	/**
	 * @param xYMainWindow
	 *            the xYMainWindow to set
	 */
	public synchronized final void setxYMainWindow(final Point xYMainWindow) {
		this.xYMainWindow = xYMainWindow;
	}

	/**
	 * @return the sliderBottomPos
	 */
	public synchronized final Integer getSliderBottomPos() {
		return this.sliderBottomPos;
	}

	/**
	 * @param sliderBottomPos
	 *            the sliderBottomPos to set
	 */
	public synchronized final void setSliderBottomPos(final Integer sliderBottomPos) {
		this.sliderBottomPos = sliderBottomPos;
	}

	/**
	 * @return the refreshOnStartup
	 */
	public synchronized final Boolean getRefreshOnStartup() {
		return this.refreshOnStartup;
	}

	/**
	 * @param refreshOnStartup
	 *            the refreshOnStartup to set
	 */
	public synchronized final void setRefreshOnStartup(final Boolean refreshOnStartup) {
		this.refreshOnStartup = refreshOnStartup;
	}

}

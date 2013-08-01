/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author lalbrecht
 * 
 */
public class VideoFileFilter implements FileFilter {
	private final String[]	fileExt	= {
			".avi", ".mkv", ".m2ts", ".mk3d", ".mts", ".mka", ".mpg", ".mpeg", ".mp4", ".wmv", ".mov", ".flv", ".rm", ".3gp", ".qt",
			".xvid", ".divx", ".vob", ".evo", ".asf", ".ps", ".ts", ".tsp", ".rmvb", ".ra", ".ram", ".ogv", ".ogm", ".mxf", ".omf", ".dv",
			".ratDVD", ".iso", ".img"
									};

	@Override
	public boolean accept(final File path) {
		if (path.isFile()) {
			for (final String ext : this.fileExt) {
				if (path.getName().toLowerCase().endsWith(ext.toLowerCase())) {
					return true;
				}
			}
		} else if (path.isDirectory()) {
			return true;
		}
		return false;
	}

}

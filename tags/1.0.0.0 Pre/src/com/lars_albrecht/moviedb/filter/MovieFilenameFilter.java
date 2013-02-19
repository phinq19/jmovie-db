/**
 * 
 */
package com.lars_albrecht.moviedb.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * @author lalbrecht
 * 
 */
public class MovieFilenameFilter extends FileFilter implements FilenameFilter {

	/**
	 * 
	 * @param dir
	 * @param name
	 * @return boolean
	 */
	@Override
	public boolean accept(final File dir, final String name) {
		final ArrayList<String> ext = new ArrayList<String>();
		if(dir.exists()) {
			ext.add(".avi");
			ext.add(".mkv");
			ext.add(".mpg");
			ext.add(".mpeg");
			ext.add(".iso");
			ext.add(".img");
			ext.add(".xvid");
			ext.add(".divx");
			for(final String string : ext) {
				if(name.toLowerCase().endsWith(string.toLowerCase())) {
					return Boolean.TRUE;
				}
			}
			if(dir.canRead() && dir.isDirectory() && !name.equals(".") && !name.equals("..")) {
				return Boolean.TRUE;
			}
		}
		return false;
	}

	@Override
	public boolean accept(final File f) {
		return this.accept(f, f.getName());
	}

	@Override
	public String getDescription() {
		return null;
	}
}

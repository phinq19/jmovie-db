/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.event;

import java.util.EventListener;

/**
 * @author lalbrecht
 * 
 */
public interface FinderListener extends EventListener {

	/**
	 * Called after all directories are searched for new files. Event contains
	 * an ArrayList<File> with all found files.
	 * 
	 * @param e
	 */
	public void finderAddFinish(FinderEvent e);

	/**
	 * Called after found filelist is added. Event contains an ArrayList<File>
	 * with all found files.
	 * 
	 * @param e
	 */
	public void finderAfterAdd(FinderEvent e);

	/**
	 * Called when a directory is found. Event contains an ArrayList<File> with
	 * the found dir.
	 * 
	 * @param e
	 */
	public void finderFoundDir(FinderEvent e);

	/**
	 * Called when a file is found. Event contains an ArrayList<File> with the
	 * found file.
	 * 
	 * @param e
	 */
	public void finderFoundFile(FinderEvent e);

	/**
	 * Called before found filelist is added. Event contains an ArrayList<File>
	 * with the found files that will be added.
	 * 
	 * @param e
	 */
	public void finderPreAdd(FinderEvent e);

}

/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.event;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * @author albrela
 * 
 */
public class FinderEvent extends EventObject {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8832074186744046862L;

	public final static int		FINDER_FOUNDDIR		= 10001;
	public final static int		FINDER_FOUNDFILE	= 10002;
	public final static int		FINDER_PREADD		= 10010;
	public final static int		FINDER_AFTERADD		= 10011;
	public final static int		FINDER_ADDFINISH	= 10100;

	protected int				id;
	protected ArrayList<File>	files;

	public FinderEvent(final Object source, final int id, final ArrayList<File> files) {
		super(source);
		this.id = id;
		this.files = files;
	}

	public ArrayList<File> getFiles() {
		return this.files;
	}

	public int getId() {
		return this.id;
	}

}

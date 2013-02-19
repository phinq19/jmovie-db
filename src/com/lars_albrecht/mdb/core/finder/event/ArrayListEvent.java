/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.event;

import java.util.EventObject;

/**
 * @author albrela
 * 
 */
public class ArrayListEvent extends EventObject {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7129384196909864578L;
	public final static int		FINDER_ADD			= 10001;
	public final static int		FINDER_REMOVE		= 10002;
	public final static int		FINDER_CLEAR		= 10003;

	protected int				id;

	public ArrayListEvent(final Object source, final int id) {
		super(source);
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}

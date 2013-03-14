/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.event;

import java.util.EventObject;

/**
 * @author lalbrecht
 * 
 */
public class CollectorEvent extends EventObject {

	/**
	 * 
	 */
	private static final long	serialVersionUID				= -3781749746615106706L;

	public final static int		COLLECTOR_ENDALL_COLLECTOR		= 20001;
	public final static int		COLLECTOR_ENDSINGLE_COLLECTOR	= 20002;

	protected int				id;
	protected String			collectorName;

	public CollectorEvent(final Object source, final int id, final String collectorName) {
		super(source);
		this.id = id;
		this.collectorName = collectorName;
	}

	/**
	 * @return the collectorName
	 */
	public String getCollectorName() {
		return this.collectorName;
	}

	/**
	 * @param collectorName
	 *            the collectorName to set
	 */
	public void setCollectorName(final String collectorName) {
		this.collectorName = collectorName;
	}

	public int getId() {
		return this.id;
	}

}

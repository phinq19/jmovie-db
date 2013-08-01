/**
 * 
 */
package com.lars_albrecht.mdb.main.core.collector.event;

import java.util.EventListener;

/**
 * @author lalbrecht
 * 
 */
public interface ICollectorListener extends EventListener {

	/**
	 * Called after all collectors has finished.
	 * 
	 * @param e
	 */
	public void collectorsEndAll(CollectorEvent e);

	/**
	 * Called after a single collector has finished.
	 * 
	 * @param e
	 */
	public void collectorsEndSingle(CollectorEvent e);

}

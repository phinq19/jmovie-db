/**
 * 
 */
package com.lars_albrecht.general.components.ratingbar.events;

import java.util.EventObject;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class RaterEvent extends EventObject {

	public final static int RATER_SELECTED = 10001;

	protected int id;
	protected Integer selectedId;

	public RaterEvent(final Object source, final int id, final Integer selectedId) {
		super(source);
		this.id = id;
		this.selectedId = selectedId;
	}

	public int getID() {
		return this.id;
	}

	/**
	 * @return the selectedId
	 */
	public synchronized final Integer getSelectedId() {
		return this.selectedId;
	}

	/**
	 * @param selectedId
	 *            the selectedId to set
	 */
	public synchronized final void setSelectedId(final Integer selectedId) {
		this.selectedId = selectedId;
	}

}

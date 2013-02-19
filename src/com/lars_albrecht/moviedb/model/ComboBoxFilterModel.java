/**
 * 
 */
package com.lars_albrecht.moviedb.model;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;

/**
 * @author lalbrecht
 * 
 */
public class ComboBoxFilterModel {

	private String title = null;
	private Boolean isDefault = null;
	private Boolean buildIn = null;
	private RowFilter<? super TableModel, ? super Integer> rowFilter = null;

	@Override
	public String toString() {
		return this.title;
	}

	public ComboBoxFilterModel(final String title, final Boolean isDefault, final Boolean buildIn,
			final RowFilter<? super TableModel, ? super Integer> rowFilter) {
		super();
		this.title = title;
		this.isDefault = isDefault;
		this.buildIn = buildIn;
		this.rowFilter = rowFilter;
	}

	/**
	 * @return the title
	 */
	public synchronized final String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public synchronized final void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the isDefault
	 */
	public synchronized final Boolean getIsDefault() {
		return this.isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public synchronized final void setIsDefault(final Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the buildIn
	 */
	public synchronized final Boolean getBuildIn() {
		return this.buildIn;
	}

	/**
	 * @param buildIn
	 *            the buildIn to set
	 */
	public synchronized final void setBuildIn(final Boolean buildIn) {
		this.buildIn = buildIn;
	}

	/**
	 * @return the rowFilter
	 */
	public synchronized final RowFilter<? super TableModel, ? super Integer> getRowFilter() {
		return this.rowFilter;
	}

	/**
	 * @param rowFilter
	 *            the rowFilter to set
	 */
	public synchronized final void setRowFilter(final RowFilter<? super TableModel, ? super Integer> rowFilter) {
		this.rowFilter = rowFilter;
	}

}

/**
 * 
 */
package com.lars_albrecht.moviedb.model;

/**
 * @author lalbrecht
 * 
 */
public class TypeModel {

	final public static Integer TYPE_INTEGER = 0;
	final public static Integer TYPE_STRING = 1;
	final public static Integer TYPE_LIST = 2;
	final public static Integer TYPE_IMAGE = 3;

	private String name = null;
	private Integer type = null;
	private Object value = null;
	private Boolean inTable = null;
	private Boolean inDetail = null;
	private Integer tableSort = null;
	private Integer detailSort = null;

	/**
	 * @return the name
	 */
	public synchronized final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public synchronized final Integer getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public synchronized final void setType(final Integer type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public synchronized final Object getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public synchronized final void setValue(final Object value) {
		this.value = value;
	}

	/**
	 * @return the inTable
	 */
	public synchronized final Boolean getInTable() {
		return this.inTable;
	}

	/**
	 * @param inTable
	 *            the inTable to set
	 */
	public synchronized final void setInTable(final Boolean inTable) {
		this.inTable = inTable;
	}

	/**
	 * @return the inDetail
	 */
	public synchronized final Boolean getInDetail() {
		return this.inDetail;
	}

	/**
	 * @param inDetail
	 *            the inDetail to set
	 */
	public synchronized final void setInDetail(final Boolean inDetail) {
		this.inDetail = inDetail;
	}

	/**
	 * @return the tableSort
	 */
	public synchronized final Integer getTableSort() {
		return this.tableSort;
	}

	/**
	 * @param tableSort
	 *            the tableSort to set
	 */
	public synchronized final void setTableSort(final Integer tableSort) {
		this.tableSort = tableSort;
	}

	/**
	 * @return the detailSort
	 */
	public synchronized final Integer getDetailSort() {
		return this.detailSort;
	}

	/**
	 * @param detailSort
	 *            the detailSort to set
	 */
	public synchronized final void setDetailSort(final Integer detailSort) {
		this.detailSort = detailSort;
	}

}

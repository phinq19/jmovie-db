/**
 * 
 */
package com.lars_albrecht.moviedb.model;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ibsisini
 * 
 */
public class FieldModel implements Comparable<FieldModel> {

	private Field field = null;
	private String as = null;
	private Integer type = null;
	private String typeConf = null;
	private Integer sort = null;
	private String name = null;
	private ConcurrentHashMap<String, Object> additional = null;

	/**
	 * 
	 */
	public FieldModel() {
	}

	/**
	 * 
	 * @param field
	 * @param as
	 * @param type
	 * @param typeConf
	 * @param sort
	 * @param name
	 * @param additional
	 */
	public FieldModel(final Field field, final String as, final Integer type, final String typeConf, final Integer sort,
			final String name, final ConcurrentHashMap<String, Object> additional) {
		this.field = field;
		this.as = as;
		this.type = type;
		this.typeConf = typeConf;
		this.sort = sort;
		this.name = name;
		this.additional = additional;
	}

	/**
	 * 
	 * @param key
	 * @return Object
	 */
	public Object getValueFrom(final String key) {
		if(key.equalsIgnoreCase("name")) {
			return this.getName();
		} else if(key.equalsIgnoreCase("as")) {
			return this.getAs();
		} else if(key.equalsIgnoreCase("sort")) {
			return this.getSort();
		} else if(key.equalsIgnoreCase("type")) {
			return this.getType();
		} else if(key.equalsIgnoreCase("typeConf")) {
			this.getTypeConf();
		} else {
			if(this.getAdditional() != null) {
				this.getAdditional().get(key);
			}
		}
		return null;
	}

	/**
	 * @return the field
	 */
	public synchronized final Field getField() {
		return this.field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public synchronized final void setField(final Field field) {
		this.field = field;
	}

	/**
	 * @return the as
	 */
	public synchronized final String getAs() {
		return this.as;
	}

	/**
	 * @param as
	 *            the as to set
	 */
	public synchronized final void setAs(final String as) {
		this.as = as;
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
	 * @return the typeConf
	 */
	public synchronized final String getTypeConf() {
		return this.typeConf;
	}

	/**
	 * @param typeConf
	 *            the typeConf to set
	 */
	public synchronized final void setTypeConf(final String typeConf) {
		this.typeConf = typeConf;
	}

	/**
	 * @return the sort
	 */
	public synchronized final Integer getSort() {
		return this.sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public synchronized final void setSort(final Integer sort) {
		this.sort = sort;
	}

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
	 * @return the additional
	 */
	public synchronized final ConcurrentHashMap<String, Object> getAdditional() {
		return this.additional;
	}

	/**
	 * @param additional
	 *            the additional to set
	 */
	public synchronized final void setAdditional(final ConcurrentHashMap<String, Object> additional) {
		this.additional = additional;
	}

	@Override
	public int compareTo(final FieldModel fm) {
		if((this.sort != null) && (fm.sort != null)) {
			if(fm.sort == this.sort) {
				return 0;
			} else if(this.sort > fm.sort) {
				return 1;
			} else {
				return -1;
			}

		}
		return this.sort.compareTo(fm.sort);
	}

}

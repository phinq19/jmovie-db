/**
 * 
 */
package com.lars_albrecht.mdb.main.core.models;

import java.util.HashMap;

/**
 * This interface contains the persist methods to persist a model.
 * 
 * @author lalbrecht
 * 
 */
public interface IPersistable {

	Object fromHashMap(final HashMap<String, Object> map);

	String getDatabaseTable();

	public Integer getId();

	public void setId(final Integer id);

	HashMap<String, Object> toHashMap();

}

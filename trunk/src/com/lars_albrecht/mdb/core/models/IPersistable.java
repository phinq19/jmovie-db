/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.HashMap;

/**
 * @author albrela
 * 
 */
public interface IPersistable {

	Object fromHashMap(final HashMap<String, Object> map);

	String getDatabaseTable();

	public Integer getId();

	public void setId(final Integer id);

	HashMap<String, Object> toHashMap();

}

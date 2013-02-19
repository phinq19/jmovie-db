/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lars_albrecht.mdb.core.finder.models.IPersistable;

/**
 * @author albrela
 * 
 */
public class DataHandler {

	public void persist(final IPersistable object) {
		final HashMap<String, Object> tempObject = object.toHashMap();
		if (tempObject != null && tempObject.size() > 0) {
			String sql = "";

			for (final Map.Entry<String, Object> entry : tempObject.entrySet()) {
				sql = "INSERT into ... ON DUPLICATE KEY ... ";
			}
		}
	}

	public void persist(final ArrayList<IPersistable> objects) {
		for (final IPersistable object : objects) {
			this.persist(object);
		}
	}
}

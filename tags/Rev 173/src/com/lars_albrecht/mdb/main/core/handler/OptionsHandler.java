/**
 * 
 */
package com.lars_albrecht.mdb.main.core.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.mdb.main.database.DB;

// TODO create defaults to reduce multiple code in classes.

/**
 * @author lalbrecht
 * 
 */
public class OptionsHandler {

	private static HashMap<String, Object>	optionCache	= new HashMap<String, Object>();

	public static Object getOption(final String optionName) {
		if (OptionsHandler.optionCache.containsKey(optionName)) {
			return OptionsHandler.optionCache.get(optionName);
		}
		Object result = null;

		ResultSet rs = null;

		final String sql = "SELECT o.value AS value FROM options AS o WHERE o.name = ? LIMIT 1;";

		final ConcurrentHashMap<Integer, Object> values = new ConcurrentHashMap<Integer, Object>();
		values.put(1, optionName);
		try {
			rs = DB.queryPS(sql, values);
			if (rs.next()) {
				result = rs.getObject("value");
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		OptionsHandler.optionCache.put(optionName, result);

		return result;
	}

	public static void setOption(final String optionName, final Object optionValue) {

		final ConcurrentHashMap<Integer, Object> values = new ConcurrentHashMap<Integer, Object>();
		values.put(1, optionName);
		values.put(2, optionValue);
		try {
			DB.updatePS("REPLACE INTO options (name, value) VALUES (?, ?)", values);
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		OptionsHandler.optionCache.put(optionName, optionValue);
	}

}

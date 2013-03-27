/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.mdb.database.DB;

/**
 * @author lalbrecht
 * 
 */
public class OptionsHandler {

	public static Object getOption(final String optionName) {
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

		return result;
	}

	public static void setOption(final String optionName, final Object optionValue) {

		final ConcurrentHashMap<Integer, Object> values = new ConcurrentHashMap<Integer, Object>();
		values.put(1, optionName);
		values.put(2, optionValue);
		try {
			DB.updatePS("INSERT OR IGNORE INTO options (name, value) VALUES (?, ?)", values);
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}

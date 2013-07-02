/**
 * 
 */
package com.lars_albrecht.mdb.main.core.interfaces.web.pages.abstracts;

import com.lars_albrecht.mdb.main.core.models.KeyValue;

/**
 * @author lalbrecht
 * 
 */
public abstract class AbstractFileDetailsOutputItem {

	protected final String getDefault(final String infoType,
			final String sectionName,
			final KeyValue<String, Object> keyValue,
			final String value) {
		return value;
	}

	public abstract String getValue(final String infoType,
			final String sectionName,
			final KeyValue<String, Object> keyValue,
			final String value);

}

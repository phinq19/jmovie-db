/**
 * 
 */
package com.lars_albrecht.moviedb.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class FieldList extends ArrayList<FieldModel> {

	/**
	 * 
	 * @param field
	 * @return Integer
	 */
	public Integer fieldInList(final Field field) {
		for(int i = 0; i < this.size(); i++) {
			if(this.get(i).getField().equals(field)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param name
	 * @return Integer
	 */
	public Integer fieldNameInList(final String name) {
		for(int i = 0; i < this.size(); i++) {
			if(this.get(i).getField().getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param name
	 * @return Integer
	 */
	public Integer fieldNameInAsList(final String name) {
		for(int i = 0; i < this.size(); i++) {
			if(this.get(i).getAs().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public String toStringFromType(final Class<?>[] cArr, final String prefix, final String suffix) {
		final List<?> cList = Arrays.asList(cArr);
		String temp = "";
		for(int i = 0; i < this.size(); i++) {
			if(cList.indexOf(this.get(i).getField().getType()) > -1) {
				if(!temp.equals("")) {
					temp += ", ";
				}
				temp += (prefix != null ? prefix : "") + this.get(i).getAs() + (suffix != null ? suffix : "");
			}
		}
		return temp;
	}

}

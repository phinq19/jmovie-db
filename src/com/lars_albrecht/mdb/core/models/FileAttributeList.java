/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.ArrayList;

/**
 * @author albrela
 * 
 */
public class FileAttributeList {

	private Integer								id			= null;
	private ArrayList<KeyValue<String, Object>>	keyValues	= new ArrayList<KeyValue<String, Object>>();
	private String								sectionName	= null;
	private int									hash		= -1;

	public FileAttributeList generateHash() {
		this.hash = this.keyValues.hashCode();
		return this;
	}

	/**
	 * @param id
	 * @param keyValues
	 * @param hash
	 */
	public FileAttributeList(final Integer id,
			final ArrayList<KeyValue<String, Object>> keyValues,
			final String sectionName, final int hash) {
		super();
		this.id = id;
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.hash = hash;
	}

	/**
	 * @param keyValues
	 * @param hash
	 */
	public FileAttributeList(
			final ArrayList<KeyValue<String, Object>> keyValues,
			final String sectionName, final int hash) {
		super();
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.hash = hash;
	}

	/**
	 * @param keyValues
	 */
	public FileAttributeList(
			final ArrayList<KeyValue<String, Object>> keyValues,
			final String sectionName) {
		super();
		this.keyValues = keyValues;
		this.sectionName = sectionName;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the keyValues
	 */
	public ArrayList<KeyValue<String, Object>> getKeyValues() {
		return this.keyValues;
	}

	/**
	 * @param keyValues
	 *            the keyValues to set
	 */
	public void setKeyValues(final ArrayList<KeyValue<String, Object>> keyValues) {
		this.keyValues = keyValues;
	}

	/**
	 * @return the hash
	 */
	public int getHash() {
		return this.hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(final int hash) {
		this.hash = hash;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return this.sectionName;
	}

	/**
	 * @param sectionName
	 *            the sectionName to set
	 */
	public void setSectionName(final String sectionName) {
		this.sectionName = sectionName;
	}

	// @Override
	// public HashMap<String, Object> toHashMap() {
	// final HashMap<String, Object> tempHashMap = new HashMap<String,
	// Object>();
	// tempHashMap.put("id", this.getId());
	// tempHashMap.put("keyValue", this.getKeyValues());
	// tempHashMap.put("hash", this.getHash());
	// tempHashMap.put("sectionName", this.getSectionName());
	//
	// return tempHashMap;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public Object fromHashMap(final HashMap<String, Object> map) {
	// if (map.containsKey("id")) {
	// this.setId((Integer) map.get("id"));
	// }
	// if (map.containsKey("keyValue")
	// && (map.get("keyValue") instanceof ArrayList)) {
	// this.keyValues = new ArrayList<KeyValue<String, Object>>(
	// (ArrayList<KeyValue<String, Object>>) map.get("keyValue"));
	// }
	// if (map.containsKey("hash")) {
	// this.setHash((Integer) (map.get("hash")));
	// }
	// if (map.containsKey("sectionName")) {
	// this.setSectionName((String) (map.get("sectionName")));
	// }
	//
	// return this;
	// }
	//
	// @Override
	// public String getDatabaseTable() {
	// return "typeInformation";
	// }

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "KeyValues: " + this.keyValues
				+ " | " + "Hash: " + this.hash;
	}

}

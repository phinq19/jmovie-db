/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author albrela
 * 
 */
public class FileAttributeList {

	private Integer								id			= null;
	private ArrayList<KeyValue<String, Object>>	keyValues	= new ArrayList<KeyValue<String, Object>>();
	private String								sectionName	= null;
	private int									hash		= -1;
	private Integer								fileId		= null;

	/**
	 * 
	 */
	public FileAttributeList() {
		super();
	}

	/**
	 * @param id
	 * @param keyValues
	 * @param hash
	 */
	public FileAttributeList(final Integer id, final ArrayList<KeyValue<String, Object>> keyValues, final String sectionName,
			final int hash, final Integer fileId) {
		super();
		this.id = id;
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.hash = hash;
		this.fileId = fileId;
	}

	/**
	 * @param keyValues
	 * @param hash
	 */
	public FileAttributeList(final ArrayList<KeyValue<String, Object>> keyValues, final String sectionName, final int hash,
			final Integer fileId) {
		super();
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.hash = hash;
		this.fileId = fileId;
	}

	/**
	 * @param keyValues
	 */
	public FileAttributeList(final ArrayList<KeyValue<String, Object>> keyValues, final String sectionName, final Integer fileId) {
		super();
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.fileId = fileId;
	}

	public FileAttributeList generateHash() {
		this.hash = this.keyValues.hashCode();
		return this;
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
	 * @return the fileId
	 */
	public Integer getFileId() {
		return this.fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
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

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "KeyValues: " + this.keyValues + " | " + "Hash: " + this.hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		final FileAttributeList tempList = new FileAttributeList();
		tempList.fileId = this.fileId;
		tempList.hash = this.hash;
		tempList.id = this.id;
		tempList.keyValues.addAll((Collection<? extends KeyValue<String, Object>>) this.keyValues.clone());
		tempList.sectionName = this.sectionName;

		return tempList;
	}

}

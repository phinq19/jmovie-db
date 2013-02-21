/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This model class holds a list of KeyValues.
 * 
 * @author lalbrecht
 * 
 */
public class FileAttributeList {

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

	/**
	 * @param id
	 * @param keyValues
	 * @param hash
	 */
	public FileAttributeList(final Integer id, final ArrayList<KeyValue<String, Object>> keyValues, final String sectionName,
			final int hash, final Integer fileId) {
		super();
		this.keyValues = keyValues;
		this.sectionName = sectionName;
		this.hash = hash;
		this.fileId = fileId;
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
		tempList.keyValues.addAll((Collection<? extends KeyValue<String, Object>>) this.keyValues.clone());
		tempList.sectionName = this.sectionName;

		return tempList;
	}

	public FileAttributeList generateHash() {
		this.hash = this.keyValues.hashCode();
		return this;
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return this.fileId;
	}

	/**
	 * @return the hash
	 */
	public int getHash() {
		return this.hash;
	}

	/**
	 * @return the keyValues
	 */
	public ArrayList<KeyValue<String, Object>> getKeyValues() {
		return this.keyValues;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return this.sectionName;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(final int hash) {
		this.hash = hash;
	}

	/**
	 * @param keyValues
	 *            the keyValues to set
	 */
	public void setKeyValues(final ArrayList<KeyValue<String, Object>> keyValues) {
		this.keyValues = keyValues;
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
		return "KeyValues: " + this.keyValues + " | " + "Hash: " + this.hash;
	}

}

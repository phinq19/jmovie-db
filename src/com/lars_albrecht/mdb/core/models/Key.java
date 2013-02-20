/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.HashMap;

/**
 * @author albrela
 * 
 * @see "http://blog.buhbuhbuh.de/buhbuhbuh/entry/equals_und_hashcode_richtig_%C3%BCberschreiben"
 * 
 */
public class Key<K> implements IPersistable {

	private Integer	id			= null;
	private K		key			= null;
	private String	infoType	= null;
	private String	section		= null;
	private Boolean	editable	= null;

	public Key() {
	}

	/**
	 * @param id
	 * @param key
	 * @param infoType
	 * @param section
	 * @param editable
	 */
	public Key(final Integer id, final K key, final String infoType, final String section, final Boolean editable) {
		super();
		this.id = id;
		this.key = key;
		this.infoType = infoType;
		this.section = section;
		this.editable = editable;
	}

	/**
	 * @param key
	 * @param infoType
	 * @param section
	 * @param editable
	 */
	public Key(final K key, final String infoType, final String section, final Boolean editable) {
		super();
		this.key = key;
		this.infoType = infoType;
		this.section = section;
		this.editable = editable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object fromHashMap(final HashMap<String, Object> map) {
		final Key<K> result = new Key<K>();
		if (map.containsKey("id")) {
			result.setId((Integer) map.get("id"));
		}
		if (map.containsKey("key")) {
			result.setKey((K) map.get("key"));
		}
		if (map.containsKey("infoType")) {
			result.setInfoType((String) map.get("infoType"));
		}

		if (map.containsKey("section")) {
			result.setSection((String) map.get("section"));
		}

		return result;
	}

	@Override
	public String getDatabaseTable() {
		return "typeInformation_key";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the infoType
	 */
	public String getInfoType() {
		return this.infoType;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return this.section;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @param infoType
	 *            the infoType to set
	 */
	public void setInfoType(final String infoType) {
		this.infoType = infoType;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(final K key) {
		this.key = key;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(final String section) {
		this.section = section;
	}

	@Override
	public HashMap<String, Object> toHashMap() {
		final HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
		if (this.getId() != null) {
			tempHashMap.put("id", this.getId());
		}
		tempHashMap.put("key", this.getKey());
		tempHashMap.put("infoType", this.getInfoType());
		tempHashMap.put("section", this.getSection());

		return tempHashMap;
	}

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "Key: " + this.key + " | " + "InfoType: " + this.infoType + " | " + "Section: " + this.section;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.editable == null) ? 0 : this.editable.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
		result = prime * result + ((this.section == null) ? 0 : this.section.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Key)) {
			return false;
		}
		final Key<?> other = (Key<?>) obj;
		if ((this.id != null) && (other.id != null) && !this.id.equals(other.id)) {
			return false;
		}
		if (this.editable == null) {
			if (other.editable != null) {
				return false;
			}
		} else if (!this.editable.equals(other.editable)) {
			return false;
		}
		if (this.infoType == null) {
			if (other.infoType != null) {
				return false;
			}
		} else if (!this.infoType.equals(other.infoType)) {
			return false;
		}
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
			return false;
		}
		if (this.section == null) {
			if (other.section != null) {
				return false;
			}
		} else if (!this.section.equals(other.section)) {
			return false;
		}
		return true;
	}

}

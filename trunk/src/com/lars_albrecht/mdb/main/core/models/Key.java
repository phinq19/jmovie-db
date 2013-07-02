/**
 * 
 */
package com.lars_albrecht.mdb.main.core.models;

import java.util.HashMap;

/**
 * 
 * This model class holds the key.
 * 
 * @author lalbrecht
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
	private Boolean	searchable	= null;

	public Key() {
	}

	/**
	 * @param id
	 * @param key
	 * @param infoType
	 * @param section
	 * @param editable
	 * @param searchable
	 */
	public Key(final Integer id, final K key, final String infoType, final String section, final Boolean editable, final Boolean searchable) {
		super();
		this.id = id;
		this.key = key;
		this.infoType = infoType;
		this.section = section;
		this.editable = editable;
		this.searchable = searchable;
	}

	/**
	 * @param key
	 * @param infoType
	 * @param section
	 * @param editable
	 * @param searchable
	 */
	public Key(final K key, final String infoType, final String section, final Boolean editable, final Boolean searchable) {
		super();
		this.key = key;
		this.infoType = infoType;
		this.section = section;
		this.editable = editable;
		this.searchable = searchable;
	}

	/**
	 * @return the searchable
	 */
	public Boolean getSearchable() {
		return this.searchable;
	}

	/**
	 * @param searchable
	 *            the searchable to set
	 */
	public void setSearchable(final Boolean searchable) {
		this.searchable = searchable;
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
		if (this.searchable == null) {
			if (other.searchable != null) {
				return false;
			}
		} else if (!this.searchable.equals(other.searchable)) {
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

		if (map.containsKey("editable")) {
			if (map.get("editable") instanceof Integer) {
				final Integer tempEdit = (Integer) map.get("editable");
				result.setEditable(tempEdit == 0 ? false : true);
			} else if (map.get("editable") instanceof Boolean) {
				result.setEditable((Boolean) map.get("editable"));
			}
		}

		if (map.containsKey("searchable")) {
			if (map.get("searchable") instanceof Integer) {
				final Integer tempSearch = (Integer) map.get("searchable");
				result.setSearchable(tempSearch == 0 ? false : true);
			} else if (map.get("searchable") instanceof Boolean) {
				result.setSearchable((Boolean) map.get("searchable"));
			}
		}

		return result;
	}

	@Override
	public String getDatabaseTable() {
		return "typeInformation_key";
	}

	/**
	 * @return the editable
	 */
	public Boolean getEditable() {
		return this.editable;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.editable == null) ? 0 : this.editable.hashCode());
		result = (prime * result) + ((this.searchable == null) ? 0 : this.searchable.hashCode());
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.infoType == null) ? 0 : this.infoType.hashCode());
		result = (prime * result) + ((this.key == null) ? 0 : this.key.hashCode());
		result = (prime * result) + ((this.section == null) ? 0 : this.section.hashCode());
		return result;
	}

	/**
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(final Boolean editable) {
		this.editable = editable;
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
		tempHashMap.put("editable", this.getEditable());
		tempHashMap.put("searchable", this.getSearchable());

		return tempHashMap;
	}

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "Key: " + this.key + " | " + "InfoType: " + this.infoType + " | " + "Section: " + this.section
				+ " | " + "Editable: " + this.editable + " | " + "Searchable: " + this.searchable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		final Key<K> tempKey = new Key<K>();
		tempKey.editable = this.editable;
		tempKey.id = this.id;
		tempKey.infoType = this.infoType;
		tempKey.key = this.key;
		tempKey.searchable = this.searchable;
		tempKey.section = this.section;
		return tempKey;
	}

}

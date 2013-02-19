/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.HashMap;

/**
 * @author albrela
 * 
 */
public class TypeInformation implements IPersistable {

	private Integer	id		= null;
	private Integer	fileId	= null;
	private Integer	keyId	= null;
	private Integer	valueId	= null;

	public TypeInformation() {
	}

	/**
	 * @param fileId
	 * @param keyId
	 * @param valueId
	 */
	public TypeInformation(final Integer fileId, final Integer keyId, final Integer valueId) {
		super();
		this.fileId = fileId;
		this.keyId = keyId;
		this.valueId = valueId;
	}

	/**
	 * @param id
	 * @param fileId
	 * @param keyId
	 * @param valueId
	 */
	public TypeInformation(final Integer id, final Integer fileId, final Integer keyId, final Integer valueId) {
		super();
		this.id = id;
		this.fileId = fileId;
		this.keyId = keyId;
		this.valueId = valueId;
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
		if (!(obj instanceof TypeInformation)) {
			return false;
		}
		final TypeInformation other = (TypeInformation) obj;
		if ((this.id != null) && (other.id != null) && !this.id.equals(other.id)) {
			return false;
		}
		if (this.fileId == null) {
			if (other.fileId != null) {
				return false;
			}
		} else if (!this.fileId.equals(other.fileId)) {
			return false;
		}
		if (this.keyId == null) {
			if (other.keyId != null) {
				return false;
			}
		} else if (!this.keyId.equals(other.keyId)) {
			return false;
		}
		if (this.valueId == null) {
			if (other.valueId != null) {
				return false;
			}
		} else if (!this.valueId.equals(other.valueId)) {
			return false;
		}
		return true;
	}

	@Override
	public Object fromHashMap(final HashMap<String, Object> map) {
		final TypeInformation result = new TypeInformation();
		if (map.containsKey("id")) {
			result.setId((Integer) map.get("id"));
		}
		if (map.containsKey("file_id")) {
			result.setFileId((Integer) map.get("file_id"));
		}
		if (map.containsKey("key_id")) {
			result.setKeyId((Integer) map.get("key_id"));
		}
		if (map.containsKey("value_id")) {
			result.setValueId((Integer) map.get("value_id"));
		}
		return result;
	}

	@Override
	public String getDatabaseTable() {
		return "typeInformation";
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return this.fileId;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the keyId
	 */
	public Integer getKeyId() {
		return this.keyId;
	}

	/**
	 * @return the valueId
	 */
	public Integer getValueId() {
		return this.valueId;
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
		result = (prime * result) + ((this.fileId == null) ? 0 : this.fileId.hashCode());
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.keyId == null) ? 0 : this.keyId.hashCode());
		result = (prime * result) + ((this.valueId == null) ? 0 : this.valueId.hashCode());
		return result;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @param keyId
	 *            the keyId to set
	 */
	public void setKeyId(final Integer keyId) {
		this.keyId = keyId;
	}

	/**
	 * @param valueId
	 *            the valueId to set
	 */
	public void setValueId(final Integer valueId) {
		this.valueId = valueId;
	}

	@Override
	public HashMap<String, Object> toHashMap() {
		final HashMap<String, Object> tempHashMap = new HashMap<String, Object>();

		if (this.getId() != null) {
			tempHashMap.put("id", this.id);
		}

		tempHashMap.put("file_id", this.fileId);
		tempHashMap.put("key_id", this.keyId);
		tempHashMap.put("value_id", this.valueId);

		return tempHashMap;
	}

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "FileId: " + this.fileId + " | " + "KeyId: " + this.keyId + " | " + "ValueId: " + this.valueId;
	}

}

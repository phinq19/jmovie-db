/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.util.HashMap;

/**
 * @author lalbrecht
 * 
 */
public class FileTag implements IPersistable {

	private Integer	id		= null;
	private Integer	fileId	= null;
	private Integer	tagId	= null;
	private Boolean	isUser	= null;

	public FileTag() {
	}

	/**
	 * @param id
	 * @param fileId
	 * @param tagId
	 * @param isUser
	 */
	public FileTag(final Integer id, final Integer fileId, final Integer tagId, final Boolean isUser) {
		super();
		this.id = id;
		this.fileId = fileId;
		this.tagId = tagId;
		this.isUser = isUser;
	}

	/**
	 * @param fileId
	 * @param tagId
	 * @param isUser
	 */
	public FileTag(final Integer fileId, final Integer tagId, final Boolean isUser) {
		super();
		this.fileId = fileId;
		this.tagId = tagId;
		this.isUser = isUser;
	}

	/**
	 * @return the id
	 */
	@Override
	public final Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the fileId
	 */
	public final Integer getFileId() {
		return this.fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public final void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the tagId
	 */
	public final Integer getTagId() {
		return this.tagId;
	}

	/**
	 * @param tagId
	 *            the tagId to set
	 */
	public final void setTagId(final Integer tagId) {
		this.tagId = tagId;
	}

	/**
	 * @return the isUser
	 */
	public final Boolean getIsUser() {
		return this.isUser;
	}

	/**
	 * @param isUser
	 *            the isUser to set
	 */
	public final void setIsUser(final Boolean isUser) {
		this.isUser = isUser;
	}

	@Override
	public Object fromHashMap(final HashMap<String, Object> map) {
		final FileTag result = new FileTag();
		if (map.containsKey("id")) {
			result.setId((Integer) map.get("id"));
		}
		if (map.containsKey("file_id")) {
			result.setFileId((Integer) map.get("file_id"));
		}
		if (map.containsKey("tag_id")) {
			result.setTagId((Integer) map.get("tag_id"));
		}
		if (map.containsKey("isuser")) {
			result.setIsUser(map.get("isuser") instanceof Integer ? (Integer) map.get("isuser") == 0 ? false : true : (Boolean) map
					.get("isuser"));
		}

		return result;
	}

	@Override
	public String getDatabaseTable() {
		return "fileTags";
	}

	@Override
	public HashMap<String, Object> toHashMap() {
		final HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
		if (this.getId() != null) {
			tempHashMap.put("id", this.getId());
		}
		tempHashMap.put("file_id", this.getFileId());
		tempHashMap.put("tag_id", this.getTagId());
		tempHashMap.put("isuser", this.getIsUser());

		return tempHashMap;
	}

	@Override
	public String toString() {
		return this.id + " | " + this.fileId + " | " + this.tagId + " | " + this.isUser;
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
		if (this.id == null) {
			result = prime * result + ((this.fileId == null) ? 0 : this.fileId.hashCode());
			result = prime * result + ((this.isUser == null) ? 0 : this.isUser.hashCode());
			result = prime * result + ((this.tagId == null) ? 0 : this.tagId.hashCode());
		} else {
			result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		}
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
		if (!(obj instanceof FileTag)) {
			return false;
		}
		final FileTag other = (FileTag) obj;
		if (this.fileId == null) {
			if (other.fileId != null) {
				return false;
			}
		} else if (!this.fileId.equals(other.fileId)) {
			return false;
		}
		if ((this.id != null) && (other.id != null) && !this.id.equals(other.id)) {
			return false;
		} else if ((this.id != null) && (other.id != null) && this.id.equals(other.id)) {
			return true;
		}
		if (this.isUser == null) {
			if (other.isUser != null) {
				return false;
			}
		} else if (!this.isUser.equals(other.isUser)) {
			return false;
		}
		if (this.tagId == null) {
			if (other.tagId != null) {
				return false;
			}
		} else if (!this.tagId.equals(other.tagId)) {
			return false;
		}
		return true;
	}

}

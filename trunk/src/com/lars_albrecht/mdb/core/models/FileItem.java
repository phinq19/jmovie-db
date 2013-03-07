/**
 * 
 */
package com.lars_albrecht.mdb.core.models;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.lars_albrecht.general.utilities.ChecksumSHA1;

/**
 * This model class holds the FileItem itself.
 * 
 * @author lalbrecht
 * 
 */
public class FileItem implements IPersistable {

	private Integer							id			= null;
	private String							name		= null;
	private String							fullpath	= null;
	private String							dir			= null;
	private Long							size		= null;
	private String							ext			= null;
	private String							filehash	= null;
	private ArrayList<FileAttributeList>	attributes	= null;
	private Integer							createTS	= null;
	private String							filetype	= null;

	/**
	 * 
	 */
	public FileItem() {
		super();
		this.attributes = new ArrayList<FileAttributeList>();
	}

	/**
	 * @param id
	 * @param name
	 * @param fullpath
	 * @param dir
	 * @param size
	 * @param ext
	 * @param filetype
	 * @param createTS
	 */
	public FileItem(final Integer id, final String name, final String fullpath, final String dir, final Long size, final String ext,
			final String filetype, final Integer createTS) {
		super();
		this.id = id;
		this.name = name;
		this.fullpath = fullpath;
		this.dir = dir;
		this.size = size;
		this.ext = ext;
		this.filetype = filetype;
		this.createTS = createTS;
	}

	/**
	 * @param fullpath
	 */
	public FileItem(final String fullpath) {
		super();
		this.fullpath = fullpath;
	}

	/**
	 * @param name
	 * @param fullpath
	 * @param dir
	 * @param size
	 * @param ext
	 * @param createTS
	 */
	public FileItem(final String name, final String fullpath, final String dir, final Long size, final String ext, final Integer createTS) {
		super();
		this.name = name;
		this.fullpath = fullpath;
		this.dir = dir;
		this.size = size;
		this.ext = ext;
		this.createTS = createTS;
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
		if (!(obj instanceof FileItem)) {
			return false;
		}
		final FileItem other = (FileItem) obj;
		if ((this.id != null) && (other.id != null) && !this.id.equals(other.id)) {
			return false;
		} else if ((this.id != null) && (other.id != null) && this.id.equals(other.id)) {
			return true;
		}
		if (this.fullpath == null) {
			if (other.fullpath != null) {
				return false;
			}
		} else if (!this.fullpath.equals(other.fullpath)) {
			return false;
		} else if (this.fullpath.equals(other.fullpath)) {
			return true;
		}

		/*
		 * if (this.attributes == null) { if (other.attributes != null) { return
		 * false; } } else if (!this.attributes.equals(other.attributes)) {
		 * return false; }
		 */
		if (this.createTS == null) {
			if (other.createTS != null) {
				return false;
			}
		} else if (!this.createTS.equals(other.createTS)) {
			return false;
		}
		if (this.dir == null) {
			if (other.dir != null) {
				return false;
			}
		} else if (!this.dir.equals(other.dir)) {
			return false;
		}
		if (this.filetype == null) {
			if (other.filetype != null) {
				return false;
			}
		} else if (!this.filetype.equals(other.filetype)) {
			return false;
		}
		if (this.ext == null) {
			if (other.ext != null) {
				return false;
			}
		} else if (!this.ext.equals(other.ext)) {
			return false;
		}
		if (this.filehash == null) {
			if (other.filehash != null) {
				return false;
			}
		} else if (!this.filehash.equals(other.filehash)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.size == null) {
			if (other.size != null) {
				return false;
			}
		} else if (!this.size.equals(other.size)) {
			return false;
		}
		return true;
	}

	@Override
	public Object fromHashMap(final HashMap<String, Object> map) {
		final FileItem result = new FileItem();
		if (map.containsKey("id")) {
			result.setId((Integer) map.get("id"));
		}
		if (map.containsKey("name")) {
			result.setName((String) map.get("name"));
		}
		if (map.containsKey("fullpath")) {
			result.setFullpath((String) map.get("fullpath"));
		}

		if (map.containsKey("dir")) {
			result.setDir((String) map.get("dir"));
		}

		if (map.containsKey("size")) {
			if (map.get("size") instanceof Integer) {
				result.setSize(((Integer) map.get("size")).longValue());
			} else {
				result.setSize((Long) map.get("size"));
			}
		}

		if (map.containsKey("ext")) {
			result.setExt((String) map.get("ext"));
		}

		if (map.containsKey("filetype") && (map.get("filetype") != "") && !map.get("filetype").equals("")) {
			result.setFiletype((String) map.get("filetype"));
		}

		if (map.containsKey("filehash") && (map.get("filehash") != "") && !map.get("filehash").equals("")) {
			result.setFilehash((String) map.get("filehash"));
		}

		if (map.containsKey("createTS")) {
			final SimpleDateFormat sdfToDate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			try {
				result.setCreateTS(((Long) sdfToDate.parse((String) map.get("createTS")).getTime()).intValue());
			} catch (final ParseException e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	public FileItem generateFilehash() throws Exception {
		if ((this.getFullpath() != null) && new File(this.getFullpath()).exists()) {
			this.setFilehash(ChecksumSHA1.getSHA1Checksum(this.getFullpath()));
		}
		return this;
	}

	public ArrayList<FileAttributeList> getAttributes() {
		return this.attributes;
	}

	/**
	 * @return the createTS
	 */
	public Integer getCreateTS() {
		return this.createTS;
	}

	@Override
	public String getDatabaseTable() {
		return "fileInformation";
	}

	public String getDir() {
		return this.dir;
	}

	public String getExt() {
		return this.ext;
	}

	/**
	 * @return the filehash
	 */
	public String getFilehash() {
		return this.filehash;
	}

	public String getFullpath() {
		return this.fullpath;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Long getSize() {
		return this.size;
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
			// result = prime * result + ((this.attributes == null) ? 0 :
			// this.attributes.hashCode());
			result = (prime * result) + ((this.createTS == null) ? 0 : this.createTS.hashCode());
			result = (prime * result) + ((this.dir == null) ? 0 : this.dir.hashCode());
			result = (prime * result) + ((this.ext == null) ? 0 : this.ext.hashCode());
			result = (prime * result) + ((this.filehash == null) ? 0 : this.filehash.hashCode());
			result = (prime * result) + ((this.fullpath == null) ? 0 : this.fullpath.hashCode());
			result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
			result = (prime * result) + ((this.size == null) ? 0 : this.size.hashCode());
			result = (prime * result) + ((this.filetype == null) ? 0 : this.filetype.hashCode());
		} else {
			result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		}
		return result;
	}

	public void setAttributes(final ArrayList<FileAttributeList> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @param createTS
	 *            the createTS to set
	 */
	public void setCreateTS(final Integer createTS) {
		this.createTS = createTS;
	}

	public void setDir(final String dir) {
		this.dir = dir;
	}

	public void setExt(final String ext) {
		this.ext = ext;
	}

	/**
	 * @param filehash
	 *            the filehash to set
	 */
	public void setFilehash(final String filehash) {
		this.filehash = filehash;
	}

	public void setFullpath(final String fullpath) {
		this.fullpath = fullpath;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSize(final Long size) {
		this.size = size;
	}

	/**
	 * @return the filetype
	 */
	public String getFiletype() {
		return this.filetype;
	}

	/**
	 * @param filetype
	 *            the filetype to set
	 */
	public void setFiletype(final String filetype) {
		this.filetype = filetype;
	}

	@Override
	public HashMap<String, Object> toHashMap() {
		final HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
		if (this.getId() != null) {
			tempHashMap.put("id", this.getId());
		}
		tempHashMap.put("name", this.getName());
		tempHashMap.put("fullpath", this.getFullpath());
		tempHashMap.put("dir", this.getDir());
		tempHashMap.put("size", this.getSize());
		tempHashMap.put("ext", this.getExt());
		tempHashMap.put("filehash", this.getFilehash());
		tempHashMap.put("filetype", this.getFiletype());

		if (this.getCreateTS() != null) {
			tempHashMap.put("createTS", this.getCreateTS());
		}

		return tempHashMap;
	}

	@Override
	public String toString() {
		return "Id: " + this.id + " | " + "Name: " + this.name + " | " + "Fullpath: " + this.fullpath + " | " + "Dir: " + this.dir + " | "
				+ "Size: " + this.size + " | " + "Ext: " + this.ext + " | " + this.getAttributes();
	}

}

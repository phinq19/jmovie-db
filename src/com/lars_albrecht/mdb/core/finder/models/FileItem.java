/**
 * 
 */
package com.lars_albrecht.mdb.core.finder.models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author albrela
 * 
 */
public class FileItem implements IPersistable {

	private Integer						id			= null;
	private String						name		= null;
	private File						file		= null;
	private ArrayList<FileAttribute>	attributes	= null;

	public FileItem() {
	}

	/**
	 * @param id
	 * @param name
	 * @param file
	 */
	public FileItem(final Integer id, final String name, final File file) {
		super();
		this.id = id;
		this.name = name;
		this.file = file;
	}

	/**
	 * @param id
	 * @param name
	 * @param file
	 * @param attributes
	 */
	public FileItem(final Integer id, final String name, final File file, final ArrayList<FileAttribute> attributes) {
		super();
		this.id = id;
		this.name = name;
		this.file = file;
		this.attributes = attributes;
	}

	/**
	 * @param name
	 * @param file
	 */
	public FileItem(final String name, final File file) {
		super();
		this.name = name;
		this.file = file;
	}

	/**
	 * @param name
	 * @param file
	 * @param attributes
	 */
	public FileItem(final String name, final File file, final ArrayList<FileAttribute> attributes) {
		super();
		this.name = name;
		this.file = file;
		this.attributes = attributes;
	}

	@Override
	public Object fromHashMap(final HashMap<String, Object> map) {
		if (map.containsKey("id")) {
			this.setId((Integer) map.get("id"));
		}
		if (map.containsKey("name")) {
			this.setName((String) map.get("name"));
		}
		if (map.containsKey("file")) {
			this.setFile(new File((String) map.get("file")));
		}

		return this;
	}

	public ArrayList<FileAttribute> getAttributes() {
		return this.attributes;
	}

	public File getFile() {
		return this.file;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setAttributes(final ArrayList<FileAttribute> attributes) {
		this.attributes = attributes;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public HashMap<String, Object> toHashMap() {
		final HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
		tempHashMap.put("id", this.getId());
		tempHashMap.put("name", this.getName());
		tempHashMap.put("file", this.file.getPath());

		return tempHashMap;
	}
}

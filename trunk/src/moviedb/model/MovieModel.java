/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package moviedb.model;

import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author lalbrecht
 */
public class MovieModel {
	private Boolean ac3 = null;
	private Boolean dts = null;
	private String filepath = null;
	private String filetype = null;
	private String hdType = null;
	private HashMap<String, String> movieMap = null;
	private Vector<String> movieVector = null;
	private Boolean R5 = null;
	private String subTitle = null;
	private String title = null;
	private String version = null;

	private Boolean x264 = null;
	private Integer year = null;

	/**
     * 
     */
	public MovieModel() {
		this.movieVector = new Vector<String>();
		this.movieMap = new HashMap<String, String>();
	}

	/**
	 * @return the ac3
	 */
	public Boolean getAc3 () {
		return this.ac3;
	}

	/**
	 * @return the dts
	 */
	public Boolean getDts () {
		return this.dts;
	}

	/**
	 * @return the filepath
	 */
	public final String getFilepath () {
		return this.filepath;
	}

	/**
	 * @return the filetype
	 */
	public String getFiletype () {
		return this.filetype;
	}

	/**
	 * @return the hdType
	 */
	public String getHdType () {
		return this.hdType;
	}

	/**
	 * @return the movieVector
	 */
	public final Vector<String> getMovieVector () {
		this.movieVector.add(this.movieMap.get("title") != null ? this.movieMap.get("title") : "");
		this.movieVector.add(this.movieMap.get("subTitle") != null ? this.movieMap.get("subTitle")
				: "");
		this.movieVector.add(this.movieMap.get("version") != null ? this.movieMap.get("version")
				: "");
		this.movieVector.add(this.movieMap.get("year") != null ? this.movieMap.get("year") : "");
		this.movieVector
				.add(this.movieMap.get("hdType") != null ? this.movieMap.get("hdType") : "");
		this.movieVector.add(this.movieMap.get("filepath") != null ? this.movieMap.get("filepath")
				: "");
		return this.movieVector;
	}

	/**
	 * @return the r5
	 */
	public Boolean getR5 () {
		return this.R5;
	}

	/**
	 * @return the subTitle
	 */
	public String getSubTitle () {
		return this.subTitle;
	}

	/**
	 * @return the title
	 */
	public String getTitle () {
		return this.title;
	}

	/**
	 * @return the version
	 */
	public String getVersion () {
		return this.version;
	}

	/**
	 * @return the x264
	 */
	public Boolean getX264 () {
		return this.x264;
	}

	/**
	 * @return the year
	 */
	public Integer getYear () {
		return this.year;
	}

	/**
	 * @param ac3
	 *            the ac3 to set
	 */
	public void setAc3 (final Boolean ac3) {
		this.updateHashMap("ac3", Boolean.toString(ac3));

		this.ac3 = ac3;
	}

	/**
	 * @param dts
	 *            the dts to set
	 */
	public void setDts (final Boolean dts) {
		this.updateHashMap("dts", Boolean.toString(dts));
		this.dts = dts;
	}

	/**
	 * @param filepath
	 *            the filepath to set
	 */
	public final void setFilepath (final String filepath) {
		this.updateHashMap("filepath", filepath);
		this.filepath = filepath;
	}

	/**
	 * @param filetype
	 *            the filetype to set
	 */
	public void setFiletype (final String filetype) {
		this.updateHashMap("filetype", filetype);
		this.filetype = filetype;
	}

	/**
	 * @param hdType
	 *            the hdType to set
	 */
	public void setHdType (final String hdType) {
		this.updateHashMap("hdType", hdType);

		this.hdType = hdType;
	}

	/**
	 * @param r5
	 *            the r5 to set
	 */
	public void setR5 (final Boolean r5) {
		this.updateHashMap("r5", Boolean.toString(r5));
		this.R5 = r5;
	}

	/**
	 * @param subTitle
	 *            the subTitle to set
	 */
	public void setSubTitle (final String subTitle) {
		if (this.subTitle != null) {
			this.subTitle += " " + subTitle;
		}
		this.updateHashMap("subTitle", subTitle);

		this.subTitle = subTitle;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle (final String title) {
		this.updateHashMap("title", title);

		this.title = title;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion (final String version) {
		this.updateHashMap("version", version);

		this.version = version;
	}

	/**
	 * @param x264
	 *            the x264 to set
	 */
	public void setX264 (final Boolean x264) {
		this.updateHashMap("x264", Boolean.toString(x264));
		this.x264 = x264;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear (final Integer year) {
		this.updateHashMap("year", Integer.toString(year));

		this.year = year;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	private void updateHashMap (final String key, final String value) {
		if (this.movieMap.get(key) != null) {
			this.movieMap.put(key, value);
		} else {
			this.movieMap.remove(key);
			this.movieMap.put(key, value);
		}
	}

}

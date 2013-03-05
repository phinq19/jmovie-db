/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.rottentomatoes.model;

import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class RottenTomatoesModel extends MovieModel {

	@ViewInTab(as = "Rotten Tomatoes Id", tabname = "rt", sort = 10, editable = true, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "rtid", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private Integer rtId = null;

	@ViewInTab(as = "Certification", tabname = "rt", sort = 20, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "certification", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private String certification = null;

	@DatabaseOptions(as = "cryear", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer year = null;

	/**
	 * @return the rtId
	 */
	public synchronized final Integer getRtId() {
		return this.rtId;
	}

	/**
	 * @param rtId
	 *            the rtId to set
	 */
	public synchronized final void setRtId(final Integer rtId) {
		this.rtId = rtId;
	}

	/**
	 * @return the certification
	 */
	public synchronized final String getCertification() {
		return this.certification;
	}

	/**
	 * @param certification
	 *            the certification to set
	 */
	public synchronized final void setCertification(final String certification) {
		this.certification = certification;
	}

}

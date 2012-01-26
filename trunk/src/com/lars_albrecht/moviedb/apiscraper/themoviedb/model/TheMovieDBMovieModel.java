/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.themoviedb.model;

import java.awt.Image;
import java.util.ArrayList;

import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class TheMovieDBMovieModel extends MovieModel {

	@ViewInTab(as = "The MovieDB Id", tabname = "tmdb", sort = 10, editable = true, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "tmdbid", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private Integer tmdbId = null;

	@ViewInTab(as = "Alternative Name", tabname = "tmdb", sort = 30, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "alternativename", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private String alternativeName = null;

	@ViewInTab(as = "Runtime", tabname = "tmdb", sort = 40, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "runtime", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private Integer runtime = null;

	@ViewInTab(as = "Budget", tabname = "tmdb", sort = 50, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "budget", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private Integer budget = null;

	@ViewInTab(as = "Rating", tabname = "tmdb", sort = 60, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "rating", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	private Integer rating = null;

	@ViewInTab(as = "Homepage", tabname = "tmdb", sort = 70, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "homepage", type = DatabaseOptions.TYPE_FIELD, additionalType = "LONGVARCHAR", defaultValues = {}, isUnique = false)
	protected String homepage = null;

	@ViewInTab(as = "Countries", tabname = "tmdb", sort = 80, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "countries", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = {}, isUnique = false)
	private ArrayList<String> countries = null;

	@ViewInTab(as = "People", tabname = "tmdb", sort = 90, editable = false, type = ViewInTab.TYPE_AUTO, format = ViewInTab.FORMAT_AUTO)
	@DatabaseOptions(as = "people", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = {}, isUnique = false)
	private ArrayList<String> people = null;

	@DatabaseOptions(as = "cover", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Image cover = null;

	@DatabaseOptions(as = "descriptionshort", type = DatabaseOptions.TYPE_FIELD, additionalType = "LONGVARCHAR", defaultValues = {}, isUnique = false)
	protected String descriptionShort = null;

	@DatabaseOptions(as = "originalname", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected String originalName = null;

	@DatabaseOptions(as = "maintitle", type = DatabaseOptions.TYPE_FIELD, additionalType = "VARCHAR(128) NOT NULL", defaultValues = {}, isUnique = false)
	protected String maintitle = null;

	@DatabaseOptions(as = "cryear", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer year = null;

	/**
	 * @return the tmdbId
	 */
	public synchronized final Integer getTmdbId() {
		return this.tmdbId;
	}

	/**
	 * @param tmdbId
	 *            the tmdbId to set
	 */
	public synchronized final void setTmdbId(final Integer tmdbId) {
		this.tmdbId = tmdbId;
	}

	/**
	 * @return the alternativeName
	 */
	public synchronized final String getAlternativeName() {
		return this.alternativeName;
	}

	/**
	 * @param alternativeName
	 *            the alternativeName to set
	 */
	public synchronized final void setAlternativeName(final String alternativeName) {
		this.alternativeName = alternativeName;
	}

	/**
	 * @return the runtime
	 */
	public synchronized final Integer getRuntime() {
		return this.runtime;
	}

	/**
	 * @param runtime
	 *            the runtime to set
	 */
	public synchronized final void setRuntime(final Integer runtime) {
		this.runtime = runtime;
	}

	/**
	 * @return the budget
	 */
	public synchronized final Integer getBudget() {
		return this.budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public synchronized final void setBudget(final Integer budget) {
		this.budget = budget;
	}

	/**
	 * @return the rating
	 */
	public synchronized final Integer getRating() {
		return this.rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public synchronized final void setRating(final Integer rating) {
		this.rating = rating;
	}

	/**
	 * @return the countries
	 */
	public synchronized final ArrayList<String> getCountries() {
		return this.countries;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	public synchronized final void setCountries(final ArrayList<String> countries) {
		this.countries = countries;
	}

	/**
	 * @return the people
	 */
	public synchronized final ArrayList<String> getPeople() {
		return this.people;
	}

	/**
	 * @param people
	 *            the people to set
	 */
	public synchronized final void setPeople(final ArrayList<String> people) {
		this.people = people;
	}

	/**
	 * @return the homepage
	 */
	public synchronized final String getHomepage() {
		return this.homepage;
	}

	/**
	 * @param homepage
	 *            the homepage to set
	 */
	public synchronized final void setHomepage(final String homepage) {
		this.homepage = homepage;
	}

}

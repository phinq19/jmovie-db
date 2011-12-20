/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.themoviedb.model;

import java.util.ArrayList;

import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.annotation.ViewInTable;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class TheMovieDBMovieModel extends MovieModel {

	@ViewInTab(as = "The MovieDB Id", tabname = "tmdb", sort = 10, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "tmdbid", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer tmdbId = null;

	@ViewInTab(as = "Alternative Name", tabname = "tmdb", sort = 20, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "alternativename", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected String alternativeName = null;

	@ViewInTab(as = "Original Name", tabname = "tmdb", sort = 30, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "originalname", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	@ViewInTable(as = "Original Name", sort = 10000)
	protected String originalName = null;

	@ViewInTab(as = "Runtime", tabname = "tmdb", sort = 40, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "runtime", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer runtime = null;

	@ViewInTab(as = "Budget", tabname = "tmdb", sort = 50, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "budget", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer budget = null;

	@ViewInTab(as = "Rating", tabname = "tmdb", sort = 630, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "rating", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	protected Integer rating = null;

	@ViewInTab(as = "Countries", tabname = "tmdb", sort = 70, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "countries", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = {}, isUnique = false)
	protected ArrayList<String> countries = null;

	@ViewInTab(as = "People", tabname = "tmdb", sort = 80, editable = false, type = ViewInTab.TYPE_AUTO)
	@DatabaseOptions(as = "people", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = {}, isUnique = false)
	protected ArrayList<String> people = null;

	/**
	 * @return the tmdbId
	 */
	protected synchronized final Integer getTmdbId() {
		return this.tmdbId;
	}

	/**
	 * @param tmdbId
	 *            the tmdbId to set
	 */
	protected synchronized final void setTmdbId(final Integer tmdbId) {
		this.tmdbId = tmdbId;
	}

	/**
	 * @return the alternativeName
	 */
	protected synchronized final String getAlternativeName() {
		return this.alternativeName;
	}

	/**
	 * @param alternativeName
	 *            the alternativeName to set
	 */
	protected synchronized final void setAlternativeName(final String alternativeName) {
		this.alternativeName = alternativeName;
	}

	/**
	 * @return the originalName
	 */
	protected synchronized final String getOriginalName() {
		return this.originalName;
	}

	/**
	 * @param originalName
	 *            the originalName to set
	 */
	protected synchronized final void setOriginalName(final String originalName) {
		this.originalName = originalName;
	}

	/**
	 * @return the runtime
	 */
	protected synchronized final Integer getRuntime() {
		return this.runtime;
	}

	/**
	 * @param runtime
	 *            the runtime to set
	 */
	protected synchronized final void setRuntime(final Integer runtime) {
		this.runtime = runtime;
	}

	/**
	 * @return the budget
	 */
	protected synchronized final Integer getBudget() {
		return this.budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	protected synchronized final void setBudget(final Integer budget) {
		this.budget = budget;
	}

	/**
	 * @return the rating
	 */
	protected synchronized final Integer getRating() {
		return this.rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	protected synchronized final void setRating(final Integer rating) {
		this.rating = rating;
	}

	/**
	 * @return the countries
	 */
	protected synchronized final ArrayList<String> getCountries() {
		return this.countries;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	protected synchronized final void setCountries(final ArrayList<String> countries) {
		this.countries = countries;
	}

	/**
	 * @return the people
	 */
	protected synchronized final ArrayList<String> getPeople() {
		return this.people;
	}

	/**
	 * @param people
	 *            the people to set
	 */
	protected synchronized final void setPeople(final ArrayList<String> people) {
		this.people = people;
	}

}

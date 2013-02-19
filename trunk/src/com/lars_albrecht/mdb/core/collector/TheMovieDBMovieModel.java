/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.awt.Image;
import java.util.ArrayList;

/**
 * @author lalbrecht
 * 
 */
public class TheMovieDBMovieModel {

	private Integer				tmdbId				= null;

	private String				alternativeName		= null;

	private Integer				runtime				= null;

	private Integer				budget				= null;

	private Integer				rating				= null;

	protected String			homepage			= null;

	private ArrayList<String>	countries			= null;

	private ArrayList<String>	people				= null;

	protected Image				cover				= null;

	protected String			descriptionShort	= null;

	protected String			originalName		= null;

	protected String			maintitle			= null;

	protected Integer			year				= null;

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
	public synchronized final void setAlternativeName(
			final String alternativeName) {
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
	public synchronized final void setCountries(
			final ArrayList<String> countries) {
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

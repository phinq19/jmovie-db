/**
 * 
 */
package com.lars_albrecht.mdb.core.collector.model;

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
	 * @return the alternativeName
	 */
	public synchronized final String getAlternativeName() {
		return this.alternativeName;
	}

	/**
	 * @return the budget
	 */
	public synchronized final Integer getBudget() {
		return this.budget;
	}

	/**
	 * @return the countries
	 */
	public synchronized final ArrayList<String> getCountries() {
		return this.countries;
	}

	/**
	 * @return the homepage
	 */
	public synchronized final String getHomepage() {
		return this.homepage;
	}

	/**
	 * @return the people
	 */
	public synchronized final ArrayList<String> getPeople() {
		return this.people;
	}

	/**
	 * @return the rating
	 */
	public synchronized final Integer getRating() {
		return this.rating;
	}

	/**
	 * @return the runtime
	 */
	public synchronized final Integer getRuntime() {
		return this.runtime;
	}

	/**
	 * @return the tmdbId
	 */
	public synchronized final Integer getTmdbId() {
		return this.tmdbId;
	}

	/**
	 * @param alternativeName
	 *            the alternativeName to set
	 */
	public synchronized final void setAlternativeName(final String alternativeName) {
		this.alternativeName = alternativeName;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public synchronized final void setBudget(final Integer budget) {
		this.budget = budget;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	public synchronized final void setCountries(final ArrayList<String> countries) {
		this.countries = countries;
	}

	/**
	 * @param homepage
	 *            the homepage to set
	 */
	public synchronized final void setHomepage(final String homepage) {
		this.homepage = homepage;
	}

	/**
	 * @param people
	 *            the people to set
	 */
	public synchronized final void setPeople(final ArrayList<String> people) {
		this.people = people;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public synchronized final void setRating(final Integer rating) {
		this.rating = rating;
	}

	/**
	 * @param runtime
	 *            the runtime to set
	 */
	public synchronized final void setRuntime(final Integer runtime) {
		this.runtime = runtime;
	}

	/**
	 * @param tmdbId
	 *            the tmdbId to set
	 */
	public synchronized final void setTmdbId(final Integer tmdbId) {
		this.tmdbId = tmdbId;
	}

}

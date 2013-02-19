/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.interfaces;

import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public interface IApiScraperPlugin {

	String getPluginName();

	String getTabTitle();

	String getVersion();

	MovieModel getMovieFromStringYear(final String s, final Integer year);

	MovieModel getMovieFromKey(final String key);

	ConcurrentHashMap<String, Object> getAdditionalInformationFromStringYear(final String s, final Integer year);

	ConcurrentHashMap<String, Object> getAdditionalInformationFromKey(final String key);

	Class<? extends MovieModel> getMovieModelInstance();

	String getIdFieldName();

}

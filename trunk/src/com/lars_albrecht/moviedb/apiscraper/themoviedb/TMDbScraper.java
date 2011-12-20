/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.themoviedb;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.apiscraper.themoviedb.model.TheMovieDBMovieModel;
import com.moviejukebox.themoviedb.TheMovieDb;
import com.moviejukebox.themoviedb.model.MovieDB;

/**
 * @author lalbrecht
 * 
 */
public class TMDbScraper implements IApiScraperPlugin {

	private final String apiKey = "d2bfb8abb70809759df091b8d23876af";
	private final String langKey = "de";

	com.moviejukebox.themoviedb.TheMovieDb tmdb = null;

	public TMDbScraper() {
		this.tmdb = new com.moviejukebox.themoviedb.TheMovieDb(this.apiKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#
	 * getMovieFromKey(java.lang.String)
	 */
	@Override
	public TheMovieDBMovieModel getMovieFromKey(final String key) {
		final MovieDB m = this.tmdb.moviedbGetInfo(key, this.langKey);
		if (m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#
	 * getMovieFromString(java.lang.String, java.lang.Integer)
	 */
	@Override
	public TheMovieDBMovieModel getMovieFromStringYear(final String s, final Integer year) {
		final MovieDB m = TheMovieDb.findMovie(this.tmdb.moviedbSearch(s, this.langKey), s, (year != null ? Integer.toString(year) : null));
		if (m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#
	 * getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "TMDb";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#getTabTitle
	 * ()
	 */
	@Override
	public String getTabTitle() {
		return "The Movie DB - TMDb";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#getVersion
	 * ()
	 */
	@Override
	public String getVersion() {
		return "1.0.0.0";
	}

	private TheMovieDBMovieModel returnInfosFromMovie(final MovieDB m) {

		final TheMovieDBMovieModel movie = new TheMovieDBMovieModel();
		try {
			movie.set("maintitle", m.getTitle());
			movie.set("tmdbId", m.getId());
			// movie.setId(Integer.parseInt(m.getId()));
			movie.set("descriptionShort", m.getOverview());
			// for (final Category category : m.getCategories()) {
			// movie.getGenreList().add(category.getName());
			// }

			if (m.getReleaseDate() != null) {
				movie.set("year", Integer.parseInt(m.getReleaseDate().substring(0, 4)));
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// resultMap = new HashMap<String, Object>();
		// resultMap.put("adult", m.getAdult());
		// resultMap.put("alternativeName", m.getAlternativeName());
		// resultMap.put("budget", m.getBudget());
		// resultMap.put("categories", m.getCategories());
		// resultMap.put("certification", m.getCertification());
		// resultMap.put("countries", m.getCountries());
		// resultMap.put("homepage", m.getHomepage());
		// resultMap.put("id", m.getId());
		// resultMap.put("imdb", m.getImdb());
		// resultMap.put("language", m.getLanguage());
		// resultMap.put("originalName", m.getOriginalName());
		// resultMap.put("overview", m.getOverview());
		// resultMap.put("people", m.getPeople());
		// resultMap.put("popularity", m.getPopularity());
		// resultMap.put("productionCountries", m.getProductionCountries());
		// resultMap.put("rating", m.getRating());
		// resultMap.put("releaseDate", m.getReleaseDate());
		// resultMap.put("revenue", m.getRevenue());
		// resultMap.put("runtime", m.getRuntime());
		// resultMap.put("studios", m.getStudios());
		// resultMap.put("tagline", m.getTagline());
		// resultMap.put("title", m.getTitle());
		// resultMap.put("trailer", m.getTrailer());
		// resultMap.put("translated", m.getTranslated());
		// resultMap.put("type", m.getType());
		// resultMap.put("url", m.getUrl());
		// resultMap.put("version", m.getVersion());

		return movie;
	}

	@Override
	public ConcurrentHashMap<String, Object> getAdditionalInformationFromKey(final String key) {
		return null;
	}

	@Override
	public ConcurrentHashMap<String, Object> getAdditionalInformationFromStringYear(final String s, final Integer year) {
		return null;
	}

	@Override
	public Class<? extends TheMovieDBMovieModel> getMovieModelInstance() {
		return new TheMovieDBMovieModel().getClass();
	}
}

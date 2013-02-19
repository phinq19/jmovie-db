/**
 * 
 */
package com.lars_albrecht.moviedb.apiscraper.rottentomatoes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.apiscraper.rottentomatoes.model.RottenTomatoesModel;
import com.moviejukebox.rottentomatoes.RottenTomatoes;
import com.moviejukebox.rottentomatoes.model.Movie;

/**
 * @author lalbrecht
 * 
 */
public class RottenTomatoesScraper implements IApiScraperPlugin {

	private final String apiKey = "4kxqrp92jw9jmj66eke7hts3";

	RottenTomatoes rt = null;

	public RottenTomatoesScraper() {
		this.rt = new RottenTomatoes(this.apiKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin# getMovieFromKey(java.lang.String)
	 */
	@Override
	public RottenTomatoesModel getMovieFromKey(final String key) {
		final Movie m = this.rt.movieInfo(Integer.parseInt(key));
		if(m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin# getMovieFromString(java.lang.String, java.lang.Integer)
	 */
	@Override
	public RottenTomatoesModel getMovieFromStringYear(final String s, final Integer year) {
		final Set<Movie> movieSet = this.rt.moviesSearch(s);
		Movie m = null;
		for(final Movie movie : movieSet) {
			if((((year != null) && (movie.getYear() == year)) || (year == null))) {
				m = movie;
				break;
			}
		}
		if(m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin# getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "RT";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#getTabTitle ()
	 */
	@Override
	public String getTabTitle() {
		return "Rotten Tomatoes - RT";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin#getVersion ()
	 */
	@Override
	public String getVersion() {
		return "1.0.0.0";
	}

	private RottenTomatoesModel returnInfosFromMovie(final Movie m) {

		final RottenTomatoesModel movie = new RottenTomatoesModel();
		System.out.println("--------------- " + this.getClass().getSimpleName());
		System.out.println(m);
		System.out.println(m.getGenres());
		System.out.println("/--------------- " + this.getClass().getSimpleName());

		try {
			movie.setRtId(m.getId());
			movie.setCertification(m.getCertification());
			movie.set("year", m.getYear());
			movie.set("genreList", new ArrayList<String>(m.getGenres()));
		} catch(final SecurityException e) {
			e.printStackTrace();
		} catch(final IllegalAccessException e) {
			e.printStackTrace();
		} catch(final IllegalArgumentException e) {
			e.printStackTrace();
		} catch(final InvocationTargetException e) {
			e.printStackTrace();
		}
		// movie.setMaintitle(m.getTitle());
		// movie.setId(m.getId());

		// final HashMap<String, Object> resultMap = new HashMap<String,
		// Object>();
		// resultMap.put("certification", m.getCertification());
		// resultMap.put("id", m.getId());
		// resultMap.put("title", m.getTitle());
		// resultMap.put("directors", m.getDirectors());
		// resultMap.put("genres", m.getGenres());
		// resultMap.put("rating", m.getRatings());
		// resultMap.put("synopsis", m.getSynopsis());
		// resultMap.put("year", m.getYear());

		// resultMap.put("cast", m.getCast());
		// resultMap.put("version", m.getLinks());
		// resultMap.put("version", m.getReleaseDates()));

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
	public Class<? extends RottenTomatoesModel> getMovieModelInstance() {
		return new RottenTomatoesModel().getClass();
	}

	@Override
	public String getIdFieldName() {
		return "rtId";
	}

}

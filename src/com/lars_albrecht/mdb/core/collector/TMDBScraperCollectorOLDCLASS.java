/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import com.lars_albrecht.general.utilities.Helper;
import com.moviejukebox.themoviedb.MovieDbException;
import com.moviejukebox.themoviedb.TheMovieDb;
import com.moviejukebox.themoviedb.model.Artwork;
import com.moviejukebox.themoviedb.model.Person;

/**
 * @author lalbrecht
 * 
 */
public class TMDBScraperCollectorOLDCLASS {

	private final String	apiKey	= "d2bfb8abb70809759df091b8d23876af";
	private final String	langKey	= "de";

	private TheMovieDb		tmdb	= null;

	public TMDBScraperCollectorOLDCLASS() {
		try {
			this.tmdb = new TheMovieDb(this.apiKey);
		} catch (final MovieDbException e) {
			e.printStackTrace();
		}
	}

	public TheMovieDBMovieModel getMovieFromKey(final String key) {
		final MovieDB m = this.tmdb.moviedbGetInfo(key, this.langKey);
		if (m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	public TheMovieDBMovieModel getMovieFromStringYear(final String s,
			final Integer year) {
		final List<MovieDB> searchResults = this.tmdb.moviedbSearch(s,
				this.langKey);
		MovieDB m = null;
		if (searchResults.size() > 1) {
			m = this.findMovie(searchResults, s,
					(year != null ? Integer.toString(year) : null));
		} else if (searchResults.size() == 1) {
			m = searchResults.get(0);
		}
		if (m != null) {
			return this.returnInfosFromMovie(m);
		}
		return null;
	}

	/**
	 * Search a list of movies and return the one that matches the title & year
	 * 
	 * @param movieList
	 *            The list of movies to search
	 * @param title
	 *            The title to search for
	 * @param year
	 *            The year of the title to search for
	 * @return The matching movie
	 */
	private MovieDB findMovie(final Collection<MovieDB> movieList,
			final String title, final String year) {
		if ((movieList == null) || movieList.isEmpty()) {
			return null;
		}
		for (final MovieDB moviedb : movieList) {
			if (this.compareMovies(moviedb, title, year)) {
				return moviedb;
			}
		}

		return null;
	}

	/**
	 * Compare the MovieDB object with a title & year
	 * 
	 * @param moviedb
	 *            The moviedb object to compare too
	 * @param title
	 *            The title of the movie to compare
	 * @param year
	 *            The year of the movie to compare
	 * @return True if there is a match, False otherwise.
	 */
	private boolean compareMovies(final MovieDB moviedb, final String title,
			final String year) {
		if ((moviedb == null) || (!Helper.isValidString(title))) {
			return false;
		}
		if (Helper.isValidString(year)) {
			if (Helper.isValidString(moviedb.getReleaseDate())) {
				// Compare with year
				final String movieYear = moviedb.getReleaseDate().substring(0,
						4);
				if (movieYear.equals(year)
						&& (moviedb.getTitle().equalsIgnoreCase(title)
								|| moviedb.getOriginalName().equalsIgnoreCase(
										title)
								|| moviedb.getAlternativeName()
										.equalsIgnoreCase(title)
								|| (moviedb.getTitle().contains("-") && moviedb
										.getTitle().split("-")[0].trim()
										.equalsIgnoreCase(title))
								|| moviedb.getTitle().replaceAll("'", "")
										.equalsIgnoreCase(title)
								|| moviedb.getOriginalName()
										.replaceAll("'", "")
										.equalsIgnoreCase(title) || moviedb
								.getAlternativeName().replaceAll("'", "")
								.equalsIgnoreCase(title))) {
					return true;
				}
			}
		} else {
			// Compare without year
			if (moviedb.getTitle().equalsIgnoreCase(title)
					|| moviedb.getOriginalName().equalsIgnoreCase(title)
					|| moviedb.getAlternativeName().equalsIgnoreCase(title)) {
				return true;
			}
		}
		return false;
	}

	private TheMovieDBMovieModel returnInfosFromMovie(final MovieDB m) {
		final ArrayList<String> tempList = new ArrayList<String>();
		final TheMovieDBMovieModel movie = new TheMovieDBMovieModel();
		try {
			// Helper.getFieldsFromClass(MovieDB.class);
			// FieldList fl =
			// Helper.getDBFieldModelFromClass(TheMovieDBMovieModel.class);
			// for (FieldModel fieldModel : fl) {
			// System.out.println(fieldModel.getField().getName());
			// Method method = MovieDB.class.getMethod("get" +
			// Helper.ucfirst(fieldModel.getField().getName()));
			// movie.set(fieldModel.getField().getName(), method.invoke(m));
			// }

			movie.set("maintitle", m.getTitle());
			movie.setAlternativeName(m.getAlternativeName());
			movie.setBudget(((m.getBudget() != null)
					&& !m.getBudget().equals("") ? Integer.parseInt(m
					.getBudget()) : null));
			movie.set("originalName", m.getOriginalName());
			movie.setRating(((m.getRating() != null)
					&& !m.getRating().equals("") ? new Double(m.getRating())
					.intValue() : null));
			movie.setRuntime(((m.getRuntime() != null)
					&& !m.getRuntime().equals("") ? Integer.parseInt(m
					.getRuntime()) : null));
			movie.setTmdbId(Integer.parseInt(m.getId()));
			movie.set("descriptionShort", m.getOverview());
			tempList.clear();
			for (final Country c : m.getCountries()) {
				tempList.add(c.getName());
			}
			movie.set("year",
					Integer.parseInt(m.getReleaseDate().substring(0, 4)));

			movie.setCountries(tempList);
			tempList.clear();
			for (final Person p : m.getPeople()) {
				tempList.add(p.getName());
			}
			movie.setPeople(tempList);

			if ((m.getArtwork().size() > 0)
					&& (m.getFirstArtwork(Artwork.ARTWORK_TYPE_POSTER,
							Artwork.ARTWORK_SIZE_MID) != null)) {

				BufferedImage bi;
				try {
					bi = ImageIO.read(new URL(m.getFirstArtwork(
							Artwork.ARTWORK_TYPE_POSTER,
							Artwork.ARTWORK_SIZE_MID).getUrl()));
					movie.set("cover", Helper.bufferedImageToImage(bi));
				} catch (final MalformedURLException e) {
					e.printStackTrace();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			movie.setHomepage(m.getHomepage());

			// System.out.println("Categories: " + m.getCategories().size());
			// for(final Category c : m.getCategories()) {
			// System.out.println(c.getName());
			// System.out.println(c.getType());
			// System.out.println("-----");
			// }

		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

		return movie;
	}

}

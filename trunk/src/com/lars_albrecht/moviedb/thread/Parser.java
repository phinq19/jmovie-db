/**
 * 
 */
package com.lars_albrecht.moviedb.thread;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.moviedb.controller.ThreadController;
import com.lars_albrecht.moviedb.model.DefaultMovieModel;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.utilities.Debug;
import com.lars_albrecht.moviedb.utilities.Helper;

/**
 * @author lalbrecht
 * 
 */
public class Parser implements Runnable {

	private ThreadController tc = null;
	private ArrayList<File> files = null;

	private final ArrayList<MovieModel> movies = new ArrayList<MovieModel>();

	private ConcurrentHashMap<String, ArrayList<String>> dbListItems = null;
	private FieldList fieldList = null;

	private String regex = null;

	public Parser(final ThreadController tc, final ArrayList<File> files, final ConcurrentHashMap<String, ArrayList<String>> dbListItems, final FieldList fieldList, final String regex) {
		this.tc = tc;
		this.files = files;
		this.dbListItems = dbListItems;
		this.fieldList = fieldList;
		this.regex = regex;
	}

	/**
	 * Parse the file(name) with the given regex and fill out model to add them
	 * to a list.
	 * 
	 * @param filename
	 * @param tempMovie
	 */
	@SuppressWarnings("unchecked")
	private void parseMoviename(final File file) {
		Debug.startTimer("parseMovie2");
		final MovieModel tempMovie = new DefaultMovieModel();
		final String filename = Helper.getFileNameWithoutExtension(file.getName());

		final Pattern filePattern = Pattern.compile(this.regex, Pattern.CASE_INSENSITIVE);
		final Matcher matcherValues = filePattern.matcher(filename);

		Boolean found = false;
		try {
			int i = 0;
			while (matcherValues.find()) {
				final String val = matcherValues.group(0).trim();
				found = false;
				for (final Entry<String, ArrayList<String>> x : this.dbListItems.entrySet()) {
					if (Helper.containsIgnoreCase(x.getValue(), matcherValues.group(0).trim())) {
						final FieldModel item = this.fieldList.get(this.fieldList.fieldNameInAsList(x.getKey()));
						Helper.call("get" + Helper.ucfirst(item.getField().getName()), tempMovie);
						final ArrayList<String> list = (ArrayList<String>) Helper.call("get" + Helper.ucfirst(item.getField().getName()), tempMovie);
						list.add(matcherValues.group(0).trim());

						Helper.call("set" + Helper.ucfirst(item.getField().getName()), tempMovie, list);

						found = true;
						break;
					}
				}
				if (!found) {
					// notFoundList.add(val);
					System.out.println("not found: " + matcherValues.group(0));
					if (i == 0) {
						tempMovie.set("maintitle", val);
					} else if ((i == 1) && !val.matches("([0-9]{4})")) {
						tempMovie.set("subtitle", val);
					} else if (val.matches("([0-9]{4})")) {
						tempMovie.set("year", Integer.parseInt(val));
					}

					// TODO each fieldmodel where TYPE = TYPE_REGEX

				}
				i++;
			}

			// defaults
			tempMovie.set("file", file);
			tempMovie.set("validPath", Boolean.TRUE);

			this.movies.add(tempMovie);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

		// if regex matches file ...
		Debug.endTimer("parseMovie2");
		Debug.log(Debug.LEVEL_DEBUG, "parseMovie2 " + Debug.getFormattedTime("parseMovie2"));
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		if (this.files != null) {
			for (final File file : this.files) {
				this.parseMoviename(file);
			}
			this.tc.getParserList().remove(Thread.currentThread());
			this.tc.addMovies(this.movies);
		} else {
			this.tc.getParserList().remove(Thread.currentThread());
		}
	}

}

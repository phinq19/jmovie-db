/**
 * 
 */
package com.lars_albrecht.moviedb.thread;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.moviedb.annotation.ParseOptions;
import com.lars_albrecht.moviedb.controller.ThreadController;
import com.lars_albrecht.moviedb.model.DefaultMovieModel;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
@Deprecated
public class RegexParser implements Runnable {

	private ThreadController tc = null;
	private ArrayList<File> files = null;

	private final ArrayList<MovieModel> movies = new ArrayList<MovieModel>();

	private LinkedHashMap<String, ArrayList<String>> regexList = null;
	private FieldList fieldList = null;

	/**
	 * 
	 * @param tc
	 * @param files
	 * @param regexList
	 * @param fieldList
	 */
	public RegexParser(final ThreadController tc, final ArrayList<File> files,
			final LinkedHashMap<String, ArrayList<String>> regexList, final FieldList fieldList) {
		this.tc = tc;
		this.files = files;
		this.regexList = regexList;
		this.fieldList = fieldList;
	}

	/**
	 * Parse the file(name) with the given regex and fill out model to add them to a list.
	 * 
	 * @param filename
	 * @param tempMovie
	 */
	@SuppressWarnings("unchecked")
	private void parseMoviename(final File file) {
		Debug.startTimer("parseMovie");
		final MovieModel tempMovie = new DefaultMovieModel();
		final String filename = Helper.getFileNameWithoutExtension(file.getName());

		String regex = null;
		ArrayList<String> nameList = null;

		// for each regex ...
		for(final Entry<String, ArrayList<String>> regexList : this.regexList.entrySet()) {
			regex = regexList.getKey();
			nameList = regexList.getValue();
			// System.out.println(regex);
			final Pattern filePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			final Matcher matcherValues = filePattern.matcher(filename);

			final ConcurrentHashMap<String, ArrayList<String>> keyValueList = new ConcurrentHashMap<String, ArrayList<String>>();

			// if regex matches file ...
			if(matcherValues.find()) {
				// fill key value list (e.g.: year = 2011)
				// System.out.println(regex + " - " + filename);
				for(int i = 1; i <= matcherValues.groupCount(); i++) {
					ArrayList<String> temp = keyValueList.get(nameList.get(i - 1));
					if(temp == null) {
						temp = new ArrayList<String>();
					}
					temp.add(matcherValues.group(i));
					keyValueList.put(nameList.get(i - 1), temp);

					Debug.log(Debug.LEVEL_DEBUG, nameList.get(i - 1) + " - " + matcherValues.group(i));
				}

				// if list filled
				if(keyValueList.size() > 0) {
					// for each entry
					for(final Map.Entry<String, ArrayList<String>> entry : keyValueList.entrySet()) {
						// System.out.println(e.getKey() + " = " +
						// e.getValue());
						// final String name = (entry.getKey().indexOf("_") >
						// -1) ? entry.getKey().substring(0,
						// entry.getKey().indexOf("_")) : (String)
						// entry.getKey();
						final Integer posInList = this.fieldList.fieldNameInAsList(entry.getKey());
						if(posInList > -1) {
							try {
								final FieldModel item = this.fieldList.get(posInList);
								final ParseOptions parseAnnotation = item.getField().getAnnotation(ParseOptions.class);
								if((item.getField().getType() == Integer.class) || (item.getField().getType() == String.class)) {
									Helper.call("set" + Helper.ucfirst(item.getField().getName()), tempMovie, ((item.getField()
											.getType() == String.class) ? keyValueList.get(parseAnnotation.as()).get(0) : Integer
											.parseInt(keyValueList.get(parseAnnotation.as()).get(0))));
								} else if(item.getField().getType() == ArrayList.class) {
									final ArrayList<String> list = (ArrayList<String>) Helper.call("get"
											+ Helper.ucfirst(item.getField().getName()), tempMovie);
									final ArrayList<String> temp = keyValueList.get(parseAnnotation.as());
									for(final String string : temp) {
										list.add(string);
									}
								}
							} catch(final NumberFormatException e) {
								e.printStackTrace();
							} catch(final NoSuchMethodException e) {
								e.printStackTrace();
							} catch(final SecurityException e) {
								e.printStackTrace();
							} catch(final IllegalAccessException e) {
								e.printStackTrace();
							} catch(final IllegalArgumentException e) {
								e.printStackTrace();
							} catch(final InvocationTargetException e) {
								e.printStackTrace();
							} catch(final Exception e) {
								e.printStackTrace();
							}
						}
					}
					// defaults
					// TODO define defaults with annotations
					try {
						tempMovie.set("file", file);
						tempMovie.set("validPath", Boolean.TRUE);
					} catch(final SecurityException e) {
						e.printStackTrace();
					} catch(final IllegalAccessException e) {
						e.printStackTrace();
					} catch(final IllegalArgumentException e) {
						e.printStackTrace();
					} catch(final InvocationTargetException e) {
						e.printStackTrace();
					}

					this.movies.add(tempMovie);
					break;
				}

			}
		}
		Debug.endTimer("parseMovie");
		Debug.log(Debug.LEVEL_DEBUG, "parseMovie " + Debug.getFormattedTime("parseMovie"));
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		if(this.files != null) {
			for(final File file : this.files) {
				this.parseMoviename(file);
			}
			this.tc.getParserList().remove(Thread.currentThread());
			this.tc.addMovies(this.movies);
		} else {
			this.tc.getParserList().remove(Thread.currentThread());
		}
	}

}

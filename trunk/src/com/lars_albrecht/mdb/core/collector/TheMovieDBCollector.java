/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.Value;
import com.moviejukebox.themoviedb.MovieDbException;
import com.moviejukebox.themoviedb.TheMovieDb;
import com.moviejukebox.themoviedb.model.Genre;
import com.moviejukebox.themoviedb.model.Language;
import com.moviejukebox.themoviedb.model.MovieDb;

/**
 * @author lalbrecht
 * 
 */
public class TheMovieDBCollector extends ACollector {

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	private final String												apiKey					= "d2bfb8abb70809759df091b8d23876af";
	private final String												langKey					= "de";

	public TheMovieDBCollector(final MainController mainController, final IController controller) {
		super(mainController, controller);

		this.fileAttributeListToAdd = new ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>();
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();

		this.init();
	}

	@Override
	public void doCollect() {
		this.fileAttributeListToAdd.clear();
		for (final FileItem item : this.fileItems) {
			// collect all data for all found items in the list
			this.fileAttributeListToAdd.put(item, this.getFileAttributeListsForItem(item));

		}
	}

	private MovieDb findMovie(final String[] titles, final Integer year) {
		ArrayList<MovieDb> tempList = null;
		TheMovieDb tmdb = null;
		try {
			tmdb = new TheMovieDb(this.apiKey);
			// tmdb.getConfiguration().setBaseUrl("http://api.themoviedb.org/3/");
			// search with different combinations to find the movie.
			// implode titles to one title
			String searchTitle = Helper.implode(titles, " - ", null, null);
			if (searchTitle != null) {
				// for each title, get imploded title and cut one part out of
				// it, if no movie is found.
				for (@SuppressWarnings("unused")
				final String title : titles) {
					// search for title
					tempList = (ArrayList<MovieDb>) tmdb.searchMovie(searchTitle, (year != null ? year : 0), this.langKey, true, 0);

					if ((tempList != null) && (tempList.size() > 0)) {
						// if found, break loop
						break;
					}

					if (year != null) {
						tempList = (ArrayList<MovieDb>) tmdb.searchMovie(searchTitle, 0, this.langKey, true, 0);

						if ((tempList != null) && (tempList.size() > 0)) {
							// if found, break loop
							break;
						}
					}

					// if in string contains " - ", then get subpart of string
					// without LAST " - <else>".
					if (searchTitle.indexOf(" - ") > -1) {
						searchTitle = searchTitle.substring(0, searchTitle.lastIndexOf(" - "));
					} else {
						// if string dont contains " - ", then search without
						// year if exists
						if (year != null) {
							tempList = (ArrayList<MovieDb>) tmdb.searchMovie(searchTitle, 0, this.langKey, true, 0);
							break;
						}
						break;
					}
				}

			}

		} catch (final MovieDbException e) {
			e.printStackTrace();
		}
		// TODO if more than one result in list, than try to find the right
		int id = -1;
		if ((tempList != null) && (tempList.size() > 0)) {
			if (tempList.size() > 1) {
				id = tempList.get(0).getId();
			} else {
				id = tempList.get(0).getId();
			}
			tempList = null;

			MovieDb loadedMovie = null;
			try {
				loadedMovie = tmdb.getMovieInfo(id, this.langKey);
			} catch (final MovieDbException e) {
				e.printStackTrace();
			}
			return loadedMovie;

		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDataForFilename(final String filename) {
		final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
		data.put("titles", new ArrayList<String>());
		data.put("year", -1);

		final String strPattern = "[\\.\\-\\_0-9a-zA-ZÄÖÜßäöü\\ ]+";
		final String yearPattern = "[0-9]{4}";
		final String regex = "(^(" + strPattern + ") - (" + strPattern + ") - (" + yearPattern + ")+)|(^(" + strPattern + ") - ("
				+ strPattern + "))|(^(" + strPattern + ") - (" + yearPattern + ")+)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(filename);

		while (m.find()) {
			if ((m.group(2) != null) && (m.group(3) != null) && (m.group(4) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(2));
				((ArrayList<String>) data.get("titles")).add(m.group(3));
				data.replace("year", Integer.parseInt(m.group(4)));
			}

			if ((m.group(5) != null) && (m.group(6) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(5));
				((ArrayList<String>) data.get("titles")).add(m.group(6));
			}

			if ((m.group(8) != null) && (m.group(9) != null) && (m.group(10) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(8));
				((ArrayList<String>) data.get("titles")).add(m.group(9));
				data.replace("year", Integer.parseInt(m.group(10)));
			}
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FileAttributeList> getFileAttributeListsForItem(final FileItem item) {
		ArrayList<FileAttributeList> resultList = null;
		if (item != null) {
			String[] titles = null;
			Integer year = null;

			final ConcurrentHashMap<String, Object> data = (ConcurrentHashMap<String, Object>) this.getDataForFilename(item.getName());
			if ((data != null) && data.containsKey("titles") && data.containsKey("year")) {
				titles = ((ArrayList<Key<String>>) data.get("titles")).toArray(new String[data.size()]);
				year = (Integer) data.get("year");

				if ((titles != null) && (titles.length > 0)) {
					resultList = this.getMovieInfo(titles, year);
				}
			}
		}

		return resultList;
	}

	@Override
	public ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> getFileAttributeListToAdd() {
		return this.fileAttributeListToAdd;
	}

	@Override
	public String getInfoType() {
		return "TheMovieDB";
	}

	@Override
	public ArrayList<Key<String>> getKeysToAdd() {
		return this.keysToAdd;
	}

	private ArrayList<FileAttributeList> getMovieInfo(final String[] titles, final Integer year) {
		final ArrayList<FileAttributeList> tempKeyValueList = new ArrayList<FileAttributeList>();
		final MovieDb movie = this.findMovie(titles, year);
		final String infoType = "themoviedb";

		if (movie != null) {
			final FileAttributeList attributeList = new FileAttributeList();
			final ArrayList<KeyValue<String, Object>> keyValueList = new ArrayList<KeyValue<String, Object>>();
			// TODO move to own method

			// add general infos
			if (new Integer(movie.getId()) != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("tmdb_id", infoType, "general", false, false),
						new Value<Object>(new Integer(movie.getId()))));
			}
			if (movie.getImdbID() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("imdb_id", infoType, "general", false, false),
						new Value<Object>(movie.getImdbID())));
			}
			if (movie.getOriginalTitle() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("original_title", infoType, "general", false, false),
						new Value<Object>(movie.getOriginalTitle())));
			}
			if (movie.getTitle() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("title", infoType, "general", false, false),
						new Value<Object>(movie.getTitle())));
			}
			if (movie.getOverview() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("overview", infoType, "general", false, false),
						new Value<Object>(movie.getOverview())));
			}

			if (movie.getTagline() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("tagline", infoType, "general", false, false),
						new Value<Object>(movie.getTagline())));
			}

			// add movie facts
			if (new Long(movie.getBudget()) != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("budget", infoType, "facts", false, false),
						new Value<Object>(movie.getBudget())));
			}
			if (movie.getBelongsToCollection() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("collection_id", infoType, "facts", false, false),
						new Value<Object>(movie.getBelongsToCollection().getId())));
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("collection_name", infoType, "facts", false, true),
						new Value<Object>(movie.getBelongsToCollection().getName())));
			}
			if (movie.getStatus() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("status", infoType, "facts", false, false),
						new Value<Object>(movie.getStatus())));
			}
			if (new Integer(movie.getRuntime()) != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("runtime", infoType, "facts", false, false),
						new Value<Object>(movie.getRuntime())));
			}
			if (new Long(movie.getRevenue()) != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("revenue", infoType, "facts", false, false),
						new Value<Object>(movie.getRevenue())));
			}
			if (movie.getSpokenLanguages() != null) {
				for (final Language language : movie.getSpokenLanguages()) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("language", infoType, "facts", false, true),
							new Value<Object>(language.getName())));
				}
			}
			if (movie.getHomepage() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("homepage", infoType, "facts", false, false),
						new Value<Object>(movie.getHomepage())));
			}

			if (movie.getGenres() != null) {
				for (final Genre genre : movie.getGenres()) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("genre", infoType, "genre", false, true),
							new Value<Object>(genre.getName())));
				}
			}

			for (final KeyValue<String, Object> keyValue : keyValueList) {
				if ((keyValue != null) && (keyValue.getKey() != null) && !this.keysToAdd.contains(keyValue.getKey())) {
					this.keysToAdd.add(keyValue.getKey());
				}

				if ((keyValue != null) && (keyValue.getValue() != null) && !this.valuesToAdd.contains(keyValue.getValue())) {
					this.valuesToAdd.add(keyValue.getValue());
				}
			}

			attributeList.setSectionName(infoType);
			attributeList.setKeyValues(keyValueList);

			tempKeyValueList.add(attributeList);
		}
		return tempKeyValueList;
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return this.valuesToAdd;
	}

	private void init() {
	}

}

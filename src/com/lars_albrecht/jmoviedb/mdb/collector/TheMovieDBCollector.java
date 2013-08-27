/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.collector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.mdb.main.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.main.core.handler.datahandler.MediaHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.abstracts.ADataHandler;
import com.lars_albrecht.mdb.main.core.models.FileAttributeList;
import com.lars_albrecht.mdb.main.core.models.KeyValue;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;
import com.lars_albrecht.mdb.main.core.models.persistable.Key;
import com.lars_albrecht.mdb.main.core.models.persistable.MediaItem;
import com.lars_albrecht.mdb.main.core.models.persistable.Value;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Artwork;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.Language;
import com.omertron.themoviedbapi.model.MovieDb;
import com.omertron.themoviedbapi.model.PersonCast;
import com.omertron.themoviedbapi.model.PersonCrew;
import com.omertron.themoviedbapi.model.Trailer;

/**
 * @author lalbrecht
 * @see "https://github.com/Omertron/api-themoviedb"
 * 
 */
public class TheMovieDBCollector extends ACollector {

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	private final String												apiKey					= RessourceBundleEx.getInstance("jmdb")
																										.getProperty("apikey.themoviedb");
	private final String												langKey					= "de";

	public TheMovieDBCollector() {
		super();
		this.addType("movie");
		this.fileAttributeListToAdd = new ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>();
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	@Override
	public void doCollect() {
		this.fileAttributeListToAdd.clear();
		ArrayList<FileAttributeList> tempFileAttributes = null;
		for (final FileItem item : this.getFileItems()) {
			// collect all data for all found items in the list
			if (item != null) {
				tempFileAttributes = this.getFileAttributeListsForItem(item);
				if (tempFileAttributes != null) {
					this.fileAttributeListToAdd.put(item, tempFileAttributes);
				}
			}
		}
	}

	/**
	 * Returns a list of all infos in a keyValue-List.
	 * 
	 * @param movie
	 * @param infoType
	 * @return ArrayList<KeyValue<String, Object>>
	 */
	private ArrayList<KeyValue<String, Object>> fillKeyValueList(final MovieDb movie, final String infoType, final FileItem fileItem) {
		ArrayList<KeyValue<String, Object>> resultList = null;
		if (movie != null) {
			resultList = new ArrayList<KeyValue<String, Object>>();

			// add general infos
			if (new Integer(movie.getId()) != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("tmdb_id", infoType, "general", false, false),
						new Value<Object>(new Integer(movie.getId()))));
			}
			if (movie.getImdbID() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("imdb_id", infoType, "general", false, false),
						new Value<Object>(movie.getImdbID())));
			}
			if (movie.getOriginalTitle() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("original_title", infoType, "general", false, false),
						new Value<Object>(movie.getOriginalTitle())));
			}
			if (movie.getTitle() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("title", infoType, "general", false, false), new Value<Object>(
						movie.getTitle())));
			}
			if (movie.getOverview() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("overview", infoType, "general", false, false),
						new Value<Object>(movie.getOverview())));
			}

			if (movie.getTagline() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("tagline", infoType, "general", false, false),
						new Value<Object>(movie.getTagline())));
			}

			// add movie facts
			if (new Long(movie.getBudget()) != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("budget", infoType, "facts", false, false), new Value<Object>(
						movie.getBudget())));
			}
			if (movie.getBelongsToCollection() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("collection_id", infoType, "facts", false, true),
						new Value<Object>(movie.getBelongsToCollection().getId())));
				resultList.add(new KeyValue<String, Object>(new Key<String>("collection_name", infoType, "facts", false, true),
						new Value<Object>(movie.getBelongsToCollection().getName())));
			}
			if (movie.getStatus() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("status", infoType, "facts", false, false), new Value<Object>(
						movie.getStatus())));
			}
			if (new Integer(movie.getRuntime()) != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("runtime", infoType, "facts", false, false), new Value<Object>(
						movie.getRuntime())));
			}
			if (new Long(movie.getRevenue()) != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("revenue", infoType, "facts", false, false), new Value<Object>(
						movie.getRevenue())));
			}
			if (movie.getSpokenLanguages() != null) {
				for (final Language language : movie.getSpokenLanguages()) {
					resultList.add(new KeyValue<String, Object>(new Key<String>("language", infoType, "facts", false, true),
							new Value<Object>(language.getName())));
				}
			}
			if (movie.getHomepage() != null) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("homepage", infoType, "facts", false, false),
						new Value<Object>(movie.getHomepage())));
			}

			// add reviews | null pointer
			// if (movie.getReviews() != null && movie.getReviews().size() > 0)
			// {
			// for (final Reviews review : movie.getReviews()) {
			// resultList.add(new KeyValue<String, Object>(new
			// Key<String>("review_id", infoType, "reviews_" + review.getId(),
			// false,
			// false), new Value<Object>(review.getId())));
			// resultList.add(new KeyValue<String, Object>(new
			// Key<String>("review_author", infoType, "reviews_" +
			// review.getId(),
			// false, false), new Value<Object>(review.getAuthor())));
			// resultList.add(new KeyValue<String, Object>(new
			// Key<String>("review_content", infoType, "reviews_" +
			// review.getId(),
			// false, false), new Value<Object>(review.getContent())));
			// resultList.add(new KeyValue<String, Object>(new
			// Key<String>("review_url", infoType, "reviews_" + review.getId(),
			// false,
			// false), new Value<Object>(review.getUrl())));
			// }
			// }

			// add cast and crew
			if ((movie.getCast() != null) && (movie.getCast().size() > 0)) {
				for (final PersonCast personCast : movie.getCast()) {
					resultList.add(new KeyValue<String, Object>(new Key<String>("cast", infoType, "cast_and_crew", false, true),
							new Value<Object>(personCast.getName()
									+ (personCast.getCharacter() != null ? " (" + personCast.getCharacter() + ")" : ""))));
				}
			}

			if ((movie.getCrew() != null) && (movie.getCrew().size() > 0)) {
				for (final PersonCrew personCrew : movie.getCrew()) {
					resultList
							.add(new KeyValue<String, Object>(new Key<String>("crew", infoType, "cast_and_crew", false, true),
									new Value<Object>(personCrew.getName()
											+ (personCrew.getJob() != null ? " (" + personCrew.getJob() + ")" : ""))));
				}
			}

			// add genres
			if (movie.getGenres() != null) {
				for (final Genre genre : movie.getGenres()) {
					resultList.add(new KeyValue<String, Object>(new Key<String>("genre", infoType, "genre", false, true),
							new Value<Object>(genre.getName())));
				}
			}

			// add images
			if (movie.getPosterPath() != null) {
				try {
					ADataHandler.getDataHandler(MediaHandler.class)
							.addData(
									"mediaItems",
									fileItem,
									new MediaItem("poster", MediaItem.TYPE_WEB_IMAGE, new URI(movie.getPosterPath()), this
											.getOptionsFor("poster")));
				} catch (final URISyntaxException e) {
					e.printStackTrace();
				}
			}

			if (movie.getBackdropPath() != null) {
				try {
					ADataHandler.getDataHandler(MediaHandler.class).addData(
							"mediaItems",
							fileItem,
							new MediaItem("backdrop", MediaItem.TYPE_WEB_IMAGE, new URI(movie.getBackdropPath()), this
									.getOptionsFor("backdrop")));
				} catch (final URISyntaxException e) {
					e.printStackTrace();
				}
			}

			if ((movie.getImages() != null) && (movie.getImages().size() > 0)) {
				int i = 0;
				for (final Artwork artwork : movie.getImages()) {
					try {
						ADataHandler.getDataHandler(MediaHandler.class).addData(
								"mediaItems",
								fileItem,
								new MediaItem("artwork_" + i + "_" + artwork.getArtworkType().name(), MediaItem.TYPE_WEB_IMAGE, new URI(
										artwork.getFilePath()), this.getOptionsFor(artwork.getArtworkType().name())));
					} catch (final URISyntaxException e) {
						e.printStackTrace();
					}
					i++;
				}

			}

			// add votes
			if (movie.getVoteAverage() > 0) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("vote_average", infoType, "votes", false, false),
						new Value<Object>(movie.getVoteAverage())));
			}
			if (movie.getVoteCount() > 0) {
				resultList.add(new KeyValue<String, Object>(new Key<String>("vote_count", infoType, "votes", false, false),
						new Value<Object>(movie.getVoteCount())));
			}

			// add trailers

			if ((movie.getTrailers() != null) && (movie.getTrailers().size() > 0)) {
				for (final Trailer trailer : movie.getTrailers()) {
					if (trailer != null && trailer.getWebsite() != null && trailer.getSource() != null) {
						try {
							ADataHandler.getDataHandler(MediaHandler.class).addData(
									"mediaItems",
									fileItem,
									new MediaItem(trailer.getName(), MediaItem.TYPE_WEB_VIDEO, new URI(trailer.getSource()), this
											.getOptionsFor(trailer.getWebsite())));
						} catch (final URISyntaxException e) {
							// e.printStackTrace();
						}
					}

				}
			}

		}

		return resultList;
	}

	/**
	 * 
	 * TODO improve detecting of the real filename and the finding of the movie
	 * 
	 * @param titles
	 * @param year
	 * @return
	 * @throws IOException
	 */
	private MovieDb findMovie(final String[] titles, final Integer year) throws IOException {
		ArrayList<MovieDb> tempList = null;
		TheMovieDbApi tmdb = null;
		try {
			tmdb = new TheMovieDbApi(this.apiKey);

			// tmdb.getConfiguration().setBaseUrl("http://api.themoviedb.org/3/");
			// search with different combinations to find the movie.
			// implode titles to one title
			String searchTitle = Helper.implode(titles, " - ", null, null);
			if (searchTitle != null) {
				// for each title, get imploded title and cut one part out of
				// it, if no movie is found.
				for (@SuppressWarnings("unused")
				final String title : titles) {
					// search for all titles and year (if exists)
					tempList = (ArrayList<MovieDb>) tmdb.searchMovie(searchTitle, (year != null ? year : 0), this.langKey, true, 0)
							.getResults();

					if ((tempList != null) && (tempList.size() > 0)) {
						// if found, break loop
						break;
					}

					// search with all titles and without year if year exists
					if ((year != null) && (year > -1)) {
						tempList.addAll(tmdb.searchMovie(searchTitle, 0, this.langKey, true, 0).getResults());

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
							tempList.addAll(tmdb.searchMovie(searchTitle, 0, this.langKey, true, 0).getResults());
							break;
						}
						break;
					}
				}

			}
		} catch (final MovieDbException e) {
			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e1) {
				e1.printStackTrace();
			}
			Debug.log(
					Debug.LEVEL_ERROR,
					e.getExceptionType() + " in MovieDBCollector, try to research directly (Response: " + e.getResponse() + "): "
							+ e.getMessage());
			return this.findMovie(titles, year);
			// e.printStackTrace();
			/*
			 * Request Rate Limiting OR add the error items to a stack and
			 * retry.
			 * 
			 * 
			 * We do enforce a small amount of rate limiting. Please be aware
			 * that should you exceed these limits, you will receive a 503
			 * error. 30 requests every 10 seconds per IP Maximum 20
			 * simultaneous connections
			 */
		}
		// TODO if more than one result in list, than try to find the right
		int id = -1;
		if ((tempList != null) && (tempList.size() > 0)) {
			if (tempList.size() > 1) {
				// TODO reduce duplicate code
				// TODO use a fuzzy search for this:
				// http://stackoverflow.com/questions/327513/fuzzy-string-search-in-java
				// search for all titles
				for (final MovieDb movieDb : tempList) {
					// TODO put in function
					final String tempTitle = Helper.implode(titles, " - ", null, null);

					if (movieDb.getTitle().equalsIgnoreCase(tempTitle)) {
						id = movieDb.getId();
					}

					if (id > -1) {
						break;
					}
				}

				if (id == -1) {
					// search for first title only
					for (final MovieDb movieDb : tempList) {
						// TODO put in function
						final String tempTitle = titles[0];

						if (movieDb.getTitle().equalsIgnoreCase(tempTitle)) {
							id = movieDb.getId();
						}

						if (id > -1) {
							break;
						}
					}
				}
				// TODO search for year only when name is near found

				// TODO if the excact name is not the real name, try with more
				// information
				if (id == -1) {
					id = tempList.get(0).getId();
				}
			} else {
				id = tempList.get(0).getId();
			}
			tempList = null;

			final String[] appendingResponse = {
					"trailers", "images", "casts"
			};
			MovieDb loadedMovie = null;
			try {
				loadedMovie = tmdb.getMovieInfo(id, this.langKey, appendingResponse);
			} catch (final MovieDbException e) {
				e.printStackTrace();
			}
			return loadedMovie;

		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDataForFilename(String filename) {
		final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
		data.put("titles", new ArrayList<String>());
		data.put("year", -1);

		// remove file extension from filename
		final String fileExtension = Helper.getFileExtension(filename);
		filename = Helper.replaceLast(filename, fileExtension, "");

		final String separator = " - ";
		final String strPattern = "([\\.\\_\\-0-9a-zA-ZÄÖÜßäöü\\ ]+?(?= - |$))";
		final String strPatternSingle = "([\\.\\_\\-0-9a-zA-ZÄÖÜßäöü\\ ]+)";
		final String yearPattern = "([0-9]{4})+(?= - |$)";
		final String endYearPattern = "([\\ \\.]){0,1}";
		final String fullYearPattern = "(" + yearPattern + endYearPattern + ")";

		final String titleSubtitleYearPattern = strPattern + separator + strPattern + separator + fullYearPattern;
		final String titleYearPattern = strPattern + separator + fullYearPattern;
		final String titleSubtitlePattern = strPattern + separator + strPattern;
		final String titlePattern = strPatternSingle;

		final String regex = "^((" + titleSubtitleYearPattern + ")|(" + titleYearPattern + ")|(" + titleSubtitlePattern + ")|("
				+ titlePattern + "))" + "(.*$)";

		Debug.log(Debug.LEVEL_DEBUG, regex);
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(filename);

		// 0 = all default
		// 1 = all-2
		// 2 = pattern 1 full
		// 3 = pattern 1 title
		// 4 = pattern 1 subtitle
		// 5 = pattern 1 year + next of year
		// 6 = pattern 1 year
		// 7 = pattern 1 next of year
		// 8 = pattern 2 full
		// 9 = pattern 2 title
		// 10 = pattern 2 year + next of year
		// 11 = pattern 2 year
		// 12 = pattern 2 next of year
		// 13 = pattern 3 full
		// 14 = pattern 3 title
		// 15 = pattern 3 subtitle
		// 16 = pattern 4 full
		// 17 = pattern 4 title
		// 18 = all other

		while (m.find()) {

			if (true) {
				Debug.log(Debug.LEVEL_DEBUG, "Groups: " + m.groupCount());
				for (int i = 0; i <= m.groupCount(); i++) {
					Debug.log(Debug.LEVEL_DEBUG, "Group: " + i + ": " + m.group(i));
				}
			}

			// titleSubtitleYearPattern -> GROUP 2 - GROUP 7
			if ((m.group(2) != null) && (m.group(3) != null) && (m.group(4) != null) && (m.group(6) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(3));
				((ArrayList<String>) data.get("titles")).add(m.group(4));
				data.replace("year", Integer.parseInt(m.group(6)));
			}

			// titleYearPattern -> GROUP 8 - GROUP 12
			if ((m.group(8) != null) && (m.group(9) != null) && (m.group(11) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(9));
				data.replace("year", Integer.parseInt(m.group(11)));
			}

			// titleSubtitlePattern -> GROUP 13 - GROUP 15
			if ((m.group(13) != null) && (m.group(14) != null) && (m.group(15) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(14));
				((ArrayList<String>) data.get("titles")).add(m.group(15));
			}

			// titlePattern -> GROUP 16 - GROUP 17
			if ((m.group(16) != null) && (m.group(17) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(17));
			}
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FileAttributeList> getFileAttributeListsForItem(final FileItem fileItem) {
		ArrayList<FileAttributeList> resultList = null;
		if (fileItem != null) {
			String[] titles = null;
			Integer year = null;

			final ConcurrentHashMap<String, Object> data = (ConcurrentHashMap<String, Object>) this.getDataForFilename(fileItem.getName());
			if ((data != null) && data.containsKey("titles") && data.containsKey("year")) {
				titles = ((ArrayList<Key<String>>) data.get("titles")).toArray(new String[((ArrayList<String>) data.get("titles")).size()]);
				year = (Integer) data.get("year");

				if ((titles != null) && (titles.length > 0)) {
					resultList = this.getMovieInfo(titles, year, fileItem);
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

	private ArrayList<FileAttributeList> getMovieInfo(final String[] titles, final Integer year, final FileItem fileItem) {
		final ArrayList<FileAttributeList> tempKeyValueList = new ArrayList<FileAttributeList>();
		MovieDb movie;
		try {
			movie = this.findMovie(titles, year);

			final String infoType = "themoviedb";

			if (movie != null) {
				final FileAttributeList attributeList = new FileAttributeList();
				ArrayList<KeyValue<String, Object>> keyValueList = null;
				keyValueList = this.fillKeyValueList(movie, infoType, fileItem);

				if (keyValueList != null) {
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
			}
		} catch (final IOException e) {
			Debug.log(Debug.LEVEL_ERROR, e.getMessage());
		}
		return tempKeyValueList;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Integer, Object> getOptionsFor(final String type) {
		String optionsStr = "";
		final String basePath = "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/";
		final String secureBasePath = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/";

		optionsStr = MediaItem.OPTION_WEB_BASE_PATH + "|" + basePath + ";" + MediaItem.OPTION_WEB_SECURE_BASE_PATH + "|" + secureBasePath;

		if (type.equalsIgnoreCase("poster")) {
			optionsStr += ";" + MediaItem.OPTION_SIZES + "|w92,w154,w185,w342,w500,original";
		} else if (type.equalsIgnoreCase("backdrop")) {
			optionsStr += ";" + MediaItem.OPTION_SIZES + "|w300,w780,w1280,original";
		} else if (type.equalsIgnoreCase("youtube")) {
			optionsStr = MediaItem.OPTION_WEB_BASE_PATH + "|http://www.youtube.com/watch?v=;" + MediaItem.OPTION_WEB_ISDIRECT + "|false";
		}

		return (ConcurrentHashMap<Integer, Object>) Helper.explode(optionsStr, ";", "|");
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return this.valuesToAdd;
	}

}

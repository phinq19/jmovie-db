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
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.mdb.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.Value;
import com.omertron.thetvdbapi.TheTVDBApi;
import com.omertron.thetvdbapi.model.Episode;
import com.omertron.thetvdbapi.model.Series;

/**
 * @author lalbrecht
 * @see "https://github.com/Omertron/api-thetvdb"
 * 
 */
public class TheTVDBCollector extends ACollector {

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	private final String												apiKey					= RessourceBundleEx.getInstance()
																										.getProperty("apikey.thetvdb");
	private final String												langKey					= "de";

	public TheTVDBCollector(final MainController mainController, final IController controller) {
		super(mainController, controller);

		this.fileAttributeListToAdd = new ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>();
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	@Override
	public void doCollect() {
		this.fileAttributeListToAdd.clear();
		for (final FileItem item : this.fileItems) {
			// collect all data for all found items in the list
			if (item.getFiletype().equalsIgnoreCase("serie")) {
				this.fileAttributeListToAdd.put(item, this.getFileAttributeListsForItem(item));
			}

		}
	}

	private Episode findEpisode(final String seriesId, final int seasonNr, final int episodeNr) {
		Episode tempEpisode = null;
		TheTVDBApi ttvdb = null;
		try {
			ttvdb = new TheTVDBApi(this.apiKey);

			if (seriesId != null && seasonNr > 0 && episodeNr > 0) {
				tempEpisode = ttvdb.getEpisode(seriesId, seasonNr, episodeNr, "de");
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return tempEpisode;
	}

	private Series findSerie(final String[] titles) {
		ArrayList<Series> tempList = null;
		TheTVDBApi ttvdb = null;
		try {
			ttvdb = new TheTVDBApi(this.apiKey);
			// search with different combinations to find the serie.
			// implode titles to one title
			String searchTitle = Helper.implode(titles, " - ", null, null);
			if (searchTitle != null) {
				// for each title, get imploded title and cut one part out of
				// it, if no movie is found.
				for (@SuppressWarnings("unused")
				final String title : titles) {
					// search for title
					tempList = (ArrayList<Series>) ttvdb.searchSeries(searchTitle, this.langKey);

					if ((tempList != null) && (tempList.size() > 0)) {
						// if found, break loop
						break;
					}

					// if in string contains " - ", then get subpart of string
					// without LAST " - <else>".
					if (searchTitle.indexOf(" - ") > -1) {
						searchTitle = searchTitle.substring(0, searchTitle.lastIndexOf(" - "));
					}
				}

			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		// TODO if more than one result in list, than try to find the right
		String id = null;
		if ((tempList != null) && (tempList.size() > 0)) {
			if (tempList.size() > 1) {
				id = tempList.get(0).getId();
			} else {
				id = tempList.get(0).getId();
			}
			tempList = null;

			Series loadedSerie = null;
			try {
				loadedSerie = ttvdb.getSeries(id, this.langKey);
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return loadedSerie;

		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDataForFilename(final String filename) {
		final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
		data.put("titles", new ArrayList<String>());
		data.put("episode", new String());

		final String strPattern = "[\\.\\-\\_0-9a-zA-ZÄÖÜßäöü\\ ]+";
		final String episodePattern = "S[0-9]{1,2}E[0-9]{1,2}";
		final String regex = "(^(" + strPattern + ") - (" + strPattern + ") - (" + episodePattern + ")+)|(^(" + strPattern + ") - ("
				+ episodePattern + ")+)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(filename);

		while (m.find()) {
			if ((m.group(2) != null) && (m.group(3) != null) && (m.group(4) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(2));
				((ArrayList<String>) data.get("titles")).add(m.group(3));
				data.replace("episode", Integer.parseInt(m.group(4)));
			}

			if ((m.group(6) != null) && (m.group(7) != null)) {
				((ArrayList<String>) data.get("titles")).add(m.group(6));
				data.replace("episode", m.group(7));
			}
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FileAttributeList> getFileAttributeListsForItem(final FileItem item) {
		ArrayList<FileAttributeList> resultList = null;
		if (item != null) {
			String[] titles = null;
			String episode = null;

			final ConcurrentHashMap<String, Object> data = (ConcurrentHashMap<String, Object>) this.getDataForFilename(item.getName());
			if ((data != null) && data.containsKey("titles") && data.containsKey("episode")) {
				titles = ((ArrayList<Key<String>>) data.get("titles")).toArray(new String[data.size()]);
				episode = (String) data.get("episode");

				if ((titles != null) && (titles.length > 0)) {
					resultList = this.getSerieInfo(titles, episode);
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
		return "TheTVDB";
	}

	@Override
	public ArrayList<Key<String>> getKeysToAdd() {
		return this.keysToAdd;
	}

	private ArrayList<FileAttributeList> getSerieInfo(final String[] titles, final String episodeStr) {
		final ArrayList<FileAttributeList> tempKeyValueList = new ArrayList<FileAttributeList>();
		final Series serie = this.findSerie(titles);
		final String infoType = "thetvdb";

		if (serie != null) {
			Episode episode = null;
			final int seasonNr = this.getSeasonFromEpisodeStr(episodeStr);
			final int episodeNr = this.getEpisodeFromEpisodeStr(episodeStr);
			if (seasonNr > 0 && episodeNr > 0) {
				episode = this.findEpisode(serie.getId(), seasonNr, episodeNr);
			}
			final FileAttributeList attributeList = new FileAttributeList();
			final ArrayList<KeyValue<String, Object>> keyValueList = new ArrayList<KeyValue<String, Object>>();
			// TODO move to own method

			// add general infos
			if (serie.getId() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("ttvdb_id", infoType, "general", false, false),
						new Value<Object>(new Integer(serie.getId()))));
			}
			if (serie.getImdbId() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("imdb_id", infoType, "general", false, false),
						new Value<Object>(serie.getImdbId())));
			}
			if (serie.getSeriesName() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("title", infoType, "general", false, false),
						new Value<Object>(serie.getSeriesName())));
			}
			if (serie.getOverview() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("overview", infoType, "general", false, false),
						new Value<Object>(serie.getOverview())));
			}
			if (serie.getNetwork() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("network", infoType, "general", false, false),
						new Value<Object>(serie.getNetwork())));
			}
			if (serie.getZap2ItId() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("Zap2It_id", infoType, "general", false, false),
						new Value<Object>(serie.getZap2ItId())));
			}

			// add series facts
			if (serie.getSeriesId() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("collection_id", infoType, "facts", false, false),
						new Value<Object>(serie.getSeriesId())));
			}
			if (serie.getStatus() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("status", infoType, "facts", false, false),
						new Value<Object>(serie.getStatus())));
			}
			if (new Integer(serie.getRuntime()) != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("runtime", infoType, "facts", false, false),
						new Value<Object>(serie.getRuntime())));
			}
			if (serie.getLanguage() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("language", infoType, "facts", false, false),
						new Value<Object>(serie.getLanguage())));
			}
			if (serie.getContentRating() != null) {
				keyValueList.add(new KeyValue<String, Object>(new Key<String>("content_rating", infoType, "facts", false, false),
						new Value<Object>(serie.getContentRating())));
			}

			// add genres
			if (serie.getGenres() != null) {
				for (final String genre : serie.getGenres()) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("genre", infoType, "genre", false, true),
							new Value<Object>(genre)));
				}
			}

			// add episode infos
			if (episode != null) {
				if (episode.getAbsoluteNumber() != null) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("absolute_number", infoType, "episode", false, false),
							new Value<Object>(episode.getAbsoluteNumber())));
				}
				if (episode.getEpisodeName() != null) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("name", infoType, "episode", false, false),
							new Value<Object>(episode.getCombinedEpisodeNumber())));
				}
				if (episode.getEpisodeNumber() > 0) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("episode_number", infoType, "episode", false, false),
							new Value<Object>(episode.getEpisodeNumber())));
				}
				if (episode.getSeasonNumber() > 0) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("season_number", infoType, "episode", false, false),
							new Value<Object>(episode.getSeasonNumber())));
				}
				if (episode.getDirectors() != null && episode.getGuestStars().size() > 0) {
					for (final String directors : episode.getGuestStars()) {
						keyValueList.add(new KeyValue<String, Object>(new Key<String>("directors", infoType, "episode", false, true),
								new Value<Object>(directors)));
					}
				}
				if (episode.getGuestStars() != null && episode.getGuestStars().size() > 0) {
					for (final String guestStar : episode.getGuestStars()) {
						keyValueList.add(new KeyValue<String, Object>(new Key<String>("guest_stars", infoType, "episode", false, true),
								new Value<Object>(guestStar)));
					}
				}
				if (episode.getOverview() != null) {
					keyValueList.add(new KeyValue<String, Object>(new Key<String>("overview", infoType, "episode", false, false),
							new Value<Object>(episode.getOverview())));
				}
				if (episode.getWriters() != null && episode.getGuestStars().size() > 0) {
					for (final String writers : episode.getGuestStars()) {
						keyValueList.add(new KeyValue<String, Object>(new Key<String>("writers", infoType, "episode", false, true),
								new Value<Object>(writers)));
					}
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

	/**
	 * episodeStr is a String like "S01E10" which means: Season 01, Episode 10.
	 * 
	 * @param episodeStr
	 * @return int
	 */
	private int getSeasonFromEpisodeStr(final String episodeStr) {
		final String regex = "S([0-9]{1,2})E([0-9]{1,2})";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(episodeStr);

		if (m.find() && m.groupCount() > 1 && m.group(1) != null) {
			return Integer.parseInt(m.group(1));
		}

		return -1;
	}

	/**
	 * episodeStr is a String like "S10E01" which means: Season 10, Episode 01.
	 * 
	 * @param episodeStr
	 * @return int
	 */
	private int getEpisodeFromEpisodeStr(final String episodeStr) {
		final String regex = "S([0-9]{1,2})E([0-9]{1,2})";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(episodeStr);

		if (m.find() && m.groupCount() > 1 && m.group(2) != null) {
			return Integer.parseInt(m.group(1));
		}

		return -1;
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return this.valuesToAdd;
	}

}

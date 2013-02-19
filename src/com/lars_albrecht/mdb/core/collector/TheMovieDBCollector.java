/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.mdb.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.controller.interfaces.IController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.Value;

/**
 * @author albrela
 * 
 */
public class TheMovieDBCollector extends ACollector {

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	public TheMovieDBCollector(final MainController mainController,
			final IController controller) {
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
			this.fileAttributeListToAdd.put(item,
					this.getFileAttributeListsForItem(item));

		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FileAttributeList> getFileAttributeListsForItem(
			final FileItem item) {
		final ArrayList<FileAttributeList> resultList = new ArrayList<FileAttributeList>();

		String[] titles = null;
		Integer year = null;

		final ConcurrentHashMap<String, Object> data = (ConcurrentHashMap<String, Object>) this
				.getDataForFilename(item.getName());
		titles = (String[]) ((ArrayList<String>) data.get("titles")).toArray();
		year = (Integer) data.get("year");

		final HashMap<String, ArrayList<KeyValue<String, Object>>> sectionsWithKeyValue = this
				.getMovieInfo(titles, year);

		for (final Map.Entry<String, ArrayList<KeyValue<String, Object>>> section : sectionsWithKeyValue
				.entrySet()) {
			resultList.add(new FileAttributeList(section.getValue(), section
					.getKey(), item.getId()));
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDataForFilename(final String filename) {
		final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();
		data.put("titles", new ArrayList<String>());
		data.put("year", -1);

		final String regex = "(^([0-9a-zA-ZÄÖÜßäöü\\ ]+) - ([0-9a-zA-ZÄÖÜßäöü\\ ]+) - ([0-9]{4})+)|(^([0-9a-zA-ZÄÖÜßäöü\\ ]+) - ([0-9a-zA-ZÄÖÜßäöü\\ ]+))|(^([0-9a-zA-ZÄÖÜßäöü\\ ]+) - ([0-9]{4})+)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(filename);

		while (m.find()) {
			if (m.group(2) != null && m.group(3) != null && m.group(4) != null) {
				((ArrayList<String>) data.get("titles")).add(m.group(2));
				((ArrayList<String>) data.get("titles")).add(m.group(3));
				data.replace("year", m.group(3));
			}

			if (m.group(5) != null && m.group(6) != null) {
				((ArrayList<String>) data.get("titles")).add(m.group(5));
				data.replace("year", m.group(6));
			}

			if (m.group(8) != null && m.group(9) != null && m.group(10) != null) {
				((ArrayList<String>) data.get("titles")).add(m.group(8));
				((ArrayList<String>) data.get("titles")).add(m.group(9));
				data.replace("year", m.group(10));
			}

		}
		return data;
	}

	private HashMap<String, ArrayList<KeyValue<String, Object>>> getMovieInfo(
			final String[] titles, final Integer year) {

		return null;
	}

	@Override
	public ArrayList<Key<String>> getKeysToAdd() {
		return null;
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return null;
	}

	@Override
	public ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> getFileAttributeListToAdd() {
		return null;
	}

	@Override
	public String getInfoType() {
		return "TheMovieDB";
	}

}

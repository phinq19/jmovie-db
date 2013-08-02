/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.jmoviedb.mdb.external.ProcExec;
import com.lars_albrecht.mdb.main.core.collector.abstracts.ACollector;
import com.lars_albrecht.mdb.main.core.models.FileAttributeList;
import com.lars_albrecht.mdb.main.core.models.FileItem;
import com.lars_albrecht.mdb.main.core.models.Key;
import com.lars_albrecht.mdb.main.core.models.KeyValue;
import com.lars_albrecht.mdb.main.core.models.Value;

/**
 * @author lalbrecht
 * 
 * @see "http://mediainfo.sourceforge.net/"
 * 
 */
public class MediaInfoCollector extends ACollector {

	public static int													SECTION_NONE			= -1;
	public static int													SECTION_GENERAL			= 0;
	public static int													SECTION_VIDEO			= 1;
	public static int													SECTION_AUDIO			= 2;
	public static int													SECTION_MENU			= 3;

	public static String												SECTIONNAME_NONE		= "none";
	public static String												SECTIONNAME_GENERAL		= "general";
	public static String												SECTIONNAME_VIDEO		= "video";
	public static String												SECTIONNAME_AUDIO		= "audio";
	public static String												SECTIONNAME_MENU		= "menu";

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	public MediaInfoCollector() {
		super();
		this.addType("movie");
		this.addType("serie");
		this.fileAttributeListToAdd = new ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>();
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	@Override
	public void doCollect() {
		this.fileAttributeListToAdd.clear();
		for (final FileItem item : this.getFileItems()) {
			// collect all data for all found items in the list
			this.fileAttributeListToAdd.put(item, this.getFileAttributeListsForItem(item));
		}
	}

	private String getDataStrForFile(final String filepath) {
		String line = null;
		final String command = this.mainController.getConfigHandler().getConfigOptionModuleCollectorMediainfoPathCli();
		final String templatePath = this.mainController.getConfigHandler().getConfigOptionModuleCollectorMediainfoPathTemplate();
		if ((command != null) && (templatePath != null) && new File(command).exists() && new File(templatePath).exists()) {
			final ProcExec pe = new ProcExec();

			final String[] parameters = {
					"--Inform=file://" + templatePath, "\"" + filepath + "\""
			};

			try {
				final BufferedReader br = pe.getProcOutput(command, parameters);
				line = br.readLine();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				pe.closeProc();
			}
		}

		return line;
	}

	private ArrayList<FileAttributeList> getFileAttributeListsForItem(final FileItem item) {
		final ArrayList<FileAttributeList> resultList = new ArrayList<FileAttributeList>();

		final HashMap<String, ArrayList<KeyValue<String, Object>>> sectionsWithKeyValue = this.parseDataString(this.getDataStrForFile(item
				.getFullpath()));

		if ((sectionsWithKeyValue != null) && (sectionsWithKeyValue.size() > 0)) {
			for (final Map.Entry<String, ArrayList<KeyValue<String, Object>>> section : sectionsWithKeyValue.entrySet()) {
				resultList.add(new FileAttributeList(section.getValue(), section.getKey(), item.getId(), this.getInfoType()));
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
		return "MediaInfo";
	}

	@Override
	public ArrayList<Key<String>> getKeysToAdd() {
		return this.keysToAdd;
	}

	/**
	 * 
	 * @param sectionKeyStr
	 * @return
	 */
	private String getSectionname(final String sectionKeyStr) {
		if (sectionKeyStr.equalsIgnoreCase("GENERAL")) {
			return MediaInfoCollector.SECTIONNAME_GENERAL;
		} else if (sectionKeyStr.equalsIgnoreCase("VIDEO")) {
			return MediaInfoCollector.SECTIONNAME_VIDEO;
		} else if (sectionKeyStr.equalsIgnoreCase("AUDIO")) {
			return MediaInfoCollector.SECTIONNAME_AUDIO;
		} else if (sectionKeyStr.equalsIgnoreCase("MENU")) {
			return MediaInfoCollector.SECTIONNAME_MENU;
		} else {
			return MediaInfoCollector.SECTIONNAME_NONE;
		}
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return this.valuesToAdd;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, ArrayList<KeyValue<String, Object>>> parseDataString(final String dataString) {
		String[] sections = null;
		HashMap<String, ArrayList<KeyValue<String, Object>>> resultList = null;
		if (dataString != null) {
			sections = dataString.split("\\~");
			if (sections != null) {
				resultList = new HashMap<String, ArrayList<KeyValue<String, Object>>>();
				final ArrayList<Key<?>> keys = this.mainController.getDataHandler().getKeys();
				final ArrayList<Value<?>> values = this.mainController.getDataHandler().getValues();
				for (String completeSection : sections) {
					if (completeSection != null && !completeSection.equalsIgnoreCase("")) {
						final String sectionName = completeSection
								.substring(completeSection.indexOf("{") + 1, completeSection.indexOf("}"));
						completeSection = completeSection.substring(completeSection.indexOf("}") + 1);
						final ArrayList<KeyValue<String, Object>> keyValueList = new ArrayList<KeyValue<String, Object>>();
						final String[] items = completeSection.split("\\|");
						for (final String string : items) {
							final String[] keyValues = string.split("\\:");
							if (keyValues.length > 1) {
								Key<String> key = new Key<String>(keyValues[0], this.getInfoType(), this.getSectionname(sectionName),
										false, false);
								int pos = -1;
								if (((pos = keys.indexOf(key)) > -1) && (keys.get(pos) instanceof Key<?>)) {
									key = (Key<String>) keys.get(pos);
								} else {
									if (!this.keysToAdd.contains(key)) {
										this.keysToAdd.add(key);
									}
								}
								Value<Object> value = new Value<Object>(keyValues[1]);
								pos = -1;
								if (((pos = values.indexOf(value)) > -1) && (values.get(pos) instanceof Value<?>)) {
									value = (Value<Object>) values.get(pos);
								} else {
									if (!this.valuesToAdd.contains(value)) {
										this.valuesToAdd.add(value);
									}
								}

								keyValueList.add(new KeyValue<String, Object>(key, value));
							}

						}
						resultList.put(this.getSectionname(sectionName), keyValueList);
					}
				}
			}

		}

		return resultList;
	}
}

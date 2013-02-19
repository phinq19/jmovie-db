/**
 * 
 */
package com.lars_albrecht.mdb.core.collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.mdb.core.controller.CollectorController;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileAttributeList;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.KeyValue;
import com.lars_albrecht.mdb.core.models.Value;
import com.lars_albrecht.mdb.external.ProcExec;

/**
 * @author albrela
 * 
 */
public class MediaInfoCollector extends ACollector {

	public static int													SECTION_GENERAL			= 0;
	public static int													SECTION_VIDEO			= 1;
	public static int													SECTION_AUDIO			= 2;
	public static int													SECTION_MENU			= 3;

	public static String												SECTIONNAME_GENERAL		= "general";
	public static String												SECTIONNAME_VIDEO		= "video";
	public static String												SECTIONNAME_AUDIO		= "audio";
	public static String												SECTIONNAME_MENU		= "menu";

	private ArrayList<Key<String>>										keysToAdd				= null;
	private ArrayList<Value<?>>											valuesToAdd				= null;
	private ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>	fileAttributeListToAdd	= null;

	public MediaInfoCollector(final MainController mainController,
			final CollectorController controller) {
		super(mainController, controller);
		this.fileAttributeListToAdd = new ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>>();
		this.keysToAdd = new ArrayList<Key<String>>();
		this.valuesToAdd = new ArrayList<Value<?>>();
	}

	@Override
	public void doCollect() {
		// TODO clean up -> move to abstract class
		this.fileAttributeListToAdd.clear();
		for (final FileItem item : this.fileItems) {
			// collect all data for all found items in the list
			this.fileAttributeListToAdd.put(item,
					this.getFileAttributeListsForItem(item));

		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private ArrayList<Key<String>> prepareDataToSave() {
		final ArrayList<FileAttributeList> fileAttributeList = new ArrayList<FileAttributeList>();
		final ArrayList<Key<String>> keyList = new ArrayList<Key<String>>();
		for (final Map.Entry<FileItem, ArrayList<FileAttributeList>> entry : this.fileAttributeListToAdd
				.entrySet()) {
			fileAttributeList.addAll(entry.getValue());
		}

		for (final FileAttributeList fileAttrib : fileAttributeList) {
			final ArrayList<KeyValue<String, Object>> singleList = fileAttrib
					.getKeyValues();
			for (final KeyValue<String, Object> keyValue : singleList) {
				keyList.add(keyValue.getKey());
			}
		}
		return Helper.removeDuplicatedEntries(keyList);
	}

	private ArrayList<FileAttributeList> getFileAttributeListsForItem(
			final FileItem item) {
		final ArrayList<FileAttributeList> resultList = new ArrayList<FileAttributeList>();

		final HashMap<String, ArrayList<KeyValue<String, Object>>> sectionsWithKeyValue = this
				.parseDataString(this.getDataStrForFile(item.getFullpath()));

		for (final Map.Entry<String, ArrayList<KeyValue<String, Object>>> section : sectionsWithKeyValue
				.entrySet()) {
			resultList.add(new FileAttributeList(section.getValue(), section
					.getKey(), item.getId()));
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, ArrayList<KeyValue<String, Object>>> parseDataString(
			final String dataString) {
		String[] sections = null;
		HashMap<String, ArrayList<KeyValue<String, Object>>> resultList = null;
		if (dataString != null) {
			sections = dataString.split("\\~");

			if (sections != null) {
				resultList = new HashMap<String, ArrayList<KeyValue<String, Object>>>();
				int i = 0;
				final ArrayList<Key<?>> keys = this.mainController
						.getDataHandler().getKeys();
				final ArrayList<Value<?>> values = this.mainController
						.getDataHandler().getValues();
				for (final String section : sections) {
					final ArrayList<KeyValue<String, Object>> keyValueList = new ArrayList<KeyValue<String, Object>>();
					final String[] items = section.split("\\|");
					for (final String string : items) {
						final String[] keyValues = string.split("\\:");
						if (keyValues.length > 1) {
							Key<String> key = new Key<String>(keyValues[0],
									this.getInfoType(), this.getSectionname(i));
							int pos = -1;
							if ((pos = keys.indexOf(key)) > -1
									&& (keys.get(pos) instanceof Key<?>)) {
								key = (Key<String>) keys.get(pos);
							} else {
								if (!this.keysToAdd.contains(key)) {
									this.keysToAdd.add(key);
								}
							}
							Value<Object> value = new Value<Object>(
									keyValues[1]);
							pos = -1;
							if ((pos = values.indexOf(value)) > -1
									&& (values.get(pos) instanceof Value<?>)) {
								value = (Value<Object>) values.get(pos);
							} else {
								if (!this.valuesToAdd.contains(value)) {
									this.valuesToAdd.add(value);
								}
							}

							keyValueList.add(new KeyValue<String, Object>(key,
									value));
						}

					}
					resultList.put(this.getSectionname(i), keyValueList);
					i++;
				}
			}

		}

		return resultList;
	}

	private String getDataStrForFile(final String filepath) {
		final ProcExec pe = new ProcExec();

		final String command = RessourceBundleEx.getInstance().getProperty(
				"module.collector.mediainfo.path.cliexe");
		final String templatePath = RessourceBundleEx.getInstance()
				.getProperty("module.collector.mediainfo.path.template");
		final String[] parameters = {
				"--Inform=file://" + templatePath, "\"" + filepath + "\""
		};
		String line = null;

		try {
			final BufferedReader br = pe.getProcOutput(command, parameters);
			line = br.readLine();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			pe.closeProc();
		}

		return line;
	}

	@Override
	public String getInfoType() {
		return "MediaInfo";
	}

	/**
	 * 
	 * @param sectionId
	 * @return
	 */
	private String getSectionname(final int sectionId) {
		switch (sectionId) {
			case 0:
				return MediaInfoCollector.SECTIONNAME_GENERAL;
			case 1:
				return MediaInfoCollector.SECTIONNAME_VIDEO;
			case 2:
				return MediaInfoCollector.SECTIONNAME_AUDIO;
			case 3:
				return MediaInfoCollector.SECTIONNAME_MENU;
			default:
				return null;
		}
	}

	@Override
	public ArrayList<Key<String>> getKeysToAdd() {
		return this.keysToAdd;
	}

	@Override
	public ArrayList<Value<?>> getValuesToAdd() {
		return this.valuesToAdd;
	}

	@Override
	public ConcurrentHashMap<FileItem, ArrayList<FileAttributeList>> getFileAttributeListToAdd() {
		return this.fileAttributeListToAdd;
	}

}

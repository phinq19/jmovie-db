/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.TypeInformation;
import com.lars_albrecht.mdb.core.models.Value;

/**
 * @author lalbrecht
 * 
 */
public class ObjectHandler {

	public static ArrayList<FileItem> castObjectListToFileItemList(final ArrayList<Object> oList) {
		final ArrayList<FileItem> resultList = new ArrayList<FileItem>();
		if (oList != null) {
			for (final Object oItem : oList) {
				if (oItem instanceof FileItem) {
					resultList.add((FileItem) oItem);
				}
			}
		}
		return resultList;
	}

	public static ArrayList<Key<?>> castObjectListToKeyList(final ArrayList<Object> oList) {

		final ArrayList<Key<?>> resultList = new ArrayList<Key<?>>();
		for (final Object oItem : oList) {
			if (oItem instanceof Key) {
				resultList.add((Key<?>) oItem);
			}
		}

		return resultList;
	}

	public static ArrayList<TypeInformation> castObjectListToTypeInformationList(final ArrayList<Object> oList) {
		final ArrayList<TypeInformation> resultList = new ArrayList<TypeInformation>();
		for (final Object oItem : oList) {
			if (oItem instanceof TypeInformation) {
				resultList.add((TypeInformation) oItem);
			}
		}

		return resultList;
	}

	public static ArrayList<Value<?>> castObjectListToValueList(final ArrayList<Object> oList) {

		final ArrayList<Value<?>> resultList = new ArrayList<Value<?>>();
		for (final Object oItem : oList) {
			if (oItem instanceof Value) {
				resultList.add((Value<?>) oItem);
			}
		}

		return resultList;
	}

	public static ArrayList<File> castStringListToFileList(final ArrayList<String> oList) {
		final ArrayList<File> resultList = new ArrayList<File>();
		if (oList != null) {
			for (final String oItem : oList) {
				if (oItem instanceof String) {
					resultList.add(new File(oItem));
				}
			}
		}

		return resultList;
	}

	/**
	 * 
	 * @param foundFilesList
	 * @return ArrayList<FileItem>
	 */
	public static ArrayList<FileItem> fileListToFileItemList(final ArrayList<File> foundFilesList) {
		final ArrayList<FileItem> tempFileItemList = new ArrayList<FileItem>();
		for (final File file : foundFilesList) {
			try {
				tempFileItemList.add(new FileItem(file.getName(), file.getAbsolutePath(), file.getParent(), file.length(), Helper
						.getFileExtension(file.getName()), null, null));

			} catch (final NullPointerException e) {
				Debug.log(Debug.LEVEL_ERROR, "null pointer " + file + " " + file.getName() + " - " + file.getAbsolutePath());
			}
		}

		return tempFileItemList;
	}

	public static String fileItemListToJSON(final ArrayList<FileItem> fileItemList) {
		String jsonString = null;

		if (fileItemList != null && fileItemList.size() > 0) {
			jsonString = "{";
			// "{\"BigBuckBunny\" : \"BigBuckBunny\"}"
			int i = 0;
			for (final FileItem fileItem : fileItemList) {
				jsonString += "\"" + fileItem.getName() + "\"" + ":" + "\"" + fileItem.getName() + "\"";
				if (i < fileItemList.size() - 1) {
					jsonString += ",";
				}
				i++;
			}
			jsonString += "}";

		}

		return jsonString;
	}
}

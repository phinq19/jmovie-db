/**
 * 
 */
package com.lars_albrecht.mdb.core.handler;

import java.io.File;
import java.util.ArrayList;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.models.Key;
import com.lars_albrecht.mdb.core.models.TypeInformation;
import com.lars_albrecht.mdb.core.models.Value;

/**
 * @author lalbrecht
 * 
 */
public class TypeHandler {

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
		for (final String oItem : oList) {
			if (oItem instanceof String) {
				resultList.add(new File(oItem));
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
			tempFileItemList.add(new FileItem(file.getName(), file.getAbsolutePath(), file.getParent(), file.length(), Helper
					.getFileExtension(file.getName()), Helper.getCurrentTimestamp().intValue()));
		}

		return tempFileItemList;
	}
}

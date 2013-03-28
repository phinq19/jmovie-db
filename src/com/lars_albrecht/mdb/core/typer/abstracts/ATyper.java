/**
 * 
 */
package com.lars_albrecht.mdb.core.typer.abstracts;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * @author albrela
 * 
 */
public abstract class ATyper {

	protected MainController	mainController	= null;

	public ATyper(final MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Iterates through fileItemList and returns the list after setting a type.
	 * 
	 * @param fileItemList
	 * @return ArrayList<FileItem>
	 */
	public final ArrayList<FileItem> fillFileItemsWithType(final ArrayList<FileItem> fileItemList) {
		final ArrayList<FileItem> tempFileItemList = new ArrayList<FileItem>();
		String tempFileItemStr = null;
		for (final FileItem fileItem : fileItemList) {
			if (fileItem.getFiletype() == null) {
				tempFileItemStr = this.getTypeForFileItem(fileItem);
				if (tempFileItemStr != null) {
					fileItem.setFiletype(tempFileItemStr);
				}
			}
			tempFileItemList.add(fileItem);
		}
		return tempFileItemList;
	}

	/**
	 * Override this method to get a type.
	 * 
	 * @param fileItem
	 * @return String
	 */
	protected abstract String getTypeForFileItem(final FileItem fileItem);

	/**
	 * Returns a list of types for the given typer.
	 * 
	 * @return ArrayList<String>
	 */
	public abstract ArrayList<String> getTypes();

}

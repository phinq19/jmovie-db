/**
 * 
 */
package com.lars_albrecht.mdb.main.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.main.core.models.FileItem;
import com.lars_albrecht.mdb.main.core.typer.VideoTyper;
import com.lars_albrecht.mdb.main.core.typer.abstracts.ATyper;

/**
 * @author lalbrecht
 * 
 */
public class TypeController {

	public static final int		TYPE_ALL		= 0;
	public static final int		TYPE_MOVIE		= 1;
	public static final int		TYPE_SERIE		= 2;

	private MainController		mainController	= null;
	private ArrayList<ATyper>	typers			= null;
	private ArrayList<String>	availableTypes	= null;

	public TypeController(final MainController mainController) {
		this.mainController = mainController;
		this.typers = new ArrayList<ATyper>();
		this.availableTypes = new ArrayList<String>();
		this.initTyper();
	}

	/**
	 * Find the types for each given FileItem in fileItems.
	 * 
	 * @param fileItems
	 * @return ArrayList<FileItem>
	 */
	public ArrayList<FileItem> findOutType(final ArrayList<FileItem> fileItems) {
		final ArrayList<FileItem> tempFileItemList = new ArrayList<FileItem>();
		for (final ATyper typer : this.typers) {
			tempFileItemList.addAll(typer.fillFileItemsWithType(fileItems));
		}

		return tempFileItemList;
	}

	/**
	 * Initialize the typer.
	 */
	private void initTyper() {
		this.typers.add(new VideoTyper(this.mainController));

		for (final ATyper typer : this.typers) {
			final ArrayList<String> tempTypes = typer.getTypes();
			if (tempTypes != null && tempTypes.size() > 0) {
				this.availableTypes.addAll(tempTypes);
			}
		}
	}

}

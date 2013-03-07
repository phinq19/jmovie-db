/**
 * 
 */
package com.lars_albrecht.mdb.core.controller;

import java.util.ArrayList;

import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.typer.MovieTyper;
import com.lars_albrecht.mdb.core.typer.SeriesTyper;
import com.lars_albrecht.mdb.core.typer.abstracts.ATyper;

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

	public TypeController(final MainController mainController) {
		this.mainController = mainController;
		this.typers = new ArrayList<ATyper>();
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
		this.typers.add(new SeriesTyper(this.mainController));
		this.typers.add(new MovieTyper(this.mainController));
	}

}

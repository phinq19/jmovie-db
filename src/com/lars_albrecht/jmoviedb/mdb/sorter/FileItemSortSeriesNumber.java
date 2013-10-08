package com.lars_albrecht.jmoviedb.mdb.sorter;

import java.util.ArrayList;
import java.util.Comparator;

import com.lars_albrecht.mdb.main.core.handler.datahandler.AttributeHandler;
import com.lars_albrecht.mdb.main.core.handler.datahandler.abstracts.ADataHandler;
import com.lars_albrecht.mdb.main.core.models.FileAttributeList;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;

/**
 * @author lalbrecht
 * @param <FileItem>
 * 
 */
public class FileItemSortSeriesNumber implements Comparator<FileItem> {

	public final static int	SORT_ASC	= 0;
	public final static int	SORT_DESC	= 1;

	private int				sort		= FileItemSortSeriesNumber.SORT_ASC;

	public FileItemSortSeriesNumber(final int sort) {
		this.sort = sort;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(final FileItem item1, final FileItem item2) {
		final ArrayList<FileAttributeList> item1FileAttributes = (ArrayList<FileAttributeList>) ADataHandler.getHandlerDataFromFileItem(
				item1, AttributeHandler.class);

		final ArrayList<FileAttributeList> item2FileAttributes = (ArrayList<FileAttributeList>) ADataHandler.getHandlerDataFromFileItem(
				item2, AttributeHandler.class);

		Object item1Number = null;
		Object item2Number = null;
		for (final FileAttributeList fileAttributeList : item1FileAttributes) {
			item1Number = fileAttributeList.getValueByKey("absolute_number");
			if (item1Number != null) {
				break;
			}
		}
		for (final FileAttributeList fileAttributeList : item2FileAttributes) {
			item2Number = fileAttributeList.getValueByKey("absolute_number");
			if (item2Number != null) {
				break;
			}
		}

		if (item1Number != null && item1Number != "" && item2Number != null && item2Number != "") {
			if (this.sort == FileItemSortSeriesNumber.SORT_ASC) {
				return ((Integer) item2Number).compareTo(((Integer) item1Number));
			} else {
				return ((Integer) item1Number).compareTo(((Integer) item2Number));
			}
		} else {
			return -1;
		}

	}
}

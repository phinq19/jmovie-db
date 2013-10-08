package com.lars_albrecht.jmoviedb.mdb.sorter;

import java.util.Comparator;

import com.lars_albrecht.jmoviedb.mdb.helper.SeriesHelper;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;

/**
 * @author lalbrecht
 * @param <FileItem>
 * 
 */
public class FileItemSortSeriesRegExName implements Comparator<FileItem> {

	public final static int	SORT_ASC	= 0;
	public final static int	SORT_DESC	= 1;

	private int				sort		= FileItemSortSeriesRegExName.SORT_ASC;

	public FileItemSortSeriesRegExName(final int sort) {
		this.sort = sort;
	}

	@Override
	public int compare(final FileItem item1, final FileItem item2) {
		final Integer item1EpisodeSeason = SeriesHelper.getSeasonFromFileItem(item1);
		final Integer item2EpisodeSeason = SeriesHelper.getSeasonFromFileItem(item2);

		if ((item1EpisodeSeason != null) && (item2EpisodeSeason != null)) {
			if (this.sort == FileItemSortSeriesRegExName.SORT_ASC) {
				return item2EpisodeSeason.compareTo(item1EpisodeSeason);
			} else {
				return item1EpisodeSeason.compareTo(item2EpisodeSeason);
			}
		}
		return 0;

	}
}

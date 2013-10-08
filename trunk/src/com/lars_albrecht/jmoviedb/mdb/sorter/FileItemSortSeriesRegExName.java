package com.lars_albrecht.jmoviedb.mdb.sorter;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String item1EpisodeSeason = null;
		String item2EpisodeSeason = null;

		final Pattern p = Pattern.compile("(S[0-9]{1,2}E[0-9]{1,2})");
		Matcher m = null;
		m = p.matcher(item1.getName());
		if (m.find()) {
			item1EpisodeSeason = m.group(1);
		}
		m = p.matcher(item2.getName());
		if (m.find()) {
			item2EpisodeSeason = m.group(1);
		}

		if (item1EpisodeSeason != null && item2EpisodeSeason != null) {
			if (this.sort == FileItemSortSeriesRegExName.SORT_ASC) {
				return item2EpisodeSeason.compareToIgnoreCase(item1EpisodeSeason);
			} else {
				return item1EpisodeSeason.compareToIgnoreCase(item2EpisodeSeason);
			}
		}
		return 0;

	}
}

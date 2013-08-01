/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.typer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.mdb.main.core.models.FileItem;
import com.lars_albrecht.mdb.main.core.typer.abstracts.ATyper;

/**
 * @author lalbrecht
 * 
 */
public class VideoTyper extends ATyper {

	private final static String	TYPE_SERIE	= "serie";
	private final static String	TYPE_MOVIE	= "movie";

	public VideoTyper() {
		super();
	}

	@Override
	protected String getTypeForFileItem(final FileItem fileItem) {
		final Pattern pattern = Pattern.compile("(S[0-9]{1,2}E[0-9]{1,2})");
		final Matcher matcher = pattern.matcher(fileItem.getName());
		if (matcher.find()) {
			return VideoTyper.TYPE_SERIE;
		} else {
			return VideoTyper.TYPE_MOVIE;
		}
	}

	@Override
	public ArrayList<String> getTypes() {
		return new ArrayList<String>(Arrays.asList(VideoTyper.TYPE_SERIE, VideoTyper.TYPE_MOVIE));
	}

}

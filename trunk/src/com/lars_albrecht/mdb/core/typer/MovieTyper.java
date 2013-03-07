/**
 * 
 */
package com.lars_albrecht.mdb.core.typer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.models.FileItem;
import com.lars_albrecht.mdb.core.typer.abstracts.ATyper;

/**
 * @author lalbrecht
 * 
 */
public class MovieTyper extends ATyper {

	public MovieTyper(final MainController mainController) {
		super(mainController);
	}

	@Override
	protected String getTypeForFileItem(final FileItem fileItem) {
		final Pattern pattern = Pattern.compile("(S[0-9]{1,2}E[0-9]{1,2})");
		final Matcher matcher = pattern.matcher(fileItem.getName());
		if (matcher.find()) {
			return null;
		} else {
			return "movie";
		}
	}

}

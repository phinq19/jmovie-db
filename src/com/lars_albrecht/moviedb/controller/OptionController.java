/**
 * 
 */
package com.lars_albrecht.moviedb.controller;

import java.awt.Point;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.moviedb.database.DB;
import com.lars_albrecht.moviedb.exceptions.OptionsNotLoadedException;
import com.lars_albrecht.moviedb.exceptions.OptionsNotSavedException;
import com.lars_albrecht.moviedb.model.Options;

/**
 * @author lalbrecht
 * 
 */
public class OptionController {

	/**
	 * Adds an option to the database.
	 * 
	 * @param optionName
	 *            String
	 * @param optionValues
	 *            ArrayList<?>
	 * @throws SQLException
	 */
	@SuppressWarnings( { "unchecked" })
	private static void addOption(final String optionName, final ArrayList<?> optionValues) throws SQLException {
		String sql = null;
		final ArrayList<String> tempList = new ArrayList<String>();
		if((optionValues != null) && (optionValues.size() > 0)) {
			if(optionValues.get(0).getClass() == File.class) {
				for(final Object f : optionValues) {
					tempList.add(((File) f).getAbsolutePath());
				}
			} else if(optionValues.get(0).getClass() == String.class) {
				tempList.addAll((Collection<? extends String>) optionValues);
			} else if(optionValues.get(0).getClass() == Integer.class) {
				tempList.addAll((Collection<? extends String>) optionValues);
			}

			sql = "MERGE INTO options (name, values) KEY(name) VALUES('" + optionName + "', '"
					+ Helper.implode(tempList, ",", null, null) + "')";
			DB.update(sql);
		}
	}

	/**
	 * Returns an option from the database.
	 * 
	 * @param optionName
	 *            String
	 * @return ArrayList<String>
	 * @throws SQLException
	 */
	private static ArrayList<String> getOption(final String optionName) throws SQLException {
		String sql = null;
		sql = "SELECT * FROM options WHERE name = '" + optionName + "'";
		final ResultSet rs = DB.query(sql);
		if((rs != null) && rs.next()) {
			return Helper.explode(rs.getString("values"), ",");
		}
		return null;
	}

	/**
	 * Load the options and returns the fullfilled optionsclass.
	 * 
	 * @return Options
	 * @throws OptionsNotLoadedException
	 */
	public static Options loadOptions() throws OptionsNotLoadedException {
		final Options tempOptions = new Options();
		ArrayList<String> optionListTemp = null;
		try {
			// paths
			optionListTemp = OptionController.getOption("paths");
			final ArrayList<File> optionListFile = new ArrayList<File>();
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				for(final String string : optionListTemp) {
					optionListFile.add(new File(string));
				}
				tempOptions.setPaths(optionListFile);
			}

			// filenameseperator
			optionListTemp = OptionController.getOption("filenameseperator");
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				tempOptions.setFilenameSeperator(optionListTemp.get(0));
			}

			optionListTemp = OptionController.getOption("widthHeightMainWindow");
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				tempOptions.setWidthHeightMainWindow(new Point(Integer.parseInt(optionListTemp.get(0)), Integer
						.parseInt(optionListTemp.get(1))));
			}

			optionListTemp = OptionController.getOption("xYMainWindow");
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				tempOptions.setxYMainWindow(new Point(Integer.parseInt(optionListTemp.get(0)), Integer.parseInt(optionListTemp
						.get(1))));
			}

			optionListTemp = OptionController.getOption("sliderBottomPos");
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				tempOptions.setSliderBottomPos(Integer.parseInt(optionListTemp.get(0)));
			}

			optionListTemp = OptionController.getOption("refreshOnStartup");
			if((optionListTemp != null) && (optionListTemp.size() > 0)) {
				tempOptions.setRefreshOnStartup(Boolean.parseBoolean(optionListTemp.get(0)));
			}

		} catch(final SQLException e) {
			throw new OptionsNotLoadedException("Options not loaded: " + e.getMessage());
		}
		return tempOptions;
	}

	/**
	 * Saves the options.
	 * 
	 * @param options
	 *            Options
	 * @throws OptionsNotSavedException
	 */
	public static void saveOptions(final Options options) throws OptionsNotSavedException {
		// paths
		try {
			OptionController.addOption("paths", options.getPaths());
			OptionController.addOption("filenameseperator", new ArrayList<String>(Arrays.asList(options.getFilenameSeperator())));
			final ArrayList<Object> tempList = new ArrayList<Object>();
			tempList.add(options.getWidthHeightMainWindow().x);
			tempList.add(options.getWidthHeightMainWindow().y);
			OptionController.addOption("widthHeightMainWindow", tempList);
			tempList.clear();
			tempList.add(options.getxYMainWindow().x);
			tempList.add(options.getxYMainWindow().y);
			OptionController.addOption("xYMainWindow", tempList);

			OptionController.addOption("sliderBottomPos", new ArrayList<Integer>(Arrays.asList(options.getSliderBottomPos())));
		} catch(final SQLException e) {
			e.printStackTrace();
		}

	}
}

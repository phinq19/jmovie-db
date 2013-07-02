/**
 * 
 */
package com.lars_albrecht.mdb.test.core.collector;

import com.lars_albrecht.mdb.main.core.collector.TheMovieDBCollector;
import com.lars_albrecht.mdb.main.core.collector.TheTVDBCollector;
import com.lars_albrecht.mdb.main.core.controller.MainController;
import com.lars_albrecht.mdb.main.database.DB;

/**
 * @author lalbrecht
 * 
 */
public class TestUtility {

	public static TheMovieDBCollector	movieDBCollector	= null;
	public static TheTVDBCollector		tvDBCollector		= null;

	public static void init() {
		try {
			new DB().init();

			TestUtility.movieDBCollector = new TheMovieDBCollector(new MainController(), null);
			TestUtility.tvDBCollector = new TheTVDBCollector(new MainController(), null);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}

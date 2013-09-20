/**
 * 
 */
package com.lars_albrecht.jmoviedb.main;

import java.io.File;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.jmoviedb.mdb.collector.MediaInfoCollector;
import com.lars_albrecht.jmoviedb.mdb.collector.TheMovieDBCollector;
import com.lars_albrecht.jmoviedb.mdb.collector.TheTVDBCollector;
import com.lars_albrecht.jmoviedb.mdb.filter.VideoFileFilter;
import com.lars_albrecht.jmoviedb.mdb.outputItems.MovieFileDetailsOutputItem;
import com.lars_albrecht.jmoviedb.mdb.typer.VideoTyper;
import com.lars_albrecht.mdb.main.MDB;
import com.lars_albrecht.mdb.main.MDBConfig;
import com.lars_albrecht.mdb.main.utilities.Paths;

/**
 * @author lalbrecht
 * 
 */
public class JMovieDB {

	public static void main(final String[] args) {
		new JMovieDB();
	}

	public JMovieDB() {
		try {
			final MDBConfig mdbConfig = new MDBConfig();
			mdbConfig.setFinderFileFilter(new VideoFileFilter());
			mdbConfig.setSystemTrayInterfaceIconImageFile(new File(Paths.TRAY + File.separator + "icon.gif"));
			mdbConfig.setWebInterfaceFileDetailsOutputItem(new MovieFileDetailsOutputItem());
			mdbConfig.setLoglevel(Debug.LEVEL_INFO);

			mdbConfig.getListOfCollectors().add(new MediaInfoCollector());
			mdbConfig.getListOfCollectors().add(new TheMovieDBCollector());
			mdbConfig.getListOfCollectors().add(new TheTVDBCollector());

			mdbConfig.getListOfTypers().add(new VideoTyper());

			mdbConfig.addTitleExtraction("movie", "themoviedb", "general", "title");
			// TODO <movie name> (<year>) - <collection>
			// TODO <series name> <episode title> - series/episode number
			// TODO find possibility to add more than one string | alternatives
			// | additionals
			// mdbConfig.addTitleExtraction("serie", "thetvdb", "episode",
			// "name");

			final MDB mdb = new MDB(mdbConfig);
			mdb.run();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}

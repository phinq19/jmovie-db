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

/**
 * @author lalbrecht
 * 
 */
public class JMovieDB {

	public JMovieDB() {
		try {
			final MDBConfig mdbConfig = new MDBConfig();
			mdbConfig.setFinderFileFilter(new VideoFileFilter());
			mdbConfig.setSystemTrayInterfaceIconImageFile(new File("trunk/tray/bulb.gif"));
			mdbConfig.setWebInterfaceFileDetailsOutputItem(new MovieFileDetailsOutputItem());
			mdbConfig.setLoglevel(Debug.LEVEL_ERROR);

			mdbConfig.getListOfCollectors().add(new MediaInfoCollector());
			mdbConfig.getListOfCollectors().add(new TheMovieDBCollector());
			mdbConfig.getListOfCollectors().add(new TheTVDBCollector());

			mdbConfig.getListOfTypers().add(new VideoTyper());

			final MDB mdb = new MDB(mdbConfig);
			mdb.run();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		new JMovieDB();
	}
}

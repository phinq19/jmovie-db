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
import com.lars_albrecht.jmoviedb.mdb.interfaces.pages.MoviesPage;
import com.lars_albrecht.jmoviedb.mdb.interfaces.pages.SeriesExPage;
import com.lars_albrecht.jmoviedb.mdb.interfaces.pages.SeriesPage;
import com.lars_albrecht.jmoviedb.mdb.outputItems.MovieFileDetailsOutputItem;
import com.lars_albrecht.jmoviedb.mdb.typer.VideoTyper;
import com.lars_albrecht.mdb.main.MDB;
import com.lars_albrecht.mdb.main.MDBConfig;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.AttributesTagsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.BrowsePage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.FileDetailsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.HomePage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.InfoControlPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.SearchResultsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.SettingsPage;
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.config.WebPageConfig;
import com.lars_albrecht.mdb.main.core.utilities.Paths;

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

			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(HomePage.class, new String[] {
					"home", "index", "Start"
			}, "Start", true, 0));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(InfoControlPage.class, new String[] {
					"infoControl", "infoKontrolle"
			}, "Info / Kontrolle", true, 5));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(FileDetailsPage.class, new String[] {
					"filedetails", "detailansicht"
			}));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SearchResultsPage.class, new String[] {
					"searchresults", "suchergebnisse"
			}));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SettingsPage.class, new String[] {
					"settings", "einstellungen"
			}, "Einstellungen", true, 6));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(BrowsePage.class, new String[] {
					"browser", "durchsuchen"
			}, "Durchsuchen", true, 3));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(AttributesTagsPage.class, new String[] {
					"attributestags", "AttributeTags"
			}, "Attribute / Tags", true, 4));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(MoviesPage.class, new String[] {
					"movies", "filme"
			}, "Filme", true, 1));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SeriesPage.class, new String[] {
					"series", "serien"
			}, "Serien", true, 2));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SeriesExPage.class, new String[] {
					"seriesex", "serien"
			}));

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

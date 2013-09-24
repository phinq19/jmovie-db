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
import com.lars_albrecht.mdb.main.core.interfaces.web.pages.AllPage;
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

			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(HomePage.class, "Start", new String[] {
					"home", "index", "Start"
			}, true, 0));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(InfoControlPage.class, "Info / Kontrolle", new String[] {
					"infoControl", "infoKontrolle"
			}, true, 4));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(FileDetailsPage.class, null, new String[] {
					"filedetails", "detailansicht"
			}, false, 0));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SearchResultsPage.class, null, new String[] {
					"searchresults", "suchergebnisse"
			}, false, 0));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(SettingsPage.class, "Einstellungen", new String[] {
					"settings", "einstellungen"
			}, true, 5));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(BrowsePage.class, "Durchsuchen", new String[] {
					"browser", "durchsuchen"
			}, true, 2));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(AttributesTagsPage.class, "Attribute / Tags", new String[] {
					"attributestags", "AttributeTags"
			}, true, 3));
			mdbConfig.getWebInterfacePageConfigs().add(new WebPageConfig(AllPage.class, "Alle", new String[] {
					"all", "alle"
			}, true, 1));

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

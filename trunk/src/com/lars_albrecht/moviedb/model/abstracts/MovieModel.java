/**
 * 
 */
package com.lars_albrecht.moviedb.model.abstracts;

import java.awt.Image;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.ParseOptions;
import com.lars_albrecht.moviedb.annotation.ViewInTab;
import com.lars_albrecht.moviedb.annotation.ViewInTable;
import com.lars_albrecht.moviedb.utilities.Helper;

/**
 * @author lalbrecht
 * 
 */
@DatabaseOptions(as = "movie", type = 0, additionalType = "", defaultValues = {}, isUnique = false)
public abstract class MovieModel {

	@DatabaseOptions(as = "id", type = DatabaseOptions.TYPE_FIELD, additionalType = "IDENTITY", defaultValues = {}, isUnique = false)
	protected Integer id = null;

	@ViewInTable(as = "Place", sort = 100)
	@DatabaseOptions(as = "filepath", type = DatabaseOptions.TYPE_FIELD, additionalType = "VARCHAR(512) NOT NULL", defaultValues = {}, isUnique = true)
	@ViewInTab(as = "Place", tabname = "general", sort = 50, editable = false, type = ViewInTab.TYPE_AUTO)
	protected File file = null;

	// @ViewInTable(as = "IMDB-Key", sort = 90)
	// @ParseOptions(as = "imdbkey", type = 0, typeConf =
	// "([a-zA-Z]{2}[0-9]{7})")
	// @DatabaseOptions(as = "imdbkey", type = 1, additionalType = "",
	// defaultValues = {}, isUnique = false)
	// @ShowInTab(as = "IMDB-Key", tabname = "imdb", sort = 0, editable = true)
	// protected String imdbKey = null;

	@ViewInTable(as = "Genres", sort = 90)
	@ParseOptions(as = "genre", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "genre", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "Action", "Horror", "Komödie", "Sci-Fi", "Thriller", "Liebesfilm", "Erotik",
			"Abenteuerfilm", "Katastrophenfilm", "Krimi", "Mystery", "Fantasy", "Western", "Kriegsfilm", "Dokumentation", "Historisch", "Biografie", "Antikriegsfilm", "Martial-Arts", "Eastern",
			"Animationsfilm", "Comic", "Noir" }, isUnique = false)
	@ViewInTab(as = "Genres", tabname = "general", sort = 110, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> genreList = null;

	@ViewInTable(as = "Videostreams", sort = 80)
	@ParseOptions(as = "video", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "video", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "720p", "1080p", "HDTV", "HDRip", "DVDRip", "BRRIP", "HD2DVD", "R5" }, isUnique = false)
	@ViewInTab(as = "Videostreams", tabname = "general", sort = 100, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> videoList = null;

	@ViewInTable(as = "Audiostreams", sort = 70)
	@ParseOptions(as = "audio", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "audio", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "DD5.1", "DTS", "DTSHD", "DTSD", "AC3", "AC3D", "AHC3", "AC5.1", "AC3 2.0", "5.1",
			"AC3D 5.1" }, isUnique = false)
	@ViewInTab(as = "Audiostreams", tabname = "general", sort = 90, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> audioList = null;

	@ViewInTable(as = "Encoders", sort = 60)
	@ParseOptions(as = "encoder", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "encoder", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "x264", "Xvid", "DivX", "H.264", "Nero Digital", "CoreAVC", "3ivx", "HDX4", "libmpeg2",
			"FFmpeg", "MPEG-4" }, isUnique = false)
	@ViewInTab(as = "Encoders", tabname = "general", sort = 80, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> encoderList = null;

	@ViewInTable(as = "Languages", sort = 50)
	@ParseOptions(as = "lang", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "lang", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "de", "en" }, isUnique = false)
	@ViewInTab(as = "Languages", tabname = "general", sort = 70, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> langList = null;

	@ViewInTable(as = "Versions", sort = 40)
	@ParseOptions(as = "version", type = ParseOptions.TYPE_LIST, typeConf = "db")
	@DatabaseOptions(as = "version", type = DatabaseOptions.TYPE_TABLE, additionalType = "", defaultValues = { "Uncut", "Unrated", "Extended", "Remastered", "Extended Remastered",
			"Extended Directors Cut", "Jubiläums Edition", "Directors Cut", "SE", "Special edition", "Platinum Edition", "Final Cut" }, isUnique = false)
	@ViewInTab(as = "Versions", tabname = "general", sort = 60, editable = true, type = ViewInTab.TYPE_AUTO)
	protected ArrayList<String> versionList = null;

	@ViewInTable(as = "Year", sort = 30)
	@ParseOptions(as = "year", type = ParseOptions.TYPE_REGEX, typeConf = "([0-9]{4})")
	@DatabaseOptions(as = "cryear", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Year", tabname = "general", sort = 40, editable = true, type = ViewInTab.TYPE_AUTO)
	protected Integer year = null;

	@DatabaseOptions(as = "descriptionlong", type = DatabaseOptions.TYPE_FIELD, additionalType = "LONGVARCHAR", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Long Description", tabname = "general", sort = 130, editable = true, type = ViewInTab.TYPE_AREA)
	protected String descriptionLong = null;

	@DatabaseOptions(as = "descriptionshort", type = DatabaseOptions.TYPE_FIELD, additionalType = "LONGVARCHAR", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Short Description", tabname = "general", sort = 120, editable = true, type = ViewInTab.TYPE_AREA)
	protected String descriptionShort = null;

	@ViewInTable(as = "Subtitle", sort = 20)
	@ParseOptions(as = "subtitle", type = ParseOptions.TYPE_REGEX, typeConf = "(.*)?")
	@DatabaseOptions(as = "subtitle", type = DatabaseOptions.TYPE_FIELD, additionalType = "VARCHAR(128)", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Subtitle", tabname = "general", sort = 30, editable = true, type = ViewInTab.TYPE_AUTO)
	protected String subtitle = null;

	@ViewInTable(as = "Maintitle", sort = 10)
	@ParseOptions(as = "maintitle", type = ParseOptions.TYPE_REGEX, typeConf = "(.*)?")
	@DatabaseOptions(as = "maintitle", type = DatabaseOptions.TYPE_FIELD, additionalType = "VARCHAR(128) NOT NULL", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Maintitle", tabname = "general", sort = 20, editable = true, type = ViewInTab.TYPE_AUTO)
	protected String maintitle = null;

	@ViewInTable(as = "Cover", sort = 5)
	@DatabaseOptions(as = "cover", type = DatabaseOptions.TYPE_FIELD, additionalType = "", defaultValues = {}, isUnique = false)
	@ViewInTab(as = "Cover", tabname = "general", sort = 10, editable = true, type = ViewInTab.TYPE_AUTO)
	protected Image cover = null;

	@ViewInTable(as = " ", sort = 0)
	protected Boolean validPath = null;

	private ConcurrentHashMap<String, Object> additional = null;

	/**
	 * 
	 */
	public MovieModel() {
		additional = new ConcurrentHashMap<String, Object>();
	}

	final public Object get(final String s) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			return Helper.call("get" + Helper.ucfirst(s), this);
		} catch (NoSuchMethodException e) {
			return additional.get(s);
		}
	}

	final public void set(final String s, final Object set) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			Helper.call("set" + Helper.ucfirst(s), this, set);
		} catch (NoSuchMethodException e) {
			additional.put(s, set);
		}
	}

	/**
	 * @return the id
	 */
	protected synchronized final Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	protected synchronized final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the versionList
	 */
	protected synchronized final ArrayList<String> getVersionList() {
		if (this.versionList == null) {
			this.versionList = new ArrayList<String>();
		}
		return this.versionList;
	}

	/**
	 * @param versionList
	 *            the versionList to set
	 */
	protected synchronized final void setVersionList(final ArrayList<String> versionList) {
		this.versionList = versionList;
	}

	/**
	 * @return the videoList
	 */
	protected synchronized final ArrayList<String> getVideoList() {
		if (this.videoList == null) {
			this.videoList = new ArrayList<String>();
		}
		return this.videoList;
	}

	/**
	 * @param videoList
	 *            the videoList to set
	 */
	protected synchronized final void setVideoList(final ArrayList<String> videoList) {
		this.videoList = videoList;
	}

	/**
	 * @return the encoderList
	 */
	protected synchronized final ArrayList<String> getEncoderList() {
		if (this.encoderList == null) {
			this.encoderList = new ArrayList<String>();
		}
		return this.encoderList;
	}

	/**
	 * @param encoderList
	 *            the encoderList to set
	 */
	protected synchronized final void setEncoderList(final ArrayList<String> encoderList) {
		this.encoderList = encoderList;
	}

	/**
	 * @return the langList
	 */
	protected synchronized final ArrayList<String> getLangList() {
		if (this.langList == null) {
			this.langList = new ArrayList<String>();
		}
		return this.langList;
	}

	/**
	 * @param langList
	 *            the langList to set
	 */
	protected synchronized final void setLangList(final ArrayList<String> langList) {
		this.langList = langList;
	}

	/**
	 * @return the year
	 */
	protected synchronized final Integer getYear() {
		return this.year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	protected synchronized final void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * @return the file
	 */
	protected synchronized final File getFile() {
		return this.file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	protected synchronized final void setFile(final File file) {
		this.file = file;
	}

	/**
	 * @param audioList
	 *            the soundList to set
	 */
	protected synchronized final void setAudioList(final ArrayList<String> audioList) {
		this.audioList = audioList;
	}

	/**
	 * @return the maintitle
	 */
	protected synchronized final String getMaintitle() {
		return this.maintitle;
	}

	/**
	 * @param maintitle
	 *            the maintitle to set
	 */
	protected synchronized final void setMaintitle(final String maintitle) {
		this.maintitle = maintitle;
	}

	/**
	 * @return the subtitle
	 */
	protected synchronized final String getSubtitle() {
		return this.subtitle;
	}

	/**
	 * @param subtitle
	 *            the subtitle to set
	 */
	protected synchronized final void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the audioList
	 */
	protected synchronized final ArrayList<String> getAudioList() {
		if (this.audioList == null) {
			this.audioList = new ArrayList<String>();
		}
		return this.audioList;
	}

	/**
	 * @return the descriptionLong
	 */
	protected synchronized final String getDescriptionLong() {
		return this.descriptionLong;
	}

	/**
	 * @param descriptionLong
	 *            the descriptionLong to set
	 */
	protected synchronized final void setDescriptionLong(final String descriptionLong) {
		this.descriptionLong = descriptionLong;
	}

	/**
	 * @return the descriptionShort
	 */
	protected synchronized final String getDescriptionShort() {
		return this.descriptionShort;
	}

	/**
	 * @param descriptionShort
	 *            the descriptionShort to set
	 */
	protected synchronized final void setDescriptionShort(final String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	/**
	 * @return the validPath
	 */
	protected synchronized final Boolean getValidPath() {
		return this.validPath;
	}

	/**
	 * @param validPath
	 *            the validPath to set
	 */
	protected synchronized final void setValidPath(final Boolean validPath) {
		this.validPath = validPath;
	}

	/**
	 * @return the genreList
	 */
	protected synchronized final ArrayList<String> getGenreList() {
		if (this.genreList == null) {
			this.genreList = new ArrayList<String>();
		}
		return this.genreList;
	}

	/**
	 * @param genreList
	 *            the genreList to set
	 */
	protected synchronized final void setGenreList(final ArrayList<String> genreList) {
		this.genreList = genreList;
	}

	/**
	 * @return the cover
	 */
	protected synchronized final Image getCover() {
		return this.cover;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	protected synchronized final void setCover(final Image cover) {
		this.cover = cover;
	}
}

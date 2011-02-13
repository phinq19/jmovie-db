/**
 * 
 */
package moviedb.threads;

import java.io.File;
import java.util.ArrayList;

import moviedb.Filter.MovieFilenameFilter;
import moviedb.model.MovieModel;
import moviedb.parser.Parser;

/**
 * @author lalbrecht
 * 
 */
public class FileHandler implements Runnable {

	private String dir = null;

	private ArrayList<File> dirs = null;
	private ArrayList<MovieModel> movies = null;
	private Parser p = null;
	private final Boolean subFolders = Boolean.TRUE;

	private final ArrayList<String> versionNames = new ArrayList<String>();

	public FileHandler(final Parser p, final String dir) {
		this.p = p;
		this.dir = dir;
		this.movies = new ArrayList<MovieModel>();
		this.fillValueArrays();
	}

	private void fillValueArrays () {
		this.versionNames.add("extended");
		this.versionNames.add("unrated");
		this.versionNames.add("uncut");
		this.versionNames.add("special edition");
		this.versionNames.add("uncut");
		this.versionNames.add("extended directors cut");
		this.versionNames.add("directors cut");
		this.versionNames.add("jubiläums edition");
		this.versionNames.add("HDTV");
	}

	/**
	 * 
	 * @param dirs
	 */
	private void getMoviesFromFolders (final ArrayList<File> dirs) {
		MovieModel tempMovie = null;
		for (int i = 0; i < dirs.size(); i++) {
			final File[] files = dirs.get(i).listFiles(new MovieFilenameFilter());
			for (final File file : files) {
				if (file.exists() && file.isDirectory() && this.subFolders) {
					final ArrayList<File> tempList = new ArrayList<File>();
					tempList.add(file);
					this.getMoviesFromFolders(tempList);
				}
				if (!file.isDirectory()) {
					tempMovie = new MovieModel();
					this.parseMoviename(file, tempMovie);
					this.movies.add(tempMovie);
				}
			}
		}
	}

	/**
	 * @param filename
	 * @param tempMovie
	 */
	private void parseMoviename (final File file, final MovieModel tempMovie) {
		String filename = file.getName();
		// fix name (delete ending) - TODO Rewrite with filetypeArr
		if (filename.endsWith(".mkv") || filename.endsWith(".avi")) {
			tempMovie.setFiletype(filename.substring(filename.length() - 3).trim());
			filename = filename.substring(0, filename.length() - 4).trim();
		}
		// split string on " - "
		final String[] items = filename.split("([-])");
		for (int i = 0; i < items.length; i++) {
			final String value = items[i].trim();
			if (value.matches("([0-9]{4})")) {
				tempMovie.setYear(Integer.parseInt(value));
			} else if (value.matches("([0-9]{3,4}[ip]{1})")) {
				tempMovie.setHdType(value);
			} else if (value.toLowerCase().equals("dts")) {
				tempMovie.setDts(Boolean.TRUE);
			} else if (value.toLowerCase().equals("ac3")) {
				tempMovie.setAc3(Boolean.TRUE);
			} else if (value.toLowerCase().equals("r5")) {
				tempMovie.setR5(Boolean.TRUE);
			} else if (value.toLowerCase().equals("x264")) {
				tempMovie.setX264(true);
			} else if (i > 0) {
				if (this.versionNames.contains(value.toLowerCase())) {
					tempMovie.setVersion(value);
				} else {
					tempMovie.setSubTitle(value);
				}
			} else if (i == 0) {
				tempMovie.setTitle(value);
			} else {
				System.out.println("unused: " + value);
			}
			tempMovie.setFilepath(file.getAbsolutePath());
		}
		if (false) {
			System.out.println(filename);
			if (tempMovie.getTitle() != null) {
				System.out.println("Titel: " + tempMovie.getTitle());
			}
			if (tempMovie.getSubTitle() != null) {
				System.out.println("SubTitel: " + tempMovie.getSubTitle());
			}
			if (tempMovie.getYear() != null) {
				System.out.println("Jahr: " + tempMovie.getYear());
			}
			if (tempMovie.getHdType() != null) {
				System.out.println("Typ: " + tempMovie.getHdType());
			}
			if (tempMovie.getDts() != null) {
				System.out.println("DTS: " + tempMovie.getDts());
			}
			if (tempMovie.getAc3() != null) {
				System.out.println("AC3: " + tempMovie.getAc3());
			}
			if (tempMovie.getR5() != null) {
				System.out.println("R5: " + tempMovie.getR5());
			}
			if (tempMovie.getFiletype() != null) {
				System.out.println("FileType: " + tempMovie.getFiletype());
			}
			if (tempMovie.getFilepath() != null) {
				System.out.println("FilePath: " + tempMovie.getFilepath());
			}
			System.out.println("---------------------");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run () {
		if (this.dir != null) {
			this.dirs = new ArrayList<File>();
			this.dirs.add(new File(this.dir));
			this.getMoviesFromFolders(this.dirs);
			this.p.addMovies(this.movies);
		}
	}

}

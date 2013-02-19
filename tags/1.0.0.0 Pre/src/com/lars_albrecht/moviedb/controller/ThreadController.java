/**
 * 
 */
package com.lars_albrecht.moviedb.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.h2.jdbc.JdbcSQLException;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.annotation.ParseOptions;
import com.lars_albrecht.moviedb.components.movietablemodel.MovieTableModel;
import com.lars_albrecht.moviedb.database.DB;
import com.lars_albrecht.moviedb.exceptions.NoMovieIDException;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.thread.Finder;
import com.lars_albrecht.moviedb.thread.Parser;
import com.lars_albrecht.moviedb.thread.TableRefresh;

/**
 * @author lalbrecht
 * 
 */
public class ThreadController {

	private Controller controller = null;

	private MovieTableModel tableModel = null;
	private Integer moviesAddedCount = 0;

	ConcurrentHashMap<String, ArrayList<String>> dbListItems = null;
	private ConcurrentHashMap<String, ArrayList<String>> regexList = null;
	private FieldList fieldList = null;

	private final ArrayList<Thread> parserList = new ArrayList<Thread>();
	private final ArrayList<Thread> finderList = new ArrayList<Thread>();
	private final ArrayList<Thread> tableRefreshList = new ArrayList<Thread>();

	/**
	 * 
	 * @param controller
	 * @param tableModel
	 * @param userPattern
	 * @param files
	 */
	public ThreadController(final Controller controller, final MovieTableModel tableModel, final String[] userPattern,
			final ArrayList<File> files) {
		this.controller = controller;
		this.tableModel = tableModel;
		// final ArrayList<String> regexList = new ArrayList<String>();
		this.regexList = new ConcurrentHashMap<String, ArrayList<String>>();

		this.dbListItems = ThreadController.getDBListItems();

		// Contains the names of the given regular expression.
		ArrayList<String> nameList = null;

		// Contains all fields in MovieModel.class.
		this.fieldList = Controller.flParse;

		if(userPattern != null) {
			for(final String matcherStr : userPattern) {
				String regex = matcherStr;
				final Pattern pattern = Pattern.compile("%(.*?)%");
				final Matcher matcherNames = pattern.matcher(matcherStr);
				String foundStr = null;
				Integer pos = null;
				nameList = new ArrayList<String>();

				while(matcherNames.find()) {
					foundStr = matcherNames.group().replaceAll("%", "");
					pos = this.fieldList.fieldNameInAsList(foundStr);
					if((pos > -1) && (this.fieldList.get(pos).getType() == ParseOptions.TYPE_REGEX)) {
						regex = regex.replaceFirst(matcherNames.group(), this.fieldList.get(pos).getTypeConf());
					} else if((pos > -1) && (this.fieldList.get(pos).getType() == ParseOptions.TYPE_LIST)) {
						regex = regex.replaceFirst(matcherNames.group(), "("
								+ this.dbListItems.get(this.fieldList.get(pos).getAs()).toString().replaceAll(",", "|")
										.replaceAll("\\[|\\]|\\s", "") + ")");
						// Debug.log(Debug.LEVEL_DEBUG, matcherNames.group());
						// Debug.log(Debug.LEVEL_DEBUG, regex);
					} else {
						if((foundStr.indexOf("_") > -1)
								&& ((pos = this.fieldList.fieldNameInAsList(foundStr.substring(0, foundStr.indexOf("_")))) > -1)
								&& (this.fieldList.get(pos).getType() == ParseOptions.TYPE_REGEX)) {
							regex = regex.replaceFirst(matcherNames.group(), this.fieldList.get(pos).getTypeConf());
						} else {
							regex = regex.replaceFirst(matcherNames.group(), "(.*?)");
							// Debug.log(Debug.LEVEL_DEBUG, "else: " +
							// matcherNames.group());
						}
					}
					final String name = matcherNames.group().replaceAll("%", "");
					nameList.add(name);
				}

				this.regexList.put("^" + regex + "$", nameList);

			}

		}

		for(int i = 0; i < files.size(); i++) {
			Debug.log(Debug.LEVEL_DEBUG, "start find and parse");
			this.finderList.add(new Thread(new Finder(this, files.get(i))));
			this.finderList.get(this.finderList.size() - 1).start();
		}
	}

	public ThreadController(final Controller controller, final MovieTableModel tableModel) {
		this.controller = controller;
		this.tableModel = tableModel;
		this.tableRefreshList.add(new Thread(new TableRefresh(this, this.tableModel)));
		this.tableRefreshList.get(this.tableRefreshList.size() - 1).start();
	}

	/**
	 * 
	 * @param fileList
	 */
	public synchronized void startParse(final ArrayList<File> fileList) {
		// this.parserList.add(new Thread(new Parser(this, fileList,
		// this.regexList, this.fieldList)));
		// this.parserList.get(this.parserList.size() - 1).start();

		// final String regex = "((?!-\\s).)+";
		// System.out.println("USE REGEX: " + Controller.options.getFilenameSeperator());
		this.parserList.add(new Thread(new Parser(this, fileList, this.dbListItems, this.fieldList, Controller.options
				.getFilenameSeperator())));
		this.parserList.get(this.parserList.size() - 1).start();
	}

	/**
	 * 
	 * @return ConcurrentHashMap<String, ArrayList<String>>
	 */
	public static ConcurrentHashMap<String, ArrayList<String>> getDBListItems() {
		final FieldList fieldList = Controller.flParse;
		final ConcurrentHashMap<String, ArrayList<String>> list = new ConcurrentHashMap<String, ArrayList<String>>();
		try {
			ResultSet rs = null;
			String sql = null;
			DatabaseOptions dbo = null;
			ArrayList<String> itemList = null;
			for(final FieldModel item : fieldList) {
				dbo = null;
				itemList = new ArrayList<String>();
				if(item.getField().getType() == ArrayList.class) {
					if((dbo = item.getField().getAnnotation(DatabaseOptions.class)) != null) {
						sql = "SELECT name FROM " + dbo.as();
						rs = DB.query(sql);
						while(rs.next()) {
							itemList.add(rs.getString("name"));
						}
						list.put(item.getAs(), itemList);
					}
				}
			}
		} catch(final SecurityException e) {
			e.printStackTrace();
		} catch(final IllegalArgumentException e) {
			e.printStackTrace();
		} catch(final SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param movies
	 */
	public synchronized void addMovies(final ArrayList<MovieModel> movies) {
		int addCounter = 0;
		final ArrayList<MovieModel> notAddedList = new ArrayList<MovieModel>();
		for(final MovieModel movie : movies) {
			Integer movieId = null;
			try {
				// add movie to database
				movieId = MovieController.addMovie(movie);
			} catch(final JdbcSQLException e) {
				try {
					notAddedList.add(movie);
					Debug.log(Debug.LEVEL_INFO, "not added: " + movie.get("maintitle") + " - ERROR CODE: " + e.getErrorCode());
				} catch(final SecurityException e1) {
					e1.printStackTrace();
				} catch(final IllegalAccessException e1) {
					e1.printStackTrace();
				} catch(final IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch(final InvocationTargetException e1) {
					e1.printStackTrace();
				}
			} catch(final NoSuchMethodException e) {
				e.printStackTrace();
			} catch(final IllegalAccessException e) {
				e.printStackTrace();
			} catch(final InvocationTargetException e) {
				e.printStackTrace();
			} catch(final SQLException e) {
				e.printStackTrace();
			} catch(final SecurityException e) {
				e.printStackTrace();
			} catch(final IllegalArgumentException e) {
				e.printStackTrace();
			} catch(final NoMovieIDException e) {
				e.printStackTrace();
			}
			if((movieId != null) && (movieId > -1)) {
				try {
					movie.set("id", movieId);
					// add movie to table
					this.tableModel.addMovie(movie);
					addCounter++;
				} catch(final IllegalArgumentException e) {
					e.printStackTrace();
				} catch(final SecurityException e) {
					e.printStackTrace();
				} catch(final IllegalAccessException e) {
					e.printStackTrace();
				} catch(final InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		this.moviesAddedCount += addCounter;
		// System.out.println("ParserList: " + this.parserList.size());
		if((this.parserList.size() == 0) && (this.finderList.size() == 0)) {
			this.controller.getSbStatus().setText(
					String.format(RessourceBundleEx.getInstance().getProperty("application.status.movielist.added"),
							this.moviesAddedCount));
		}
	}

	/**
	 * @return the controller
	 */
	public synchronized final Controller getController() {
		return this.controller;
	}

	/**
	 * @return the parserList
	 */
	public synchronized final ArrayList<Thread> getParserList() {
		return this.parserList;
	}

	/**
	 * @return the finderList
	 */
	public synchronized final ArrayList<Thread> getFinderList() {
		return this.finderList;
	}

	/**
	 * @return the tableRefreshList
	 */
	public synchronized final ArrayList<Thread> getTableRefreshList() {
		return this.tableRefreshList;
	}

}

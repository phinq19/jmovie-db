/**
 * 
 */
package com.lars_albrecht.moviedb.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.lars_albrecht.general.types.KeyValue;
import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.moviedb.annotation.DatabaseOptions;
import com.lars_albrecht.moviedb.database.DB;
import com.lars_albrecht.moviedb.exceptions.NoMovieIDException;
import com.lars_albrecht.moviedb.model.DefaultMovieModel;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class MovieController {

	/**
	 * Adds a movie to the database.
	 * 
	 * @param movie
	 *            MovieModel
	 * @return Integer
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 * @throws NoMovieIDException
	 */
	@SuppressWarnings("unchecked")
	public static Integer addMovie(final MovieModel movie) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException,
			NoMovieIDException {
		Debug.startTimer("addfilmdb");
		final FieldList fieldListDb = Controller.flDB;
		final ConcurrentHashMap<String, Object> tempGeneral = new ConcurrentHashMap<String, Object>();
		final ConcurrentHashMap<String, ArrayList<String>> list = new ConcurrentHashMap<String, ArrayList<String>>();
		for (final FieldModel item : fieldListDb) {
			if ((item.getField().getType() == String.class) || (item.getField().getType() == Integer.class)) {
				final Object methodResult = movie.get(item.getField().getName());
				if (methodResult != null) {
					tempGeneral.put(item.getAs(), (item.getField().getType() == String.class ? "'" + methodResult + "'" : methodResult));
				}
			} else if (item.getField().getType() == Long.class) {
				final Long methodResult = (Long) movie.get(item.getField().getName());
				if (methodResult != null) {
					tempGeneral.put(item.getAs(), methodResult);
				}
			} else if (item.getField().getType() == File.class) {
				final File methodResult = (File) movie.get(item.getField().getName());
				if (methodResult != null) {
					tempGeneral.put(item.getAs(), "'" + methodResult.getAbsolutePath() + "'");
				}
			} else if (item.getField().getType() == ArrayList.class) {
				final ArrayList<String> tempList = ((ArrayList<String>) movie.get(item.getField().getName()));
				// System.out.println("--> " + "get" +
				// Helper.ucfirst(item.getField().getName()));
				// System.out.println("--> tempList: " + tempList);
				if (tempList != null) {
					for (final String string : tempList) {
						if (!list.containsKey(item.getAs())) {
							list.put(item.getAs(), new ArrayList<String>());
						}
						list.get(item.getAs()).add(string);
					}
				}
			} else if (item.getField().getType() == Image.class) {
				final Image methodResult = (Image) movie.get(item.getField().getName());
				if (methodResult != null) {
					tempGeneral.put(item.getAs(), methodResult);
				}
			}
		}
		final Integer movieId = MovieController.addMovieGeneral(tempGeneral);
		MovieController.addMovieListLink(movieId, list);
		if (Debug.inDebugLevel(Debug.LEVEL_ALL)) {
			for (final Map.Entry<String, ArrayList<String>> litem : list.entrySet()) {
				Debug.log(Debug.LEVEL_DEBUG, litem.getKey());
				for (final String s : litem.getValue()) {
					Debug.log(Debug.LEVEL_DEBUG, s);
				}
				Debug.log(Debug.LEVEL_DEBUG, "--");
			}
		}

		DB.commit();

		Debug.endTimer("addfilmdb");

		Debug.log(Debug.LEVEL_DEBUG, "added a film in " + Debug.getFormattedTime("addfilmdb"));
		return movieId;
	}

	/**
	 * 
	 * Updates a movie. Currently it removes the old one and add the new one.
	 * 
	 * @param movie
	 *            MovieModel
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 * @throws NoMovieIDException
	 */
	public static void updateMovie(final MovieModel movie) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, NoSuchMethodException,
			NoMovieIDException {
		if (movie.get("id") != null) {
			MovieController.removeMovie(movie);
		} else {
			throw new NoMovieIDException("No movieid to update movie.");
		}
		MovieController.addMovie(movie);
	}

	/**
	 * 
	 * Add the default values (no special table) to the database and returns the
	 * id.
	 * 
	 * @param dbFields
	 *            ConcurrentHashMap<String, Object>
	 * @return Integer
	 * @throws SQLException
	 * @throws NoMovieIDException
	 */
	private static Integer addMovieGeneral(final ConcurrentHashMap<String, Object> dbFields) throws SQLException, NoMovieIDException {
		final String sql = "INSERT INTO movie (" + Helper.implode(new ArrayList<Object>(dbFields.keySet()), ",", null, null) + ") " + "VALUES ("
				+ Helper.repeatString("?", ", ", dbFields.keySet().size()) + ")";
		// Helper.implode(new ArrayList<Object>(dbFields.values()), ",", null,
		// null)).replace("'", "\'")
		final ConcurrentHashMap<Integer, Object> temp = new ConcurrentHashMap<Integer, Object>();
		int i = 1;
		for (final Entry<String, Object> entry : dbFields.entrySet()) {
			temp.put(i, entry.getValue());
			i++;
		}
		DB.updatePS(sql, temp);
		if (dbFields.containsKey("id")) {
			return (Integer) dbFields.get("id");
		} else {
			final ResultSet rs = DB.query("CALL IDENTITY()");
			if (rs.first()) {
				return (Integer) rs.getObject(1);
			} else {
				throw new NoMovieIDException("No movieid to return");
			}
		}

	}

	/**
	 * Add a list to a movie.
	 * 
	 * @param movieId
	 *            Long
	 * @param list
	 *            ConcurrentConcurrentHashMap<String, ArrayList<String>>
	 * @throws SQLException
	 * @throws NoMovieIDException
	 */
	private static void addMovieListLink(final Integer movieId, final ConcurrentHashMap<String, ArrayList<String>> list) throws SQLException, NoMovieIDException {
		if (movieId != null) {
			String sql = null;
			ResultSet rs = null;
			Integer fieldId = null;
			for (final Entry<String, ArrayList<String>> entry : list.entrySet()) {
				for (final String value : entry.getValue()) {
					final ConcurrentHashMap<Integer, Object> temp = new ConcurrentHashMap<Integer, Object>();
					temp.put(1, value);
					sql = "SELECT id FROM " + entry.getKey() + " WHERE UPPER(name) = UPPER(?)";
					rs = DB.queryPS(sql, temp);
					if (rs.next()) {
						fieldId = (Integer) rs.getObject("id");
						sql = "INSERT INTO movie_" + entry.getKey() + " (movie_id, " + entry.getKey() + "_id) " + "VALUES (" + movieId + ", " + fieldId + ")";
						// System.out.println(sql);
						DB.update(sql);
					} else {
						// System.out.println(">>>>>>> " + movieId +
						// " - no list added");
						// System.out.println(movieId + " - " + sql);
					}
				}
			}
		} else {
			throw new NoMovieIDException("No movieid.");
		}
	}

	/**
	 * Returns a movie with the given id.
	 * 
	 * @param id
	 *            Integer
	 * @return MovieModel
	 * 
	 * @throws SQLException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public static MovieModel getMovie(final Integer id) throws SQLException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		MovieModel tempMovie = null;

		final Class<?>[] cList = { String.class, Integer.class, File.class, Image.class, Boolean.class, Float.class };
		final String sql = "SELECT " + Controller.flDB.toStringFromType(cList, "m.", null) + " FROM movie as m WHERE id = " + id;
		String subSql = "";
		final ResultSet rs = DB.query(sql);
		ResultSet rsSub = null;
		Boolean found = null;
		if (rs.next()) {
			tempMovie = new DefaultMovieModel();
			// TODO refactor in new own function
			for (final FieldModel field : Controller.flDB) {
				try {
					found = Helper.isFieldInResult(rs, field.getAs());
				} catch (final SQLException e) {
					e.printStackTrace();
				}
				if (found) {
					if (field.getField().getType() == File.class) {
						tempMovie.set(field.getField().getName(), new File((String) rs.getObject(field.getAs())));
					} else if (field.getField().getType() == Image.class) {
						if (rs.getBinaryStream(field.getAs()) != null) {
							tempMovie.set(field.getField().getName(), Helper.bufferedImageToImage(ImageIO.read(rs.getBinaryStream(field.getAs()))));
						}
					} else if (rs.getObject(field.getAs()) != null) {
						tempMovie.set(field.getField().getName(), rs.getObject(field.getAs()));
					}
				} else if (field.getField().getType() == ArrayList.class) {
					subSql = "SELECT " + field.getAs() + ".id, " + field.getAs() + ".name FROM " + field.getAs() + ", movie_" + field.getAs() + " WHERE movie_id = " + tempMovie.get("id")
							+ " AND movie_" + field.getAs() + "." + field.getAs() + "_id = " + field.getAs() + ".id";
					rsSub = DB.query(subSql);
					final ArrayList<String> tempRsList = new ArrayList<String>();
					while (rsSub.next()) {
						tempRsList.add(rsSub.getString("name"));
					}
					if (tempRsList.size() > 0) {
						tempMovie.set(field.getField().getName(), tempRsList);
					}
				} else {
					// Debug.log(Debug.LEVEL_DEBUG, "nothing");
				}
			}
		}

		return tempMovie;
	}

	/**
	 * Returns all movies as ArrayList<MovieModel>.
	 * 
	 * @return ArrayList<MovieModel>
	 * 
	 * @throws SQLException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public static ArrayList<MovieModel> getMovies() throws SQLException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IOException {
		final ArrayList<MovieModel> tempList = new ArrayList<MovieModel>();
		MovieModel tempMovie = null;
		final Class<?>[] cList = { String.class, Integer.class, File.class, Image.class, Boolean.class, Float.class, Integer.class, Byte.class, Long.class, Short.class, BigDecimal.class,
				Double.class, Date.class, Time.class, Timestamp.class };

		final String sql = "SELECT " + Controller.flDB.toStringFromType(cList, "m.", null) + " FROM movie as m";
		String subSql = "";
		final ResultSet rs = DB.query(sql);
		ResultSet rsSub = null;
		Boolean found = null;
		while (rs.next()) {
			tempMovie = new DefaultMovieModel();
			for (final FieldModel field : Controller.flDB) {
				try {
					found = Helper.isFieldInResult(rs, field.getAs());
				} catch (final SQLException e) {
					e.printStackTrace();
				}
				if (found) {
					if (field.getField().getType() == File.class) {
						tempMovie.set(field.getField().getName(), new File((String) rs.getObject(field.getAs())));
					} else if (field.getField().getType() == Image.class) {
						if (rs.getBinaryStream(field.getAs()) != null) {
							tempMovie.set(field.getField().getName(), Helper.bufferedImageToImage(ImageIO.read(rs.getBinaryStream(field.getAs()))));
						}
					} else if (rs.getObject(field.getAs()) != null) {
						tempMovie.set(field.getField().getName(), rs.getObject(field.getAs()));
					}
				} else if (field.getField().getType() == ArrayList.class) {
					subSql = "SELECT " + field.getAs() + ".id, " + field.getAs() + ".name FROM " + field.getAs() + ", movie_" + field.getAs() + " WHERE movie_id = " + tempMovie.get("id")
							+ " AND movie_" + field.getAs() + "." + field.getAs() + "_id = " + field.getAs() + ".id";
					rsSub = DB.query(subSql);
					final ArrayList<String> tempRsList = new ArrayList<String>();
					while (rsSub.next()) {
						tempRsList.add(rsSub.getString("name"));
					}
					if (tempRsList.size() > 0) {
						tempMovie.set(field.getField().getName(), tempRsList);
					}
				} else {
					// Debug.log(Debug.LEVEL_DEBUG, "nothing2");
				}
			}
			tempList.add(tempMovie);

		}

		return tempList;
	}

	/**
	 * Returns a list of listvalues like genres.
	 * 
	 * @param listName
	 *            String
	 * @return ArrayList<KeyValue<Integer, String>>
	 * @throws SQLException
	 */
	public static ArrayList<KeyValue<Integer, String>> getList(final String listName) throws SQLException {
		final ArrayList<KeyValue<Integer, String>> tempList = new ArrayList<KeyValue<Integer, String>>();
		final String sql = "SELECT * FROM " + listName.replace("List", "") + " as l";
		final ResultSet rs = DB.query(sql);

		while (rs.next()) {
			tempList.add(new KeyValue<Integer, String>(rs.getInt("id"), rs.getString("name")));
		}

		return tempList;
	}

	/**
	 * Removes all movies from database.
	 * 
	 * @throws SQLException
	 */
	public static void removeAllMovies() throws SQLException {
		String sql = "DELETE FROM movie";
		DB.update(sql);

		final FieldList fieldListDb = Controller.flDB;
		for (final FieldModel item : fieldListDb) {
			if (item.getType() == DatabaseOptions.TYPE_TABLE) {
				sql = "DELETE FROM movie_" + item.getAs();
				// System.out.println("sql: " + sql);
				DB.update(sql);
			}
		}

		DB.commit();
	}

	/**
	 * 
	 * @param movie
	 *            MovieModel
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public static void removeMovie(final MovieModel movie) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		String sql = "DELETE FROM movie WHERE id = ?";

		final ConcurrentHashMap<Integer, Object> temp = new ConcurrentHashMap<Integer, Object>();
		temp.put(1, movie.get("id"));
		DB.updatePS(sql, temp);

		for (final FieldModel item : Controller.flDB) {
			if (item.getType() == DatabaseOptions.TYPE_TABLE) {
				sql = "DELETE FROM movie_" + item.getAs() + " WHERE movie_id = " + movie.get("id");
				// System.out.println("sql: " + sql);
				System.out.println("SQL: " + sql);
				DB.update(sql);
			}
		}

		DB.commit();
	}

	/**
	 * 
	 * Returns all duplicated movies (duplicate means: same maintitle, subtitle
	 * and same year).
	 * 
	 * @return ConcurrentHashMap<Integer, MovieModel>
	 * 
	 * @throws SQLException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public static ConcurrentHashMap<Integer, MovieModel> getDuplicates() throws SQLException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			IOException {
		final ConcurrentHashMap<Integer, MovieModel> temp = new ConcurrentHashMap<Integer, MovieModel>();
		final String sql = "SELECT id FROM movie " + "INNER JOIN (SELECT cryear, maintitle " + "FROM movie " + "GROUP BY cryear, maintitle " + "HAVING COUNT(*) > 1) AS m2 "
				+ "ON movie.maintitle = m2.maintitle";

		ResultSet rs;
		rs = DB.query(sql);
		while (rs.next()) {
			temp.put(rs.getInt("id"), MovieController.getMovie(rs.getInt("id")));
		}

		return temp;
	}
	// /**
	// *
	// * @param tablename
	// * @param fields
	// * @param where
	// * @param last
	// * @return ArrayList<ConcurrentHashMap<String, Object>>
	// * @throws SQLException
	// */
	// public static ArrayList<ConcurrentHashMap<String, Object>> getItems(final
	// String
	// tablename, final ArrayList<String> fields, final String where, final
	// String last) throws SQLException {
	// final String sql = "SELECT id, " + Utilities.implode(fields, ",") +
	// " FROM " + tablename + (where != null ? " WHERE " + where : "") + (last
	// != null ? " " + last : "") + ";";
	//
	// return DB.returnResultFromItems(sql, fields);
	// }
	//
	// /**
	// *
	// * @param tablename
	// * @param fields
	// * @param where
	// * @param last
	// * @return ArrayList<ConcurrentHashMap<String, Object>>
	// * @throws SQLException
	// */
	// public static ArrayList<ConcurrentHashMap<String, Object>> getItems(final
	// String
	// tablename, final String[] fields, final String where, final String last)
	// throws SQLException {
	// final String sql = "SELECT id, " + Utilities.implode(fields, ",") +
	// " FROM " + tablename + (where != null ? " WHERE " + where : "") + (last
	// != null ? " " + last : "") + ";";
	//
	// return DB.returnResultFromItems(sql, new
	// ArrayList<String>(Arrays.asList(fields)));
	// }

}

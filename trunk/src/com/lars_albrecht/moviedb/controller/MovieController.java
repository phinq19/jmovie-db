/**
 * 
 */
package com.lars_albrecht.moviedb.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.lars_albrecht.moviedb.database.DB;
import com.lars_albrecht.moviedb.model.DefaultMovieModel;
import com.lars_albrecht.moviedb.model.FieldList;
import com.lars_albrecht.moviedb.model.FieldModel;
import com.lars_albrecht.moviedb.model.KeyValue;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;
import com.lars_albrecht.moviedb.utilities.Debug;
import com.lars_albrecht.moviedb.utilities.Helper;

/**
 * @author lalbrecht
 * 
 */
public class MovieController {

	@SuppressWarnings("unchecked")
	public static Boolean addMovie(final MovieModel movie) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
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
			} else if (item.getField().getType() == File.class) {
				final File methodResult = (File) movie.get(item.getField().getName());
				if (methodResult != null) {
					tempGeneral.put(item.getAs(), "'" + methodResult.getAbsolutePath() + "'");
				}
			} else if (item.getField().getType() == ArrayList.class) {
				final ArrayList<String> tempList = ((ArrayList<String>) movie.get(item.getField().getName()));
				System.out.println("--> " + "get" + Helper.ucfirst(item.getField().getName()));
				System.out.println("--> tempList: " + tempList);
				if (tempList != null) {
					for (final String string : tempList) {
						if (!list.containsKey(item.getAs())) {
							list.put(item.getAs(), new ArrayList<String>());
						}
						list.get(item.getAs()).add(string);
					}
				}
			} else if (item.getField().getType() == Image.class) {
				// m = MovieModel.class.getMethod("get" +
				// Helper.ucfirst(item.getField().getName()));
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
		return true;
	}

	/**
	 * 
	 * @param dbFields
	 * @return
	 * @throws SQLException
	 */
	private static Integer addMovieGeneral(final ConcurrentHashMap<String, Object> dbFields) throws SQLException {
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
		final ResultSet rs = DB.query("CALL IDENTITY()");
		rs.next();
		final Integer result = (Integer) rs.getObject(1);
		return result;
	}

	/**
	 * 
	 * @param movieId
	 *            Long
	 * @param list
	 *            ConcurrentConcurrentHashMap<String, ArrayList<String>>
	 * @throws SQLException
	 */
	private static void addMovieListLink(final Integer movieId, final ConcurrentHashMap<String, ArrayList<String>> list) throws SQLException {
		String sql = null;
		ResultSet rs = null;
		Integer fieldId = null;
		for (final Entry<String, ArrayList<String>> entry : list.entrySet()) {
			for (final String value : entry.getValue()) {
				sql = "SELECT id FROM " + entry.getKey() + " WHERE LOWER(name) = LOWER('" + value + "')";
				rs = DB.query(sql);
				if (rs.next()) {
					fieldId = (Integer) rs.getObject("id");
					sql = "INSERT INTO movie_" + entry.getKey() + " (movie_id, " + entry.getKey() + "_id) " + "VALUES (" + movieId + ", " + fieldId + ")";
					DB.update(sql);
				} else {
					// System.out.println(movieId + " - " + sql);
				}
			}
		}
	}

	public static ArrayList<MovieModel> getMovies() throws SQLException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IOException {
		final ArrayList<MovieModel> tempList = new ArrayList<MovieModel>();
		final FieldList fl = Controller.flDB;
		MovieModel tempMovie = null;
		final Class<?>[] cList = { String.class, Integer.class, File.class, Image.class };
		final String sql = "SELECT " + fl.toStringFromType(cList, "m.", null) + " FROM movie as m";
		String subSql = "";
		final ResultSet rs = DB.query(sql);
		ResultSet rsSub = null;
		Boolean found = null;
		while (rs.next()) {
			tempMovie = new DefaultMovieModel();
			for (final FieldModel field : fl) {
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
					subSql = "SELECT " + field.getAs() + ".id, " + field.getAs() + ".name FROM " + field.getAs() + ", movie_" + field.getAs() + " WHERE movie_id = " + tempMovie.getId()
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
					Debug.log(Debug.LEVEL_DEBUG, "nothing");
				}
			}
			tempList.add(tempMovie);

		}

		return tempList;
	}

	public static ArrayList<KeyValue<Integer, String>> getList(final String listName) throws SQLException {
		final ArrayList<KeyValue<Integer, String>> tempList = new ArrayList<KeyValue<Integer, String>>();
		final String sql = "SELECT * FROM " + listName.replace("List", "") + " as l";
		final ResultSet rs = DB.query(sql);

		while (rs.next()) {
			tempList.add(new KeyValue<Integer, String>(rs.getInt("id"), rs.getString("name")));
		}

		return tempList;
	}

	public static void removeAllMovies() throws SQLException {
		final String sql = "DELETE FROM movie";
		DB.update(sql);
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

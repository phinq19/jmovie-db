/**
 * 
 */
package com.lars_albrecht.moviedb;

import java.lang.reflect.InvocationTargetException;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.moviedb.apiscraper.interfaces.IApiScraperPlugin;
import com.lars_albrecht.moviedb.apiscraper.themoviedb.TMDbScraper;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class MovieDB {

	/**
	 * Constructor.
	 */
	public MovieDB() {
		// TODO REMOVE THIS DEBUG / DEMO / TEST CODE
		// final ConcurrentHashMap<Integer, Object> x = new
		// ConcurrentHashMap<Integer, Object>();
		//
		// try {
		// final Image img = Toolkit.getDefaultToolkit().getImage(
		// "C:\\Users\\lalbrecht\\Pictures\\lordoftherings-thefellowshipoftherings.jpg");
		// x.put(1, img);
		// x.put(2, 2);
		// DB.updatePS("UPDATE movie SET cover = ? WHERE id = ?", x);
		// //
		// // x.clear();
		// // x.put(1, 1);
		// // final ResultSet rs =
		// DB.queryPS("SELECT cover FROM movie WHERE id = ?", x);
		// // rs.next();
		// // final BufferedImage bi =
		// ImageIO.read(rs.getBinaryStream("cover"));
		// // ImageIO.write(bi, "jpeg", new
		// File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala2.jpg"));
		// //
		// } catch(final SQLException e) {
		// e.printStackTrace();
		// } /*
		// * catch(final IOException e) { e.printStackTrace(); }
		// */
		//
		// System.exit(-1);

		// DefaultMovieModel dmm = new DefaultMovieModel();
		// try {
		// dmm.set("maintitlex", "Test");
		// System.out.println(dmm.get("maintitlex"));
		// } catch (SecurityException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (NoSuchMethodException e) {
		// e.printStackTrace();
		// }

		// testApiScraper();
		// System.exit(-1);
		System.setProperty("java.net.useSystemProxies", "true");
		Debug.log(Debug.LEVEL_DEBUG, "Start MovieDB");
		new Controller();

	}

	/**
	 * Main.
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(final String[] args) {
		new MovieDB();
	}

	@SuppressWarnings("unused")
	private void testApiScraper() {
		System.setProperty("java.net.useSystemProxies", "true");

		final String debugStr = "Blade Runner";
		final Integer debugYear = 1982;
		final IApiScraperPlugin x = new TMDbScraper();
		final MovieModel m = x.getMovieFromStringYear(debugStr, debugYear);
		if(m != null) {
			try {
				System.out.println(m.get("tmdbId"));
				System.out.println(m.get("alternativeName"));
				System.out.println(m.get("maintitle"));
			} catch(final SecurityException e) {
				e.printStackTrace();
			} catch(final IllegalAccessException e) {
				e.printStackTrace();
			} catch(final IllegalArgumentException e) {
				e.printStackTrace();
			} catch(final InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		// System.out.println("--------------");
		// x = new RottenTomatoesScraper();
		// m = x.getMovieFromStringYear(debugStr, debugYear);
		// if (m != null) {
		// // sysout informations
		// }

	}
}

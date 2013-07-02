package com.lars_albrecht.mdb.test.core.collector;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lars_albrecht.mdb.main.core.collector.TheMovieDBCollector;
import com.omertron.themoviedbapi.model.MovieDb;

/**
 * 
 * @author lalbrecht
 * 
 */
public class TheMovieDBCollectorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtility.init();
	}

	/**
	 * Test of:<br>
	 * - Herr der Ringe - Die Gefährten.mkv<br>
	 * - Herr der Ringe - Die Gefährten - 1080p - DTS.mkv<br>
	 * - Herr der Ringe - Die Gefährten - DTS.mkv<br>
	 * - Herr der Ringe - Die Gefährten - 2001.mkv<br>
	 * - Herr der Ringe - Die Gefährten - 2001 - 720p - x264.mkv<br>
	 * - Herr der Ringe - Die Gefährten - 2001 - 1080p - DTS.mkv<br>
	 * - Matrix - 1999.mkv<br>
	 * - Matrix - 1999 - 1024p.mkv<br>
	 * - Matrix.mkv<br>
	 * - Matrix - 1024p.mkv<br>
	 */
	@Test
	public void testGetDataForFilename() {
		Method method = null;
		try {
			method = TheMovieDBCollector.class.getDeclaredMethod("getDataForFilename", String.class);
			method.setAccessible(true);

			final ConcurrentHashMap<String, Object> testMap = new ConcurrentHashMap<String, Object>();
			ArrayList<String> titles = new ArrayList<String>();
			titles.add("Herr der Ringe");
			titles.add("Die Gefährten");
			testMap.put("titles", titles);

			testMap.put("year", -1);

			Assert.assertEquals("Herr der Ringe - Die Gefährten (clean)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten.mkv"));
			Assert.assertEquals("Herr der Ringe - Die Gefährten (+extras)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten - 1080p - DTS.mkv"));
			Assert.assertEquals("Herr der Ringe - Die Gefährten (+extras2)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten - DTS.mkv"));

			testMap.put("year", 2001);
			Assert.assertEquals("Herr der Ringe - Die Gefährten - 2001 (clean)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten - 2001.mkv"));
			Assert.assertEquals("Herr der Ringe - Die Gefährten - 2001 (+extras)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten - 2001 - 720p - x264.mkv"));
			Assert.assertEquals("Herr der Ringe - Die Gefährten - 2001 (+extras2)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Herr der Ringe - Die Gefährten - 2001 - 1080p - DTS.mkv"));

			titles = new ArrayList<String>();
			titles.add("Matrix");
			testMap.put("titles", titles);
			testMap.put("year", 1999);
			Assert.assertEquals("Matrix - 1999 (clean)", testMap, method.invoke(TestUtility.movieDBCollector, "Matrix - 1999.mkv"));
			Assert.assertEquals("Matrix - 1999 (+extras)", testMap,
					method.invoke(TestUtility.movieDBCollector, "Matrix - 1999 - 1024p.mkv"));

			testMap.put("year", -1);
			Assert.assertEquals("Matrix (clean)", testMap, method.invoke(TestUtility.movieDBCollector, "Matrix.mkv"));

			titles = new ArrayList<String>();
			titles.add("Matrix");
			titles.add("1024p");
			testMap.put("titles", titles);
			Assert.assertEquals("Matrix (+extras)", testMap, method.invoke(TestUtility.movieDBCollector, "Matrix - 1024p.mkv"));

			titles = new ArrayList<String>();
			titles.add("Example");
			titles.add("2000");
			testMap.put("titles", titles);
			testMap.put("year", 2000);
			Assert.assertEquals("Example - 2000 - 2000", testMap, method.invoke(TestUtility.movieDBCollector, "Example - 2000 - 2000.mkv"));

		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGetInfoType() {
		Assert.assertEquals("TheMovieDB", "TheMovieDB", TestUtility.movieDBCollector.getInfoType());
	}

	@Test
	public void testFindMovie() {
		// private MovieDb findMovie(final String[] titles, final Integer year)
		// throws IOException {
		String[] titles = {
			"Robin Hood"
		};
		Integer year = 2010;

		Method method = null;
		try {
			method = TheMovieDBCollector.class.getDeclaredMethod("findMovie", Array.newInstance(String.class, 0).getClass(), Integer.class);
			method.setAccessible(true);

			Assert.assertEquals("find Robin Hood of 2010 -> ID", 20662,
					((MovieDb) method.invoke(TestUtility.movieDBCollector, titles, year)).getId());

			year = 1973;
			Assert.assertEquals("find Robin Hood of 1973 -> ID", 11886,
					((MovieDb) method.invoke(TestUtility.movieDBCollector, titles, year)).getId());

			titles = new String[] {
					"Herr der Ringe", "Die Gefährten"
			};
			year = 2001;
			Assert.assertEquals("find Herr der Ringe - Die Gefährten -> ID", 120,
					((MovieDb) method.invoke(TestUtility.movieDBCollector, titles, year)).getId());

			titles = new String[] {
				"VOB_VS1_VS2_VOB",
			};
			Assert.assertNull("find Herr der Ringe - Die Gefährten -> ID", (method.invoke(TestUtility.movieDBCollector, titles, -1)));

		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}

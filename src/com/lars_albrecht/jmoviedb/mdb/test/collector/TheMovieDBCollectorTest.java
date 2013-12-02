/**
 * 
 */
package com.lars_albrecht.jmoviedb.mdb.test.collector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lars_albrecht.jmoviedb.mdb.collector.TheMovieDBCollector;
import com.lars_albrecht.mdb.main.core.models.persistable.FileItem;

/**
 * @author ibsisini
 * 
 */
public class TheMovieDBCollectorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.lars_albrecht.jmoviedb.mdb.collector.TheMovieDBCollector# getDataForFilename(FileItem file)}
	 * .
	 */
	@Test
	public void testGetDataForFilename() {
		final TheMovieDBCollector movieDBCollector = new TheMovieDBCollector();
		Method method = null;
		try {
			method = TheMovieDBCollector.class.getDeclaredMethod("getDataForFilename", FileItem.class);

			method.setAccessible(true);

			final ConcurrentHashMap<String, Object> equalsTest = new ConcurrentHashMap<String, Object>();

			// Ich, einfach unverbesserlich - 2010 - 1080p - AC3.mkv
			FileItem testFileItem = new FileItem(null, "Ich, einfach unverbesserlich - 2010 - 1080p - AC3.mkv",
					"Ich, einfach unverbesserlich - 2010 - 1080p - AC3.mkv", "", (long) 0, ".mkv", "movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", 2010);
			equalsTest.put("titles", Arrays.asList("Ich, einfach unverbesserlich"));

			Assert.assertEquals("{year=2010, titles=[Ich, einfach unverbesserlich]}", equalsTest,
					method.invoke(movieDBCollector, testFileItem));

			// Ich, einfach unverbesserlich 2 - 2013 - 1080p - AC3.mkv
			testFileItem = new FileItem(null, "Ich, einfach unverbesserlich 2 - 2013 - 1080p - AC3.mkv",
					"Ich, einfach unverbesserlich - 2013 - 1080p - AC3.mkv", "", (long) 0, ".mkv", "movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", 2013);
			equalsTest.put("titles", Arrays.asList("Ich, einfach unverbesserlich 2"));

			Assert.assertEquals("{year=2013, titles=[Ich, einfach unverbesserlich 2]}", equalsTest,
					method.invoke(movieDBCollector, testFileItem));

			// Hangover 3 - 2013 - 1080p - AC3.mkv
			testFileItem = new FileItem(null, "Hangover 3 - 2013 - 1080p - AC3.mkv", "Hangover 3", "", (long) 0, ".mkv", "movie", null,
					null, null);

			equalsTest.clear();
			equalsTest.put("year", 2013);
			equalsTest.put("titles", Arrays.asList("Hangover 3"));

			Assert.assertEquals("{year=2013, titles=[Hangover 3]}", equalsTest, method.invoke(movieDBCollector, testFileItem));

			// G.I. Joe - Die Abrechnung - 2013 - Extended Action Cut - 1080p -
			// AC3 - x264.mkv
			testFileItem = new FileItem(null, "G.I. Joe - Die Abrechnung - 2013 - Extended Action Cut - 1080p - AC3 - x264.mkv",
					"G.I. Joe - Die Abrechnung", "", (long) 0, ".mkv", "movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", 2013);
			equalsTest.put("titles", Arrays.asList("G.I. Joe", "Die Abrechnung"));
			Assert.assertEquals("{year=2013, titles=[G.I. Joe, Die Abrechnung]}", equalsTest, method.invoke(movieDBCollector, testFileItem));

			// Harold & Kumar - Extreme Unrated - 2004 - 720p - AC3D - x264.mkv
			testFileItem = new FileItem(null, "Harold & Kumar - Extreme Unrated - 2004 - 720p - AC3D - x264.mkv",
					"Harold & Kumar - Extreme Unrated", "", (long) 0, ".mkv", "movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", 2004);
			equalsTest.put("titles", Arrays.asList("Harold & Kumar", "Extreme Unrated"));
			Assert.assertEquals("{year=2004, titles=[Harold & Kumar, Extreme Unrated]}", equalsTest,
					method.invoke(movieDBCollector, testFileItem));

			// X-Men Origins Wolverine - 2009 - 720p.mkv
			testFileItem = new FileItem(null, "X-Men Origins Wolverine - 2009 - 720p.mkv", "X-Men Origins Wolverine - 2009 - 720p", "",
					(long) 0, ".mkv", "movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", 2009);
			equalsTest.put("titles", Arrays.asList("X-Men Origins Wolverine"));
			Assert.assertEquals("{year=2009, titles=[X-Men Origins Wolverine]}", equalsTest, method.invoke(movieDBCollector, testFileItem));

			// X-Men Origins Wolverine - 720p.mkv
			testFileItem = new FileItem(null, "X-Men Origins Wolverine - 720p.mkv", "X-Men Origins Wolverine", "", (long) 0, ".mkv",
					"movie", null, null, null);

			equalsTest.clear();
			equalsTest.put("year", -1);
			equalsTest.put("titles", Arrays.asList("X-Men Origins Wolverine", "720p"));
			Assert.assertEquals("{year=null, titles=[X-Men Origins Wolverine, 720p]}", equalsTest,
					method.invoke(movieDBCollector, testFileItem));

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

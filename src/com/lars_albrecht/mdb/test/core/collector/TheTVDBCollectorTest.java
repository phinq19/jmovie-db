package com.lars_albrecht.mdb.test.core.collector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lars_albrecht.mdb.main.core.collector.TheTVDBCollector;
import com.lars_albrecht.mdb.test.TestUtility;

/**
 * 
 * @author lalbrecht
 * 
 */
public class TheTVDBCollectorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtility.init();
	}

	@Test
	public void testGetDataForFilename() {
		Method method = null;

		try {
			method = TheTVDBCollector.class.getDeclaredMethod("getDataForFilename", String.class);
			method.setAccessible(true);

			final ConcurrentHashMap<String, Object> testMap = new ConcurrentHashMap<String, Object>();
			ArrayList<String> titles = new ArrayList<String>();
			titles.add("Dexter");
			testMap.put("titles", titles);
			testMap.put("episode", "S01E05");

			Assert.assertEquals("Dexter - S01E05 (clean)", testMap, method.invoke(TestUtility.tvDBCollector, "Dexter - S01E05.mkv"));
			Assert.assertEquals("Dexter - S01E05 (+extras)", testMap,
					method.invoke(TestUtility.tvDBCollector, "Dexter - S01E05 - 720p - x264.mkv"));

			titles = new ArrayList<String>();
			titles.add("Prison Break");
			testMap.put("titles", titles);
			testMap.put("episode", "S04E01E02");
			Assert.assertEquals("Prison Break - S04E01E02 (clean)", testMap,
					method.invoke(TestUtility.tvDBCollector, "Prison Break - S04E01E02.mkv"));

			titles = new ArrayList<String>();
			titles.add("The Big Bang Theory");
			testMap.put("titles", titles);
			testMap.put("episode", "S02E10");

			Assert.assertEquals("The Big Bang Theory (clean)", testMap,
					method.invoke(TestUtility.tvDBCollector, "The Big Bang Theory - S02E10.mkv"));
			Assert.assertEquals("The Big Bang Theory (+extras)", testMap,
					method.invoke(TestUtility.tvDBCollector, "The Big Bang Theory - S02E10 - AC3 - x264.mkv"));

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

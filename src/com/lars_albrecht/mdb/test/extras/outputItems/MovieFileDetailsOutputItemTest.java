/**
 * 
 */
package com.lars_albrecht.mdb.test.extras.outputItems;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lars_albrecht.mdb.main.core.models.Key;
import com.lars_albrecht.mdb.main.extras.outputItems.MovieFileDetailsOutputItem;

/**
 * @author lalbrecht
 * 
 */
public class MovieFileDetailsOutputItemTest {

	private static MovieFileDetailsOutputItem	testObject	= null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MovieFileDetailsOutputItemTest.testObject = new MovieFileDetailsOutputItem();
	}

	/**
	 * Test method for
	 * {@link com.lars_albrecht.mdb.main.extras.outputItems.MovieFileDetailsOutputItem#keyAllowed(com.lars_albrecht.mdb.main.core.models.Key)}
	 * .
	 */
	@Test
	public void testKeyAllowed() {
		final Key<String> testKey = new Key<String>("Filename", "TheMovieDB", "general", false, false);
		Assert.assertTrue("Test general key", MovieFileDetailsOutputItemTest.testObject.keyAllowed(testKey));
	}
}

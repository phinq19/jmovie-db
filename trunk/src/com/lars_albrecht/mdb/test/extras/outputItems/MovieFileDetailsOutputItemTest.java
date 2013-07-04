/**
 * 
 */
package com.lars_albrecht.mdb.test.extras.outputItems;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lars_albrecht.mdb.main.core.models.Key;
import com.lars_albrecht.mdb.main.core.models.KeyValue;
import com.lars_albrecht.mdb.main.core.models.Value;
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
		final KeyValue<String, Object> keyValue = new KeyValue<String, Object>(new Key<String>("homepage", "TheMovieDB", "facts", false,
				false), new Value<Object>("http://www.google.com/"));
		Assert.assertTrue("Test general key", MovieFileDetailsOutputItemTest.testObject.keyAllowed(keyValue.getKey().getInfoType(),
				keyValue.getKey().getSection(), keyValue));

		keyValue.getValue().setValue(null);
		Assert.assertFalse("Test general key", MovieFileDetailsOutputItemTest.testObject.keyAllowed(keyValue.getKey().getInfoType(),
				keyValue.getKey().getSection(), keyValue));
	}
}

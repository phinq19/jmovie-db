/**
 * 
 */
package com.lars_albrecht.moviedb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author lalbrecht
 * @see "http://www.devx.com/Java/Article/27235"
 * @see "http://bill.burkecentral.com/2008/01/14/scanning-java-annotations-at-runtime/"
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInTab {

	final public static int TYPE_AUTO = 0;
	final public static int TYPE_INPUT = 1;
	final public static int TYPE_AREA = 2;
	final public static int TYPE_SELECT = 3;
	final public static int TYPE_INT = 4;
	final public static int TYPE_IMAGE = 5;
	final public static int TYPE_CHECK = 6;
	final public static int TYPE_RATER = 7;

	final public static int FORMAT_AUTO = 0;
	final public static int FORMAT_FILESIZE = 1;

	String as();

	int sort();

	String tabname();

	boolean editable();

	int type();

	int format();
}

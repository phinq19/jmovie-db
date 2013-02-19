/**
 * 
 */
package com.lars_albrecht.moviedb.annotation;

/**
 * @author lalbrecht
 * 
 */
public @interface FilterOptions {

	public static int TYPE_AUTO = 0;
	public static int TYPE_CHECK = 1;
	public static int TYPE_INPUT = 2;

	String as();

	int sort();

	int type();

}

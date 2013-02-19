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
public @interface ViewInTable {
	String as();

	int sort();
}

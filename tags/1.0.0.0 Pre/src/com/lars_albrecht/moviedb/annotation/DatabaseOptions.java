/**
 * 
 */
package com.lars_albrecht.moviedb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author lalbrecht
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseOptions {
	public static int TYPE_TABLE = 0;
	public static int TYPE_FIELD = 1;

	String as();

	int type();

	String additionalType();

	String[] defaultValues();

	boolean isUnique();
}

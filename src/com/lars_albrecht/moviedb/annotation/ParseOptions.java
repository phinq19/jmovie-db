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
public @interface ParseOptions {
	final public static int TYPE_REGEX = 0;
	final public static int TYPE_LIST = 1;

	String as();

	int type();

	String typeConf();

}

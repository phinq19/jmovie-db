/**
 * 
 */
package com.lars_albrecht.moviedb.exceptions;

/**
 * @author lalbrecht
 * 
 */
public class NoMovieIDException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1548786304431702809L;

	public NoMovieIDException() {
	}

	public NoMovieIDException(final String s) {
		super(s);
	}
}

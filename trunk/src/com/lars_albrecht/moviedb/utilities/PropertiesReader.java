/*
 * Copyright (c) 2011 Lars Chr. Albrecht
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * 
 */
package com.lars_albrecht.moviedb.utilities;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Read out properties from *.properties-files to access strings for localisation.
 * 
 * @author lalbrecht
 * @version 1.0.0.0
 * 
 */
public final class PropertiesReader {

	private static PropertiesReader instance = null;
	private Locale locale = Locale.getDefault();

	/**
	 * @return the locale
	 */
	public synchronized final Locale getLocale() {
		return this.locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public synchronized final void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/**
	 * Private default constructor.
	 */
	private PropertiesReader() {
	}

	/**
	 * 
	 * @return PropertiesReader
	 */
	public static PropertiesReader getInstance() {

		if(PropertiesReader.instance == null) {
			PropertiesReader.instance = new PropertiesReader();
		}
		return PropertiesReader.instance;
	}

	/**
	 * Returns the value of the key. Converts the iso-8859-1 Strings to UTF-8.
	 * 
	 * @param key
	 * @return String
	 */
	public String getProperties(final String key) {
		String result = null;
		try {
			final ResourceBundle bundle = ResourceBundle.getBundle("moviedb", this.locale);
			result = bundle.getString(key);
			final byte bytes[] = result.getBytes("ISO-8859-1");
			result = new String(bytes, "UTF-8");
			return result;
		} catch(final MissingResourceException e) {
			e.printStackTrace();
		} catch(final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}

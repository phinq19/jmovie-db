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
package com.lars_albrecht.general.utilities;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Read out properties from *.properties-files to access strings for e.g. localisation.
 * 
 * @author lalbrecht
 * @version 1.0.0.0
 * 
 */
public final class RessourceBundleEx {

	private static RessourceBundleEx instance = null;
	private Locale locale = Locale.getDefault();
	private String prefix = "";

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
	private RessourceBundleEx() {
	}

	/**
	 * 
	 * @return PropertiesReader
	 */
	public static RessourceBundleEx getInstance() {

		if(RessourceBundleEx.instance == null) {
			RessourceBundleEx.instance = new RessourceBundleEx();
		}
		return RessourceBundleEx.instance;
	}

	/**
	 * Returns the value of the key. Converts the iso-8859-1 Strings to UTF-8.
	 * 
	 * @param key
	 * @return String
	 */
	public String getProperty(final String key) {
		try {
			final ResourceBundle bundle = ResourceBundle.getBundle(this.prefix, this.locale);
			return new String(bundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch(final MissingResourceException e) {
			e.printStackTrace();
		} catch(final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean contains(final String key) {
		return ResourceBundle.getBundle(this.prefix, this.locale).containsKey(key);
	}

	/**
	 * @return the prefix
	 */
	public synchronized final String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public synchronized final void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

}

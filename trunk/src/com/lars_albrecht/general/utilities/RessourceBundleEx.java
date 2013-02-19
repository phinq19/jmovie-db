/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Read out properties from *.properties-files to access strings for e.g.
 * localisation.
 * 
 * @author lalbrecht
 * @version 1.0.0.0
 * 
 */
public final class RessourceBundleEx {

	private static RessourceBundleEx	instance	= null;

	/**
	 * 
	 * @return PropertiesReader
	 */
	public static RessourceBundleEx getInstance() {

		if (RessourceBundleEx.instance == null) {
			RessourceBundleEx.instance = new RessourceBundleEx();
		}
		return RessourceBundleEx.instance;
	}

	private Locale	locale	= Locale.getDefault();

	private String	prefix	= "";

	/**
	 * Private default constructor.
	 */
	private RessourceBundleEx() {
	}

	public Boolean contains(final String key) {
		return ResourceBundle.getBundle(this.prefix, this.locale).containsKey(key);
	}

	/**
	 * @return the locale
	 */
	public synchronized final Locale getLocale() {
		return this.locale;
	}

	/**
	 * @return the prefix
	 */
	public synchronized final String getPrefix() {
		return this.prefix;
	}

	public ArrayList<String> getProperties(final String key) {
		final ArrayList<String> resultList = new ArrayList<String>();
		int i = 1;
		while (this.contains(key + "." + i)) {
			resultList.add(this.getProperty(key + "." + i));
			i++;
		}
		return resultList;
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
		} catch (final MissingResourceException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public synchronized final void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public synchronized final void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

}

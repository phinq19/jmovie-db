/**
 * 
 */
package com.lars_albrecht.general.utilities;

//-----------------------------------------------------------------------------
//PropertyFiles.java
//-----------------------------------------------------------------------------

/*
 * =============================================================================
 * Copyright (c) 1998-2005 Jeffrey M. Hunter. All rights reserved.
 *
 * All source code and material located at the Internet address of
 * http://www.idevelopment.info is the copyright of Jeffrey M. Hunter, 2005 and
 * is protected under copyright laws of the United States. This source code may
 * not be hosted on any other site without my express, prior, written
 * permission. Application to host any of the material elsewhere can be made by
 * contacting me at jhunter@idevelopment.info.
 *
 * I have made every effort and taken great care in making sure that the source
 * code and other content included on my web site is technically accurate, but I
 * disclaim any and all responsibility for any loss, damage or destruction of
 * data or any other property which may arise from relying on it. I will in no
 * case be liable for any monetary damages arising from such loss, damage or
 * destruction.
 *
 * As with any code, ensure to test this code in a development environment
 * before attempting to run it in production.
 * =============================================================================
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * -----------------------------------------------------------------------------
 * Used to provide an example of how to get/set Properties and load/save
 * property files.
 * 
 * @version 1.0
 * @author Jeffrey M. Hunter (jhunter@idevelopment.info)
 * @author http://www.idevelopment.info
 *         ------------------------------------------
 *         -----------------------------------
 */

public class PropertyFiles {

	private static Properties alterProperties(final Properties p) {

		final Properties newProps = new Properties();
		final Enumeration<?> enProps = p.propertyNames();
		String key = "";

		while (enProps.hasMoreElements()) {

			key = (String) enProps.nextElement();

			// If we were going to use the "log_level" or "database_oid" key in
			// our application, we would want to cast the value to an Integer or
			// Long.
			// log_level = Integer.parseInt(props.getProperty("log_level"));
			// database_oid = Long.parseLong(props.getProperty("database_oid"));

			if (!key.equals("fake_entry")) {
				if (key.equals("log_level")) {
					newProps.setProperty(key, "3");
				} else {
					newProps.setProperty(key, p.getProperty(key));
				}
			}

		}

		return newProps;

	}

	private static Properties createDefaultProperties() {

		final Properties tempProp = new Properties();

		tempProp.setProperty("company_name", "iDevelopment, Inc.");
		tempProp.setProperty("company_domain", "idevelopment.info");
		tempProp.setProperty("global_database_name", "TRUESRC_SUNDEV5.IDEVELOPMENT.INFO");
		tempProp.setProperty("oracle_sid", "TRUESRC");
		tempProp.setProperty("listener_port", "1521");
		tempProp.setProperty("database_server", "sundev5.idevelopment.info");
		tempProp.setProperty("truesourceAdminUsername", "ts_admin");
		tempProp.setProperty("truesource_admin_password", "ts_adm1n");
		tempProp.setProperty("truesource_guest_username", "ts_guest");
		tempProp.setProperty("truesource_guest_password", "ts_gu3st");
		tempProp.setProperty("log_level", "9");
		tempProp.setProperty("database_oid", "99745190543");
		tempProp.setProperty("fake_entry", "000000000000");
		return tempProp;

	}

	private static Properties loadProperties(final String fileName) {

		InputStream propsFile;
		final Properties tempProp = new Properties();

		try {
			propsFile = new FileInputStream(fileName);
			tempProp.load(propsFile);
			propsFile.close();
		} catch (final IOException ioe) {
			System.out.println("I/O Exception.");
			ioe.printStackTrace();
			System.exit(0);
		}

		return tempProp;

	}

	/**
	 * Sole entry point to the class and application.
	 * 
	 * @param args
	 *            Array of String arguments.
	 */
	public static void main(final String[] args) {

		final String PROPFILE = "Application.properties";
		Properties myProp;
		Properties myNewProp;

		myProp = PropertyFiles.createDefaultProperties();
		PropertyFiles.printProperties(myProp, "Newly Created (Default) Properties");
		PropertyFiles.saveProperties(myProp, PROPFILE);
		myNewProp = PropertyFiles.loadProperties(PROPFILE);
		PropertyFiles.printProperties(myNewProp, "Loaded Properties");
		myNewProp = PropertyFiles.alterProperties(myProp);
		PropertyFiles.printProperties(myNewProp, "After Altering Properties");
		PropertyFiles.saveProperties(myNewProp, PROPFILE);

	}

	private static void printProperties(final Properties p, final String s) {

		System.out.println();
		System.out.println("========================================");
		System.out.println(s);
		System.out.println("========================================");
		System.out.println("+---------------------------------------+");
		System.out.println("| Print Application Properties          |");
		System.out.println("+---------------------------------------+");
		p.list(System.out);
		System.out.println();
	}

	private static void saveProperties(final Properties p, final String fileName) {

		OutputStream propsFile;

		try {
			propsFile = new FileOutputStream(fileName);
			p.store(propsFile, "Properties File to the Test Application");
			propsFile.close();
		} catch (final IOException ioe) {
			System.out.println("I/O Exception.");
			ioe.printStackTrace();
			System.exit(0);
		}

	}

}
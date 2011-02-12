/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moviedb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class holds the database-connection and load the driver. If the connection is not connected,
 * the class builds the connection to the database.
 *
 * @author Lars
 *
 */
public class DBConn {

	private static Connection conn = null;

	private final static String dbUsername = "moviedb";

	private final static String dbPassword = "mypw";

	private final static String dbName = "moviedb";

	private final static String dbUrl = "jdbc:hsqldb:file:";

	private final static String Strdb = "org.hsqldb.jdbcDriver";



	/**
	 * Returns the Connection
	 *
	 * @return connection
	 */
	public static Connection getConn () {
            try {
                    if (conn == null || conn.isReadOnly() || conn.isClosed()) {
                        try {
                            // Laden des Treibers
                            Class.forName(Strdb);
                            // Verbindung aufbauen
                            conn = DriverManager.getConnection(dbUrl + dbName, dbUsername, dbPassword);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
	}

}

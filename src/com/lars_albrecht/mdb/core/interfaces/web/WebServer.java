/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

/*

 Common Port Assignments and Corresponding RFC Numbers              

 Port Common Name RFC#  Purpose
 7     Echo        862   Echoes data back. Used mostly for testing.
 9     Discard     863   Discards all data sent to it. Used mostly for testing.
 13    Daytime     867   Gets the date and time.
 17    Quotd       865   Gets the quote of the day.
 19    Chargen     864   Generates characters. Used mostly for testing.
 20    ftp-data    959   Transfers files. FTP stands for File Transfer Protocol.
 21    ftp         959   Transfers files as well as commands.
 23    telnet      854   Logs on to remote systems.
 25    SMTP        821   Transfers Internet mail. Stands for Simple Mail Transfer Protocol.
 37    Time        868   Determines the system time on computers.
 43    whois       954   Determines a user's name on a remote system.
 70    gopher     1436   Looks up documents, but has been mostly replaced by HTTP.
 79    finger     1288   Determines information about users on other systems.
 80    http       1945   Transfer documents. Forms the foundation of the Web.
 110   pop3       1939   Accesses message stored on servers. Stands for Post Office Protocol, version 3.
 443   https      n/a    Allows HTTP communications to be secure. Stands for Hypertext Transfer Protocol over Secure Sockets Layer (SSL).

 */

///A Simple Web Server (WebServer.java)

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.handler.TypeHandler;
import com.lars_albrecht.mdb.core.models.FileItem;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 * 
 * @see "http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm"
 */
/*
 * Examining the Mini Web Server
 * 
 * Server sockets use the ServerSocket object rather than the Socket object that
 * client sockets use. There are several constructors available with the
 * ServerSocket object. The simplest constructor accepts only the port number on
 * which the program should be listening. Listening refers to the mode that a
 * server is in while it waits for clients to connect. The following lines of
 * code are used in Listing 1.3 to create a new ServerSocket object and reserve
 * port 80 as the port number on which the web server should listen for
 * connections:
 * 
 * try { // create the main server socket s = new ServerSocket(80); }
 * catch(Exception e) { System.out.println("Error: " + e ); return; }
 * 
 * The try block is necessary because any number of errors could occur when the
 * program attempts to register port 80. The most common error that would result
 * is that there is already a server listening to port 80 on this machine.
 * Warning
 * 
 * This program will not work on a machine that already has a web server, or
 * some other program, listening on port 80.
 * 
 * Once the program has port 80 registered, it can begin listening for
 * connections. The following line of code is used to wait for a connection:
 * 
 * Socket remote = s.accept();
 * 
 * The Socket object that is returned by accept is exactly the same class that
 * is used for client sockets. Once the connection is established, the
 * difference between client and server sockets fade. The primary difference
 * between client and server sockets is the way in which they connect. A client
 * sever connects to something. A server socket waits for something to connect
 * to it.
 * 
 * The accept method is a blocking call, which means the current thread will
 * wait for a connection. This can present problems for your program if there
 * are other tasks it would like to accomplish while it is waiting for
 * connections. Because of this, it is very common to see the accept method call
 * placed in a worker thread. This allows the main thread to carry on other
 * tasks, while the worker thread waits for connections to arrive.
 * 
 * Once a connection is made, the accept method will return a socket object for
 * the new socket. After this point, reading and writing is the same between
 * client and server sockets. Many client server programs would create a new
 * thread to handle this new connection.
 * 
 * Now that a connection has been made, a new thread could be created to handle
 * it. This new worker thread would process all the requests from this client in
 * the background, which allows the ServerSocket object to wait for and service
 * more connections. However, the example program in Listing 1.3 does not
 * require such programming. As soon as the socket is accepted, input and output
 * objects are created; this same process was used with the SMTP client. The
 * following lines from Listing 1.3 show the process of preparing the newly
 * accepted socket for input and output:
 * 
 * //remote is now the connected socket
 * System.out.println("Connection, sending data."); BufferedReader in = new
 * BufferedReader( new InputStreamReader(remote.getInputStream()) ); PrintWriter
 * out = new PrintWriter(remote.getOutputStream());
 * 
 * Now that the program has input and output objects, it can process the HTTP
 * request. It first reads the HTTP request lines. A full-featured server would
 * parse each line and determine the exact nature of this request, however, our
 * ultra-simple web server just reads in the request lines and ignores them, as
 * shown here:
 * 
 * //read the data sent. We basically ignore it, //stop reading once a blank
 * line is hit. This //blank line signals the end of the //client HTTP headers.
 * String str="."; while(!str.equals("")) str = in.readLine();
 * 
 * These lines cause the server to read in lines of text from the newly
 * connected socket. Once a blank line (which indicates the end of the HTTP
 * header) is reached, the loop stops, and the server stops reading. Now that
 * the HTTP header has been retrieved, the server sends an HTTP response. The
 * following lines of code accomplish this:
 * 
 * //Send the response //Send the headers out.println("HTTP/1.0 200 OK");
 * out.println("Content-Type: text/html"); out.println("Server: Bot"); //this
 * blank line signals the end of the headers out.println(""); // Send the HTML
 * page out.println( " <H1> Welcome to the Ultra Mini-WebServer </H2> ");
 * 
 * Status code 200, as shown on line 3 of the preceding code, is used to show
 * that the page was properly transferred, and that the required HTTP headers
 * were sent. (Refer to Chapter 2 for more information about HTTP headers.)
 * Following the HTTP headers, the actual HTML page is transferred. Once the
 * page is transferred, the following lines of code from Listing 1.3 are
 * executed to clean up:
 * 
 * out.flush(); remote.close();
 * 
 * The flush method is necessary to ensure that all data is transferred, and the
 * close method is necessary to close the socket. Although Java will discard the
 * Socket object, it will not generally close the socket on most platforms.
 * Because of this, you must close the socket or else you might eventually get
 * an error indicating that there are no more file handles. This becomes very
 * important for a program that opens up many connections, including one to a
 * spider.
 */
public class WebServer {

	private MainController	mainController	= null;

	/**
	 * WebServer constructor.
	 */
	protected void start(final MainController mainController) {
		this.mainController = mainController;

		System.out.println("WebServerConstructor start() at line ~160");
		ServerSocket s;

		System.out.println("Webserver starting up on port 80");
		System.out.println("(press ctrl-c to exit)");
		try {
			// create the main server socket
			s = new ServerSocket(8080);
		} catch (final Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection");
		for (;;) {
			try {
				// wait for a connection
				final Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data.");
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(remote.getInputStream()));
				final PrintWriter out = new PrintWriter(
						remote.getOutputStream());

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.

				String requestLine = "";
				final ConcurrentHashMap<String, String> headerKeyValue = new ConcurrentHashMap<String, String>();
				final ConcurrentHashMap<String, String> getKeyValue = new ConcurrentHashMap<String, String>();

				// GET /asdasdasdasd?asdasd HTTP/1.1
				String urlStr = "";

				requestLine = in.readLine();
				boolean notNull = false;
				if (requestLine != null && !requestLine.equals("")) {
					notNull = true;
					// System.out.println("notNull = true");
				} else {
					// System.out.println("notNull = false");
				}

				while ((notNull || ((requestLine = in.readLine()) != null)
						&& (!requestLine.equals("")))) {
					notNull = false;
					final String[] keyValue = requestLine.split(":");
					if (keyValue.length > 1) {
						headerKeyValue.put(keyValue[0].trim(),
								keyValue[1].trim());
					}
					if (requestLine.startsWith("GET ")) {
						final int urlStart = 4;
						final int urlEnd = requestLine.indexOf(" HTTP/1.1");
						urlStr = requestLine.substring(urlStart, urlEnd);
						final int paramPos = requestLine.indexOf("?");
						String paramString = "";
						if (paramPos > -1) {
							urlStr = urlStr.substring(0, urlStr.indexOf("?"));
							paramString = requestLine.substring(paramPos,
									urlEnd);
							paramString = paramString.replaceFirst("\\?", "");
							final String[] paramsArr = paramString.split("&");
							for (final String string : paramsArr) {
								final String[] paramArr = string.split("=");
								if (paramArr.length > 0 && paramArr.length > 1) {
									getKeyValue.put(paramArr[0], paramArr[1]);
								} else {
									getKeyValue.put(paramArr[0], "");
								}
							}

							requestLine = requestLine.substring(4, paramPos);
						}

						// System.out.println(urlStr);
						// System.out.println("PARAMS: " + paramString);
						// System.out.println(uri.getURI().getPath());
					}
				}

				// Send the HTML page
				final String content = this.getContent(urlStr, getKeyValue,
						headerKeyValue);

				// Send the response
				// Send the headers
				if (content == null) {
					out.println("HTTP/1.0 404 Not Found");
				} else {
					out.println("HTTP/1.0 200 OK");
				}

				if (urlStr != null) {
					if (urlStr.endsWith(".js")) {
						out.println("Content-Type: text/javascript; charset=utf-8");
					} else if (urlStr.endsWith(".css")) {
						out.println("Content-Type: text/css; charset=utf-8");
					} else if (urlStr.endsWith(".ico")) {
						out.println("image/x-icon; charset=utf-8");
					} else {
						out.println("Content-Type: text/html; charset=utf-8");
					}
				}

				// out.println("Content-Type: text/html");
				out.println("Server: MDB");
				// this blank line signals the end of the headers
				out.println("");

				out.println(content != null ? content : "");
				out.flush();
				remote.close();
			} catch (final Exception e) {
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
		}
	}

	private String getContent(final String url,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
		if (url != null) {
			final File file = (new File(new File("").getAbsolutePath()
					+ "/web/" + url));
			// System.out.println("APATH: " + file.getAbsolutePath());
			try {
				if (file != null && file.exists()) {
					// System.out.println("load file: " +
					// file.getAbsolutePath());
					String content = "";
					content = Helper.getFileContents(file);
					content = this.generateContent(content, file.getName(),
							GETParams, headerKeyValue);

					return content;
				} else if (file != null && !file.exists()) {
					// System.out.println("cant load file: " +
					// file.getAbsolutePath());
				} else {
					// System.out.println("cant load file with url");
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private String generateContent(final String content, final String filename,
			final ConcurrentHashMap<String, String> GETParams,
			final ConcurrentHashMap<String, String> headerKeyValue) {
		String generatedContent = content;
		String contentMarkerReplacement = "";
		System.out.println("Params: " + GETParams);
		if (filename.equalsIgnoreCase("index.html")) {
			String action = null;
			if (GETParams.containsKey("action")) {
				action = GETParams.get("action");
			} else {
				action = "index";
			}

			if (action.equalsIgnoreCase("index")) {
				contentMarkerReplacement = "index";
			} else if (action.equalsIgnoreCase("showSearchresults")) {
				if (GETParams.containsKey("searchStr")
						&& GETParams.get("searchStr") != null) {
					// get DATA for output
					String searchStr = GETParams.get("searchStr");
					searchStr = searchStr.replaceAll("\\+", " ");
					ArrayList<FileItem> foundList = TypeHandler
							.castObjectListToFileItemList(this.mainController
									.getDataHandler().findAllInfoForString(
											searchStr, false));
					foundList = Helper.unique(foundList);

					contentMarkerReplacement = this.generateListOutput(
							foundList, true);
				} else {
					contentMarkerReplacement = "<p>Suchen Sie mit hilfe der Suche</p>";
				}
			} else if (action.equalsIgnoreCase("showFileDetails")) {
				contentMarkerReplacement = "showFileDetails";
			}

			// replace contentmarker with "contentMarkerReplacement" if marker
			// exists.
			if (this.containsMarker(generatedContent, "content")) {
				generatedContent = this.replaceMarker(generatedContent,
						"content", contentMarkerReplacement);
			}

			// replace "free" marker.
			if (this.containsMarker(generatedContent, "lastFiveAdded")) {
				final ArrayList<FileItem> lastFiveList = TypeHandler
						.castObjectListToFileItemList(this.mainController
								.getDataHandler().findAll(new FileItem(),
										false, 5));
				final String listOutput = this.generateListOutput(lastFiveList,
						false);
				System.out.println("LAST FIVE: " + listOutput);
				generatedContent = this.replaceMarker(generatedContent,
						"lastFiveAdded", listOutput);
			}

		}

		return generatedContent;
	}

	private Boolean containsMarker(final String content, final String marker) {
		Pattern pattern = null;
		if (marker == null) {
			pattern = Pattern.compile("\\{(.*)\\}");
		} else {
			pattern = Pattern.compile("\\{" + marker + "\\}");
		}
		final Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}

	/**
	 * Returns the first markername.
	 * 
	 * @param content
	 * @return String
	 */
	private String getNextMarkername(final String content) {
		final Pattern pattern = Pattern.compile("\\{(.*)\\}");
		final Matcher matcher = pattern.matcher(content);
		String name = null;
		if (matcher.find()) {
			name = matcher.group(1);
		}
		return name;
	}

	/**
	 * Replace a marker "markername" in "content" with "replacement".
	 * 
	 * @param content
	 * @param markername
	 * @param replacement
	 * @return String
	 */
	private String replaceMarker(final String content, final String markername,
			final String replacement) {
		String inContent = content;
		inContent = inContent.replaceFirst("(\\{" + markername + "\\})+",
				replacement);
		return inContent;
	}

	/**
	 * Generate HTML Output from FileItem-List.
	 * 
	 * @param fileItemList
	 * @return String
	 */
	private String generateListOutput(final ArrayList<FileItem> fileItemList,
			final boolean printOutCount) {
		String resultStr = "";
		if (fileItemList.size() > 0) {
			if (printOutCount) {
				resultStr += "<p>" + fileItemList.size()
						+ " Einträge gefunden</p>";
			}
			resultStr += "<div class=\"resulttable\">";
			resultStr += "<table>";
			resultStr += "<tr>";
			resultStr += "<th>Filename</th>";
			resultStr += "</tr>";

			for (final FileItem fileItem : fileItemList) {
				resultStr += "<tr>";
				resultStr += "<td><a href=\"?action=showFileDetails&fileId="
						+ fileItem.getId() + "\">" + fileItem.getName()
						+ "</a></td>";
				resultStr += "</tr>";
			}
			resultStr += "</table>";
			resultStr += "</div>";

		} else {
			resultStr = "<p>Keine Ergebnisse</p>";
		}
		return resultStr;
	}
}
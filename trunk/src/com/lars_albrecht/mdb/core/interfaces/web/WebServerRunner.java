/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.concurrent.ConcurrentHashMap;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.mdb.core.controller.MainController;
import com.lars_albrecht.mdb.core.interfaces.web.helper.WebServerHelper;

/**
 * @author lalbrecht
 * 
 */
public class WebServerRunner implements Runnable {

	private MainController						mainController	= null;
	private Socket								clientSocket	= null;
	private ConcurrentHashMap<String, String>	headerKeyValue	= null;
	private ConcurrentHashMap<String, String>	getKeyValue		= null;

	public WebServerRunner(final MainController mainController, final Socket clientSocket) {
		this.mainController = mainController;
		this.clientSocket = clientSocket;
		this.headerKeyValue = new ConcurrentHashMap<String, String>();
		this.getKeyValue = new ConcurrentHashMap<String, String>();
	}

	private ConcurrentHashMap<String, String> getQuery(final String url) throws UnsupportedEncodingException {
		ConcurrentHashMap<String, String> resultList = null;
		if (url != null) {
			resultList = new ConcurrentHashMap<String, String>();
			final int paramPos = url.indexOf("?");
			String paramString = "";
			paramString = url.substring(paramPos);
			paramString = paramString.replaceFirst("\\?", "");
			final String[] paramsArr = paramString.split("&");
			for (final String string : paramsArr) {
				final String[] paramArr = string.split("=");
				if ((paramArr.length > 0) && (paramArr.length > 1)) {
					resultList.put(URLDecoder.decode(paramArr[0], "utf-8"), URLDecoder.decode(paramArr[1], "utf-8"));
				} else if (paramArr.length > 0) {
					resultList.put(URLDecoder.decode(paramArr[0], "utf-8"), "");
				}
			}
		}

		return resultList;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		String line = null;
		boolean notNull = false;
		String urlStr = null;
		boolean run = true;

		try {
			in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			out = new PrintWriter(this.clientSocket.getOutputStream());
		} catch (final IOException e) {
			System.err.println("Error on connection init.");
		}

		while (run) {
			try {

				// read the data sent. We basically ignore it,
				// stop reading once a blank line is hit. This
				// blank line signals the end of the client HTTP
				// headers.
				line = in.readLine();
				if ((line != null) && !line.equals("")) {
					notNull = true;
				}

				while ((notNull || (((line = in.readLine()) != null) && (!line.equals(""))))) {
					notNull = false;
					final String[] keyValue = line.split(":");
					if (keyValue.length > 1) {
						this.headerKeyValue.put(keyValue[0].trim(), keyValue[1].trim());
					}
					if (line.startsWith("GET ") || line.startsWith("POST ")) {
						final int urlStart = line.startsWith("GET ") ? 5 : 6;
						final int urlEnd = line.indexOf(" HTTP/1.1");
						urlStr = line.substring(urlStart, urlEnd);
						Debug.log(Debug.LEVEL_TRACE, "URL: " + urlStr);
						if (urlStr.indexOf("?") > -1) {
							this.getKeyValue = this.getQuery(urlStr);
							urlStr = urlStr.substring(0, urlStr.indexOf("?"));
						}
					}
				}

				String content = null;
				if (urlStr != null) {
					if (!urlStr.startsWith("ajax.html")) {
						content = new WebServerHelper(this.mainController).getFileContent(urlStr, this.getKeyValue, this.headerKeyValue);

						// Send the response
						// Send the headers
						if ((content == null) || urlStr.endsWith(".ico")) {
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

					} else if (urlStr.startsWith("ajax.html")) {
						content = new WebServerHelper(this.mainController).getAjaxContent(urlStr, this.getKeyValue, this.headerKeyValue);
						if (content != null) {
							out.println("HTTP/1.0 200 OK");
							out.println("Content-Type: text/html; charset=utf-8");
							out.println("Server: MDB");
							out.println("");
							out.println(content);
						} else {
							out.println("HTTP/1.0 404 Not Found");
							out.println("Content-Type: text/html; charset=utf-8");
							out.println("Server: MDB");
							out.println("");
							out.println("404");
						}
					}
				}

			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
				System.err.println("Error on encoding query");
			} catch (final IOException e) {
				e.printStackTrace();
				System.err.println("Error on connection read");
			} finally {
				try {
					if (run) {
						out.flush();
						this.clientSocket.close();
					} else {
						if ((out != null) && !out.checkError() && this.clientSocket.isConnected() && this.clientSocket.isBound()) {
							this.clientSocket.close();
						}
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
				run = false;
			}
		}
	}
}
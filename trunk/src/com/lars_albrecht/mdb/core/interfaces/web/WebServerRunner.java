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

	private MainController	mainController	= null;
	private Socket			clientSocket	= null;

	public WebServerRunner(final MainController mainController, final Socket clientSocket) {
		this.mainController = mainController;
		this.clientSocket = clientSocket;
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
		PrintWriter out = null;
		String urlStr = null;
		boolean run = true;

		try {
			out = new PrintWriter(this.clientSocket.getOutputStream());
		} catch (final IOException e) {
			System.err.println("Error on connection init.");
		}

		while (run) {
			try {

				final WebServerRequest request = this.createRequest(this.clientSocket);
				urlStr = request.getUrl();

				String content = null;
				if (urlStr != null) {
					if (!urlStr.startsWith("ajax.html") && !urlStr.startsWith("json.html")) {

						content = new WebServerHelper(this.mainController).getFileContent(urlStr, "web", request);

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
						content = new WebServerHelper(this.mainController).getAjaxContent(urlStr, request, false);
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
					} else if (urlStr.startsWith("json.html")) {
						content = new WebServerHelper(this.mainController).getAjaxContent(urlStr, request, true);
						if (content != null) {
							out.println("HTTP/1.0 200 OK");
							out.println("Content-Type: application/json; charset=utf-8");
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

	private WebServerRequest createRequest(final Socket clientSocket) throws IOException {
		final WebServerRequest request = new WebServerRequest();
		final BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		String line = null;
		boolean notNull = false;
		String urlStr = null;
		// read the data sent. We basically ignore it,
		// stop reading once a blank line is hit. This
		// blank line signals the end of the client HTTP
		// headers.
		line = in.readLine();
		if ((line != null) && !line.equals("")) {
			notNull = true;
		}

		// TODO CREATE HEADER OBJECT AND PARSE FULL REQUEST
		while ((notNull || (((line = in.readLine()) != null) && (!line.equals(""))))) {
			notNull = false;
			// if (keyValue.length > 1) {
			// this.headerKeyValue.put(keyValue[0].trim(), keyValue[1].trim());
			// }
			if (line.startsWith("GET ") || line.startsWith("POST ")) {

				final int urlStart = line.startsWith("GET ") ? 5 : 6;
				final int urlEnd = line.indexOf(" HTTP/1.1");
				urlStr = line.substring(urlStart, urlEnd);

				request.setMethod(line.substring(0, urlStart - 2));
				request.setFullUrl(urlStr);
				Debug.log(Debug.LEVEL_TRACE, "URL: (" + line.substring(0, urlStart - 2) + ")" + urlStr);
				if (urlStr.indexOf("?") > -1) {
					request.setGetParams(this.getQuery(urlStr));
					urlStr = urlStr.substring(0, urlStr.indexOf("?"));
				}
				request.setUrl(urlStr);
			} else {
				final String[] asParam = this.getHeaderParam(line);
				if (asParam != null) {
					request.getGetParams().put(asParam[0], asParam[1]);
					Debug.log(Debug.LEVEL_TRACE, "ELSE " + line);
				}
			}
		}

		String content = "";
		int value = 0;
		while (in.ready() && ((value = in.read()) != -1)) {
			// converts int to character
			final char c = (char) value;

			// prints character
			content += c;
		}
		request.setContent(content);
		request.setPostParams(this.getQuery("?" + content));

		return request;
	}

	private String[] getHeaderParam(final String paramLine) {
		if (paramLine.contains(":")) {
			final String[] param = new String[2];
			param[0] = paramLine.substring(0, paramLine.indexOf(":"));
			param[1] = paramLine.substring(paramLine.indexOf(":"), paramLine.length() - 1);
			return param;
		} else {
			return null;
		}
	}
}
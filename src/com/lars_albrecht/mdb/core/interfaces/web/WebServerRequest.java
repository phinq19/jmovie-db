/**
 * 
 */
package com.lars_albrecht.mdb.core.interfaces.web;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lalbrecht
 * 
 */
public class WebServerRequest {

	private ConcurrentHashMap<String, Object>	headerValues	= null;
	private String								content			= null;
	private String								fullUrl			= null;
	private String								url				= null;
	private String								method			= null;
	private ConcurrentHashMap<String, String>	getParams		= null;
	private ConcurrentHashMap<String, String>	postParams		= null;

	public WebServerRequest() {
		this.headerValues = new ConcurrentHashMap<String, Object>();
		this.getParams = new ConcurrentHashMap<String, String>();
	}

	/**
	 * @return the headerValues
	 */
	public final ConcurrentHashMap<String, Object> getHeaderValues() {
		return this.headerValues;
	}

	/**
	 * @param headerValues
	 *            the headerValues to set
	 */
	public final void setHeaderValues(final ConcurrentHashMap<String, Object> headerValues) {
		this.headerValues = headerValues;
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public final void setContent(final String content) {
		this.content = content;
	}

	/**
	 * @return the urlStr
	 */
	public final String getFullUrl() {
		return this.fullUrl;
	}

	/**
	 * @param fullUrl
	 *            the fullUrl to set
	 */
	public final void setFullUrl(final String fullUrl) {
		this.fullUrl = fullUrl;
	}

	/**
	 * @return the method
	 */
	public final String getMethod() {
		return this.method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public final void setMethod(final String method) {
		this.method = method;
	}

	/**
	 * @return the getParams
	 */
	public final ConcurrentHashMap<String, String> getGetParams() {
		return this.getParams;
	}

	/**
	 * @param getParams
	 *            the getParams to set
	 */
	public final void setGetParams(final ConcurrentHashMap<String, String> getParams) {
		this.getParams = getParams;
	}

	/**
	 * @return the url
	 */
	public final String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public final void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * @return the postParams
	 */
	public final ConcurrentHashMap<String, String> getPostParams() {
		return this.postParams;
	}

	/**
	 * @return the postParams
	 */
	public final ConcurrentHashMap<String, String> getParams() {
		final ConcurrentHashMap<String, String> newParamMap = new ConcurrentHashMap<String, String>();
		newParamMap.putAll(this.getParams);
		newParamMap.putAll(this.postParams);
		return newParamMap;
	}

	/**
	 * @param postParams
	 *            the postParams to set
	 */
	public final void setPostParams(final ConcurrentHashMap<String, String> postParams) {
		this.postParams = postParams;
	}

}

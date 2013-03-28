/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @see "http://www.rgagnon.com/javadetails/java-0416.html"
 * 
 */
public class ChecksumMD5 {

	public static byte[] createChecksum(final String filename) throws Exception {
		final InputStream fis = new FileInputStream(filename);

		final byte[] buffer = new byte[1024];
		final MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(final String filename) throws Exception {
		final byte[] b = ChecksumMD5.createChecksum(filename);
		final String result = Helper.getHex(b);
		return result;
	}

}
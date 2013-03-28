/**
 * 
 */
package com.lars_albrecht.general.utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * @author albrela
 * 
 */
public class ChecksumCRC32 {

	public static long createCRC32Checksum(final String filename) throws Exception {
		final InputStream fis = new FileInputStream(filename);

		final byte[] buffer = new byte[1024];
		final CRC32 crc = new CRC32();
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				crc.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return crc.getValue();
	}

	public static String getCRC32Checksum(final String filename) throws Exception {
		return Long.toString(ChecksumCRC32.createCRC32Checksum(filename));
	}
}

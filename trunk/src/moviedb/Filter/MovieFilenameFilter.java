/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package moviedb.Filter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 
 * @author lalbrecht
 */
public class MovieFilenameFilter implements FilenameFilter {

	@Override
	public boolean accept (final File file, final String s) {
		return s.toLowerCase().endsWith(".avi") || s.toLowerCase().endsWith(".mkv")
				|| s.toLowerCase().endsWith(".mpg") || s.toLowerCase().endsWith(".mpeg")
				|| s.toLowerCase().endsWith(".iso") || s.toLowerCase().endsWith(".xvid")
				|| s.toLowerCase().endsWith(".divx")
				|| (file.isDirectory() && !s.equals(".") && !s.equals(".."));
	}
}

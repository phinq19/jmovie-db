/**
 * 
 */
package moviedb.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * @author lalbrecht
 * 
 */
public class SwingUtilities {

	/**
	 * Returns the center-position of the screen.
	 * 
	 * @param width
	 * @param height
	 * @return center-point for given width / height
	 */
	public static Point getScreenCenterPoint (final Integer width, final Integer height) {
		final Point resultPoint = new Point();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		resultPoint.y = (screenSize.width - width) / 2;
		resultPoint.y = (screenSize.height - height) / 2;

		return resultPoint;
	}

}

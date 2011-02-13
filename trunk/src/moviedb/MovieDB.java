/**
 * 
 */
package moviedb;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import moviedb.gui.MovieListGUI;

/**
 * 
 * @author lalbrecht
 * 
 */
public class MovieDB {

	public static void main (final String[] args) {
		new MovieDB();
	}

	/**
	 * 
	 */
	public MovieDB() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (final ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (final InstantiationException e1) {
				e1.printStackTrace();
			} catch (final IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (final UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		final MovieListGUI movieListGUI = new MovieListGUI();
	}

}

/**
 * 
 */
package moviedb.parser;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import moviedb.model.MovieModel;
import moviedb.threads.FileHandler;

/**
 * 
 * @author lalbrecht
 * 
 */
public class Parser {

	private DefaultTableModel tableModel = null;

	/**
	 * 
	 */
	public Parser(final ArrayList<String> dirs, final DefaultTableModel tableModel) {
		System.out.println("new Parser");
		this.tableModel = tableModel;
		for (final String dir : dirs) {
			System.out.println(dir);
			new Thread(new FileHandler(this, dir)).start();
		}
	}

	public void addMovies (final ArrayList<MovieModel> movies) {
		for (final MovieModel movie : movies) {
			System.out.println(movie.getTitle());
		}
	}

}

/**
 * 
 */
package moviedb.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import moviedb.components.MovieTableModel;
import moviedb.model.MovieModel;
import moviedb.util.SwingUtilities;

/**
 * 
 * @author lalbrecht
 * 
 */
public class MovieListGUI extends JFrame {

	private JButton addMovie = null;
	private JMenuBar mainMenu = null;

	private JMenu mFile = null;
	private JMenuItem miAdd = null;

	private JMenuItem miClose = null;
	private ActionListener mListener = null;
	private JMenu mMovie = null;

	private ParserGUI parserGui = null;
	private JButton searchMovies = null;
	private JTable table = null;
	private MovieTableModel tableModel = null;
	private JScrollPane tableScrollPane = null;

	private JToolBar toolBar = null;

	private JButton updateIMDBInfos = null;

	private JButton updateMoviesFromIMDB = null;

	public MovieListGUI() {
		super("MovieDB");
		this.mListener = new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				if (e.getSource() == MovieListGUI.this.miClose) {
					System.exit(0);
				} else if (e.getSource() == MovieListGUI.this.addMovie) {
				} else if (e.getSource() == MovieListGUI.this.searchMovies) {
					if (MovieListGUI.this.parserGui == null) {
						MovieListGUI.this.parserGui = new ParserGUI(MovieListGUI.this.tableModel);
					}
					MovieListGUI.this.parserGui.setVisible(true);
				}
			}
		};
		this.setFrameSettings(this);
		this.addComponents(this);
		this.setVisible(true);
	}

	/**
	 * 
	 * @param frame
	 */
	private void addComponents (final JFrame frame) {
		this.tableModel = this.setTableModelSettings(this.tableModel);
		final RowSorter<TableModel> tableRowSorter = new TableRowSorter<TableModel>(this.tableModel);
		this.table = new JTable(this.tableModel);
		this.table.setRowSorter(tableRowSorter);
		this.tableScrollPane = new JScrollPane(this.table);
		this.mainMenu = this.setMainMenuSettings(this.mainMenu);
		this.toolBar = this.setToolBarSettings(this.toolBar);
		frame.add(this.mainMenu, BorderLayout.NORTH);
		frame.getContentPane().add(this.toolBar, BorderLayout.SOUTH);
		frame.getContentPane().add(this.tableScrollPane, BorderLayout.CENTER);
		frame.pack();
	}

	/**
	 * 
	 * @param movie
	 */
	@SuppressWarnings ("unused")
	private void addMovieToTable (final MovieModel movie) {

	}

	/**
	 * 
	 * @param frame
	 */
	private void setFrameSettings (final JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Point centerPos = SwingUtilities.getScreenCenterPoint(500, 500);
		frame.setBounds(centerPos.x, centerPos.y, 500, 500);
	}

	private JMenuBar setMainMenuSettings (JMenuBar mainMenu) {
		mainMenu = new JMenuBar();

		// file menu
		this.mFile = new JMenu("Datei");

		this.miClose = new JMenuItem("Beenden");
		this.miClose.addActionListener(this.mListener);
		this.mFile.add(this.miClose);

		// movie menu
		this.mMovie = new JMenu("Film");

		this.miAdd = new JMenuItem("Hinzufügen");
		this.miAdd.addActionListener(this.mListener);
		this.mMovie.add(this.miAdd);
		mainMenu.add(this.mFile);

		// add
		return mainMenu;
	}

	/**
	 * 
	 * @param tableModel
	 */
	private MovieTableModel setTableModelSettings (MovieTableModel tableModel) {
		final String[] coloumnNames = new String[] { "Titel", "Untertitel", "Version", "Jahr",
				"HDTV-Typ", "Ort" };

		final String[][] columnRows = new String[][] { {} };

		return tableModel = new MovieTableModel(columnRows, coloumnNames);
	}

	/**
	 * 
	 * @param toolBar
	 * @return
	 */
	private JToolBar setToolBarSettings (JToolBar toolBar) {
		toolBar = new JToolBar("Tool Bar", SwingConstants.NORTH);
		this.addMovie = new JButton("Film hinzufügen");
		this.addMovie.addActionListener(this.mListener);
		toolBar.add(this.addMovie);

		this.searchMovies = new JButton("Filme suchen");
		this.searchMovies.addActionListener(this.mListener);
		toolBar.add(this.searchMovies);

		this.updateIMDBInfos = new JButton("IMDB Informationen erneuern");
		this.updateIMDBInfos.addActionListener(this.mListener);
		toolBar.add(this.updateIMDBInfos);

		this.updateMoviesFromIMDB = new JButton("Filminformationen von IMDB erneuern");
		this.updateMoviesFromIMDB.addActionListener(this.mListener);
		toolBar.add(this.updateMoviesFromIMDB);

		return toolBar;
	}

}

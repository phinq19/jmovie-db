/**
 * 
 */
package com.lars_albrecht.moviedb.thread;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.event.TableModelEvent;

import com.lars_albrecht.general.utilities.Debug;
import com.lars_albrecht.moviedb.components.movietablemodel.MovieTableModel;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.ThreadController;
import com.lars_albrecht.moviedb.model.abstracts.MovieModel;

/**
 * @author lalbrecht
 * 
 */
public class TableRefresh implements Runnable {

	private ThreadController tc = null;
	private MovieTableModel tableModel = null;

	/**
	 * 
	 * @param tc
	 *            ParserController
	 * @param tableModel
	 *            MovieTableModel
	 */
	public TableRefresh(final ThreadController tc, final MovieTableModel tableModel) {
		Debug.log(Debug.LEVEL_DEBUG, "new TableRefresh");
		this.tc = tc;
		this.tableModel = tableModel;

	}

	private void refreshTable() {
		// set new status
		if(this.tableModel.getMovies().size() > 0) {
			for(final MovieModel movie : this.tableModel.getMovies()) {
				try {
					if(((File) movie.get("file")).getAbsoluteFile().exists()) {
						movie.set("validPath", Boolean.TRUE);
					} else {
						movie.set("validPath", Boolean.FALSE);
					}
				} catch(final SecurityException e1) {
					e1.printStackTrace();
				} catch(final IllegalAccessException e1) {
					e1.printStackTrace();
				} catch(final IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch(final InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
			this.tableModel.callListenersChange(new TableModelEvent(this.tableModel, 0, this.tableModel.getMovies().size() - 1,
					TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
		}

		if((Controller.options.getPaths().size() > 0) && !Controller.options.getFilenameSeperator().equals("")) {
			this.tc.getController();
			// add new movies from defaultfilestack
			new ThreadController(this.tc.getController(), this.tableModel, null, Controller.options.getPaths());
		}
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		if(this.tableModel != null) {
			this.refreshTable();
			this.tc.getTableRefreshList().remove(Thread.currentThread());
		} else {
			this.tc.getTableRefreshList().remove(Thread.currentThread());
		}
	}

}

/**
 * 
 */
package moviedb.components;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author lalbrecht
 * 
 */
public class MovieTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1134100682782773086L;

	public MovieTableModel() {
		super();
	}

	public MovieTableModel(final Object[][] rows, final Object[] coloumnNames) {
		super(rows, coloumnNames);
	}

}

/**
 * 
 */
package moviedb.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import moviedb.components.MovieTableModel;
import moviedb.parser.Parser;
import moviedb.util.SwingUtilities;

/**
 * 
 * @author lalbrecht
 * 
 */
public class ParserGUI extends JFrame {

	private JButton btnOpen = null;
	private JButton btnStartScan = null;

	private ArrayList<String> dirList = null;
	private JFileChooser fcSelect = null;

	private JList fileList = null;

	private DefaultListModel fileListModel = new DefaultListModel();

	private JPopupMenu fileListPopupMenu = null;
	private JMenuItem miDelete = null;
	private ActionListener mListener = null;

	private MovieTableModel tableModel = null;

	private JToolBar toolBar = null;

	public ParserGUI(final MovieTableModel tableModel) {
		super("FileParser");
		this.tableModel = tableModel;
		this.dirList = new ArrayList<String>();
		this.mListener = new ActionListener() {
			@Override
			public void actionPerformed (final ActionEvent e) {
				if (e.getSource() == ParserGUI.this.btnOpen) {
					if (ParserGUI.this.fcSelect == null) {
						ParserGUI.this.fcSelect = new JFileChooser();
						ParserGUI.this.fcSelect.setCurrentDirectory(new java.io.File("."));
						ParserGUI.this.fcSelect.setDialogTitle("Ordner wählen ...");
						ParserGUI.this.fcSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						ParserGUI.this.fcSelect.setAcceptAllFileFilterUsed(false);
					}

					if (ParserGUI.this.fcSelect.showOpenDialog(ParserGUI.this.rootPane) == JFileChooser.APPROVE_OPTION) {
						ParserGUI.this.fileListModel.add(0, ParserGUI.this.fcSelect
								.getSelectedFile());
						ParserGUI.this.dirList.add(ParserGUI.this.fcSelect.getSelectedFile()
								.getAbsolutePath());
					} else {
						// no selection
					}

				} else if (e.getSource() == ParserGUI.this.btnStartScan) {
					if (ParserGUI.this.dirList.size() > 0) {
						System.out.println("create Parser");
						new Parser(ParserGUI.this.dirList, tableModel);
					}
				} else if (e.getSource() == ParserGUI.this.miDelete) {
					final Integer selected = ParserGUI.this.fileList.getSelectedIndex();
					ParserGUI.this.fileListModel.remove(selected);
					ParserGUI.this.dirList.remove(selected);
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
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		this.btnStartScan = new JButton("Scan");
		this.btnStartScan.addActionListener(this.mListener);

		this.btnOpen = new JButton("Öffnen");
		this.btnOpen.addActionListener(this.mListener);

		this.fileListModel = new DefaultListModel();
		this.fileList = new JList(this.fileListModel);

		this.fileListPopupMenu = new JPopupMenu("Menü");
		this.miDelete = new JMenuItem("Löschen");
		this.miDelete.addActionListener(this.mListener);
		this.fileListPopupMenu.add(this.miDelete);

		this.fileList.setComponentPopupMenu(this.fileListPopupMenu);

		final JScrollPane fileListScrollPane = new JScrollPane(this.fileList);
		this.toolBar = new JToolBar("Main toolbar");
		this.toolBar.add(this.btnOpen);
		this.toolBar.add(this.btnStartScan);

		frame.add(this.toolBar, BorderLayout.NORTH);
		frame.add(fileListScrollPane, BorderLayout.CENTER);

		final Point centerPos = SwingUtilities.getScreenCenterPoint(500, 400);
		frame.setBounds(centerPos.x, centerPos.y, 500, 400);
	}

	/**
	 * 
	 * @param parserGUI
	 */
	private void setFrameSettings (final ParserGUI parserGUI) {
		parserGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Point centerPos = SwingUtilities.getScreenCenterPoint(500, 500);
		parserGUI.setBounds(centerPos.x, centerPos.y, 500, 500);
	}

}

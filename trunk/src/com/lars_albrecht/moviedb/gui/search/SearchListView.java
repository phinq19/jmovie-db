/**
 * 
 */
package com.lars_albrecht.moviedb.gui.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.controller.ThreadController;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class SearchListView extends JFrame {

	private Controller controller = null;

	private JList<?> lFileList = null;
	private DefaultListModel<String> fileListModel = null;

	private JFileChooser fcSelect = null;

	private JToolBar tbTools = null;
	private JButton bAddPath = null;
	private JButton bStartScan = null;
	private JButton bSave = null;

	private JTextArea taRegExp = null;

	private JTextField tfRegExp = null;

	public SearchListView(final Controller controller) {
		super(RessourceBundleEx.getInstance().getProperty("application.frame.searchlistview.title"));
		this.controller = controller;

		this.setFrameSettings(this);
		this.addComponents(this);
		this.setVisible(true);
	}

	/**
	 * 
	 * @param frame
	 */
	private void setFrameSettings(final JFrame frame) {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Point centerPos = Helper.getScreenCenterPoint(800, 550);
		frame.setBounds(centerPos.x, centerPos.y, 800, 550);
		frame.setLayout(new GridBagLayout());
	}

	/**
	 * 
	 * @param frame
	 */
	private void addComponents(final JFrame frame) {
		final GridBagConstraints gbc = new GridBagConstraints();

		// init toolbar
		this.bAddPath = new JButton(RessourceBundleEx.getInstance().getProperty(
				"application.frame.searchlistview.button.open.title"));
		this.bAddPath.addActionListener(this.controller);

		this.bStartScan = new JButton(RessourceBundleEx.getInstance().getProperty(
				"application.frame.searchlistview.button.scan.title"));
		this.bStartScan.addActionListener(this.controller);
		this.bStartScan.setEnabled(Boolean.FALSE);

		this.bSave = new JButton(RessourceBundleEx.getInstance()
				.getProperty("application.frame.searchlistview.button.save.title"));
		this.bSave.addActionListener(this.controller);
		this.bSave.setEnabled(Boolean.FALSE);

		this.tbTools = new JToolBar("Main toolbar");
		this.tbTools.setFloatable(Boolean.FALSE);
		this.tbTools.add(this.bAddPath);
		this.tbTools.add(this.bStartScan);
		this.tbTools.add(this.bSave);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTH;

		frame.add(this.tbTools, gbc);

		// init textfield
		this.taRegExp = new JTextArea(

		"%maintitle% - %subtitle% - %year% - %video% - %encoder%" + "\n"
				+ "%maintitle% - %subtitle% - %year% - %video% - %audio%" + "\n"
				+ "%maintitle% - %subtitle% - %video% - %audio% - %video%" + "\n"
				+ "%maintitle% - %subtitle% - %audio% - %video% - %year%" + "\n"
				+ "%maintitle% - %version% - %year% - %video% - %audio% - %encoder%" + "\n"
				+ "%maintitle% - %subtitle% - %year% - %video%" + "\n" + "%maintitle% - %subtitle% - %year% - %audio%" + "\n"
				+ "%maintitle% - %subtitle% - %year% - %encoder%" + "\n" + "%maintitle% - %subtitle% - %video% - %audio%" + "\n"
				+ "%maintitle% - %subtitle% - %year%" + "\n" + "%maintitle% - %year% - %version% - %video% - %audio% - %encoder%"
				+ "\n" + "%maintitle% - %year% - %version% - %video% - %audio%" + "\n" + "%maintitle% - %year% - %video%" + "\n"
				+ "%maintitle% - %year% - %version% - %audio% - %video% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %version% - %video% - %audio% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %video% - %audio% - %encoder% - %audio%" + "\n"
				+ "%maintitle% - %year% - %video% - %audio% - %audio% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %audio% - %encoder%" + "\n" + "%maintitle% - %year% - %video% - %video% - %encoder%"
				+ "\n" + "%maintitle% - %subtitle% - %year% - %video% - %video% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %audio% - %video% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %video% - %encoder% - %audio%" + "\n"
				+ "%maintitle% - %year% - %video% - %audio% - %encoder%" + "\n" + "%maintitle% - %year% - %version% - %audio%"
				+ "\n" + "%maintitle% - %year% - %video% - %audio%" + "\n" + "%maintitle% - %year% - %video% - %video%" + "\n"
				+ "%maintitle% - %year% - %audio%" + "\n" + "%maintitle% - %year% - %video% - %encoder%" + "\n"
				+ "%maintitle% - %year% - %video%" + "\n" + "%maintitle% - %year% - %encoder%" + "\n" + "%maintitle% - %year%"
				+ "\n" + "%maintitle% - %subtitle% - %year% - %video% - %audio% - %encoder%" + "\n" + "%maintitle% - %subtitle%"
				+ "\n" + "%maintitle%" + "\n");
		// this.tfRegExp = new
		// JTextArea("%maintitle% - %year% - %video% - %audio%" + "\n"+
		// "%maintitle% - %subtitle% - %year% - %video% - %encoder%" + "\n"
		// + "%maintitle% - %subtitle% - %year% - %video% - %video% - %encoder%"
		// + "\n");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.2;
		gbc.anchor = GridBagConstraints.NORTH;
		// frame.add(new JScrollPane(this.taRegExp), gbc);

		// default: ((?!-\\s).)+
		this.tfRegExp = new JTextField((!Controller.options.getFilenameSeperator().equals("") ? Controller.options
				.getFilenameSeperator() : "((?!-\\s).)+"));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		frame.add(new JScrollPane(this.tfRegExp), gbc);

		// init list
		this.fileListModel = new DefaultListModel<String>();
		this.fileListModel.addListDataListener(this.controller);

		this.lFileList = new JList<String>(this.fileListModel);
		final JScrollPane fileListScrollPane = new JScrollPane(this.lFileList);
		for(final File f : Controller.options.getPaths()) {
			this.fileListModel.addElement(f.getAbsolutePath());
		}
		if(this.fileListModel.size() > 0) {
			this.getbStartScan().setEnabled(Boolean.TRUE);
			this.getbSave().setEnabled(Boolean.TRUE);
		}

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.LINE_START;

		frame.add(fileListScrollPane, gbc);

	}

	/**
	 * 
	 */
	public void startScanMovies() {
		final ArrayList<File> temp = new ArrayList<File>();
		for(int i = 0; i < this.fileListModel.getSize(); i++) {
			temp.add(new File(this.fileListModel.get(i)));
		}
		new ThreadController(this.controller, this.controller.getLv().getTableModel(), this.tfRegExp.getText().split("\n"), temp);
	}

	/**
	 * 
	 */
	public void addPathToSearchView() {
		if(this.fcSelect == null) {
			this.fcSelect = new JFileChooser();
			// this.slv.getFcSelect().setCurrentDirectory(new
			// java.io.File("."));
			if(new File("J:\\files\\video").exists()) {
				this.fcSelect.setCurrentDirectory(new File("J:\\files\\video"));
			} else {
				this.fcSelect.setCurrentDirectory(new File("D:\\lalbrecht\\Programme\\WDT\\workspace\\MD\\testdata"));
			}
			this.fcSelect.setDialogTitle(RessourceBundleEx.getInstance().getProperty("application.dialog.folder.title"));
			this.fcSelect.setAcceptAllFileFilterUsed(false);
			this.fcSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		if(this.fcSelect.showOpenDialog(this.getRootPane()) == JFileChooser.APPROVE_OPTION) {
			this.fileListModel.add(0, this.fcSelect.getSelectedFile().toString());
		} else {
			// no selection
		}
	}

	/**
	 * 
	 */
	public void saveSearchOptions() {
		final ArrayList<File> tempList = new ArrayList<File>();
		for(int i = 0; i < this.fileListModel.size(); i++) {
			tempList.add(new File(this.fileListModel.get(i)));
		}
		if(tempList.size() > 0) {
			Controller.options.setPaths(tempList);
			Controller.options.setFilenameSeperator(this.tfRegExp.getText());
		}
	}

	/**
	 * @return the fileListModel
	 */
	public synchronized final DefaultListModel<String> getFileListModel() {
		return this.fileListModel;
	}

	/**
	 * @param fileListModel
	 *            the fileListModel to set
	 */
	public synchronized final void setFileListModel(final DefaultListModel<String> fileListModel) {
		this.fileListModel = fileListModel;
	}

	/**
	 * @return the fcSelect
	 */
	public synchronized final JFileChooser getFcSelect() {
		return this.fcSelect;
	}

	/**
	 * @param fcSelect
	 *            the fcSelect to set
	 */
	public synchronized final void setFcSelect(final JFileChooser fcSelect) {
		this.fcSelect = fcSelect;
	}

	/**
	 * @return the bAddPath
	 */
	public synchronized final JButton getbAddPath() {
		return this.bAddPath;
	}

	/**
	 * @param bAddPath
	 *            the bAddPath to set
	 */
	public synchronized final void setbAddPath(final JButton bAddPath) {
		this.bAddPath = bAddPath;
	}

	/**
	 * @return the bStartScan
	 */
	public synchronized final JButton getbStartScan() {
		return this.bStartScan;
	}

	/**
	 * @param bStartScan
	 *            the bStartScan to set
	 */
	public synchronized final void setbStartScan(final JButton bStartScan) {
		this.bStartScan = bStartScan;
	}

	/**
	 * @return the taRegExp
	 */
	public synchronized final JTextArea getTaRegExp() {
		return this.taRegExp;
	}

	/**
	 * @param taRegExp
	 *            the taRegExp to set
	 */
	public synchronized final void setTaRegExp(final JTextArea taRegExp) {
		this.taRegExp = taRegExp;
	}

	/**
	 * @return the tfRegExp
	 */
	public synchronized final JTextField getTfRegExp() {
		return this.tfRegExp;
	}

	/**
	 * @param tfRegExp
	 *            the tfRegExp to set
	 */
	public synchronized final void setTfRegExp(final JTextField tfRegExp) {
		this.tfRegExp = tfRegExp;
	}

	/**
	 * @return the bSave
	 */
	public synchronized final JButton getbSave() {
		return this.bSave;
	}

	/**
	 * @param bSave
	 *            the bSave to set
	 */
	public synchronized final void setbSave(final JButton bSave) {
		this.bSave = bSave;
	}

}

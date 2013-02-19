/**
 * 
 */
package com.lars_albrecht.moviedb.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import com.lars_albrecht.general.utilities.Helper;
import com.lars_albrecht.general.utilities.RessourceBundleEx;
import com.lars_albrecht.moviedb.controller.Controller;
import com.lars_albrecht.moviedb.gui.tablefilter.AdditionalFilter;
import com.lars_albrecht.moviedb.model.ComboBoxFilterModel;

/**
 * @author lalbrecht
 * 
 */
@SuppressWarnings("serial")
public class Platform extends JFrame {

	private Controller controller = null;

	private JMenuBar mainMenu = null;
	private JMenu mFile = null;
	private JMenuItem miAdd = null;
	private JMenuItem miSearch = null;
	private JMenuItem miFindDuplicates = null;
	private JMenuItem miRemoveAll = null;
	private JMenuItem miClose = null;
	private JMenu mhelp = null;
	private JMenuItem miAbout = null;
	private JPanel rpPanel = null;
	private JSplitPane splitPaneBottom = null;
	private JSplitPane splitPaneTop = null;

	private JToolBar tbTools = null;
	private JButton bRefreshTable = null;
	private JComboBox<ComboBoxFilterModel> cbFilter = null;
	private DefaultComboBoxModel<ComboBoxFilterModel> mcbmFilter = null;
	private JRadioButton rbAndSearch = null;
	private JRadioButton rbOrSearch = null;

	public Platform(final Controller controller) {
		super(RessourceBundleEx.getInstance().getProperty("application.frame.platform.title"));
		this.controller = controller;

		this.setFrameSettings(this);
		this.addComponents(this);

		this.setVisible(Boolean.TRUE);
	}

	/**
	 * 
	 * @param frame
	 */
	private void setFrameSettings(final JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Point centerPos = Helper.getScreenCenterPoint(800, 700);
		frame.setBounds((Controller.options.getxYMainWindow() != null ? Controller.options.getxYMainWindow().x : centerPos.x),
				(Controller.options.getxYMainWindow() != null ? Controller.options.getxYMainWindow().y : centerPos.y),
				(Controller.options.getWidthHeightMainWindow() != null ? Controller.options.getWidthHeightMainWindow().x : 800),
				(Controller.options.getWidthHeightMainWindow() != null ? Controller.options.getWidthHeightMainWindow().y : 700));

		frame.addWindowListener(this.controller);

	}

	/**
	 * 
	 * @param frame
	 */
	private void addComponents(final JFrame frame) {
		final GridBagConstraints gbc = new GridBagConstraints();
		this.rpPanel = new JPanel(new GridBagLayout());
		this.mainMenu = this.setMainMenuSettings(this.mainMenu);
		frame.add(this.mainMenu, BorderLayout.NORTH);

		// init TabViewContainer
		// TODO Repair
		// JPanel tabViewContainer = new JPanel(new GridBagLayout());
		// gbc.fill = GridBagConstraints.BOTH;
		// gbc.gridx = 0;
		// gbc.gridy = 0;
		// gbc.weightx = 1;
		// gbc.weighty = 1;
		// tabViewContainer.add(this.controller.getTv(), gbc);

		// init splitPanes
		this.splitPaneBottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.controller.getLv(), this.controller.getTv()); // tabViewContainer
		this.splitPaneBottom.setOneTouchExpandable(true);
		this.splitPaneBottom.setDividerSize(7);
		this.splitPaneBottom.setDividerLocation((Controller.options.getSliderBottomPos() != null ? Controller.options
				.getSliderBottomPos() : 750));

		this.controller.setAf(new AdditionalFilter(this.controller));

		this.splitPaneTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.controller.getAf(), this.splitPaneBottom);
		this.splitPaneTop.setDividerSize(7);
		this.splitPaneTop.setOneTouchExpandable(true);
		this.splitPaneTop.setDividerLocation(25);

		// init toolbar
		this.mcbmFilter = new DefaultComboBoxModel<ComboBoxFilterModel>();
		this.mcbmFilter.addElement(new ComboBoxFilterModel("", true, true, null));
		this.mcbmFilter.addListDataListener(this.controller);
		this.cbFilter = new JComboBox<ComboBoxFilterModel>(this.mcbmFilter);
		this.cbFilter.setEditable(true);

		this.bRefreshTable = new JButton(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.button.refresh.title"));
		this.bRefreshTable.addActionListener(this.controller);

		this.tbTools = new JToolBar("Main toolbar");
		this.tbTools.setFloatable(Boolean.FALSE);
		this.tbTools.add(this.bRefreshTable);
		this.tbTools.add(this.cbFilter);

		final ButtonGroup bgAndOr = new ButtonGroup();
		this.rbAndSearch = new JRadioButton(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.radio.and.title"), Boolean.TRUE);
		this.rbOrSearch = new JRadioButton(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.radio.or.title"));

		bgAndOr.add(this.rbAndSearch);
		bgAndOr.add(this.rbOrSearch);
		this.tbTools.add(this.rbAndSearch);
		this.tbTools.add(this.rbOrSearch);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTH;

		this.rpPanel.add(this.tbTools, gbc);

		// init sbStatus
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.rpPanel.add(this.controller.getSbStatus(), gbc);

		// init splitPane
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		this.rpPanel.add(this.splitPaneTop, gbc);

		frame.add(this.rpPanel, BorderLayout.CENTER);
	}

	/**
	 * 
	 * @param mainMenu
	 * @return
	 */
	private JMenuBar setMainMenuSettings(JMenuBar mainMenu) {
		mainMenu = new JMenuBar();

		// file menu
		this.mFile = new JMenu(RessourceBundleEx.getInstance().getProperty("application.frame.platform.menu.file.title"));

		this.miAdd = new JMenuItem(RessourceBundleEx.getInstance().getProperty("application.frame.platform.menuitem.add.title"));
		this.miAdd.addActionListener(this.controller);
		this.mFile.add(this.miAdd);

		this.miSearch = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.menuitem.search.title"));
		this.miSearch.addActionListener(this.controller);
		this.mFile.add(this.miSearch);

		this.miFindDuplicates = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.menuitem.findduplicates.title"));
		this.miFindDuplicates.addActionListener(this.controller);
		this.mFile.add(this.miFindDuplicates);

		this.miRemoveAll = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.menuitem.removeall.title"));
		this.miRemoveAll.addActionListener(this.controller);
		this.mFile.add(this.miRemoveAll);

		this.miClose = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.menuitem.close.title"));
		this.miClose.addActionListener(this.controller);
		this.mFile.add(this.miClose);

		// Help menu
		this.mhelp = new JMenu(RessourceBundleEx.getInstance().getProperty("application.frame.platform.menu.help.title"));

		this.miAbout = new JMenuItem(RessourceBundleEx.getInstance().getProperty(
				"application.frame.platform.menuitem.about.title"));
		this.miAbout.addActionListener(this.controller);
		this.mhelp.add(this.miAbout);

		mainMenu.add(this.mFile);
		mainMenu.add(this.mhelp);

		// add
		return mainMenu;
	}

	/**
	 * @return the miAdd
	 */
	public synchronized final JMenuItem getMiAdd() {
		return this.miAdd;
	}

	/**
	 * @param miAdd
	 *            the miAdd to set
	 */
	public synchronized final void setMiAdd(final JMenuItem miAdd) {
		this.miAdd = miAdd;
	}

	/**
	 * @return the miSearch
	 */
	public synchronized final JMenuItem getMiSearch() {
		return this.miSearch;
	}

	/**
	 * @param miSearch
	 *            the miSearch to set
	 */
	public synchronized final void setMiSearch(final JMenuItem miSearch) {
		this.miSearch = miSearch;
	}

	/**
	 * @return the miClose
	 */
	public synchronized final JMenuItem getMiClose() {
		return this.miClose;
	}

	/**
	 * @param miClose
	 *            the miClose to set
	 */
	public synchronized final void setMiClose(final JMenuItem miClose) {
		this.miClose = miClose;
	}

	/**
	 * @return the cbFilter
	 */
	public synchronized final JComboBox<ComboBoxFilterModel> getCbFilter() {
		return this.cbFilter;
	}

	/**
	 * @param cbFilter
	 *            the cbFilter to set
	 */
	public synchronized final void setCbFilter(final JComboBox<ComboBoxFilterModel> cbFilter) {
		this.cbFilter = cbFilter;
	}

	/**
	 * @return the mcbmFilter
	 */
	public synchronized final DefaultComboBoxModel<ComboBoxFilterModel> getMcbmFilter() {
		return this.mcbmFilter;
	}

	/**
	 * @param mcbmFilter
	 *            the mcbmFilter to set
	 */
	public synchronized final void setMcbmFilter(final DefaultComboBoxModel<ComboBoxFilterModel> mcbmFilter) {
		this.mcbmFilter = mcbmFilter;
	}

	/**
	 * @return the rbAndSearch
	 */
	public synchronized final JRadioButton getRbAndSearch() {
		return this.rbAndSearch;
	}

	/**
	 * @param rbAndSearch
	 *            the rbAndSearch to set
	 */
	public synchronized final void setRbAndSearch(final JRadioButton rbAndSearch) {
		this.rbAndSearch = rbAndSearch;
	}

	/**
	 * @return the rbOrSearch
	 */
	public synchronized final JRadioButton getRbOrSearch() {
		return this.rbOrSearch;
	}

	/**
	 * @param rbOrSearch
	 *            the rbOrSearch to set
	 */
	public synchronized final void setRbOrSearch(final JRadioButton rbOrSearch) {
		this.rbOrSearch = rbOrSearch;
	}

	/**
	 * @return the miAbout
	 */
	public synchronized final JMenuItem getMiAbout() {
		return this.miAbout;
	}

	/**
	 * @param miAbout
	 *            the miAbout to set
	 */
	public synchronized final void setMiAbout(final JMenuItem miAbout) {
		this.miAbout = miAbout;
	}

	/**
	 * @return the splitPaneBottom
	 */
	public synchronized final JSplitPane getSplitPaneBottom() {
		return this.splitPaneBottom;
	}

	/**
	 * @param splitPaneBottom
	 *            the splitPaneBottom to set
	 */
	public synchronized final void setSplitPaneBottom(final JSplitPane splitPaneBottom) {
		this.splitPaneBottom = splitPaneBottom;
	}

	/**
	 * @return the splitPaneTop
	 */
	public synchronized final JSplitPane getSplitPaneTop() {
		return this.splitPaneTop;
	}

	/**
	 * @param splitPaneTop
	 *            the splitPaneTop to set
	 */
	public synchronized final void setSplitPaneTop(final JSplitPane splitPaneTop) {
		this.splitPaneTop = splitPaneTop;
	}

	/**
	 * @return the bRefresh
	 */
	public synchronized final JButton getbRefreshTable() {
		return this.bRefreshTable;
	}

	/**
	 * @param bRefreshTable
	 *            the bRefreshTable to set
	 */
	public synchronized final void setbRefreshTable(final JButton bRefreshTable) {
		this.bRefreshTable = bRefreshTable;
	}

	/**
	 * @return the miRemoveAll
	 */
	public synchronized final JMenuItem getMiRemoveAll() {
		return this.miRemoveAll;
	}

	/**
	 * @param miRemoveAll
	 *            the miRemoveAll to set
	 */
	public synchronized final void setMiRemoveAll(final JMenuItem miRemoveAll) {
		this.miRemoveAll = miRemoveAll;
	}

	/**
	 * @return the miFindDuplicates
	 */
	public synchronized final JMenuItem getMiFindDuplicates() {
		return this.miFindDuplicates;
	}

	/**
	 * @param miFindDuplicates
	 *            the miFindDuplicates to set
	 */
	public synchronized final void setMiFindDuplicates(final JMenuItem miFindDuplicates) {
		this.miFindDuplicates = miFindDuplicates;
	}

}

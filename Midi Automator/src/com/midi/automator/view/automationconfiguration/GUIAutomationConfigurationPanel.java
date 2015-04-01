package com.midi.automator.view.automationconfiguration;

/**
 * A configuration panel for adding, deleting and editing GUI automation.
 *  
 * @author aguelle
 * @date 10-12-2014
 */
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import com.midi.automator.guiautomator.GUIAutomation;
import com.midi.automator.view.MidiLearnPopupMenu;
import com.midi.automator.view.ScaleableImageIcon;

import de.tieffrequent.midi.automator.IApplication;

public class GUIAutomationConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final String ADD_LABEL = "+";
	private final String DELETE_LABEL = "-";
	private final int EDITOR_BUTTON_SIZE = 41; // min size for Windows

	private GUIAutomationConfigurationTable configurationTable;
	private JPanel editorPanel;
	private JButton addButton;
	private JButton deleteButton;

	public static final String NAME_CONFIGURATION_TABLE = "configuration table";

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The application.
	 */
	public GUIAutomationConfigurationPanel(IApplication application) {
		super(new GridBagLayout());

		// configuration table
		configurationTable = new GUIAutomationConfigurationTable(application);
		configurationTable.setName(NAME_CONFIGURATION_TABLE);
		configurationTable
				.setCache(configurationTable.getSelectionBackground());
		JScrollPane scrollPane = new JScrollPane(configurationTable);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(scrollPane, c);

		// editor panel
		createEditorPanel();
	}

	/**
	 * Gets the popup menu
	 * 
	 * @return The popup menu
	 */
	public MidiLearnPopupMenu getPopupMenu() {
		return configurationTable.getPopupMenu();
	}

	/**
	 * Gets the configuration table
	 * 
	 * @return The configuration JTable
	 */
	public GUIAutomationConfigurationTable getConfigurationTable() {
		return configurationTable;
	}

	/**
	 * Gets all GUI automations
	 * 
	 * @return An array with all GUI automations
	 */
	public GUIAutomation[] getGUIAutomations() {

		TableModel model = configurationTable.getModel();
		int numberOfConfigurations = model.getRowCount();
		GUIAutomation[] guiAutomations = new GUIAutomation[numberOfConfigurations];

		// stop open edits
		if (configurationTable.isEditing()) {
			configurationTable.getCellEditor().stopCellEditing();
		}

		// build GUI automation objects
		for (int i = 0; i < numberOfConfigurations; i++) {

			ScaleableImageIcon image = (ScaleableImageIcon) model.getValueAt(i,
					ConfigurationTableModel.COLUMN_INDEX_IMAGE);

			// check for valid image
			String imagePath = null;
			if (image != null) {
				imagePath = image.getPath();
			}

			String type = (String) model.getValueAt(i,
					ConfigurationTableModel.COLUMN_INDEX_TYPE);
			String trigger = (String) model.getValueAt(i,
					ConfigurationTableModel.COLUMN_INDEX_TRIGGER);
			String midiSignature = (String) model.getValueAt(i,
					ConfigurationTableModel.COLUMN_INDEX_MIDISIGNATURE);
			long minDelay = (long) model.getValueAt(i,
					ConfigurationTableModel.COLUMN_INDEX_MIN_DELAY);

			GUIAutomation guiAutomation = new GUIAutomation(imagePath, type,
					trigger, minDelay, midiSignature);
			guiAutomations[i] = guiAutomation;
		}

		return guiAutomations;
	}

	/**
	 * Creates the panel with add and delete buttons.
	 */
	private void createEditorPanel() {

		editorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		addButton = new JButton(ADD_LABEL);
		addButton.addActionListener(new AddAutomationListener());
		addButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(addButton);

		deleteButton = new JButton(DELETE_LABEL);
		deleteButton.addActionListener(new DeleteAutomationListener());
		deleteButton.setPreferredSize(new Dimension(EDITOR_BUTTON_SIZE,
				EDITOR_BUTTON_SIZE));
		editorPanel.add(deleteButton);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		add(editorPanel, c);
	}

	/**
	 * Adds a new configuration to the configuration table.
	 */
	class AddAutomationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			configurationTable.setAutomation(null, null, null, 0, null, -1);
		}
	}

	/**
	 * Deletes the chosen configuration from the configuration table.
	 */
	class DeleteAutomationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			configurationTable.deleteAutomation(configurationTable
					.getSelectedRow());
		}
	}
}
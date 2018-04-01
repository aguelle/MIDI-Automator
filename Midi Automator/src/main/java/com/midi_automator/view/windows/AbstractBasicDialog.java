package com.midi_automator.view.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.tray.listener.TrayMenuCloseListener;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Base class of dialogs with save and cancel buttons.
 * 
 * @author aguelle
 *
 */
public abstract class AbstractBasicDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(AbstractBasicDialog.class.getName());

	private final int PADDING_HORIZONTAL = 20;
	private final int PADDING_VERTICAL = 10;

	private final String BUTTON_SAVE = "Save";
	private final String BUTTON_CANCEL = "Cancel";

	public static String NAME_SAVE_BUTTON = "save button";
	public static String NAME_CANCEL_BUTTON = "cancel button";

	protected JButton buttonSave;
	protected JButton buttonCancel;

	protected JPanel middlePanel;
	protected JPanel footerPanel;
	// The number of rows in the dialogs global grid layout
	protected int gridRows = 0;

	@Autowired
	protected MainFrame mainFrame;
	@Autowired
	private TrayMenuCloseListener trayCloseListener;

	protected boolean initialized = false;

	/**
	 * Initializes some basic dialog features.
	 */
	public void init() {

		log.debug("Initilaizing Abstract Dialog...");
		setParent(mainFrame);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setResizable(false);

		// set layout
		JPanel contentPanel = new JPanel();
		Border padding = BorderFactory.createEmptyBorder(PADDING_VERTICAL,
				PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL);
		contentPanel.setBorder(padding);
		contentPanel.setLayout(new BorderLayout());
		setContentPane(contentPanel);

		middlePanel = new JPanel(new GridBagLayout());
		add(middlePanel, BorderLayout.CENTER);
		footerPanel = new JPanel(new FlowLayout());
		add(footerPanel, BorderLayout.PAGE_END);

		buttonSave = new JButton(BUTTON_SAVE);
		buttonSave.setName(NAME_SAVE_BUTTON);
		buttonSave.addActionListener(new SaveAction());
		buttonSave.addKeyListener(new SaveKeyListener());

		buttonCancel = new JButton(BUTTON_CANCEL);
		buttonCancel.setName(NAME_CANCEL_BUTTON);
		buttonCancel.addActionListener(new CancelAction());
		buttonCancel.addKeyListener(new CancelKeyListener());

		GUIUtils.addMouseListenerToAllComponents(this, trayCloseListener);

	}

	/**
	 * Shows the build dialog.
	 */
	public void showDialog() {
		pack();
		setVisible(true);
	}

	/**
	 * Sets the parent of the dialog
	 * 
	 * @param parent
	 *            The parent Container
	 */
	public void setParent(Container parent) {
		try {
			Field declaredField = Component.class.getDeclaredField("parent");
			declaredField.setAccessible(true);
			declaredField.set(this, parent);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Creates a text field
	 * 
	 * @param metaLabel
	 *            The label value to describe the text field
	 * @param textFieldName
	 *            The name of the text field
	 * @param metaWidth
	 *            The width of the meta label
	 * @param textFieldWidth
	 *            The width of the text field
	 * @return the created text field
	 */
	protected JTextField createTextField(String metaLabel,
			String textFieldName, int metaWidth, int textFieldWidth) {

		JLabel label = new JLabel(metaLabel);
		label.setPreferredSize(new Dimension(metaWidth, label
				.getPreferredSize().height));
		addComponentAtPosition(label, 0, gridRows);

		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(textFieldWidth, textField
				.getPreferredSize().height));
		textField.setName(textFieldName);
		addComponentAtPosition(textField, 1, gridRows);
		gridRows++;

		return textField;
	}

	/**
	 * Creates a label with meta information
	 * 
	 * @param metaLabel
	 *            The meta information
	 * @param labelName
	 *            The name of the label
	 * @param metaWidth
	 *            The width of the meta label
	 * @param labelWidth
	 *            The width of the label
	 * @return A named meta label
	 */
	protected JLabel createMetaLabel(String metaLabel, String labelName,
			int metaWidth, int labelWidth) {

		JLabel nameLabel = new JLabel(metaLabel);
		nameLabel.setPreferredSize(new Dimension(metaWidth, nameLabel
				.getPreferredSize().height));
		addComponentAtPosition(nameLabel, 0, gridRows);

		JLabel label = new JLabel("x");
		label.setName(labelName);
		label.setPreferredSize(new Dimension(labelWidth, label
				.getPreferredSize().height));
		addComponentAtPosition(label, 1, gridRows);
		gridRows++;

		return label;
	}

	/**
	 * Sets the component to the desired position
	 * 
	 * @param component
	 *            The component
	 * @param x
	 *            The X coordinate
	 * @param y
	 *            Thy Y coordinate
	 */
	protected void addComponentAtPosition(Component component, int x, int y) {

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = x;
		c.gridy = y;
		middlePanel.add(component, c);
	}

	/**
	 * Closes the frame
	 */
	protected void close() {
		WindowEvent windowClosing = new WindowEvent(this,
				WindowEvent.WINDOW_CLOSING);
		dispatchEvent(windowClosing);
	}

	/**
	 * Called by the "save" button
	 */
	abstract protected void save();

	/**
	 * Closes the frame
	 * 
	 * @author aguelle
	 * 
	 */
	class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	}

	/**
	 * Closes the frame
	 * 
	 * @author aguelle
	 *
	 */
	class CancelKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			close();
		}
	}

	/**
	 * Calls the save method
	 * 
	 * @author aguelle
	 * 
	 */
	class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			save();
		}
	}

	/**
	 * Calls the save method
	 * 
	 * @author aguelle
	 *
	 */
	class SaveKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			save();
		}
	}
}

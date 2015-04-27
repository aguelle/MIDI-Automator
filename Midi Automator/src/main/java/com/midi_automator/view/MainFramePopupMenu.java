package com.midi_automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.midi_automator.Messages;
import com.midi_automator.MidiAutomator;
import com.midi_automator.view.frames.AddFrame;
import com.midi_automator.view.frames.EditFrame;
import com.midi_automator.view.frames.MainFrame;

/**
 * The PopupMenu of the file list in the main window.
 * 
 * @author aguelle
 * 
 */
public class MainFramePopupMenu extends MidiLearnPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MIDI_UNLEARN = "Midi unlearn";
	private final String MENU_ITEM_MOVE_UP = "Move up";
	private final String MENU_ITEM_MOVE_DOWN = "Move down";
	private final String MENU_ITEM_DELETE = "Delete";
	private final String MENU_ITEM_ADD = "Add";
	private final String MENU_ITEM_EDIT = "Edit";

	private final String NAME_MENU_ITEM_MOVE_UP = "move up";
	private final String NAME_MENU_ITEM_MOVE_DOWN = "move down";
	private final String NAME_MENU_ITEM_DELETE = "delete";
	private final String NAME_MENU_ITEM_ADD = "add";
	private final String NAME_MENU_ITEM_EDIT = "edit";

	private final String NAME_MENU_ITEM_MIDI_UNLEARN = "midi unlearn";

	private JMenuItem midiUnlearnMenuItem;
	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem addMenuItem;
	private JMenuItem editMenuItem;

	/**
	 * Constructor
	 * 
	 * @param parentFrame
	 *            The frame the menu was called from
	 * @param application
	 *            The application
	 */
	public MainFramePopupMenu(MainFrame parentFrame, MidiAutomator application) {
		super(parentFrame, application);

		moveUpMenuItem = new JMenuItem(MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setName(NAME_MENU_ITEM_MOVE_UP);
		moveUpMenuItem.setEnabled(true);
		moveUpMenuItem.addActionListener(new MoveUpAction(mainFrame));

		moveDownMenuItem = new JMenuItem(MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setName(NAME_MENU_ITEM_MOVE_DOWN);
		moveDownMenuItem.setEnabled(true);
		moveDownMenuItem.addActionListener(new MoveDownAction(mainFrame));

		deleteMenuItem = new JMenuItem(MENU_ITEM_DELETE);
		deleteMenuItem.setName(NAME_MENU_ITEM_DELETE);
		deleteMenuItem.setEnabled(true);
		deleteMenuItem.addActionListener(new DeleteAction(mainFrame));

		addMenuItem = new JMenuItem(MENU_ITEM_ADD);
		addMenuItem.setName(NAME_MENU_ITEM_ADD);
		addMenuItem.setEnabled(true);
		addMenuItem.addActionListener(new AddAction(mainFrame));

		editMenuItem = new JMenuItem(MENU_ITEM_EDIT);
		editMenuItem.setName(NAME_MENU_ITEM_EDIT);
		editMenuItem.setEnabled(true);
		editMenuItem.addActionListener(new EditAction(mainFrame));

		midiUnlearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setName(NAME_MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setEnabled(false);
		midiUnlearnMenuItem.addActionListener(new MidiUnlearnAction(mainFrame));

	}

	/**
	 * Configures the popup menu of the file list.
	 */
	public void configureFileListPopupMenu() {

		removeAll();
		add(addMenuItem);
		add(editMenuItem);
		add(deleteMenuItem);
		add(moveUpMenuItem);
		add(moveDownMenuItem);
		addSeparator();
		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
	}

	/**
	 * Creates the popup menu of the switch buttons
	 */
	public void configureSwitchButtonPopupMenu() {

		removeAll();
		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
	}

	/**
	 * Adds the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class AddAction extends PopUpMenuAction {

		public AddAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			new AddFrame(application, parentFrame);
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class DeleteAction extends PopUpMenuAction {

		public DeleteAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			application
					.deleteItem(parentFrame.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Deletes the selected item from the model
	 * 
	 * @author aguelle
	 * 
	 */
	class EditAction extends PopUpMenuAction {

		public EditAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			new EditFrame(application, parentFrame, parentFrame.getFileList()
					.getSelectedIndex());
		}
	}

	/**
	 * Deletes the learned midi signature from the component
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiUnlearnAction extends PopUpMenuAction {

		public MidiUnlearnAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JMenuItem menuItem = (JMenuItem) e.getSource();

			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			Component component = popupMenu.getInvoker();
			application.setMidiSignature(null, component);
			parentFrame.getFileList().setLastSelectedIndex();

			parentFrame.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					parentFrame.getMidiComponentName(component)));
		}
	}

	/**
	 * Moves the selected item one step up in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveUpAction extends PopUpMenuAction {

		public MoveUpAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			application
					.moveUpItem(parentFrame.getFileList().getSelectedIndex());
		}
	}

	/**
	 * Moves the selected item one step down in the list
	 * 
	 * @author aguelle
	 * 
	 */
	class MoveDownAction extends PopUpMenuAction {

		public MoveDownAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			application.moveDownItem(parentFrame.getFileList()
					.getSelectedIndex());
		}
	}

	public JMenuItem getMoveUpMenuItem() {
		return moveUpMenuItem;
	}

	public JMenuItem getMoveDownMenuItem() {
		return moveDownMenuItem;
	}

	public JMenuItem getMidiUnlearnMenuItem() {
		return midiUnlearnMenuItem;
	}

	public JMenuItem getDeleteMenuItem() {
		return deleteMenuItem;
	}

	public JMenuItem getEditMenuItem() {
		return editMenuItem;
	}

}
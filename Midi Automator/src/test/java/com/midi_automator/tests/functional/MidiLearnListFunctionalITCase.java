package com.midi_automator.tests.functional;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.Messages;
import com.midi_automator.presenter.services.MidiExecuteService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.menus.MidiLearnPopupMenu;

public class MidiLearnListFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel1 = 1;
	private int channel2 = 2;
	private int controlNo = 108;
	private int value = 127;

	public MidiLearnListFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesFile = "RemoteINBus_1.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesFile = "RemoteINLoopBe_Internal_MIDI.properties";
		}
	}

	// @Test
	// Blocked due to
	// https://github.com/joel-costigliola/assertj-swing/issues/169"
	public void midiLearnShouldBeCanceled() {
		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
		startApplication();

		// midi learn
		midiLearnListEntry(0);
		getFileList().requireDisabled();

		// cancel midi learn
		cancelMidiLearnListEntry();

		JPopupMenuFixture popupMenu = openFileListPopupMenu(0);
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireVisible();

		getFileList().requireEnabled();
	}

	@Test
	public void midiLearnShouldBeInactive() {
		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// check if midi learn is inactive
		JPopupMenuFixture popupMenu = openFileListPopupMenu(1);
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireDisabled();
	}

	@Test
	public void midiShouldBeUnlearned() {

		try {

			MockUpUtils
					.setMockupMidoFile("mockups/Hello_World_12_midi_learned.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			startApplication();

			// midi unlearn
			midiUnlearnListEntry(0);

			// check for unlearned message
			checkInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					"Hello World 1"));

			// check for inactive menu item
			JPopupMenuFixture popupMenu = openFileListPopupMenu(0);
			popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
					.requireDisabled();
			getFileList().click();

			// open first files by learned midi message
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 1, 127);
			Thread.sleep(2000);

			// check that file was not opened
			sikulix.checkIfFileNotOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiShouldBeLearned() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set midi in device
			DialogFixture preferencesDialog = openPreferences();
			setMidiInRemoteDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			midiLearnListEntry(0);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);
			Thread.sleep(1000);

			midiLearnListEntry(1);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			Thread.sleep(2000);

			// open files by learned midi message
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);
			Thread.sleep(2000);

			// check that file was opened
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			Thread.sleep(2000);

			// check that file was opened
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiMasterLearnShouldBeRejected() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			startApplication();

			// midi learn master signature
			midiLearnListEntry(1);
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName,
					MidiExecuteService.OPEN_FILE_MIDI_COMMAND,
					MidiExecuteService.OPEN_FILE_MIDI_CHANNEL,
					MidiExecuteService.OPEN_FILE_MIDI_CONTROL_NO, 1);
			Thread.sleep(2000);

			// check failure
			checkInfoText(String.format(Messages.MSG_DUPLICATE_MIDI_SIGNATURE,
					"channel " + MidiExecuteService.OPEN_FILE_MIDI_CHANNEL
							+ ": CONTROL CHANGE "
							+ MidiExecuteService.OPEN_FILE_MIDI_CONTROL_NO
							+ " value: " + 1));

			// open second file by master midi message
			MidiUtils.sendMidiMessage(deviceName,
					MidiExecuteService.OPEN_FILE_MIDI_COMMAND,
					MidiExecuteService.OPEN_FILE_MIDI_CHANNEL,
					MidiExecuteService.OPEN_FILE_MIDI_CONTROL_NO, 1);
			Thread.sleep(2000);

			checkInfoText(String.format(Messages.MSG_OPENING_ENTRY,
					"Hello World 2"));
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiDuplicateLearnShouldBeRejected() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			startApplication();

			// midi learn same signature twice
			midiLearnListEntry(1);
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			Thread.sleep(2000);

			midiLearnListEntry(0);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			Thread.sleep(2000);

			// check failure
			checkInfoText(String.format(Messages.MSG_DUPLICATE_MIDI_SIGNATURE,
					"channel " + channel2 + ": CONTROL CHANGE " + controlNo
							+ " value: " + value));

			// open second file by learned midi message
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			Thread.sleep(2000);

			checkInfoText(String.format(Messages.MSG_OPENING_ENTRY,
					"Hello World 2"));
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}

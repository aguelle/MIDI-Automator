package com.midi_automator.tests.IntegrationTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.MidiAutomator;

public class ImportFileIntegrationTest extends IntegrationTest {

	@Test
	public void midoFileShouldBeImported() {

		String testFilePath = "testfiles";
		String testFileName = "ImportTest.midauto";

		MidiAutomator presenter = (MidiAutomator) ctx
				.getBean(MidiAutomator.class);
		presenter
				.importMidautoFile(new File(testFilePath + "/" + testFileName));

		// test file_list.mido
		assertEquals(presenter.getEntryNameByIndex(0), "Hello World 1");
		assertEquals(presenter.getEntryNameByIndex(1), "Hello World 2");
		assertEquals(presenter.getMidiSendingSignature(0),
				"channel 16: CONTROL CHANGE 1 value: 127");
		assertEquals(presenter.getMidiSendingSignature(1),
				"channel 16: CONTROL CHANGE 2 value: 127");

		// test midautomator.properties
		assertEquals(
				presenter
						.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE),
				"Bus 1");
	}
}
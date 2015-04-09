package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;

public class TestSwitchButtons extends GUITest {

	@Test
	public void nextButtonNotActiveOnEmptyList() {
		try {
			// mockup
			GUIAutomations.restartMidiAutomator();

			// check for inactive next button
			GUIAutomations.checkResult("next_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// delete second file
			GUIAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// ad third file
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open third file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevButtonNotActiveOnEmptyList() {
		try {
			// mockup
			GUIAutomations.restartMidiAutomator();

			// check for disabled prev button
			GUIAutomations.checkResult("prev_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// cycle second file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open first file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			GUIAutomations.restartMidiAutomator();

			// cycle third file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// delete second file
			GUIAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// cycle second file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open first file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// add third file
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// cycle third file
			GUIAutomations.prevFile();
			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
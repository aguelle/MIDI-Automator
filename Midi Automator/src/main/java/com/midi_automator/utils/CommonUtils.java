package com.midi_automator.utils;

/**
 * All handy functions that have no specific purpose
 * 
 * @author aguelle
 *
 */
public class CommonUtils {

	/**
	 * Converts a String array to an int array
	 * 
	 * @param stringArray
	 *            The String array
	 * @return An int array
	 * @throws NumberFormatException
	 */
	public static int[] stringArrayToIntArray(String[] stringArray) {

		int[] intArray = new int[stringArray.length];

		for (int i = 0; i < stringArray.length; i++) {
			try {
				intArray[i] = Integer.valueOf(stringArray[i]);
			} catch (NumberFormatException e) {
			}
		}

		return intArray;
	}

	/**
	 * Converts a String array to an int array
	 * 
	 * @param intArray
	 *            The int array
	 * @return A String array
	 */
	public static String[] intArrayToStringArray(int[] intArray) {

		String[] stringArray = new String[intArray.length];

		for (int i = 0; i < intArray.length; i++) {
			stringArray[i] = String.valueOf(intArray[i]);
		}

		return stringArray;
	}

	/**
	 * Builds a String from an int array with a delimiter between each int
	 * 
	 * @param intArray
	 *            The int array
	 * @param delimiter
	 *            The delimiter
	 * @return A delimited String, <NULL> if array is null
	 */
	public static String intArrayToString(int[] intArray, String delimiter) {

		String returnString = "";

		if (intArray != null) {
			for (int i = 0; i < intArray.length; i++) {
				returnString = returnString + intArray[i];

				if (i != intArray.length - 1) {
					returnString = returnString + delimiter;
				}
			}
			return returnString;
		} else {
			return null;
		}
	}

	/**
	 * Checks if the given String is in an integer value
	 * 
	 * @param s
	 *            The string to check
	 * @param radix
	 *            The base of the numeric system
	 * @return <TRUE>, if String is an integer, <FALSE> if String is no integer
	 */
	public static boolean isInteger(String s, int radix) {

		if (s.isEmpty()) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}
}

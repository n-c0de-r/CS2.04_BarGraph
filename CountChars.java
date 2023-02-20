import java.io.*;
import java.util.Scanner;

/**
 * This class takes an input text file, iterates over every single character and
 * counts their occurrence. Only ASCII characters from A-Z are considered. 
 * All others are omitted.
 * 
 * @author n-c0de-r
 * @version 23.02.20
 */

public class CountChars {
	
	public static void main(String[] args) {
		// Create an instance of the class for OOP approach
		CountChars counter = new CountChars();
		
		counter.runCLI();
	}
	
	/**
	 * This provides a basic command line user interface.
	 */
	private void runCLI() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		
		System.out.println("Welcome to the Bar Graph Creator.\n"
				+ "When you want to quit, write \"quit\" or.\n"
				+ "just enter nothing to skip the input.");
		while (true) {
			System.out.println("\nPlease enter a path to a file you want to read from:");
			input = getInptut(scanner);
			BufferedReader buffer = readFile(input);
			
			// Read out the text stream and generate a graph
			int[] letters = readChars(buffer);
			String graphText = createBarGraph(letters);
			
			System.out.println("\nPlease enter a path to a file you want to save to:");
			input = getInptut(scanner);
			writeFile(graphText, input);
			
			System.out.println("\nDo you want to print the graph in the console?\n"
					+ "Type \"no\" to skip, or anything else to accept:");
			input = getInptut(scanner);
			System.out.println(graphText);
		}
	}
	
	/**
	 * Creating a buffered reader from a given file at a path.
	 * 
	 * @param inputFilePath	A path to read the text from.
	 * @return A bufferedReader containing the text of the file.
	 */
	private BufferedReader readFile(String inputFilePath) {
		BufferedReader bufferedReader = null;
		String path = "./original.txt";
		
		if (isCorrectPath(inputFilePath))
			path = inputFilePath;
		
		File originalFile = new File(path);
		
		try {
			FileReader input = new FileReader(originalFile);
			bufferedReader = new BufferedReader(input);
		
		} catch (FileNotFoundException e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
			
		}
		
		return bufferedReader;
	}
	
	/**
	 * Reads each character of a given text file
	 * 
	 * @param text	Text from a text file in a buffered reader.
	 * @return An array of counted characters.
	 */
	
	
	private int[] readChars(BufferedReader text) {
		// Letters equal the indices: 1-26 = a-z;
		// 0th index stores the count of all characters.
		int[] lettersCount = new int[27];
		
		try {
			int charValue = 0;
			while ((charValue = text.read()) != -1) {
				// Skip all non characters
				if (!isValidCharacter(charValue)) {
					text.skip(1);
					continue;
				}
				
				// Make all capital letters small with normalize,
				// as there are fewer of them in general.
				// Subtract the value of 'a'-1 (96) to get the index.
				lettersCount[normalize(charValue) - ('a'-1)]++;
			// for every counted letter increment the number
			// of all letters found in a file
			lettersCount[0]++;
			}

		} catch (IOException e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		return lettersCount;
	}
	
	/**
	 * prints out a bar graph where stars represent a certain amount of characters
	 * relative to the total.
	 * 
	 * @param characterArray	The Array of characters to make a graph from.
	 * @return The String containing the whole text representing a bar graph.
	 */
	private String createBarGraph(int[] characterArray) {
		
		String star = "*";
		// Start with a text stating the total number, not vital but fun
		String str = "Total printable characters: " + characterArray[0] + "\r\n\r\n";
		int highest = findMax(characterArray);
		double percent;
		
		// guard against very small numbers, keeps representation always readable
		if (highest < 100) {
			percent = 1.0 * characterArray[0] / highest;
		} else {
			percent = highest / 100.0;
		}
		
		for (int x = 1; x < characterArray.length; x++) {
			// Check each value in the array and set a star
			// for a calculated amount of characters.
			char c = (char) (x + 64);
			
			//Print out numbers only for task 3
			//str += c + ": " + letters[x] + "\r\n";
			
			// guard against small numbers of finds,
			//  show them at least once for representation
			if (characterArray[x] > percent / 2 && characterArray[x] < percent && characterArray[x] != 0) {
				str += c + ": *\r\n";
			} else {
				str += c + ": " + star.repeat((int) (characterArray[x] / percent)) + "\r\n";
			}
		}
		// Append a legend/description at the end, rounds doubles down
		str += "\r\n'*' = represents approximately " + (Math.round(percent * 10) / 10.0)
				+ " characters of each letter.\r\n"
				+ "Small counts are shown as one star, if they surpass half the scale ammount.";
		
		return str;
	}
	
	/**
	 * Writes a given text to a file at a given path.
	 * @param stringToWrite		The string to write to a file.
	 * @param outputFilePath	The file path to save to.
	 */
	private void writeFile(String stringToWrite, String outputFilePath) {
		// This is not a path, but the check is almost the same. Kinda wrong usage!
		if (!isCorrectPath(stringToWrite)) return;
		
		String path = "./frequency.txt";
		
		if (isCorrectPath(outputFilePath))
			path = outputFilePath;
		
		File saveToFile = new File(path);
		
		try { // to create a new output file
			saveToFile.createNewFile();
			
		} catch (IOException e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		// Once a file was created write the text to file.
		// Potential to short down to one try catch block!
		
		try  (FileWriter output = new FileWriter(saveToFile)) {
			output.write(stringToWrite);
			output.flush();
			// Write the whole text to the file.
			
		} catch (FileNotFoundException e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// HELPER METHODS
	
	/**
	 * Gets and evaluates the input from the user.
	 * Returns a string of the given input line.
	 * 
	 * @return	The next line from the Scanner, stripped & trimmed.
	 */
	private String getInptut(Scanner scanner) {
		String input = scanner.nextLine();
		if(wantsToQuit(input.toLowerCase())) System.exit(0);
		return input.strip().trim();
	}
	
	/**
	 * Checks if the user input results to a termination.
	 * @param userInput	The input string to check.
	 * @return	Either true or false, quit or continue.
	 */
	private boolean wantsToQuit(String userInput) {
		return (userInput.equals("no") || userInput.equals("quit"));
	}
	
	/**
	 * Checks if a file path is not blank.
	 * @param path	The path to check.
	 * @return	True when it is correct.
	 */
	private boolean isCorrectPath(String path) {
		return path != null && !path.isBlank() && !path.isEmpty();
	}
	
	/**
	 * Checks if a given character value is within
	 * the range of accepted characters A-Z & a-z.
	 * @param chr	The character value to check.
	 * @return	True if within.
	 */
	private boolean isValidCharacter(int chr) {
		return ((chr >= 'A'-0 && chr < 'Z'-0) ||
		(chr >= 'a'-0 && chr <= 'z'-0));
	}
	
	/**
	 * Finds the highest number of a letter, using this as the scale maximum.
	 * 
	 * @param values	Array to check.
	 * @return The highest number found.
	 */
	private int findMax(int[] values) {
		int maxValue = 0;
		
		for (int i = 1; i < values.length; i++) {
			if(values[i]>maxValue) maxValue = values[i];
		}
		
		return maxValue;
	}
	
	/**
	 * Normalizes given characters to lower case.
	 * @param chr	A character to convert.
	 */
	private int normalize(int chr) {
		// Check the ASCII code of letters
		if (chr > 64 && chr < 91) {
			// convert to lower case
			chr += 32;
		}
		return chr;
	}
}

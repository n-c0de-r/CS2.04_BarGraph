import java.io.*;

/**
 * This class takes an input text file, iterates over every single character and
 * counts their occurrence. Only ASCII characters from A-Z are considered. all
 * others are omitted.
 * 
 * @author Sönke
 * @author Nermin
 * @version 08.05.2021
 */

public class CountChars {

	private FileReader input;
	private FileWriter output;
	private BufferedReader text;
	private File original;
	private File save;
	private int[] letters;

	public static void main(String[] args) {
		// Create an instance of the class for OOP approach
		CountChars counter = new CountChars();

		counter.setup();
		counter.readChars(counter.text);
		counter.printHistogram();
	}

	/**
	 * Creating all the relevant objects for the class
	 */
	private void setup() {
		original = new File("./original.txt");
		save = new File("./frequency.txt");
		letters = new int[27];

		try {
			input = new FileReader(original);
			output = new FileWriter(save);
			text = new BufferedReader(input);
			save.createNewFile();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reads each character of a given text file
	 * 
	 * @param text Text from a text file in a buffered reader
	 */
	private void readChars(BufferedReader text) {
		try {
			int c = 0;
			while ((c = text.read()) != -1) {
				// Check the ASCII code of letters
				if (c > 96 && c < 123) {
					// convert to upper case
					c -= 32;
				}
				// Skip all line feeds & carriage returns
				if (c == 10 || c == 13) {
					text.skip(1);
				} else {
					// only count upper case letters
					if (c > 64 && c < 91) {
						// increment the counted letter in it's respective
						// position in the array of letters.
						letters[c - 64] = letters[c - 64] + 1;
					} else {
						// skip everything else
						text.skip(1);
					}
					// for every counted letter increment the number
					// of all letters found in a file
					letters[0] = letters[0] + 1;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * prints out a histogram where stars represent a certain amount of characters
	 * relative to the total.
	 */
	private void printHistogram() {
		String star = "*";
		// Start with a text stating the total number, not vital but fun
		String str = "Total printable characters: " + letters[0] + "\r\n\r\n";
		int highest = findMax(letters, 1, letters.length);
		double percent;

		// guard against very small numbers, keeps representation always readable
		if (highest < 100) {
			percent = 1.0 * letters[0] / highest;
		} else {
			percent = highest / 100.0;
		}
		
		for (int x = 1; x < letters.length; x++) {
			// Check each value in the array and set a star
			// for a calculated amount of characters.
			char c = (char) (x + 64);
			//Print out numbers only for task 3
			//str += letters[x] + "\r\n";
			
			// guard against small numbers of finds,
			//  show them at least once for representation
			if (letters[x] > percent / 2 && letters[x] < percent && letters[x] != 0) {
				str += c + ": *\r\n";
			} else {
				str += c + ": " + star.repeat((int) (letters[x] / percent)) + "\r\n";
			}
		}
		// Append a legend/description at the end, rounds doubles down
		str += "\r\n'*' = represents approximately " + (Math.round(percent * 10) / 10.0)
				+ " characters of each letter.\r\n"
				+ "Small counts are shown as one star, if they surpass half the scale ammount.";
		try {
			output.write(str);
			output.flush();
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Finds the highest number of a letter, using this as the scale maximum.
	 * Recursive function.
	 * 
	 * @param values array to check
	 * @param start  where to begin checking
	 * @param value  where to finish
	 * @return the highest number found
	 */
	public static int findMax(int[] values, int start, int value) {
		if (start == values.length)
			return value;
		else
			return findMax(values, start + 1, Math.max(value, values[start]));
	}
}

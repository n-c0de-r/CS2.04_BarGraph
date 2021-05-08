import java.io.*;

public class CountChars {

	private FileReader input;
	private FileWriter output;
	private BufferedReader text;
	private File original;
	private File save;
	private int[] letters;

	public static void main(String[] args) {
		CountChars counter = new CountChars();

		counter.setup();
		counter.readChars(counter.text);
		counter.printHistogram();
	}

	private void setup() {
		original = new File("./original.txt");
		save = new File("./output.txt");
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

	private void readChars(BufferedReader text) {
		try {
			int c = 0;
			while ((c = text.read()) != -1) {
				// Check the ASCII code of letters
				if (c > 96 && c < 123) {
					//convert to upper case
					c -= 32;
				}
				//Skip all line feeds & carriage returns
				if (c == 10 || c == 13) {
					text.skip(1);
				} else {
					//only count upper case letters
					if (c > 64 && c < 90) {
						//increment the counted letter in it's respective
						//position in the array of letters.
						letters[c - 64] = letters[c - 64] + 1;
					} else {
						//skip everything else
						text.skip(1);
					}
					//for every counted letter increment the number
					//of all letters found in a file
					letters[0] = letters[0] + 1;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printHistogram() {
		String star = "*";
		//Start with a text stating the total number, not vital but fun
		String str = "Total letters: " + letters[0] + "\r\n\r\n";
		for (int x = 1; x<letters.length; x++) {
			//Check each value in the array and set a star
			//for each 10 characters.
			char c = (char) (x+64);
			str += c + ": " + star.repeat(letters[x] / 10) + "\r\n";
		}
		//Append a legend/description at the end
		str += "\r\n'*' = represents 10 characters of each letter";
		try {
			output.write(str);
			output.flush();
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

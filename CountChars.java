import java.io.*;

public class CountChars {

	private FileReader input;
	private FileWriter output;
	private BufferedReader text;
	private File original;
	private File save;

	public static void main (String[] args) {
		CountChars counter = new CountChars();
		
		counter.setup();
		counter.readChars(counter.text);
	}

	private void setup() {
		original = new File("C:\\Users\\HTW-IMI\\eclipse-workspace\\Lab4\\src\\text.txt");
		save = new File("C:\\Users\\HTW-IMI\\eclipse-workspace\\Lab4\\src\\output.txt");
		
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
				if (c == 10 || c == 13) {
					text.skip(1);
				} else {
					char character = (char) c; // converting integer to char
					System.out.println(character);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeChars(char c) {

	}
}

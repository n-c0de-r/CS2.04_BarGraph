import java.io.*;

public class CountChars {

	private FileReader input;
	private FileWriter output;
	private BufferedReader text;
	private File original;
	private File save;

	public static void main (String[] args) {
		CountChars counter = new CountChars();
		counter.original = new File ("C:\\Users\\HTW-IMI\\eclipse-workspace\\Lab4\\src\\text.txt");
		counter.save = new File ("C:\\Users\\HTW-IMI\\eclipse-workspace\\Lab4\\src\\output.txt");
		
		
		
		try {
			counter.input = new FileReader(counter.original);
			counter.output = new FileWriter(counter.save);
			counter.text = new BufferedReader(counter.input);
			counter.save.createNewFile();
			
			counter.readChars(counter.text);
			
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
				char character = (char) c; // converting integer to char
				System.out.println(character);
				if (c == 13 || c == 10)
				text.skip(1);
				}
			}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}

	private void writeChars(char c) {

	}

}

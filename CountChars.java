import java.io.*;
import java.util.*;

/**
 * This class takes an input text file, iterates over every single character and
 * counts their occurrence. Only ASCII characters from A-Z are considered. 
 * All others are omitted. If the array approach is selected.
 * Map is used by default. Getting a single char is not done, as it is slower!
 * 
 * @author n-c0de-r
 * @version 23.02.20
 */

public class CountChars {
    private double SCALING_FACTOR = 50.0;
    private String SYMBOL = "*";
    private char UPPER_FIRST = 'A', UPPER_LAST = 'Z';
    private char LOWER_FIRST = 'a', LOWER_LAST = 'z';
    private String inputPath;
    private String outputPath;
    
    public CountChars(String input, String output) {
        inputPath = input;
        outputPath = output;
    }
    
    public CountChars() {
        this("./original.txt", "./frequencies.txt");
    }
    
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
            input = getInput(scanner);

            Reader fileContent = readFile(input);
            Reader buffer = new BufferedReader(fileContent);
            
            // Read out the text stream and generate a graph
            String[] letters = readCharsToArray(buffer);
            String graphText = createGraph(letters);
            
            System.out.println("\nPlease enter a path to a file you want to save to:");
            input = getInput(scanner);
            writeFile(graphText, input);
            
            System.out.println("\nDo you want to print the graph in the console?\n"
                    + "Type \"no\" to skip, or anything else to accept:");
            input = getInput(scanner);
            System.out.println(graphText);
        }
    }
    
    /**
     * Same as above, but for the GUI.
     * @return The full graph text to show in the GUI.
     */
    public String run() {
        Reader fileContent = readFile(inputPath);
        // Map allows more characters, but harder to handle
        String[] letterMap = readCharsToMap(fileContent);
        // Char Array, easier but limited to ASCII  A-Z only
        // String[] letterMap = readCharsToArray(fileContent);
        
        String graphText = createGraph(letterMap);
        writeFile(graphText, outputPath);
        return graphText;
    }
    
    /**
     * Creating a reader from a given file at a path.
     * @param inputFilePath    A path to read the text from.
     * @return A Reader containing the text of the file.
     */
    private Reader readFile(String inputFilePath) {
        FileReader fileContent = null;
        String filePath = inputPath; // default
        
        // Update if a path is given
        if (isCorrectPath(inputFilePath))
            filePath = inputFilePath;
        
        File originalFile = new File(filePath);
        
        try {
            fileContent = new FileReader(originalFile);
        
        } catch (FileNotFoundException e) {
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        
        return fileContent;
    }
    
    /**
     * Creates a Map of Characters and their Integer counts.
     * More flexible, works with Unicode too!
     * But can be complex to work with.
     * @param fileContent    The file to read from.
     * @return A String array representation of counted characters.
     */
    private String[] readCharsToMap(Reader fileContent) {
        Map<Character, Integer> lettersCount = new HashMap<>();
        
        try (Scanner scanner = new Scanner(fileContent)) {
             // Set the delimiter to an empty string to read character by character
            scanner.useDelimiter("");
            
            while (scanner.hasNext()) {
                Character key = nextCharacter(scanner);
                if(key == null) {
                    continue; // Skip invalid characters.
                }
                
                if(lettersCount.containsKey(key)) {
                    lettersCount.put(key, lettersCount.get(key)+1);
                } else {
                    lettersCount.put(key, 1);
                }
            }
        }
        
        // Converts Map to a String of pattern "{A=1, B=1, C=1...}"
        String str = lettersCount.toString();
        // Remove the curly braces
        str = str.substring(1, str.length()-1);
        // Split Thre string along the comma
        return str.split(", ");
    }
    
    /**
     * Reads each character of a given text file.
     * Most basic solution for standard ASCII alphabet,
     * resulting to an array of counts, with index 0
     * holding the sum of all characters for graph display.
     * @param buffer    Text from a text file in a buffered reader.
     * @return A String array representation of counted characters.
     */
    private String[] readCharsToArray(Reader buffer) {
        /* This is just for the legacy code. Can be
         * removed if only one implementation is kept.
         */
        
        // Letters equal the indices: 1 to 26 = a to z;
        int[] lettersCount = new int[LOWER_LAST-(LOWER_FIRST-1)];
        String[] str = new String[lettersCount.length];
        
        try {
            int charValue = 0;
            // if buffer returns -1, it's the end of the file
            while ((charValue = buffer.read()) != -1) {
                // Skip all non characters
                if (!isValidCharacter(charValue)) {
                    continue;
                }
                
                // Make all capital letters small with normalize,
                // as there are fewer upper-case to convert in general.
                // Subtract the value of LOWER_FIRST ('a'=97) to get the index.
                // Also subtracting 0 from a char turns it into an int, impicitly.
                lettersCount[normalize(charValue) - (LOWER_FIRST-0)]++;
            }

        } catch (IOException e) {
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        
        // Only needed for unified output to make both codes work equally.
        for (int i = 0; i < str.length; i++) {
            str[i] = (char) (i+LOWER_FIRST) + "=" + lettersCount[i];
        }
        
        return str;
    }
    
    /**
     * Prints out a bar graph where each Symbol represent a
     * certain amount of characters relative to the total.
     * @param characterMap    A String array representation of counted characters.
     * @return The String containing the whole text representing a bar graph.
     */
    private String createGraph(String[] characterMap) {
        /* This is convoluted and makes the code slower,
         * as it needs to split up everything before,
         * that had to be glued together in the first place.
         * But this way the graph making method works with
         * the old Arrays and the new Maps equally.
         */
        String[] chars = new String[characterMap.length];
        int[] values = new int[characterMap.length];
        int sum = 0;
        int index = 0;
        
        for (String pair : characterMap) {
            chars[index] = pair.split("=")[0];
            values[index] = Integer.parseInt(pair.split("=")[1]);
            sum += values[index];
            index++;
        }
        
        // Start with a text stating the total number, not vital but fun
        String graph = "Total printable characters: " + sum + "\r\n\r\n";
        
        int highest = findMax(values);
        int starCount;
        
        double percent = highest / SCALING_FACTOR;
        
        // Check each value in the array and set a star
        // for a calculated amount of characters.
        for (int i = 0; i < chars.length; i++) {
            
            //Print out numbers only for task 3
            //graph += chars[i] + ": " + values[i] + "\r\n";
            // continue;
            
            starCount = (int) Math.ceil(values[i]*SCALING_FACTOR/highest);
            graph += chars[i] + ": " + SYMBOL.repeat(starCount) + "\r\n";
        }
        
        // Append a legend/description at the end, rounds doubles down
        graph += "\r\n'*' = represents approximately " + (Math.round(percent * SCALING_FACTOR) / SCALING_FACTOR)
                + " characters of each letter.\r\n"
                + "Small counts are shown as one star, if they surpass half the scale amount.";
        
        return graph;
    }
    
    /**
     * Writes a given text to a file at a given path.
     * @param stringToWrite        The string to write to a file.
     * @param outputFilePath    The file path to save to.
     */
    private void writeFile(String stringToWrite, String outputFilePath) {
        // This is not a path, but the check is almost the same. Kinda wrong usage!
        if (!isCorrectPath(stringToWrite)) return;
        
        String filePath = outputPath;
        
        if (isCorrectPath(outputFilePath))
            filePath = outputFilePath;
        
        File saveToFile = new File(filePath);
        
        try { // to create a new output file
            saveToFile.createNewFile();
            
        } catch (IOException e) {
            // Default exception handling, not good!
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        
        // Once a file was created write the text to file.
        // Potential to short down to one try-catch block!
        
        try  (Writer output = new FileWriter(saveToFile)) {
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
     * Normalizes given characters to lower case.
     * @param chr    A character to convert.
     */
    private int normalize(int chr) {
        // Check the ASCII code of letters
        if (chr >= UPPER_FIRST-0 && chr <= UPPER_LAST-0) {
            chr += 'a'-'A'; // convert to lower case
            // chr += 'A' - 'a'; // convert to Upper Case
        }
        
        // if (chr >= LOWER_FIRST-0 && chr <= LOWER_LAST-0) {
            // chr += 'A' - 'a'; // convert to Upper Case
        // }
        return chr;
    }
    
    /**
     * Get the next character from the scanner.
     * @param scanner    Scanner, that reads the files.
     * @return A valid character.
     */
    private Character nextCharacter(Scanner scanner) {
        int letter = scanner.next().codePointAt(0);
        if(Character.isLetter(letter))
            return (char) letter;
        return null;
    }
    
    /**
     * Finds the highest number of a letter, using this as the scale maximum.
     * 
     * @param values    Array to check.
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
     * Gets and evaluates the input from the user.
     * Returns a string of the given input line.
     * @param scanner    The Scanner to read from.
     * @return    The next line from the Scanner, stripped & trimmed.
     */
    private String getInput(Scanner scanner) {
        String input = scanner.nextLine();
        if(wantsToQuit(input.toLowerCase())) System.exit(0);
        return input.strip().trim();
    }
    
    /**
     * Checks if a file path is not blank.
     * @param path    The path to check.
     * @return    True when it is correct.
     */
    private boolean isCorrectPath(String path) {
        return path != null && !path.isBlank() && !path.isEmpty();
    }
    
    /**
     * Checks if a given character value is within
     * the range of accepted characters A-Z & a-z.
     * @param chr    The character value to check.
     * @return    True if within.
     */
    private boolean isValidCharacter(int chr) {
        return ((chr >= UPPER_FIRST-0 && chr <= UPPER_LAST-0) ||
        (chr >= LOWER_FIRST-0 && chr <= LOWER_LAST-0));
    }
    
    /**
     * Checks if the user input results to a termination.
     * @param userInput    The input string to check.
     * @return    Either true or false, quit or continue.
     */
    private boolean wantsToQuit(String userInput) {
        return (userInput.equals("no") || userInput.equals("quit"));
    }
}

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
    private Dimension screenSize;
    private JTextField inputFileTextField;
    private JTextField outputFileTextField;
    private String graphText;
    
    public GUI() {
        super("Bar Graph Creator");
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Set up main panel with input and output file fields
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JLabel inputLabel = new JLabel("Input file: ");
        FontMetrics metrics = inputLabel.getFontMetrics(inputLabel.getFont());
        int labelWidth = metrics.stringWidth("Output file: ");
        inputLabel.setPreferredSize(new Dimension(labelWidth, inputLabel.getPreferredSize().height));
        inputPanel.add(inputLabel, BorderLayout.WEST);
        
        inputFileTextField = new JTextField(20);
        inputPanel.add(inputFileTextField, BorderLayout.CENTER);
        JButton inputFileChooserButton = new JButton("...");
        inputFileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseInputFile();
            }
        });
        inputPanel.add(inputFileChooserButton, BorderLayout.EAST);
        pathPanel.add(inputPanel, BorderLayout.NORTH);
        
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(new JLabel("Output file: "), BorderLayout.WEST);
        outputFileTextField = new JTextField(20);
        outputPanel.add(outputFileTextField, BorderLayout.CENTER);
        JButton outputFileChooserButton = new JButton("...");
        outputFileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseOutputFile();
            }
        });
        outputPanel.add(outputFileChooserButton, BorderLayout.EAST);
        pathPanel.add(outputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(pathPanel, BorderLayout.NORTH);
        
        // Set up button panel with "Run" and "Show Graph" buttons
        JPanel buttonPanel = new JPanel();
        
        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runBarGraphCreator();
            }
        });
        buttonPanel.add(runButton);
        
        JButton showGraphButton = new JButton("Show Graph");
        showGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGraph();
            }
        });
        buttonPanel.add(showGraphButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame and set up frame
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        
        setSize(screenSize.width / 3, screenSize.height / 5);
        
        // Center the frame on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new GUI();
    }

    
    private void chooseInputFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if(!filePath.endsWith(".txt"))
                filePath += ".txt";
            inputFileTextField.setText(filePath);
        }
    }
    
    private void chooseOutputFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if(!filePath.endsWith(".txt"))
                filePath += ".txt";
            outputFileTextField.setText(filePath);
        }
    }
    
    private void runBarGraphCreator() {
        String inputPath = inputFileTextField.getText();
        String outputPath = outputFileTextField.getText();
        
        CountChars counter = new CountChars(inputPath, outputPath);
        
        if( (inputPath == null || inputPath.isBlank() || inputPath.isEmpty()) ||
        (outputPath == null || outputPath.isBlank() || outputPath.isEmpty())) {
            counter = new CountChars();
        }
        
        graphText = counter.run();
    }
    
    private void showGraph() {
        if(graphText == null || graphText.isBlank() || graphText.isEmpty())
            return;
        
        JFrame graphFrame = new JFrame("Graph");
        JTextArea textArea = new JTextArea(graphText);
        JScrollPane scrollPane = new JScrollPane(textArea);
        graphFrame.add(scrollPane);
        graphFrame.pack();
        graphFrame.setSize(screenSize.width/2, screenSize.height/2);
        graphFrame.setLocationRelativeTo(null);
        graphFrame.setVisible(true);
    }
}

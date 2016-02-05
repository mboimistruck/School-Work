import java.awt.*;
import javax.swing.*;
import java.beans.*;
import java.awt.event.*;
import java.text.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class WorkGUI extends JPanel
                                    implements PropertyChangeListener {
    //Values for the fields
    private static String upc = "";
    private static String bbd = "";
    private static String monthInput = "";
    private static String yearInput = "";
    private static String product = "";
    private static String checkUPC = "";
    private static String quantity = "";

    //Labels to identify the fields
    private static JLabel productLabel, upcLabel, bbdLabel, monthLabel, yearLabel, checkUPCLabel, quantityLabel;

    //Strings for the labels
    private static String productString = "Product Name: ";
    private static String upcString = "Product UPC(X-XXXXX-XXXXX-X): ";
    private static String bbdString = "Day: ";
    private static String monthString = "Month: ";
    private static String yearString = "Year: ";
    private static String checkUPCString = "UPC: ";
    private static String quantityString = "Quanity: ";
    static String fileName = "Library.xls";

    private static JFormattedTextField productField, upcField, bbdField, monthField, yearField, checkUPCField, quantityField;

    private static void entryGUI() {

        //create and set up the window.
        JFrame frame = new JFrame("Entry GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton addButton = new JButton ("Add");
        frame.add(addButton);

        //declare labels
        productLabel = new JLabel(productString);
        upcLabel = new JLabel(upcString);
        bbdLabel = new JLabel(bbdString);
        monthLabel = new JLabel(monthString);
        yearLabel = new JLabel(yearString);
        quantityLabel = new JLabel(quantityString);

        //set up textfields
        productField = new JFormattedTextField();
        productField.setValue(new String(product));
        productField.setColumns(10);

        //productField.addPropertyChangeListener("char", this);
        upcField = new JFormattedTextField();
        upcField.setValue(new String(upc));
        upcField.setColumns(10);

        //upcField.addPropertyChangeListener("value", this);
        bbdField = new JFormattedTextField();
        bbdField.setValue(new String(bbd));
        bbdField.setColumns(10);

        //upcField.addPropertyChangeListener("value", this);
        monthField = new JFormattedTextField();
        monthField.setValue(new String(monthInput));
        monthField.setColumns(10);

        //upcField.addPropertyChangeListener("value", this);
        yearField = new JFormattedTextField();
        yearField.setValue(new String(yearInput));
        yearField.setColumns(10);

        //bbdField.addPropertyChangeListener("char", this);
        quantityField = new JFormattedTextField();
        quantityField.setValue(new String(quantity));
        quantityField.setColumns(10);

        //set up labels
        productLabel.setLabelFor(productField);
        upcLabel.setLabelFor(upcField);
        quantityLabel.setLabelFor(quantityField);
        bbdLabel.setLabelFor(bbdField);
        monthLabel.setLabelFor(monthField);
        yearLabel.setLabelFor(yearField);

        //add labels to panel
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(productLabel);
        labelPane.add(upcLabel);
        labelPane.add(bbdLabel);
        labelPane.add(monthLabel);
        labelPane.add(yearLabel);
        labelPane.add(quantityLabel);

        //assigns layouts for fields
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(productField);
        fieldPane.add(upcField);
        fieldPane.add(bbdField);
        fieldPane.add(monthField);
        fieldPane.add(yearField);
        fieldPane.add(quantityField);

        //text fields on right and labels on the left
        //setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(labelPane, BorderLayout.CENTER);
        frame.add(fieldPane, BorderLayout.LINE_END);
        frame.add(addButton, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                frame.dispose();

                try {

                    product = productField.getText();
                    upc = upcField.getText();
                    quantity = quantityField.getText();
                    bbd = bbdField.getText();
                    yearInput = yearField.getText();
                    monthInput = monthField.getText();
                    boolean containsString = true;
                    String searchWord = upc;
                    Scanner readFile = new Scanner(new File("Library.xls"));
                    readFile.useDelimiter("\\t+");

                    while(readFile.hasNext())
                        if(searchWord.equals(readFile.next())) {

                            containsString = false;
                            readFile.close();
                            break;
                        }
                    if(!containsString){

                        JOptionPane.showMessageDialog(null,"The UPC already exists,\n "+
                                                    "please try again.");
                    }else {

                        FileWriter fileWriter = new FileWriter(fileName, true);
                        fileWriter.write(System.getProperty( "line.separator" ));
                        fileWriter.write(product);
                        fileWriter.write("\t"+ upc);
                        fileWriter.write("\t"+ bbd + "/" + monthInput + "/" + yearInput);
                        fileWriter.write("\t"+ quantity);
                        fileWriter.close();
                        String upcFileName = "Library/UPC/"+ upc +".txt";
                        FileWriter upcWriter = new FileWriter(upcFileName, true);
                        int month = Calendar.getInstance().get(Calendar.MONTH);
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        int dayResult = (Integer.parseInt(bbd) - day);
                        int monthResult = (Integer.parseInt(monthInput) - (month + 1));
                        int yearResult = (Integer.parseInt(yearInput) - year);

                        if (yearResult <= 0 && monthResult > 0) {

                            System.out.println(monthInput);
                            System.out.println(monthResult);
                            //monthResult = monthResult*(30 + (Integer.parseInt(monthInput)/2));
                            monthResult = (monthResult*30) + (Integer.parseInt(monthInput)/2);
                            dayResult = dayResult + monthResult;
                            monthResult = 0;
                        }
                        if (yearResult > 0 && monthResult <= 0) {

                            yearResult = yearResult*365;
                            dayResult = dayResult + yearResult;
                            yearResult = 0;
                        }
                        if (yearResult > 0 && monthResult > 0) {

                            monthResult = (monthResult*30) + (Integer.parseInt(monthInput)/2);
                            yearResult = yearResult*365;
                            System.out.println(monthInput);
                            System.out.println(yearResult);
                            System.out.println(monthResult);
                            System.out.println(dayResult);
                            dayResult = (monthResult + yearResult)+dayResult;
                            monthResult = 0;
                            yearResult = 0;
                        }

                        upcWriter.write("\t"+ dayResult);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ monthResult);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ yearResult);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ day);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ month);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ year);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ quantity);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ bbd);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ monthInput);
                        upcWriter.write(System.getProperty( "line.separator" ));
                        upcWriter.write("\t"+ yearInput);
                        upcWriter.close();
                        JOptionPane.showMessageDialog(null,"Entry added!");
                    }
                }
                catch(IOException ex) {

                    JOptionPane.showMessageDialog(null,"Error writing to file '" + fileName + "', make sure Excel is closed. Folder/File may also not exist!");
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
    private static void modifyGUI() {

        System.out.println("MODIFYGUI");

        JFrame frame = new JFrame("MODIFY");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    private static void introGUI() {

        JFrame f = new JFrame("INTRO");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        //BELOW FUNCTIONS FOR THE "ADD ENTRY" BUTTON
        JButton addEntryButton = new JButton("Add Entry");
        p.add(addEntryButton);
        addEntryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                f.setVisible(false);
                f.dispose();
                JFrame checkWindow = new JFrame("Check");
                checkUPCLabel = new JLabel(checkUPCString);
                checkUPCField = new JFormattedTextField();
                checkUPCField.setValue(new String(checkUPC));
                checkUPCField.setColumns(10);
                checkUPCLabel.setLabelFor(checkUPCField);
                JPanel labelPane = new JPanel(new GridLayout(0,1));
                labelPane.add(checkUPCLabel);
                JPanel fieldPane = new JPanel(new GridLayout(0,1));
                fieldPane.add(checkUPCField);
                checkWindow.add(labelPane, BorderLayout.CENTER);
                checkWindow.add(fieldPane, BorderLayout.LINE_END);

                //BELOW FUNCTIONS FOR THE "Check UPC" BUTTON
                JButton checkButton = new JButton("Check UPC");
                checkWindow.add(checkButton, BorderLayout.SOUTH);
                checkButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try { //checks the Library.xls file, using tabs as delimiters to scan for UPCs

                            upc = checkUPCField.getText();
                            bbd = "";
                            product = "";
                            quantity = "";
                            checkUPC = checkUPCField.getText();
                            boolean containsString = false;
                            String searchWord = checkUPC;
                            Scanner readFile = new Scanner(new File("Library.xls"));
                            readFile.useDelimiter("\\t+");

                            while(readFile.hasNext())
                                if(searchWord.equals(readFile.next())) {

                                    containsString = true;
                                    readFile.close();
                                    break;
                                }
                            if(containsString){ //call modifyGUI if UPC already exists

                                modifyGUI();

                            }else { //call entryGUI if UPC doesn't exist, prompts user to create new entry

                                entryGUI();

                            }
                        }
                        catch(IOException ex) {

                            JOptionPane.showMessageDialog(null,"Error writing to file '" + fileName + "'.");
                        }
                    }
                });
                checkWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                checkWindow.pack();
                checkWindow.setVisible(true);
                checkWindow.setSize(250,100);
            }
        });
        //BELOW WILL FUNCTION FOR THE SCAN BUTTON
        JButton scanButton = new JButton("Scan Dates");
        p.add(scanButton);
        f.getContentPane().add(p, BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
        f.setSize(350,100);
    }
    // Called when a field's "value" property changes.
    public void propertyChange(PropertyChangeEvent e) {

        Object source = e.getSource();
    }
    public static void main(String[] args) {

        //creating and showing the GUI
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                UIManager.put("swing.boldMetal", Boolean.FALSE);
                introGUI();
            }
        });
    }
}

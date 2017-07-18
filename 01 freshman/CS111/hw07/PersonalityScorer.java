/*
 * PersonalityScorer.java
 * 
 * name: Matthew Huynh
 * email: mhuynh@bu.edu
 * 
 * description: This program scores responses to the Myers-Briggs personality test and uses the results to determine a person's
 * personality type. The program reads an input file containing answers to the test questions from a number of different individuals,
 * and it creates an output file that contains a summary of their answers and their resulting personality types.
 */

import java.io.*;
import java.util.*;

public class PersonalityScorer {
    
    public static final Scanner CONSOLE = new Scanner(System.in);
    
    public static void main(String[] args) throws FileNotFoundException {
        
        // asks user for input and output file names and then parses the input file, printing the results to the output file
        parseFile(askForInputFile(), askForOutputFile());
        
    }
    
    /* 
     * askForInputFile - asks the user for the name of the input file and returns a File object if the file exists
     */
    public static File askForInputFile() {
        
        System.out.print("Input file name: ");
        String fileName = CONSOLE.nextLine();
        File results = new File(fileName);
        
        while (!results.exists()) {
            System.out.print("File not found. Try again: ");
            fileName = CONSOLE.nextLine();
            results = new File(fileName);
        }
        
        return results;
    }
    
    /* 
     * askForOutputFile - asks the user for the name of the output file and returns a File object
     */
    public static File askForOutputFile() {
        
        System.out.print("Output file name: ");
        String fileName = CONSOLE.nextLine();
        File results = new File(fileName);
        
        return results;
    }

    /*
     * parseFile - determines a person's personality type and prints the results to outputFile
     */ 
    public static void parseFile(File inputFile, File outputFile) throws FileNotFoundException {
        Scanner data = new Scanner(inputFile);
        PrintStream output = new PrintStream(outputFile);
        
        while (data.hasNextLine()) {
            String personName = data.nextLine();
            String personAnswers = data.nextLine();
            
            char[] answers = personAnswers.toCharArray();
            int[] componentCount = new int[8];
            
            // count how many A's and B's for each component
            doCount(answers, componentCount);
            
            // print results to file
            printResults(output, personName, componentCount);
        }
        
    }
    
    /*
     * doCount - count how many A's and B's for each component and writes the counts to the componentCount array
     */
    public static void doCount(char[] answers, int[] componentCount) {
            // traverse entire array
            for (int i = 0; i < answers.length; i++) {
                // component 1
                if (i % 7 == 0) {
                    if (answers[i] == 'A')
                        componentCount[0] += 1;
                    else if (answers[i] == 'B')
                        componentCount[1] += 1;
                }    
                // component 2
                if (i % 7 == 1 || i % 7 == 2) {
                    if (answers[i] == 'A')
                        componentCount[2] += 1;
                    else if (answers[i] == 'B')
                        componentCount[3] += 1;
                }    
                // component 3
                if (i % 7 == 3 || i % 7 == 4) {
                    if (answers[i] == 'A')
                        componentCount[4] += 1;
                    else if (answers[i] == 'B')
                        componentCount[5] += 1;
                }    
                // component 4
                if (i % 7 == 5 || i % 7 == 6) {
                    if (answers[i] == 'A')
                        componentCount[6] += 1;
                    else if (answers[i] == 'B')
                        componentCount[7] += 1;
                }
            }
    }
    
    /*
     * printResults - prints the results of a person's personality score to an external data file
     */
    public static void printResults(PrintStream output, String personName, int[] componentCount) throws FileNotFoundException {
        
        // print name
        output.println(personName);
        
        // print A and B counts
        for (int i = 0; i < componentCount.length; i++) {
            if (i % 2 == 0) {
                output.print(componentCount[i] + "A-");
            }
            else {
                output.print(componentCount[i] + "B ");
            }
        }
        
        // start printing personality type
        output.print("= ");
        
        // component 1: Extrovert vs. Introvert (E/I)
        if (componentCount[0] > componentCount[1]) {
            output.print("E");
        }
        else if (componentCount[0] < componentCount[1]) {
            output.print("I");
        }
        else {
            output.print("X");
        }
            
        // component 2: Sensation vs. iNtuition (S/N)
        if (componentCount[2] > componentCount[3]) {
            output.print("S");
        }
        else if (componentCount[2] < componentCount[3]) {
            output.print("N");
        }
        else {
            output.print("X");
        }
            
        // component 3: Thinking vs. Feeling (T/F)
        if (componentCount[4] > componentCount[5]) {
            output.print("T");
        }
        else if (componentCount[4] < componentCount[5]) {
            output.print("F");
        }
        else {
            output.print("X");
        }
            
        // component 4: Judging vs. Perceiving (J/P)
        if (componentCount[6] > componentCount[7]) {
            output.print("J");
        }
        else if (componentCount[6] < componentCount[7]) {
            output.print("P");
        }
        else {
            output.print("X");
        }

        // jump to next line so other results can be appended to file
        output.println();
    }
    
}
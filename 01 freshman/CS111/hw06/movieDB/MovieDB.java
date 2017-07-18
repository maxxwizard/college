/*
 * MovieDB.java
 * 
 * name: Matthew Huynh
 * email: mhuynh@bu.edu
 * 
 * description: This program allows a user to lookup the movie with a given
 * earnings rank on the 200 top-grossing movies of all time. The user can also
 * lookup the movies in the database that were directed by a given director.
 */

import java.io.*;
import java.util.*;

public class MovieDB {
    public static final String MOVIE_FILE = "movie.dat";
    public static final String DIRECTOR_FILE = "director.dat";
    public static final Scanner CONSOLE = new Scanner(System.in);
    
    public static void main(String[] args) throws FileNotFoundException {
        
        boolean programRunning = true;
        
        // greets user
        System.out.println("Welcome to the Movie Database!");
        
        while (programRunning) {
           
            // presents user with menu of options
            System.out.println();
            System.out.println("MENU:");
            System.out.println("  1. Get movie by earnings rank");
            System.out.println("  2. Get movies by director");
            System.out.println("  3. Quit");
            
            // reads user choice
            int userMenuChoice = readUserMenuChoice();
            
            // if choice #1
            if (userMenuChoice == 1) {
                // prompt for earnings rank
                int userRankSelection = readUserRankSelection();
                // print out movie with corresponding earnings rank
                System.out.println("    " + movieWithRank(userRankSelection));
            } // else if choice #2
            else if (userMenuChoice == 2) {
                // prompt for director's name
                String directorName = readDirectorName();
                // print out movies associated with director
                moviesWithDirector(directorName);
            } // else quit (because the only possible value left for choice to be is 3)
            else {
                programRunning = false;
            }
        }
    }
    
    /* 
     * readUserMenuChoice - asks the user to choose a menu option
     * by typing in an integer between 1 and 3 and returns the integer
     * when the input is considered valid
     */
    public static int readUserMenuChoice() {

        int userMenuChoice = 0;
        while (userMenuChoice > 3 || userMenuChoice < 1) {
                System.out.print("Enter the number of your choice (1-3): ");
                userMenuChoice = CONSOLE.nextInt();
            }
        
        CONSOLE.nextLine();
        return userMenuChoice;
    }
    
    /* 
     * readUserRankSelection - prompts the user for an integer between
     * 1 and 200 that corresponds with an earnings rank and returns
     * the integer when the input is considered valid
     */
    public static int readUserRankSelection() {
        
        int userRankSelection = 0;
        while (userRankSelection < 1 || userRankSelection > 200) {
                System.out.print("Enter the earnings rank (1-200): ");
                userRankSelection = CONSOLE.nextInt();
            }
        
        CONSOLE.nextLine();
        return userRankSelection;
    }

    /* 
     * readDirectorName - prompts the user for the director's name
     * and returns it when the input is considered valid (not blank)
     */    
    public static String readDirectorName() {
        
        String directorName = "";
        while (directorName.length() <= 0) {
                System.out.print("Enter the director's name: ");
                directorName = CONSOLE.nextLine();
            }
        
        return directorName;
    }
    
    /*
     * movieWithRank - searches through the movie data file for the movie
     * with the specified rank and returns a String of the form 
     * "movie name (year released)".
     * 
     * preconditions: rank >= 1 and rank <= 200
     */ 
    public static String movieWithRank(int rank) throws FileNotFoundException {
        if (rank < 1 || rank > 200) {
            throw new IllegalArgumentException("invalid rank: " + rank);
        }
        
        Scanner input = new Scanner(new File(MOVIE_FILE));
        while (input.hasNextLine()) {
            Scanner data = new Scanner(input.nextLine());

            data.useDelimiter("[\t]");
            
            int id = data.nextInt();
            String name = data.next();
            int year = data.nextInt();
            int thisRank = data.nextInt();
            
            if (thisRank == rank) {
                String nameYear = name + " (" + year + ")";
                return nameYear;
            }
        }
        
        // Just in case there isn't a movie with the specified rank,
        // although there should be given our error-checking code
        // at the start of the method.
        return "no movie with rank #" + rank;   
    }
    
    /*
     * movieWithID - searches through the movie data file for the movie
     * with the specified ID number and returns a String of the form 
     * "movie name (earnings rank)".
     * 
     */    
    public static String movieWithID(int ID) throws FileNotFoundException {
        
        Scanner input = new Scanner(new File(MOVIE_FILE));
        while (input.hasNextLine()) {
            Scanner data = new Scanner(input.nextLine());

            data.useDelimiter("[\t]");
            
            int thisID = data.nextInt();
            String name = data.next();
            int year = data.nextInt();
            int rank = data.nextInt();
            
            if (thisID == ID) {
                String nameRank = name + " (" + rank + ")";
                return nameRank;
            }
        }
        
        // Just in case there isn't a movie with the specified ID,
        // although there should be given our error-checking code
        // at the start of the method.
        return "no movie with ID #" + ID;   
    }
    
    /*
     * moviesWithDirector - searches through the director data file and
     * prints out the movies associated with the director
     * 
     * the format for the printing is: "movie name (earnings rank)"
     * 
     * if the director was not found in the database, the method prints out this fact
     */ 
    public static void moviesWithDirector(String directorName) throws FileNotFoundException {
        
        boolean directorFound = false;
        
        Scanner input = new Scanner(new File(DIRECTOR_FILE));
        while (input.hasNextLine() && directorFound == false) {
            Scanner data = new Scanner(input.nextLine());

            data.useDelimiter("[\t]");
            
            int id = data.nextInt();
            String name = data.next();
            
            // if director's line is found
            if (name.equals(directorName)) {
                directorFound = true;
                System.out.println("Top-grossing movies by " + directorName + ":");
                // print out all his associated movies
                while (data.hasNextInt()) {
                    System.out.println("    " + movieWithID(data.nextInt()));
                }
            }
        }
        
        // if director was never found, state so
        if (directorFound == false)
            System.out.println("No movies in the database for " + directorName);
        
    }
}

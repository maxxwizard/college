/*
 * Craps.java
 * 
 * name: Matthew Huynh
 * email: mhuynh@bu.edu
 * 
 * description: This program is a text-based version of the popular dice game Craps.
 */

import java.util.*; // needed for Scanner and Random

public class Craps {
    
    // creates the Random object that will be used for simulating dice
    public static final Random RAND = new Random();
    
    public static void main(String[] args) {
        
        // initializes variables needed for the game
        boolean gameRunning = true;
        int money = 200;
        int round = 0;
        
        // while game is running
        while (gameRunning) {
            
            // starts a new round
            round++;
            
            // checks if this is the first round
            if (round == 1) {
                // resets money
                money = 200;
                // greet the player
                System.out.println("WELCOME TO THE GAME OF CRAPS.");
                System.out.println("You start with $200.");
                System.out.println("Good luck!");
            }
            
            // announces start of new round
            System.out.println();
            System.out.println("ROUND " + round);
            
            // plays a round
            money = money + playRound();
            
            // displays player's current money
            System.out.println("You now have $" + money);
            
            // if game-ending conditions has been reached
            if (money <= 0 || money >= 400) {
                // announce game end
                System.out.println();
                System.out.println("GAME OVER!");
                // if player wants another game
                if (continuePlaying()) {
                    // reset game
                    round = 0;
                }
                // else stop game and bid farewell to player
                else {
                    gameRunning = false;
                    System.out.println();
                    System.out.println("GOODBYE!");
                }
            }
        
        }
    }
    
    /**
     * SIMULATES THE ROLLING OF TWO 6-SIDED DICE
     * 
     * - prints the results of the rolling
     * - returns the total of the two dice (an int value between 1 and 12 inclusive)
     */
    public static int rollDice(boolean isFirst) {
            
        int diceOne = 1+RAND.nextInt(6);
        int diceTwo = 1+RAND.nextInt(6);
        
        if (isFirst) {
            System.out.println("Your first roll: " + diceOne + " " + diceTwo);
        } else {
            System.out.println("Your next roll: " + diceOne + " " + diceTwo);
        }
        
        return diceOne + diceTwo;
    }

    /**
     * SIMULATES A ROUND OF CRAPS
     * 
     * - returns a balance for the round, which will be added to the user's current money
     */
    public static int playRound() {
        
        // rolls the two dice and finds their total
        int diceTotal = rollDice(true);
        int balance = 0;
            
        // if player has rolled a total of 7 or 11, player wins $100 and round ends
        if (diceTotal == 7 || diceTotal == 11) {
            System.out.println("You have rolled " + diceTotal + " and win $100");
            balance += 100;
            
          // if player has rolled a total of 2, 3, or 12, player loses $100 and round ends
        } else if (diceTotal == 2 || diceTotal == 3 || diceTotal == 12) {
            System.out.println("Sorry! You rolled " + diceTotal + " and lose $100");
            balance -= 100;
            
          // if player has rolled any other sum (1, 4, 5, 6, 8, 9, or 10)
        } else {
            // dice total now becomes the point
            int point = diceTotal;
            System.out.println("You point is " + point);
            // player continues to roll until player rolls the point value or a 7
            while (diceTotal != point || diceTotal != 7) {
                diceTotal = rollDice(false);
                // if player has rolled the point value, player wins $50 and round ends
                if (diceTotal == point) {
                    System.out.println("You rolled your point and win $50");
                    balance += 50;
                    break;
                }
                // if player has rolled a 7, player loses $50 and round ends
                else if (diceTotal == 7) {
                    System.out.println("Sorry! You rolled 7 and lose $50");
                    balance -= 50;
                    break;
                }
            }
        }
        
        // returns the balance of the round
        return balance;
    }
    
    /**
     * ASKS THE PLAYER IF S/HE WISHES TO PLAY ANOTHER GAME
     * 
     * - returns true if answer is yes
     * - returns false is answer is no
     */
    public static boolean continuePlaying() {
        
        Scanner console = new Scanner(System.in);
        String answer;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Do you want to play another game (y/n)? ");
            answer = console.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                return true;
            } else if (answer.equalsIgnoreCase("N")) {
                validInput = true;
            }
        }
        
        return false;
    }
    
}
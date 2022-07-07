import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class TreasureMap {
    public static void nextMove(int player, int[] currSpace, int roll, Digraph di) { //Takes in player, their space, their roll, and digraph
        int nodes = di.outdegree(currSpace[player]); //Stores current amount of out degrees at player space. (Always two)
        int choice = StdRandom.uniform(0, nodes); //Picks a random number between zero or one before the loop. (zero = forwards and one = backwards)
        for (int i = 0; i < roll; i++) { //Iterates through until it meets the size of the roll.
            int[] choices = new int[nodes]; //Stores the two choices in an array (Always two as seen above)
            int j = 0; //Used for increments
            for (int a : di.adj(currSpace[player])) { //Uses the digraph iterator to give us the actual values of the out degrees.
                if (choice == 0) { //If the choice is zero(forward) the player will only go forward.
                    choices[j] = a;
                    j++;
                } else if (choice == 1) { //If the choice is one(backwards) the player will only go backwards.
                    choices[j] = a;
                    j++;
                }
                currSpace[player] = choices[choice]; //Current space of the player = either forward or backwards. Will always be one way depending on the random number.
            }
        }
    }

    public static void main(String[] args) {

        int numPlayers = Integer.parseInt(args[0]); //Takes in one argument for amount of players.
        if (numPlayers > 4 || numPlayers < 1) { //If a value lower than 1 or higher than 4 is entered the players will be randomly assigned between 1 and 4.
            StdOut.println("Please Enter between 1 to 4 players.");
            return;
        }

        In in = new In("map.txt"); //Input stream "in" to take in values for the digraph (41 vertices, 82 edges).
        Digraph di = new Digraph(in); //Digraph "di" takes in the values from map.txt.

        int[] points = new int[di.V()]; //Creates an array "points" that has 41 values (di.V() = 41 vertices).
        for (int i = 0; i < di.V(); i++) { //Fills the array "points" with random points between 1 and 5 until each vertice is filled.
            points[i] = StdRandom.uniform(1, 6);
            //StdOut.println(points[i]);
        }

        ArrayList<Integer> traps = new ArrayList<>(5); //ArrayList traps holds five different traps.
        while (traps.size() < 5) { //While the trap size() is less than 5 loop.
            int rand = StdRandom.uniform(0, 41); //Assign a random number between 0 and 40 each loop.
            if (!traps.contains(rand)) { //If traps does not contain the random number add it until ArrayList is full.
                traps.add(rand);
            }
        }
        StdOut.print("Traps " + traps.toString());
        StdOut.println();

        ArrayList<Integer> bonus = new ArrayList<>(5); //ArrayList bonus holds five different traps.
        while (bonus.size() < 5) { //While the bonus size() is less than 5 loop.
            int rand = StdRandom.uniform(0, 41); //Assign a random number between 0 and 40 each loop.
            if (!bonus.contains(rand) && !traps.contains(rand)) { //If bonus and traps does not contain the random number add it until ArrayList is full.
                bonus.add(rand);
            }
        }
        StdOut.print("Bonuses " + bonus.toString());
        StdOut.println();

        int winningPoints = StdRandom.uniform(50, 101); //Assigns winningPoints a random number between 50 and 100.

        int[] playerPoints = new int[numPlayers]; //Tracks the players points.
        int[] dieRolls = new int[numPlayers]; //Tracks the # of die rolls per player.
        int[] currentSpace = new int[numPlayers]; //Tracks the players current space on the board.
        int[] visitedSpace = new int[di.V()]; //Tracks the most visited space.
        int[] playerTurns = new int[numPlayers]; //Tracks how many turns each player made.
        int[] totalLost = new int[numPlayers]; //Tracks how much gold each player lost due to traps.
        int[] totalGained = new int[numPlayers]; //Tracks how much gold each player gained due to bonuses.
        int trapTotal = 0; //Tracks how many traps total were found in the game.
        int bonusTotal = 0; //Tracks how many bonuses total were found in the game.

        StdOut.println("Welcome to Treasure Map!");
        StdOut.println("There are " + numPlayers + " players playing.");
        StdOut.println("Let the games begin! " + winningPoints + " points needed to win!");
        StdOut.println();

        //Main game loop
        for (int i = 0; i < 1000000; i++) { //Loops up until 1,000,000. This number should never be reached.
            int player = i % numPlayers; //Each loop the next player is up. 1 - 4.
            playerTurns[(i % numPlayers)]++; //Keeps track of the players turns.

            if (playerPoints[player] < winningPoints) { //If players current points is less than the winning points continue.
                int die = StdRandom.uniform(1, 7); //Random die roll between 1 and 6.
                dieRolls[(i % numPlayers)] += die; //Used to keep track of the average die roll per player.

                StdOut.println("Turn #" + playerTurns[player]); //Prints turn number per player.
                StdOut.println("Player " + (player + 1) + " rolls a " + die); //Prints current player and what they rolled.

                nextMove(player, currentSpace, die, di); //Calls nextMove method
                visitedSpace[currentSpace[player]]++; //Keeps track of most visited space.

                if (traps.contains(currentSpace[player])) { //If traps Arraylist = current space for player continue.
                    trapTotal++; //Tracks amount of traps hit.
                    playerPoints[player] = playerPoints[player] - 5; //Each trap is minus 5 points.
                    totalLost[player] = totalLost[player] - 5; //Tracks total points lost for each player.
                    StdOut.println("The player's gold was stolen by pirates! They lose 5 gold."); //Text to show traps was hit.
                } else if (bonus.contains(currentSpace[player])) { //If bonus Arraylist = current space for player continue.
                    bonusTotal++; //Tracks amount of bonus hit.
                    playerPoints[player] = playerPoints[player] + 5; //Each bonus is plus 5 points.
                    totalGained[player] = totalGained[player] + 5; //Tracks total points gained for each player.
                    StdOut.println("The player found a pirate's chest! They gain 5 gold."); //Text to show bonus was gained.
                }


                playerPoints[player] = playerPoints[player] + points[currentSpace[player]]; //Each space has randomly assigned points. Add points depending on current space for player.
                StdOut.println("         Moves to space " + currentSpace[player]);
                StdOut.println("         Space " + currentSpace[player] + " has " + points[currentSpace[player]] + " gold.");
                StdOut.println("         Player " + (player + 1) + " now has " + playerPoints[player] + " gold.");
                StdOut.println("------------------------------------");

                if (playerPoints[player] >= winningPoints) { //If the players points is greater or equal to winning points they win!
                    StdOut.println("Player " + (player + 1) + " wins " + "with " + playerPoints[player] + " gold!!!");
                    break;
                }
            }
        }
        //End of game stats.
        StdOut.println("------------------------------------");
        StdOut.println();
        StdOut.println("End of game statistics:");
        StdOut.println();
        StdOut.println("Total bonuses found: " + bonusTotal); //How many bonuses were acquired.
        StdOut.println("Total traps hit: " + trapTotal); //How many traps were hit.
        StdOut.println();

        StdOut.println("The most visited space was:");
        int max = visitedSpace[0]; //Used to find max in visited space.
        int spaces = 0; //Used to track which space max was found.
        for (int i = 0; i < visitedSpace.length; i++) { //Iterates and compares values until a max is found.
            if (visitedSpace[i] > max) {
                max = visitedSpace[i];
                spaces = i;
            }
        }
        StdOut.println("Space number " + spaces + " was most visited with " + max + " visits."); //Prints space and max.
        StdOut.println();
        StdOut.println("------------------------------------");
        StdOut.println();

        StdOut.println("Player statistics:");
        StdOut.println();
        for (int i = 0; i < numPlayers; i++) { //Iterates through each players stats.
            StdOut.println("Player " + (i + 1) + ":");
            StdOut.println("They had a total of " + playerPoints[i] + " gold."); //Shows the total amount of gold for each player.
            StdOut.println("Total number of turns: " + playerTurns[i]); //Prints amount of turns per player.
            StdOut.println("Average dice roll: " + String.format("%.2f", (double) dieRolls[i] / (double) playerTurns[i])); //Print average dice roll (rolls / turns) and formats to two decimals.
            StdOut.println("They gained " + totalGained[i] + " gold from bonuses."); //Prints out total gained from bonuses.
            StdOut.println("They lose " + totalLost[i] + " gold from traps."); //Prints out total lost from traps.
            StdOut.println("------------------------------------");
        }

    }
}



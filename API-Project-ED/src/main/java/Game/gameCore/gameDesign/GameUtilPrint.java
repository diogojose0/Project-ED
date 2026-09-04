package Game.gameCore.gameDesign;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.corridor.ICorridor;
import Game.api.division.IDivision;
import Game.api.division.leverGame.ILever;
import Game.api.maze.IMaze;
import Game.api.maze.IMazeLoader;
import Game.api.player.IPlayerState;
import Game.api.replays.IReplay;
import Game.api.replays.IReplaysManagement;
import Game.exceptions.maze.NullDivisionException;

import java.util.Iterator;


/**
 * Utility class that centralizes all console output used by the maze game.
 * <p>
 * All messages shown to the player are printed through these static helper methods.
 * </p>
 */
public class GameUtilPrint {

    /** Prints a generic exiting message. */
    public static void exiting() { System.out.println("Exiting..."); }

    /** Prints a generic wrong option warning. */
    public static void wrongOption() { System.out.println("Wrong Option. Try again!"); }

    /**
     * Prints the list of available entry points.
     *
     * @param iter iterator over divisions that can be used as entry points
     */
    public static void printEntryPoints(Iterator<IDivision> iter) {
        System.out.println("Available entry points:");
        while (iter.hasNext()) {
            IDivision division = iter.next();
            System.out.println(division.getId() + " " + division);
        }
    }

    /**
     * Prints the state of all players.
     *
     * @param players iterator over player states
     */
    public static void printAllPlayersState(Iterator<IPlayerState> players) {
        printSpaceLine();
        System.out.println("[Players state]");
        while (players.hasNext()) {
            IPlayerState player = players.next();
            System.out.println(player);
        }
    }

    /** Prints a visual separator line. */
    public static void printSpaceLine() { System.out.println("--------------------------"); }

    /**
     * Prints the current player's turn information.
     *
     * @param player the player whose turn it is
     */
    public static void printPlayerTurn(IPlayerState player) { System.out.println("Turn -> " + player); }

    /** Prints a confirmation that the entry point was assigned to the player. */
    public static void successfullyUpdatedEntryPoint() { System.out.println("Entry point assigned to player."); }

    /**
     * Prints all possible divisions the player can move to.
     *
     * @param iter iterator over neighboring divisions
     */
    public static void printPossibleDivisionsForPlayer(Iterator<IDivision> iter) {
        System.out.println("Available divisions:");
        while (iter.hasNext()) {
            IDivision division = iter.next();
            System.out.println(division.getId() + " " + division.getName());
        }
    }

    /**
     * Prints the final win message and updates the replay with the win description.
     *
     * @param player the winning player state
     * @param replay the replay to update with the win moment
     */
    public static void winMessage(IPlayerState player, IReplay replay) {
        System.out.println("THE GAME IS OVER!");
        System.out.println("Winner -> Player - " + player.getPlayer().getName() + "\n");
        replay.setWinMoment("Winner -> Player - " + player.getPlayer().getName());
    }

    /**
     * Prints the current division name and a separator line.
     *
     * @param division the current division
     */
    public static void printCurrentDivision(IDivision division) {
        System.out.println("Current Division: " + division.getName());
        printSpaceLine();
    }

    /** Prints an error indicating that there are not enough players to start a game. */
    public static void errorNotEnoughPlayers() { System.out.println("To play the game, you must have at least two players!"); }

    /**
     * Prints the list of levers in a lever room.
     *
     * @param leverRoom iterator over levers in the room
     */
    public static void printLevers(Iterator<ILever> leverRoom) {
        int counter = 0;
        System.out.println("Levers:");
        while (leverRoom.hasNext()) {
            ILever lever = leverRoom.next();
            System.out.println(counter++ + " - " + lever);
        }
    }

    /** Prints a message indicating that the lever activation failed. */
    public static void leverFailed() { System.out.println("Lever failed!"); }

    /**
     * Prints a success message for a lever that opened a division.
     *
     * @param divisionName the name of the division that was opened
     */
    public static void leverSuccessfully (String divisionName) { System.out.println("Lever successfully opened! - " + divisionName + " opened!"); }

    /**
     * Prints the description of an event.
     *
     * @param description the event description to print
     */
    public static void printEventDescription(String description) { System.out.println("\n" + description + "\n"); }

    /**
     * Prints the text of an enigma.
     *
     * @param enigma the enigma text
     */
    public static void printEnigma(String enigma) { System.out.println(enigma); }

    /** Prints a message indicating that the enigma answer was correct. */
    public static void enigmaCorrect() { System.out.println("Correct Answer!"); }

    /** Prints a message indicating that the enigma answer was incorrect. */
    public static void enigmaIncorrect() { System.out.println("Incorrect Answer!"); }

    /**
     * Prints a list of possible target players for an action.
     *
     * @param iter iterator over player states
     */
    public static void printPlayers(Iterator<IPlayerState> iter) {
        int counter = 0;
        printSpaceLine();
        System.out.println("Possible Players to change:");
        while (iter.hasNext()) {
            IPlayerState searchPlayer = iter.next();
            System.out.println(counter + " -> " + searchPlayer.getPlayer().getName() + " - " + searchPlayer.getMovementPlayer().getDivision().getName());
            counter++;
        }
        printSpaceLine();
    }

    /**
     * Prints all available mazes in the given loader.
     *
     * @param loader the maze loader providing the mazes
     */
    public static void printAvailableMazes(IMazeLoader loader) {
        System.out.println("Available Mazes:");
        DoublyLinkedUnorderedList<IMaze> mazes = loader.getMazes();
        Iterator<IMaze> iter = mazes.iterator();
        while (iter.hasNext()) {
            IMaze maze = iter.next();
            System.out.println(maze);
        }
    }

    /** Prints an error indicating that an invalid player was chosen. */
    public static void invalidPlayerChosen() { System.out.println("Invalid player chosen. Try again!"); }

    /**
     * Prints the movement history of the player, including corridors between divisions.
     *
     * @param history iterator over the visited divisions
     * @param maze    the maze used to retrieve corridors between divisions
     */
    public static void printPlayerHistory(Iterator<IDivision> history, IMaze maze) {
        printSpaceLine();

        System.out.println("History:");
        if (!history.hasNext()) {
            printSpaceLine();
            return;
        }

        IDivision previous = history.next();

        System.out.print(previous);

        while (history.hasNext()) {
            IDivision current = history.next();

            ICorridor corridor = null;
            try {
                corridor = maze.getCorridor(previous, current);
            } catch (NullDivisionException ignored) {}

            System.out.print(" -> ");
            if (corridor != null) {
                System.out.print(corridor);
            } else {
                System.out.print("X");
            }

            System.out.print(" -> " + current + "\n");

            previous = current;
        }

        System.out.println();
        printSpaceLine();
    }

    /** Prints the header message for entering a lever division. */
    public static void printLeverGame() {
        printSpaceLine();
        System.out.println("YOU HAVE NOW ENTERED AN LEVER DIVISION - CHOOSE CORRECTLY TO ADVANCE!!!");
        printSpaceLine();
    }

    /** Prints the header message for entering an enigma division. */
    public static void printEnigmaGame() {
        printSpaceLine();
        System.out.println("YOU HAVE NOW ENTERED AN ENIGMA DIVISION - ANSWER CORRECTLY TO ADVANCE!!!");
        printSpaceLine();
    }

    /** Prints a header indicating that a random event was generated. */
    public static void printEventGenerated() {
        printSpaceLine();
        System.out.println("WOW RANDOM EVENT GENERATED!!!");
        printSpaceLine();
    }

    /** Prints a generic play again question to the user. */
    public static void playAgain() { System.out.println("Do you want to play again? (y/n)"); }

    /**
     * Prints all replays managed by the given replay's manager.
     *
     * @param replaysManagement the manager that stores replays
     */
    public static void printReplays(IReplaysManagement replaysManagement) { System.out.println(replaysManagement); }

    /**
     * Prints a single replay.
     *
     * @param replay the replay to print
     */
    public static void printReplay(IReplay replay) { System.out.println(replay); }

    /** Prints an error indicating that there are no replays available. */
    public static void noReplaysAvailable() { System.out.println("No replays available!"); }

    /** Prints an error indicating that the chosen division is not a neighbour. */
    public static void areNotNeighbours() { System.out.println("Invalid Division!"); }

    /**
     * Prints an exception message.
     *
     * @param message the exception message to print
     */
    public static void printException(String message) { System.out.println(message); }

    /** Prints a generic error message. */
    public static void error() {System.out.println("ERROR! - "); }

    /** Prints an error indicating that the maze isn't connected. */
    public static void errorMazeIsntConnected() { System.out.println("Maze isn't connected!"); }

    /** Prints an error indicating that the maze has no entry points. */
    public static void errorHasNoEntryPoints() { System.out.println("Maze has no entry points!"); }

    /**
     * Prints all divisions from the given iterator.
     *
     * @param iter iterator over divisions
     */
    public static void printAllDivisions(Iterator<IDivision> iter) {
        System.out.println("Available Divisions:");
        while (iter.hasNext()) {
            IDivision division = iter.next();
            System.out.println(division.getId() + " - " + division);
        }
    }

}

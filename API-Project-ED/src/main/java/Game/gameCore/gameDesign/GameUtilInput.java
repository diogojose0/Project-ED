package Game.gameCore.gameDesign;

import Game.gameCore.gameDesign.utility.Utility;


/**
 * Utility class that centralizes all user input prompts for the game.
 * <p>
 * Each method displays a specific message and delegates to {@link Utility}
 * to read and parse the input from the user.
 * </p>
 */
public class GameUtilInput {

    /**
     * Asks the user to choose an entry point division.
     *
     * @return the selected entry point identifier
     */
    public static int entryPointInput() { return Utility.getintOption("Choose the entry point: "); }

    /**
     * Asks the user to choose the next division to move to.
     *
     * @return the selected division identifier
     */
    public static int nextDivisionInput() { return Utility.getintOption("Choose the next Division: "); }

    /**
     * Asks the user to choose a lever.
     *
     * @return the selected lever identifier
     */
    public static int leaverInput() { return Utility.getintOption("Choose the leaver: "); }

    /**
     * Asks the user to choose an answer option for an enigma.
     *
     * @return the selected option index or code
     */
    public static int enigmaInput() {return Utility.getintOption("Choose the option: ");}

    /**
     * Asks the user to choose a target player from a list.
     *
     * @return the selected player index or identifier
     */
    public static int possiblePlayersInput() { return Utility.getintOption("Choose a player: "); }

    /**
     * Asks the user to choose a maze by its id.
     *
     * @return the selected maze id
     */
    public static int mazeInput() { return Utility.getintOption("Choose a maze(id): "); }

    /**
     * Asks the user whether they want to play another game.
     *
     * @return {@code "y"} if the user wants to play again, {@code "n"} otherwise
     */
    public static String playAgainInput() { return Utility.getStringInput("Do you want play again? (y/n)"); }

    /**
     * Asks whether the new player should be a bot.
     *
     * @return {@code "y"} if the player is a bot, {@code "n"} if the player is human
     */
    public static String isBot() { return Utility.getStringInput("Is bot? (y/n): "); }

    /**
     * Asks the user to choose a replay by its id.
     *
     * @return the selected replay id
     */
    public static int replayIDInput() { return Utility.getintOption("Choose the replay ID: "); }

    /**
     * Asks whether the user wants to change the current maze.
     *
     * @return {@code "y"} if the user wants to change the maze, {@code "n"} otherwise
     */
    public static String changeMazeInput() { return Utility.getStringInput("Do you want change maze? (y/n)"); }

    /**
     * Asks how many players will participate in the game.
     *
     * @return the number of players
     */
    public static int quantityPlayersInput() { return Utility.getintOption("How many players? "); }

    /**
     * Asks for the name of the player at the given position.
     *
     * @param counter the zero-based index of the player being created
     * @return the name entered for the player
     */
    public static String namePlayerInput(int counter) { return Utility.getStringInput("Enter Player " + (counter + 1) + " Name: "); }

    /**
     * Asks for the name of the maze to be created.
     *
     * @return the maze name entered by the user
     */
    public static String mazeNameInput() {return Utility.getStringInput("Enter maze name: ");}

    /**
     * Displays the main menu and asks the user to choose an option.
     *
     * @return the selected menu option
     */
    public static int mainMenuInput() {return Utility.getintOption("MAZE GAME\n--------------------------\n--- MAIN MENU ---\n1. Start New Game\n2. View replays\n3. Create Maze\n4. Exit\n");}

    /**
     * Displays the game menu and asks the user to choose an option.
     *
     * @return the selected menu option
     */
    public static int startMenu() {return Utility.getintOption("MAZE GAME\n--------------------------\n--- GAME MENU ---\n1. Play\n2. Add players\n3. Exit\n");}

    /**
     * Asks for a yes or no answer.
     *
     * @param message the prompt message to display
     * @return the response entered by the user
     */
    public static String detailsInput(String message) { return Utility.getStringInput(message); }

    /**
     * Asks for the name of the division to be created.
     *
     * @return the division name entered by the user
     */
    public static String divisionNameInput() {return Utility.getStringInput("Enter the name of the division: "); }

    /**
     * Asks the user to choose a division.
     *
     * @return the selected division identifier
     */
    public static int divisionInput() { return Utility.getintOption("Choose the division: "); }

    /**
     * Displays the maze creation menu and asks the user to choose an option.
     *
     * @return the selected menu option
     */
    public static int possibleMazeChanges() {return Utility.getintOption("MAZE GAME\n--------------------------\n--- CREATE MAZE - MENU ---\n1. Create Corridor\n2. Create Division\n3. Exit\n");}

    /**
     * Asks for the name of the corridor to be created.
     *
     * @return the corridor name entered by the user
     */
    public static String corridorNameInput() {return Utility.getStringInput("Enter the name of the corridor: "); }

}

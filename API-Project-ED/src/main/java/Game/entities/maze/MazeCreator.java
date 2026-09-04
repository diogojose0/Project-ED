package Game.entities.maze;

import Game.api.division.IDivision;
import Game.api.division.leverGame.ILeverGame;
import Game.api.maze.IMaze;
import Game.api.maze.IMazeCreator;
import Game.entities.corridor.Corridor;
import Game.entities.division.Division;
import Game.entities.division.games.enigmaGame.EnigmaGame;
import Game.entities.division.games.leverGame.Lever;
import Game.entities.division.games.leverGame.LeverGame;
import Game.exceptions.maze.*;
import Game.gameCore.gameDesign.GameUtilInput;
import Game.gameCore.gameDesign.GameUtilPrint;
import Game.logs.Log;
import Game.logs.LogsManagement;

import java.util.Random;


/**
 * Concrete implementation of {@link IMazeCreator} responsible for interactively
 * building mazes at runtime.
 * <p>
 * This class guides the user through the process of:
 * creating divisions, configuring them as treasure rooms or entry points,
 * optionally assigning mini-games, and connecting divisions through corridors.
 * <br>
 * All relevant errors and exceptional situations are logged via
 * {@link LogsManagement}.
 * </p>
 */
public class MazeCreator implements IMazeCreator {

    /** Logs management to record events and errors during maze creation */
    private LogsManagement logsManagement;

    /**
     * Constructs a MazeCreator with the specified logs management.
     *
     * @param logsManagement the logs management instance
     */
    public MazeCreator(LogsManagement logsManagement) {
        this.logsManagement = logsManagement;
    }

    /**
     * Creates a new maze instance with the specified name.
     * <p>
     * The method allows interactive creation of the maze by adding
     * divisions and corridors based on user input until the maze
     * is connected and has at least one entry point.
     * </p>
     *
     * @param name the name of the maze
     * @return a new IMaze instance
     */
    @Override
    public IMaze createMaze(String name) {
        IMaze maze = new Maze(name);
        int option;

        do {
            option = GameUtilInput.possibleMazeChanges();
            switch (option) {
                case 1:
                    this.createCorridor(maze);
                    break;
                case 2:
                    this.createDivision(maze);
                    break;
                case 3:
                    if(!maze.isMazeConnected()) {
                        GameUtilPrint.error();
                        GameUtilPrint.errorMazeIsntConnected();
                    } else if (!maze.getEntryPoints().hasNext()){
                        GameUtilPrint.error();
                        GameUtilPrint.errorHasNoEntryPoints();
                    } else {
                        GameUtilPrint.exiting();
                    }
                    break;
                default:
                    GameUtilPrint.wrongOption();
            }

        } while (option != 3 || !maze.isMazeConnected() || !maze.getEntryPoints().hasNext());

        return maze;
    }

    /**
     * Creates a corridor between two divisions in the maze.
     * <p>
     * The method asks the user to select two different divisions
     * and a name for the corridor, then attempts to add the corridor
     * to the maze. Errors during the process are logged and displayed.
     * </p>
     *
     * @param maze the maze to which the corridor will be added
     */
    private void createCorridor(IMaze maze) {
        IDivision firstDivision;
        IDivision secondDivision;
        String name = GameUtilInput.corridorNameInput();
        try {
            firstDivision = this.chooseDivision(maze);
            secondDivision = this.chooseDivision(maze);
        } catch (NoDivisionsAvailableException e) {
            GameUtilPrint.error();
            GameUtilPrint.printException(e.getMessage());
            return;
        }

        if (firstDivision.equals(secondDivision)) {
            GameUtilPrint.error();
            GameUtilPrint.printException("The divisions cannot be equal!");
            return;
        }

        try {
            maze.addCorridor(firstDivision, secondDivision, new Corridor(name));
        } catch (NullDivisionException | NullCorridorException | EqualDivisionException e) {
            GameUtilPrint.error();
            GameUtilPrint.printException(e.getMessage());
            logsManagement.addLog(new Log(e.getMessage()));
        }
    }

    /**
     * Prompts the user to select a division from the maze.
     * <p>
     * The method displays all available divisions and allows
     * the user to choose one by its identifier. If no divisions
     * are available, an exception is thrown.
     * </p>
     *
     * @param maze the maze containing the divisions
     * @return the selected division
     * @throws NoDivisionsAvailableException if there are no divisions in the maze
     */
    private IDivision chooseDivision(IMaze maze) throws NoDivisionsAvailableException {
        IDivision division = null;
        boolean found;

        if(!maze.iteratorDivisions().hasNext()) {
            GameUtilPrint.error();
            throw new NoDivisionsAvailableException();
        }

        GameUtilPrint.printAllDivisions(maze.iteratorDivisions());
        int option;

        do {
            found = true;
            try {
                option = GameUtilInput.divisionInput();
                division = maze.getDivision(option);
            } catch (DivisionNotFoundException e) {
                GameUtilPrint.error();
                GameUtilPrint.printException(e.getMessage());
                logsManagement.addLog(new Log(e.getMessage()));
                found = false;
            }
        } while (!found);

        return division;
    }

    /**
     * Creates a new division in the maze.
     * <p>
     * The method asks the user for division details such as
     * name, whether it is a treasure room or entry point, and
     * optionally assigns a mini-game if applicable. Errors during
     * creation are logged and displayed.
     * </p>
     *
     * @param maze the maze to which the division will be added
     */
    private void createDivision(IMaze maze) {

        IDivision division = null;

        String name = GameUtilInput.divisionNameInput();
        boolean isTreasureRoom = this.chooseDetails("Is Treasure Room? (y/n)");
        boolean isEntryPoint = this.chooseDetails("Is EntryPoint? (y/n)");

        try {
            division = new Division(name, isTreasureRoom, isEntryPoint);
            maze.addDivision(division);
        } catch (NullDivisionException | AlreadyHaveTreasureRoomException | DivisionAlreadyExistsException e) {
            GameUtilPrint.error();
            GameUtilPrint.printException(e.getMessage());
            logsManagement.addLog(new Log(e.getMessage()));
        }

        if(!isTreasureRoom && !isEntryPoint) {
            this.chooseMiniGame(division);
        }
    }

    /**
     * Allows the user to choose and configure a mini-game for the division.
     * <p>
     * The method randomly decides between a Lever Game or an Enigma Game.
     * If a Lever Game is chosen, it further configures the levers based
     * on user input.
     * </p>
     *
     * @param division the division to which the mini-game will be assigned
     */
    private void chooseMiniGame(IDivision division) {
        Random rand = new Random();
        int randomNum = rand.nextInt(3);
        boolean isLeverGameRoom = this.chooseDetails("Is Lever Game Room? (y/n)");
        if(isLeverGameRoom) {
            division.setMiniGame(new LeverGame());
            for (int i = 0; i < 3; i++) {
                if(i == randomNum) {
                    ((ILeverGame)division.getMiniGame()).addLever(new Lever(division));
                } else {
                ((ILeverGame)division.getMiniGame()).addLever(new Lever(null));
                }
            }
            return;
        }

        division.setMiniGame(new EnigmaGame());
    }

    /**
     * Asks the user for a yes/no decision.
     * <p>
     * The method repeatedly asks the user for input until
     * a valid response ('y' or 'n') is received.
     * </p>
     *
     * @param message the prompt message to display
     * @return {@code true} for 'y', {@code false} for 'n'
     */
    private boolean chooseDetails(String message) {
        while (true) {
            String decision = GameUtilInput.detailsInput(message);
            switch (decision) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    GameUtilPrint.wrongOption();
            }
        }
    }

}

package Game.gameCore.gameDesign;

import Collections.list.ArrayUnorderedList;
import Game.api.data.exporter.IExporter;
import Game.api.data.importer.IImporter;
import Game.api.data.importer.IReplaysImporter;
import Game.api.engine.IGameEngine;
import Game.api.engine.IGameUI;
import Game.api.maze.IMaze;
import Game.api.maze.IMazeLoader;
import Game.api.player.IPlayer;
import Game.api.replays.IReplay;
import Game.api.replays.IReplaysManagement;
import Game.data.exporter.Exporter;
import Game.data.importer.Importer;
import Game.data.importer.ReplaysImporter;
import Game.entities.maze.MazeCreator;
import Game.entities.maze.MazeLoader;
import Game.entities.player.Player;
import Game.exceptions.gameEngine.GameStartedException;
import Game.exceptions.gameEngine.NonePlayersException;
import Game.exceptions.mazeloader.MazeAlreadyExistsException;
import Game.exceptions.mazeloader.MazeDoesntExistException;
import Game.exceptions.mazeloader.NullMazeException;
import Game.exceptions.replays.NullReplayException;
import Game.exceptions.replays.ReplayNotFoundException;
import Game.gameCore.gameLogistic.GameEngine;
import Game.logs.Log;
import Game.logs.LogsManagement;
import Game.replays.ReplaysManagement;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * {@code GameUI} is the main user interface controller for the maze game.
 * <p>
 * It presents menus to the user, drives the game flow, and manages the viewing and exporting of replays.
 * </p>
 */
public class GameUI implements IGameUI {

    /** Exporter used to persist replays to JSON when exiting the game menu. */
    private static final IExporter exporter = new Exporter();

    /** Importer used to load mazes from JSON files at startup. */
    private static final IImporter importer = new Importer();

    /** Importer used to load replays from JSON files at startup. */
    private static final IReplaysImporter replaysImporter = new ReplaysImporter();

    /** Default path to use if no mazes file is found. */
    private static final Path defaultPath = Paths.get("files/defaultMazes.json");

    /** Path to the enigmas JSON file. */
    private static final Path enigmaPath = Paths.get("files/enigmas.json");

    /** Path to the replays JSON file. */
    private static final Path replaysPath = Paths.get("files/replays.json");

    /** Path to the mazes JSON file. */
    private static final Path mazesPath = Paths.get("files/mazes.json");

    /** Logs manager used to store error messages and exceptional situations. */
    private final LogsManagement logsManagement = new LogsManagement();

    /**
     * Sets up the game by importing mazes and replays,
     * then starts the main menu loop for user interaction.
     */
    @Override
    public void setup() {
        IMazeLoader mazeLoader = new MazeLoader();
        IReplaysManagement replaysManagement = new ReplaysManagement();
        try {
            importer.importJson(mazeLoader, this.verifyPath(), enigmaPath);
            System.out.println("Maze Imported!");
            replaysImporter.importReplaysJson(replaysManagement, replaysPath);
            System.out.println("Replays Imported!");
        } catch (ParseException e) {
            System.out.println("Error opening JSON file!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            this.menu(mazeLoader, replaysManagement);
        } catch (IOException e) {
        }
    }

    /**
     * Verifies if the mazes path exists and is not empty.
     * If it doesn't exist or is empty, returns the default path.
     *
     * @return the verified path to use for loading mazes
     */
    private Path verifyPath() {
        if(mazesPath.toFile().exists() && mazesPath.toFile().length() > 0) {
            return mazesPath;
        } else {
            return defaultPath;
        }
    }

    /**
     * Displays the main menu and handles high-level user actions:
     * starting a new game, viewing replays, creating a maze or exiting.
     *
     * @param loader maze loader used to retrieve available mazes
     * @param replaysManagement component that stores and provides access to replays
     * @throws IOException if an I/O error occurs while reading user input or exporting data
     */
    private void menu(IMazeLoader loader, IReplaysManagement replaysManagement) throws IOException {
        int option;
        int replayID;

        do {
            option = GameUtilInput.mainMenuInput();

            switch (option) {
                case 1:
                    IMaze maze = this.selectMaze(loader);
                    this.startMenu(maze, replaysManagement, loader);
                    break;

                case 2:
                    GameUtilPrint.printReplays(replaysManagement);
                    boolean foundReplay = true;
                    IReplay replay = null;
                    if (replaysManagement.getReplays().isEmpty()) {
                        GameUtilPrint.noReplaysAvailable();
                        break;
                    }
                    do {
                        try {
                            replayID = GameUtilInput.replayIDInput();
                            replay = replaysManagement.get(replayID);
                        } catch (ReplayNotFoundException e) {
                            foundReplay = false;
                            GameUtilPrint.error();
                            GameUtilPrint.printException(e.getMessage());
                            logsManagement.addLog(new Log(e.getMessage()));
                        }
                    } while (!foundReplay);
                    GameUtilPrint.printReplay(replay);
                    break;

                case 3:
                    this.createMaze(loader);
                    break;

                case 4:
                    exporter.exportJson(replaysManagement, replaysPath);
                    exporter.exportJson(loader, mazesPath);
                    GameUtilPrint.exiting();
                    break;

                default:
                    GameUtilPrint.wrongOption();
            }
            System.out.println();

        } while (option != 4);

    }

    /**
     * Lets the user select a maze from the available list.
     * <p>
     * Keeps asking the user for a maze id until a valid maze is returned
     * from the loader.
     * </p>
     *
     * @param loader the maze loader that provides available mazes
     * @return the selected {@link IMaze}
     */
    private IMaze selectMaze(IMazeLoader loader) {
        GameUtilPrint.printAvailableMazes(loader);

        while (true) {
            int option = GameUtilInput.mazeInput();
            try {
                return loader.getMaze(option);
            } catch (MazeDoesntExistException e) {
                GameUtilPrint.error();
                GameUtilPrint.printException(e.getMessage());
                logsManagement.addLog(new Log(e.getMessage()));
            }
        }

    }

    /**
     * Facilitates the creation of a new maze by asking the user for a name
     * and adding it to the maze loader.
     *
     * @param loader the maze loader where the new maze will be added
     */
    private void createMaze(IMazeLoader loader) {
        String name = GameUtilInput.mazeNameInput();
        IMaze maze = new MazeCreator(logsManagement).createMaze(name);

        try {
            loader.add(maze);
        } catch (NullMazeException | MazeAlreadyExistsException e) {
            GameUtilPrint.error();
            GameUtilPrint.printException(e.getMessage());
            logsManagement.addLog(new Log(e.getMessage()));
        }
    }

    /**
     * Displays the game menu for a specific maze and manages player setup
     * and game execution.
     * <p>
     * Allows the user to:
     * Start a game with current players, add new players, exit and export replays.
     * </p>
     *
     * @param maze the maze to be used for the games
     * @param replaysManagement manager for storing generated replays
     * @param loader maze loader, used if the user decides to change maze later
     * @throws IOException if an error occurs while exporting replays
     */
    private void startMenu(IMaze maze, IReplaysManagement replaysManagement, IMazeLoader loader) throws IOException {
        int option;
        int counter = 0;
        ArrayUnorderedList<IPlayer> players = new ArrayUnorderedList<>();

        do {
            option = GameUtilInput.startMenu();

            switch (option) {
                case 1:
                    this.start(maze, players, replaysManagement, loader);
                    break;

                case 2:
                    int option2 = GameUtilInput.quantityPlayersInput();
                    if (option2 < 2) {
                        GameUtilPrint.errorNotEnoughPlayers();
                        break;
                    }
                    do {
                        String name = GameUtilInput.namePlayerInput(counter);
                        IPlayer player = this.infoPlayer(name);
                        players.addToRear(player);
                        counter++;
                    } while (counter != option2);
                    this.start(maze, players, replaysManagement, loader);
                    break;

                case 3:
                    GameUtilPrint.exiting();
                    break;

                default:
                    GameUtilPrint.wrongOption();
            }
            System.out.println();
        } while (option != 3);
    }

    /**
     * Collects information about a player (name already provided) and
     * asks whether it is a bot or a human player.
     *
     * @param name the player name
     * @return a newly created {@link IPlayer} instance
     */
    private IPlayer infoPlayer(String name) {
        IPlayer player = null;
        String decision;

        do {
            decision = GameUtilInput.isBot();
            switch (decision) {
                case "y":
                    player = new Player(name, true);
                    break;
                case "n":
                    player = new Player(name, false);
                    break;
                default:
                    GameUtilPrint.wrongOption();
            }
        } while (!decision.equals("y") && !decision.equals("n"));

        return player;
    }

    /**
     * Starts the game loop for the given maze and players, manages replay
     * recording and optionally allows the user to play again.
     *
     * @param maze the maze used for the game
     * @param players the list of players that will participate
     * @param replaysManagement manager where replays will be stored
     * @param loader maze loader used when changing mazes between games
     * @throws IOException if an error occurs while reading user input
     */
    private void start(IMaze maze, ArrayUnorderedList<IPlayer> players, IReplaysManagement replaysManagement, IMazeLoader loader) throws IOException {
        boolean again;
        IGameEngine engine = new GameEngine(maze, players);

        do {
            try {
                engine.start();
                replaysManagement.add(engine.getReplay());
            } catch (GameStartedException e) {
                GameUtilPrint.error();
                GameUtilPrint.printException(e.getMessage());
                logsManagement.addLog(new Log(e.getMessage()));
                return;
            } catch (NonePlayersException e) {
                GameUtilPrint.errorNotEnoughPlayers();
                logsManagement.addLog(new Log(e.getMessage()));
                return;
            } catch (NullReplayException e) {
                logsManagement.addLog(new Log(e.getMessage()));
            }
            engine.endGameConfig();
            again = playAgain();

            if (again) {
                this.willChangeMaze(loader, engine);
            }
        } while (again);

    }

    /**
     * Asks the user whether to change the maze before starting a new game,
     * and updates the engine's maze if requested.
     *
     * @param loader the maze loader used to retrieve a new maze
     * @param engine the current game engine whose maze may be changed
     * @throws IOException if an error occurs while reading user input
     */
    private void willChangeMaze(IMazeLoader loader, IGameEngine engine) throws IOException {
        String option;

        do {
            option = GameUtilInput.changeMazeInput();

            switch (option) {
                case "y":
                    try {
                        engine.setMaze(this.selectMaze(loader));
                    } catch (NullMazeException e) {
                        logsManagement.addLog(new Log(e.getMessage()));
                    } catch (GameStartedException e) {
                        GameUtilPrint.error();
                        GameUtilPrint.printException(e.getMessage());
                        logsManagement.addLog(new Log(e.getMessage()));
                    }
                    break;
                case "n":
                    break;
                default:
                    GameUtilPrint.wrongOption();
            }

        } while (!option.equals("y") && !option.equals("n"));

    }

    /**
     * Asks the user if they want to play another game.
     *
     * @return {@code true} if the user chooses to play again, {@code false} if the user chooses to stop
     */
    private boolean playAgain() {
        String option;

        while (true) {
            option = GameUtilInput.playAgainInput();

            if (option.equals("y")) {
                return true;
            } else if (option.equals("n")) {
                return false;
            } else {
                GameUtilPrint.wrongOption();
                GameUtilPrint.playAgain();
            }
        }
    }

}

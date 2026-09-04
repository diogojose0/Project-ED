package Game.gameCore.gameLogistic;

import Collections.list.ArrayUnorderedList;

import Game.api.corridor.ICorridor;
import Game.api.corridor.IEventStrategy;
import Game.api.division.*;
import Game.api.engine.IGameEngine;
import Game.api.engine.ITurnManager;
import Game.api.maze.IMaze;
import Game.api.player.IPlayer;
import Game.api.player.IPlayerState;
import Game.api.division.IMiniGame;
import Game.api.division.leverGame.ILeverGame;
import Game.entities.division.Division;
import Game.entities.player.PlayerState;
import Game.exceptions.division.NullMiniGameException;
import Game.exceptions.gameEngine.*;
import Game.exceptions.mazeloader.NullMazeException;
import Game.exceptions.maze.DivisionNotFoundException;
import Game.exceptions.maze.NullDivisionException;
import Game.exceptions.replays.NullReplayEventException;
import Game.gameCore.gameDesign.GameUtilPrint;
import Game.gameCore.gameDesign.GameUtilInput;
import Game.replays.*;
import customCollections.ExtendedArrayUnorderedList;

import java.util.Iterator;
import java.util.Random;


/**
 * Core implementation of the {@link IGameEngine} for the maze game.
 * <p>
 * The {@code GameEngine} manages:
 * players and their states,
 * the active maze and movement through its divisions,
 * turn management (via {@link ITurnManager}),
 * mini-game execution (lever and enigma divisions),
 * corridor events,
 * and replay recording.
 * The game loop is driven by the {@link #start()} method, which delegates
 * to the private {@link #play()} routine.
 * </p>
 */
public class GameEngine implements IGameEngine {

    /** List of player states participating in the game. */
    private ArrayUnorderedList<IPlayerState> players;
    /** Number of players in the current game. */
    private int numberOfPlayers;
    /** Turn manager responsible for selecting which player acts next. */
    private final ITurnManager turnManager = new TurnManager();
    /** The maze (labyrinth) used for the current game session. */
    private IMaze maze;
    /** Flag indicating whether the game loop is currently running. */
    private boolean isGameRunning;
    /** Replay that records movements, events and mini-games for the current game. */
    private Replay replay;

    /**
     * Creates a new {@code GameEngine} for the given maze and players.
     * <p>
     * The constructor converts {@link IPlayer} instances into {@link IPlayerState}
     * objects and initializes the replay recorder.
     * </p>
     *
     * @param maze the maze to be used in the game
     * @param players the list of players that will participate
     */
    public GameEngine(IMaze maze, ArrayUnorderedList<IPlayer> players) {
        this.maze = maze;
        this.isGameRunning = false;
        this.metamorphPlayer(players);
        this.numberOfPlayers = players.size();
        this.replay = new Replay();
    }

    /**
     * Converts the list of {@link IPlayer} into a list of {@link IPlayerState}
     * to be managed by the engine.
     *
     * @param players list of player metadata
     */
    private void metamorphPlayer (ArrayUnorderedList<IPlayer> players) {
        ArrayUnorderedList<IPlayerState> playersState = new ArrayUnorderedList<>();
        Iterator<IPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            IPlayer player = iterator.next();
            playersState.addToRear(new PlayerState(player));
        }
        this.players = playersState;
    }

    /**
     * Sets the maze to be used by this game engine.
     * <p>
     * This method can only be called while the game is not running. If the
     * provided maze is {@code null}, a {@link NullMazeException} is thrown.
     * If the game has already started, a {@link GameStartedException} is thrown
     * to prevent changing the maze mid-game.
     * </p>
     *
     * @param maze the {@link IMaze} instance to associate with this engine
     * @throws NullMazeException if {@code maze} is {@code null}
     * @throws GameStartedException if the game is already running
     */
    @Override
    public void setMaze(IMaze maze) throws NullMazeException, GameStartedException {
        if(maze == null) {
            throw new NullMazeException();
        }

        if(this.isGameRunning) {
            throw new GameStartedException();
        }
        this.maze = maze;
    }

    /**
     * Starts the game loop.
     * <p>
     * Validates that the game has not started and that there is at least one
     * player, loads players into the turn manager and enters the main
     * {@link #play()} loop.
     * </p>
     *
     * @throws GameStartedException if the game is already running
     * @throws NonePlayersException if no players are available
     */
    @Override
    public void start() throws GameStartedException, NonePlayersException {
        if (this.isGameRunning) {
            throw new GameStartedException();
        }

        if (this.players.isEmpty()) {
            throw new NonePlayersException();
        }

        turnManager.loadPlayers(this.players);
        this.isGameRunning = true;
        this.play();
    }

    /**
     * Checks whether the given player has met the winning condition.
     * <p>
     * The game ends when the player reaches the treasure room division.
     * If the player wins, the replay timestamp is updated and the game
     * is flagged as not running.
     * </p>
     *
     * @param player the player state to verify
     * @return {@code true} if the game should end because this player has won,
     *         {@code false} otherwise
     */
    @Override
    public boolean verifyEndGame(IPlayerState player) {
        if (!this.isGameRunning) {
            return false;
        }

        if (player == null) {
            return false;
        }

        if (this.isWinner(player)) {
            player.markAsWinner();
            replay.updateLocalTimestamp();
            this.isGameRunning = false;
            return true;
        }

        return false;
    }

    /**
     * Verifies whether the given player is positioned in the treasure room.
     *
     * @param player the player state to check
     * @return {@code true} if the player is in the treasure room, {@code false} otherwise
     */
    private boolean isWinner(IPlayerState player) {
        if (player.getMovementPlayer().getDivision().equals(this.maze.getTreasureRoom())) {
            return true;
        }

        return false;
    }

    /**
     * Applies end-game configuration, preparing the engine for a new game.
     * <p>
     * This includes resetting player histories and states, resetting the
     * replay recorder and clearing the turn manager.
     * </p>
     */
    @Override
    public void endGameConfig() {
        this.resetPlayersHistory();
        this.replay = new Replay();
        turnManager.reset();
    }

    /**
     * Resets the history and state of all players.
     * <p>
     * Delegates to {@link IPlayerState#reset()} for each player.
     * </p>
     */
    private void resetPlayersHistory() {
        for (IPlayerState player : this.players) {
            player.reset();
        }
    }

    /**
     * Returns the replay associated with the current or last game session.
     *
     * @return the {@link Replay} for this engine
     */
    @Override
    public Replay getReplay() {
        return replay;
    }

    /**
     * Returns the list of player states managed by this engine.
     *
     * @return the list of {@link IPlayerState} instances
     */
    @Override
    public ArrayUnorderedList<IPlayerState> getPlayers() {
        return players;
    }

    /**
     * Main game loop.
     * <p>
     * Chooses player entry points, then repeatedly:
     * asks the turn manager for the current player,
     * prints game state and player history,
     * executes either human or bot turn logic,
     * and checks for end-game condition.
     * </p>
     */
    private void play() {
        IPlayerState player;
        boolean done = false;

        this.chooseEntryPoint();

        while (!done) {
            player = this.turnManager.thisTurn();
            GameUtilPrint.printAllPlayersState(this.players.iterator());
            GameUtilPrint.printPlayerHistory(player.getMovementPlayer().getMovements().iterator(), this.maze);
            GameUtilPrint.printPlayerTurn(player);

            if (!player.getPlayer().isBot()) {
                this.playAsPlayer(player);
            } else {
                this.playAsBot(player);
            }

            if (this.verifyEndGame(player)) {
                done = true;
                GameUtilPrint.winMessage(player, replay);
            }
        }
    }

    /**
     * Lets each player choose an entry point in the maze.
     * <p>
     * players choose from the available entry points; bots receive a
     * random valid entry division.
     * </p>
     */
    private void chooseEntryPoint() {
        Iterator<IPlayerState> iterator = this.players.iterator();

        while (iterator.hasNext()) {
            IPlayerState player = iterator.next();

            GameUtilPrint.printSpaceLine();
            GameUtilPrint.printEntryPoints(this.maze.getEntryPoints());
            GameUtilPrint.printSpaceLine();
            GameUtilPrint.printPlayerTurn(player);

            int choice;
            IDivision division = null;
            do {

                if (player.getPlayer().isBot()) {
                    try {
                        Random random = new Random();
                        division = this.maze.getDivision(random.nextInt(Division.getNextID()));
                    } catch (DivisionNotFoundException e) {}
                } else {
                    choice = GameUtilInput.entryPointInput();
                    GameUtilPrint.printSpaceLine();
                    try {
                        division = this.maze.getDivision(choice);
                    } catch (DivisionNotFoundException e) {
                        GameUtilPrint.error();
                        GameUtilPrint.printException(e.getMessage());
                    }
                }

            } while (division == null || !division.isEntryPoint());

            player.getMovementPlayer().setCurrentDivision(division);
            GameUtilPrint.successfullyUpdatedEntryPoint();
        }
    }

    /**
     * Executes a full turn for a human player:
     * prints the current division,
     * prints neighbors,
     * reads the chosen division,
     * validates neighbor relation,
     * handles corridor event and mini-game.
     *
     * @param player the player whose turn is being executed
     */
    private void playAsPlayer(IPlayerState player) {
        boolean catched;
        int option;
        IDivision divisionChosen = null;
        IDivision currentDivision = null;
        boolean neighbours;

        do {
            catched = false;

            try {
                currentDivision = player.getMovementPlayer().getDivision();
                GameUtilPrint.printCurrentDivision(currentDivision);
                GameUtilPrint.printPossibleDivisionsForPlayer(this.maze.getNeighbors(currentDivision));
                option = GameUtilInput.nextDivisionInput();
                divisionChosen = this.maze.getDivision(option);

            } catch (DivisionNotFoundException e) {
                GameUtilPrint.error();
                GameUtilPrint.printException(e.getMessage());
                catched = true;
            }

            neighbours = this.maze.areNeighbours(currentDivision, divisionChosen);
            if (!neighbours) {
                GameUtilPrint.areNotNeighbours();
            }
        } while (catched || !neighbours);

        this.handleEvent(currentDivision, divisionChosen, player);
        if (currentDivision.equals(player.getMovementPlayer().getDivision())) {
            this.handleMiniGame(currentDivision, divisionChosen, player);
        }
    }

    /**
     * Executes a full turn for a bot player.
     * <p>
     * The bot chooses its target division based on the shortest path to the
     * treasure room, then follows the same event and mini-game flow as a normal player.
     * </p>
     *
     * @param bot the bot player state
     */
    private void playAsBot(IPlayerState bot) {

        IDivision currentDivision = bot.getMovementPlayer().getDivision();
        IDivision targetDivision = findDivisionShortestPath(currentDivision, this.maze.getTreasureRoom());

        this.handleEvent(currentDivision, targetDivision, bot);
        if (currentDivision.equals(bot.getMovementPlayer().getDivision())) {
            this.handleMiniGame(currentDivision, targetDivision, bot);
        }
    }

    /**
     * Returns the next division along the shortest path between two divisions.
     *
     * @param firstDivision starting division
     * @param lastDivision target division
     * @return the next division in the shortest path, or {@code null} if none
     */
    private IDivision findDivisionShortestPath(IDivision firstDivision, IDivision lastDivision) {
        Iterator<IDivision> iterator = this.maze.getShortestPathIterator(firstDivision, lastDivision);
        iterator.next();

        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Handles the mini-game logic when moving from a division to a target division.
     * <p>
     * If the target has a lever game that is already solved, the player moves
     * immediately. Otherwise, the mini-game is checked and played if needed.
     * Movements and mini-game events are recorded in the replay.
     * </p>
     *
     * @param currentDivision the current division of the player
     * @param targetDivision the target division the player wants to enter
     * @param player the player state
     */
    private void handleMiniGame(IDivision currentDivision, IDivision targetDivision, IPlayerState player) {
        IMiniGame miniGame = targetDivision.getMiniGame();
        if (this.isLeverGame(miniGame)) {
            if (miniGame.isSolved()) {
                player.getMovementPlayer().setCurrentDivision(targetDivision);
                try {
                    replay.add(new Movement(player.getPlayer(), currentDivision, targetDivision));
                } catch (NullReplayEventException e) {}
                return;
            }
        }

        this.checkDivision(currentDivision, targetDivision, player);
    }

    /**
     * Executes the mini-game for the chosen division, or moves directly if none exists.
     * <p>
     * On success, the movement and a mini-game replay event are recorded.
     * For non-lever mini-games, the solved flag is reset after a successful move.
     * </p>
     *
     * @param currentDivision the current division before moving
     * @param divisionChosen the division the player intends to enter
     * @param player the player state
     */
    private void checkDivision(IDivision currentDivision, IDivision divisionChosen, IPlayerState player) {
        IMiniGame miniGame = divisionChosen.getMiniGame();
        try {
            divisionChosen.startMiniGame(player);
            if (miniGame.isSolved()) {
                player.getMovementPlayer().setCurrentDivision(divisionChosen);

                try {
                    replay.add(new Movement(player.getPlayer(), currentDivision, divisionChosen));
                    replay.add(new MiniGameEvent(player.getPlayer(), divisionChosen));
                } catch (NullReplayEventException e) {}

                if (!this.isLeverGame(miniGame)) {
                    miniGame.setSolved(false);
                }
            }

        } catch (NullMiniGameException e) {
            player.getMovementPlayer().setCurrentDivision(divisionChosen);

            try {
                replay.add(new Movement(player.getPlayer(), currentDivision, divisionChosen));
            } catch (NullReplayEventException exc) {}

        }
    }

    /**
     * Checks whether the given mini-game is a lever-based game.
     *
     * @param miniGame the mini-game to test
     * @return {@code true} if the mini-game is an instance of {@link ILeverGame}, {@code false} otherwise
     */
    private boolean isLeverGame(IMiniGame miniGame) {
        if (miniGame == null) {
            return false;
        }
        if (miniGame instanceof ILeverGame) {
            return true;
        }

        return false;
    }

    /**
     * Handles the corridor event when moving between two divisions.
     * <p>
     * Retrieves the corridor from the maze and delegates to
     * {@link #verifyCorridorEvent(ICorridor, IPlayerState)}.
     * </p>
     *
     * @param currentDivision the current division
     * @param targetDivision  the target division
     * @param player          the player state
     */
    private void handleEvent(IDivision currentDivision, IDivision targetDivision, IPlayerState player) {
        try {
            this.verifyCorridorEvent(this.maze.getCorridor(currentDivision, targetDivision), player);
        } catch (NullDivisionException e) {}
    }

    /**
     * Verifies and executes any event generated by the given corridor.
     * <p>
     * If an event is generated, it is executed, printed to the console and
     * recorded as a replay event.
     * </p>
     *
     * @param corridor the corridor being traversed
     * @param player   the player state affected by the event
     */
    private void verifyCorridorEvent(ICorridor corridor, IPlayerState player) {
        IEventStrategy event = corridor.generateEvent();
        if (event != null) {
            event.execute(player, this);
            GameUtilPrint.printEventDescription(event.toString());

            try {
                replay.add(new CorridorEvent(player.getPlayer(), event.getDescription(), corridor));
            } catch (NullReplayEventException exc) {}
        }
    }

    /**
     * Returns a list of valid player targets for an action, excluding
     * the provided player.
     *
     * @param player the player initiating the action
     * @return a list of all other players
     */
    public ExtendedArrayUnorderedList<IPlayerState> getValidPlayerTargets(IPlayerState player) {
        ExtendedArrayUnorderedList<IPlayerState> validTargets = new ExtendedArrayUnorderedList<>();
        Iterator<IPlayerState> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            IPlayerState currentPlayer = iterator.next();
            if (!currentPlayer.equals(player)) {
                validTargets.addToRear(currentPlayer);
            }
        }

        return validTargets;
    }

}

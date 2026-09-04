package Game.entities.corridor.events;

import Collections.list.ArrayUnorderedList;
import Game.api.division.IDivision;
import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;
import Game.gameCore.gameDesign.GameUtilPrint;

import java.util.Random;


/**
 * Event that shuffles the positions of all players in the maze.
 * <p>
 * When executed, all players' current divisions are collected, shuffled
 * randomly and then reassigned so that everyone swaps positions.
 * </p>
 */
public class ShufflePositionsEvent extends Event {

    private static final String DEFAULT_MESSAGE = "all players swapped their positions!";

    /**
     * Creates a {@code ShufflePositionsEvent} with the default description.
     */
    public ShufflePositionsEvent() {
        this.setDescription(DEFAULT_MESSAGE);
    }

    /**
     * Executes the event by randomly reassigning all players' positions
     * and updating the event description.
     *
     * @param statePlayer the player that triggered the event
     * @param engine the game engine used to access all players
     */
    @Override
    public void execute(IPlayerState statePlayer, IGameEngine engine) {
        this.setPlayer(statePlayer);
        Random rand = new Random();
        ArrayUnorderedList<IDivision> divisions = new ArrayUnorderedList<>();
        ArrayUnorderedList<IPlayerState> players = engine.getPlayers();

        GameUtilPrint.printEventGenerated();

        for (IPlayerState player : players) {
            divisions.addToRear(player.getMovementPlayer().getDivision());
        }

        int size = divisions.size();
        IDivision[] temp = new IDivision[size];
        int index = 0;

        for (IDivision div : divisions) {
            temp[index++] = div;
        }

        for (int i = 0; i < size; i++) {
            int j = rand.nextInt(size);
            IDivision aux = temp[i];
            temp[i] = temp[j];
            temp[j] = aux;
        }

        index = 0;
        for (IPlayerState player : players) {
            player.getMovementPlayer().clearHistory();
            player.getMovementPlayer().setCurrentDivision(temp[index++]);
        }
        this.setDescription(DEFAULT_MESSAGE);
    }

}

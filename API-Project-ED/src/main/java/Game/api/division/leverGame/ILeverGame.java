package Game.api.division.leverGame;

import Game.api.division.IMiniGame;
import customCollections.ExtendedArrayUnorderedList;


/**
 * Contract for mini-games based on levers inside a division.
 * <p>
 * A lever game manages {@link ILever} instances and defines
 * how pushing them contributes to solving the mini-game.
 * </p>
 */
public interface ILeverGame extends IMiniGame {

    /**
     * Adds a lever to this lever mini-game.
     *
     * @param lever the {@link ILever} to include in the game
     */
    void addLever(ILever lever);

    /**
     * Returns the list of levers in this mini-game.
     *
     * @return an {@link ExtendedArrayUnorderedList} containing all levers
     */
    ExtendedArrayUnorderedList<ILever> getLevers();
}

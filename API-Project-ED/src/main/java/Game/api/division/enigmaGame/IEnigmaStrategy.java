package Game.api.division.enigmaGame;

import Game.exceptions.division.NonEnigmaAvailableException;
import Game.exceptions.division.NullEnigmaException;


/**
 * Strategy contract for managing enigmas in enigma mini-games.
 * <p>
 * Implementations are responsible for storing one or more {@link IEnigma}
 * instances and providing an enigma when requested.
 * </p>
 */
public interface IEnigmaStrategy {

    /**
     * Returns an enigma to be used in the mini-game.
     * <p>
     * The selection logic is defined by the implementing strategy.
     * </p>
     *
     * @return an {@link IEnigma} instance
     * @throws NonEnigmaAvailableException if no enigma is available to be returned
     */
    IEnigma getEnigma() throws NonEnigmaAvailableException;

    /**
     * Adds a new enigma to the strategy.
     *
     * @param enigma the enigma to add
     * @throws NullEnigmaException if the provided enigma is {@code null}
     */
    void addEnigma(IEnigma enigma) throws NullEnigmaException;
}


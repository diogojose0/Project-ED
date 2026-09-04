package Game.api.division.enigmaGame;

import Game.api.division.IMiniGame;


/**
 * Specialized mini-game interface for enigma-based challenges.
 * <p>
 * {@code IEnigmaGame} represents mini-games where the player must solve
 * a question or riddle to progress. It does not add new methods beyond
 * {@link IMiniGame}, but allows the game engine and divisions to
 * distinguish enigma mini-games from other types.
 * </p>
 */
public interface IEnigmaGame extends IMiniGame {
}

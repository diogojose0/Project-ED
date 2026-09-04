package Game.gameCore;


import Game.api.engine.IGameUI;
import Game.gameCore.gameDesign.GameUI;


/**
 * The Play class is the entry point of the game application.
 * It initializes and sets up the game user interface.
 */
public class Play {

    /**
     * The main method that starts the game application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        IGameUI menu = new GameUI();
        menu.setup();
    }

}

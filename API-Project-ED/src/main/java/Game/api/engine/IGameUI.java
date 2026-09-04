package Game.api.engine;


/**
 * Contract for the game user interface controller.
 * <p>
 * The UI entry point displays the main menu, allows starting games, loading mazes and accessing replays.
 * </p>
 */
public interface IGameUI {

    /**
     * Sets up and initializes the game user interface.
     * <p>
     * This method is responsible for preparing the UI components,
     * loading necessary resources, and displaying the main menu.
     * </p>
     */
    public void setup();
}


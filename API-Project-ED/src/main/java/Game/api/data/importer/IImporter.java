package Game.api.data.importer;

import Game.api.maze.IMazeLoader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Contract for components that import maze data from JSON files.
 * <p>
 * Implementations of this interface read JSON resources from the given paths
 * and use an {@link IMazeLoader} to build the in-memory maze and its enigmas.
 * </p>
 */
public interface IImporter {

    /**
     * Imports maze and enigma data from JSON files.
     * <p>
     * The method reads the JSON at {@code mazePath} and {@code enigmaPath},
     * parses their contents and delegates the creation of the maze structure
     * to the provided {@link IMazeLoader}.
     * </p>
     *
     * @param mazeLoader loader responsible for creating the maze from parsed data
     * @param mazePath path to the maze JSON file
     * @param enigmaPath path to the enigmas JSON file
     * @throws IOException if an I/O error occurs while reading the files
     * @throws ParseException if the JSON content cannot be parsed
     */
    void importJson(IMazeLoader mazeLoader, Path mazePath, Path enigmaPath)
            throws IOException, ParseException;
}

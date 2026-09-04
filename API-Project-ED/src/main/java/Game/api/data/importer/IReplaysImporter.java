package Game.api.data.importer;

import Game.api.replays.IReplaysManagement;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Contract for components that import game replays from JSON.
 * <p>
 * Implementations of this interface read a JSON source and populate
 * an {@link IReplaysManagement} instance with the recovered replays.
 * </p>
 */
public interface IReplaysImporter {

    /**
     * Imports replays from a JSON source into the given replay manager.
     * <p>
     * The concrete JSON location (file, stream, etc.) is defined by the implementing class.
     * </p>
     *
     * @param replaysManagement the replay manager to populate with imported replays
     * @throws IOException if an I/O error occurs during reading
     * @throws ParseException if the JSON content cannot be parsed
     */
    void importReplaysJson(IReplaysManagement replaysManagement, Path path) throws IOException, ParseException;
}


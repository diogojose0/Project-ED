package Game.data.importer;

import Game.api.data.importer.IReplaysImporter;
import Game.api.player.IPlayer;
import Game.api.replays.IReplay;
import Game.api.replays.IReplayEvent;
import Game.api.replays.IReplaysManagement;
import Game.entities.player.Player;
import Game.exceptions.replays.NullReplayEventException;
import Game.exceptions.replays.NullReplayException;
import Game.replays.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;


/**
 * {@code ReplaysImporter} is responsible for loading saved game replays
 * from a JSON file and reconstructing their in-memory representation.
 * <p>
 * It rebuilds {@link IReplay} instances and their associated events
 * </p>
 */
public class ReplaysImporter implements IReplaysImporter {

    /**
     * Imports all replays from the default JSON file into the given manager.
     * <p>
     * If the file does not exist, an {@link IOException} is thrown indicating
     * that there are no previously saved replays.
     * </p>
     *
     * @param replaysManagement the manager that will receive the imported replays
     * @throws IOException if the replays file does not exist or cannot be read
     * @throws ParseException if the JSON content cannot be parsed
     */
    @Override
    public void importReplaysJson(IReplaysManagement replaysManagement, Path path) throws IOException, ParseException {
        File file = path.toFile();

        if (!file.exists()) {
            throw new IOException("No previously saved game replays!");
        }

        JSONParser parser = new JSONParser();
        JSONArray replaysArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object replayObj : replaysArray) {
            IReplay replay = this.importReplayJson((JSONObject) replayObj);
            try {
                replaysManagement.add(replay);
            } catch (NullReplayException e) {}
        }
    }

    /**
     * Reconstructs a {@link IReplay} instance from its JSON representation.
     * <p>
     * The method restores the replay data and all its events.
     * </p>
     *
     * @param replayJson JSON object describing the replay
     * @return a new {@link Replay} instance
     */
    private IReplay importReplayJson(JSONObject replayJson) {
        IReplay replay = new Replay(
                (String) replayJson.get("timestamp"),
                (String) replayJson.get("winMoment")
        );

        JSONArray replayEventsArray = (JSONArray) replayJson.get("history");
        for (Object replayEventObj : replayEventsArray) {
            JSONObject replayEventJson = (JSONObject) replayEventObj;
            IReplayEvent replayEvent = this.importReplayEventJson(replayEventJson);
            try {
                replay.add(replayEvent);
            } catch (NullReplayEventException e) {}
        }

        return replay;
    }

    /**
     * Reconstructs a specific replay event from its JSON representation.
     * <p>
     * The type of event is inferred from the JSON keys:
     * corridor event, mini-game event or movement.
     * </p>
     *
     * @param replayEventJson JSON object describing the replay event
     * @return a concrete {@link IReplayEvent} instance, or {@code null} if type is unknown
     */
    private IReplayEvent importReplayEventJson(JSONObject replayEventJson) {
        Importer importer = new Importer();
        JSONObject playerJson = (JSONObject) replayEventJson.get("player");
        IPlayer player = this.importPlayerJson(playerJson);

        if(replayEventJson.containsKey("corridor") && replayEventJson.containsKey("event")) {
            CorridorEvent corridorEvent = new CorridorEvent(player);
            corridorEvent.setCorridor(importer.importCorridorJson((JSONObject) replayEventJson.get("corridor")));
            corridorEvent.setStringEvent((String) replayEventJson.get("event"));
            return corridorEvent;
        }

        if(replayEventJson.containsKey("division")) {
            MiniGameEvent miniGameEvent = new MiniGameEvent(player);
            miniGameEvent.setDivisionOfMiniGame(importer.importDivisionJson((JSONObject) replayEventJson.get("division")));
            return miniGameEvent;
        }

        if(replayEventJson.containsKey("fromDivision") && replayEventJson.containsKey("toDivision")) {
            Movement movement = new Movement(player);
            movement.setFromDivision(importer.importDivisionJson((JSONObject) replayEventJson.get("fromDivision")));
            movement.setToDivision(importer.importDivisionJson((JSONObject) replayEventJson.get("toDivision")));
            return movement;
        }
        return null;
    }

    /**
     * Reconstructs a player from its JSON representation.
     *
     * @param playerJson JSON object describing the player
     * @return a new {@link Player} instance
     */
    private IPlayer importPlayerJson(JSONObject playerJson) {
        return new Player(
                (String) playerJson.get("name"),
                (Boolean) playerJson.get("isBot"),
                ((Long) playerJson.get("id")).intValue()
        );
    }

}

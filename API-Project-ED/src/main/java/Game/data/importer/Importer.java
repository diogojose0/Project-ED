package Game.data.importer;

import Collections.list.ArrayUnorderedList;
import Game.api.corridor.ICorridor;
import Game.api.data.importer.IImporter;
import Game.api.division.IDivision;
import Game.api.division.enigmaGame.IEnigma;
import Game.api.division.enigmaGame.IEnigmaStrategy;
import Game.api.division.leverGame.ILever;
import Game.api.division.leverGame.ILeverGame;
import Game.api.maze.IMaze;
import Game.api.maze.IMazeLoader;
import Game.entities.corridor.Corridor;
import Game.entities.division.Division;
import Game.entities.division.games.enigmaGame.Enigma;
import Game.entities.division.games.enigmaGame.EnigmaGame;
import Game.entities.division.games.enigmaGame.EnigmaStrategy;
import Game.entities.division.games.leverGame.Lever;
import Game.entities.division.games.leverGame.LeverGame;
import Game.entities.maze.Maze;
import Game.exceptions.division.NotCorrespondAnAnswerException;
import Game.exceptions.division.NullEnigmaException;
import Game.exceptions.maze.*;
import Game.exceptions.mazeloader.MazeAlreadyExistsException;
import Game.exceptions.mazeloader.NullMazeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;


/**
 * {@code Importer} is responsible for importing maze and enigma data
 * from JSON files and building the corresponding in-memory structures.
 * <p>
 * It reads maze definitions, constructs {@link IMaze} instances with their
 * divisions and corridors, and loads enigmas into an {@link IEnigmaStrategy}.
 * </p>
 */
public class Importer implements IImporter {

    /**
     * Imports maze and enigma definitions from JSON files.
     * <p>
     * The method parses the given maze JSON and adds each
     * created maze to the provided {@link IMazeLoader}. It also parses the
     * enigma JSON and populates an {@link IEnigmaStrategy} with the loaded
     * enigmas.
     * </p>
     *
     * @param mazeLoader loader used to register the imported mazes
     * @param mazePath path to the maze JSON file
     * @param enigmaPath path to the enigma JSON file
     * @throws IOException if an I/O error occurs while reading the files
     * @throws ParseException if the JSON content cannot be parsed
     */
    @Override
    public void importJson(IMazeLoader mazeLoader, Path mazePath, Path enigmaPath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray mazesArray = (JSONArray) parser.parse(new FileReader(mazePath.toFile()));
        JSONArray enigmasArray = (JSONArray) parser.parse(new FileReader(enigmaPath.toFile()));

        for (Object mazeObj : mazesArray) {
            IMaze maze = this.importMazeJson((JSONObject) mazeObj);
            try {
                mazeLoader.add(maze);
            } catch (MazeAlreadyExistsException | NullMazeException e) {}
        }

        IEnigmaStrategy enigmaStrategy = new EnigmaStrategy();
        for (Object enigmaObj : enigmasArray) {
            IEnigma enigma = this.importEnigmaJson((JSONObject)enigmaObj);
            try {
                enigmaStrategy.addEnigma(enigma);
            } catch (NullEnigmaException e) {}
        }
    }

    /**
     * Creates an {@link IEnigma} instance from its JSON representation.
     *
     * @param enigmaJson JSON object describing the enigma
     * @return a new {@link Enigma} instance or {@code null} if the data is invalid
     */
    private IEnigma importEnigmaJson(JSONObject enigmaJson) {

        ArrayUnorderedList<String> enigmaAnswers = new ArrayUnorderedList<>();

        JSONArray answers = (JSONArray) enigmaJson.get("answers");

        for (Object answerObj : answers) {
            String answer = (String) answerObj;
            enigmaAnswers.addToRear(answer);
        }

        try {
            return new Enigma(
                    (String) enigmaJson.get("enigma"),
                    ((Long) enigmaJson.get("answer")).intValue(),
                    enigmaAnswers
            );
        } catch (NotCorrespondAnAnswerException e) {
            return null;
        }
    }

    /**
     * Creates an {@link IMaze} instance from its JSON definition.
     * <p>
     * Divisions and corridors are imported and attached to the maze.
     * </p>
     *
     * @param mazeJson JSON object describing the maze
     * @return a fully constructed {@link IMaze}
     */
    private IMaze importMazeJson(JSONObject mazeJson) {
        IMaze maze = new Maze((String) mazeJson.get("maze_name"));

        JSONArray divisionsArray = (JSONArray) mazeJson.get("divisions");
        for (Object divisionObj : divisionsArray) {
            JSONObject divisionJson = (JSONObject) divisionObj;
            IDivision division = this.importDivisionJson(divisionJson);
            try {
                maze.addDivision(division);
            } catch (NullDivisionException | AlreadyHaveTreasureRoomException | DivisionAlreadyExistsException e) {
            }
        }

        JSONArray corridorsArray = (JSONArray) mazeJson.get("corridors");
        for (Object corridorObj : corridorsArray) {
            JSONObject corridorJson = (JSONObject) corridorObj;
            ICorridor corridor = this.importCorridorJson(corridorJson);
            try {
                maze.addCorridor(maze.getDivision((String)corridorJson.get("from")), maze.getDivision((String)corridorJson.get("to")), corridor);
            } catch (DivisionNotFoundException | NullDivisionException | NullCorridorException | EqualDivisionException e) {
            }
        }

        return maze;
    }

    /**
     * Creates a division from its JSON representation and configures any associated mini-games.
     *
     * @param divisionJson JSON object describing the division
     * @return a new {@link IDivision} instance
     */
    protected IDivision importDivisionJson(JSONObject divisionJson) {
        IDivision division;

        if(divisionJson.containsKey("id")) {
            division = new Division (
                    (String) divisionJson.get("name"),
                    (Boolean) divisionJson.get("hasTreasure"),
                    (Boolean) divisionJson.get("isEntryPoint"),
                    ((Long) divisionJson.get("id")).intValue()
            );
        } else {
            division = new Division (
                    (String) divisionJson.get("name"),
                    (Boolean) divisionJson.get("hasTreasure"),
                    (Boolean) divisionJson.get("isEntryPoint")
            );
        }

        if(divisionJson.containsKey("levers")) {
            this.importLeversJson(division, divisionJson);
            return division;
        }

        if(this.willHaveMiniGameDivision(division)) {
            division.setMiniGame(new EnigmaGame());
        }

        return division;
    }

    /**
     * Imports the lever game configuration for a division.
     *
     * @param division the division to attach the lever game to
     * @param divisionJson JSON object containing the lever definitions
     */
    private void importLeversJson(IDivision division, JSONObject divisionJson) {
        ILeverGame miniGame = new LeverGame();
        JSONArray leversArray = (JSONArray) divisionJson.get("levers");
        for (Object leverObj : leversArray) {
            JSONObject leverJson = (JSONObject) leverObj;
            ILever lever = this.importLeverJson(leverJson, division);
            miniGame.addLever(lever);
        }
        division.setMiniGame(miniGame);
    }

    /**
     * Indicates whether a division should automatically receive an enigma mini-game.
     * <p>
     * Entry points and treasure rooms do not receive enigmas.
     * </p>
     *
     * @param division the division to check
     * @return {@code true} if the division should have a mini-game, {@code false} otherwise
     */
    private boolean willHaveMiniGameDivision(IDivision division) {
        if (division.isTreasureRoom() || division.isEntryPoint()) {
            return false;
        }
        return true;
    }

    /**
     * Creates a lever instance from its JSON definition.
     *
     * @param leverJson JSON object describing the lever
     * @param division the division that owns the lever
     * @return a new {@link ILever}
     */
    private ILever importLeverJson(JSONObject leverJson, IDivision division) {
        if(leverJson.get("target").equals("right")) {
            return new Lever(division);
        } else {
            return new Lever(null);
        }
    }

    /**
     * Creates a corridor from its JSON representation.
     *
     * @param corridorJson JSON object describing the corridor
     * @return a new {@link ICorridor}
     */
    protected ICorridor importCorridorJson(JSONObject corridorJson) {
        return new Corridor((String) corridorJson.get("name"));
    }

}

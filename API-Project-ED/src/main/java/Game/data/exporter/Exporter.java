package Game.data.exporter;

import Game.api.data.JsonExportable;
import Game.api.data.exporter.IExporter;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;


public class Exporter implements IExporter {

    /**
     * Exports the given loader object to a JSON file at the specified path.
     *
     * @param loader the object to be exported, must implement JsonExportable
     * @param path file path where the JSON will be saved
     * @param <T> the type of the loader object
     * @throws IOException if an I/O error occurs during export
     */
    @Override
    public <T extends JsonExportable> void exportJson(T loader, Path path) throws IOException {
        JSONArray loaderJson = loader.toJson();

        File file = path.toFile();
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(loaderJson.toJSONString());
            writer.flush();
        }

    }

}

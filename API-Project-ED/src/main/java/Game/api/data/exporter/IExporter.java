package Game.api.data.exporter;

import Game.api.data.JsonExportable;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Defines a contract for components responsible for exporting domain objects
 * to a JSON representation.
 * <p>
 * Implementations of this interface decide how the JSON is generated and
 * where it is written. The objects to be exported must implement {@link JsonExportable}
 * so they can be converted to a JSON structure.
 * </p>
 */
public interface IExporter {

    /**
     * Exports the given loader object to a JSON file at the specified path.
     *
     * @param loader the object to be exported, must implement JsonExportable
     * @param path file path where the JSON will be saved
     * @param <T> the type of the loader object
     * @throws IOException if an I/O error occurs during export
     */
    <T extends JsonExportable> void exportJson(T loader, Path path) throws IOException;
}

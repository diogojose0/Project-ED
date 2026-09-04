package Game.api.data;

import org.json.simple.JSONArray;


/**
 * Defines a contract for objects that can be exported to JSON.
 * <p>
 * Any class that implements this interface must provide a JSON representation
 * of its internal state through the {@link #toJson()} method. The returned
 * representation is a {@link JSONArray}, which can be used by exporters to
 * persist data to files.
 * </p>
 */
public interface JsonExportable {

    /**
     * Converts the implementing object to a JSON array representation.
     *
     * @return a JSONArray representing the object
     */
    JSONArray toJson();
}

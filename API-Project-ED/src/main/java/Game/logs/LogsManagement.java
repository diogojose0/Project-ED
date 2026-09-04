package Game.logs;

import Collections.list.ArrayUnorderedList;

import java.io.FileWriter;
import java.io.IOException;


/**
 * The LogManagement class provides methods to manage logs and save/write them in a file txt.
 */
public class LogsManagement {
    /**
     * The file path to save the logs.
     */
    private static final String LOG_FILE = "files/logs.txt";
    /**
     * The ArrayUnorderedList of logs.
     */
    private ArrayUnorderedList<Log> logs; //

    /**
     * Constructor for the log Management.
     */
    public LogsManagement() {
        logs = new ArrayUnorderedList<>();
    }

    /**
     * Adds a log to the log's array.
     * @param log the log to be added.
     */
    public void addLog(Log log) {
        logs.addToRear(log);
        writeLog(log);
    }

    /**
     * Make a string with all the logs stored in the array.
     * @return a string representation of the logs.
     */
    public String listLogs() {
        StringBuilder sb = new StringBuilder();
        for (Log log : this.logs) {
            if(log != null) {
                sb.append(log);
            }
        }

        return sb.toString();
    }

    /**
     * Writes a log in the log's file.
     * @param log the log to be written.
     */
    private void writeLog(Log log) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.err.println("Error saving log file: " + e.getMessage());
        }
    }

}
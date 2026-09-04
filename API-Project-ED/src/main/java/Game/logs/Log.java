package Game.logs;

import java.time.LocalDateTime;


/**
 * The Log class represents a log that stores a message of an exception and the time.
 */
public class Log {
    /**
     * The message of the exception.
     */
    private String logMsg;

    /**
     * Constructor for the log.
     * @param logMessage the message of the exception.
     */
    public Log(String logMessage) {
        this.logMsg = logMessage;
    }

    /**
     * toString method for the log.
     * @return a string representation of the log.
     */
    @Override
    public String toString() {
        StringBuilder log = new StringBuilder();
        log.append(LocalDateTime.now()).append(" - ").append(this.logMsg).append("\n");
        return log.toString();
    }

    /**
     * Getter for the message of the exception.
     * @return the message of the exception.
     */
    public String getLogMessage() {
        return logMsg;
    }

}

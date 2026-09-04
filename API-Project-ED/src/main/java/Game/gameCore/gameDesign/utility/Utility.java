package Game.gameCore.gameDesign.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Utility class provides methods to help the user interact with the application.
 */
public abstract class Utility {
    /**
     * The static BufferedReader is used to read the user input from the console.
     */
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Gets an integer from the user input and checks if it is valid.
     * @param message the message that will be displayed in the output.
     * @return the user input as an integer.
     */
    public static int getintOption (String message) {
        boolean isValid = false;
        int option = 0;
        System.out.println(message);

        do {
            System.out.print("Option: ");

            try {
                String input = reader.readLine();
                if(input == null || input.isBlank()) {
                    System.out.println("Please, put a valid number!");
                    continue;
                }
                option = Integer.parseInt(input);
                isValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please, put a valid number!");
            } catch (IOException e) {}

        } while (!isValid);

        return option;
    }

    /**
     * Gets a string from the user input.
     * @param message - the message that will be displayed in the output.
     * @return the string entered by the user.
     */
    public static String getStringInput (String message) {
        String input = null;
        System.out.println(message);
        try {
            input = reader.readLine();
        } catch (IOException e) {}

        return input;
    }

}

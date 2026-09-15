package echo.ui;

import echo.exception.EchoException;

/**
 * Handles all interaction with the user: greeting, echoing command feedback,
 * and showing error messages.
 */
public class Ui {
    private final String name;

    /**
     * Instantiates {@link Ui} class with default parameters.
     */
    public Ui() {
        this.name = "Echo";
    }

    /**
     * Instantiates {@link Ui} class with a custom name.
     * @param name name of chatbot
     */
    public Ui(String name) {
        this.name = name;
    }

    /**
     * Sends the salutation message.
     */
    public String greet() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String greeting = "Signal acquired. " + name + " here, your feline mission controller.\n"
                + "What are we tackling today?";
        return joinLines(banner + "\n" + greeting);
    }

    /**
     * Sends the farewell message.
     */
    public String farewell() {
        String message = name + " going off-air. See you on the next mission!";
        return joinLines(message);
    }

    /**
     * Sends the input messages joined as a single string
     * @param lines lines of messages to be wrapped by the separator.
     */
    public String joinLines(String... lines) {
        return String.join("\n", lines);
    }

    /**
     * Shows the error message related to an {@link EchoException}.
     * @param e {@link EchoException} which error message to display
     */
    public String showError(EchoException e) {
        return joinLines(e.getMessage());
    }

}

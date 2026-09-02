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
        String salutation = "Hello! I'm " + name + ".\n" +
                "How can I help?";
        return echo(banner + "\n" + salutation);
    }

    /**
     * Sends the valediction message.
     */
    public String farewell() {
        String valediction = "Bye!";
        return echo(valediction);
    }

    /**
     * Sends the input message followed by a trailing newline.
     * @param input input message to be shown.
     */
    public String echo(String input) {
        return input + '\n';
    }

    /**
     * Shows the error message related to an {@link EchoException}.
     * @param e {@link EchoException} which error message to display
     */
    public String showError(EchoException e) {
        return echo(e.getMessage());
    }

}

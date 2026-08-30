package echo.ui;

import echo.exception.EchoException;

/**
 * Handles all interaction with the user: greeting, echoing command feedback,
 * and showing error messages, each framed by a separator line.
 */
public class Ui {
    private final String name;
    private final String separator;

    /**
     * Instantiates {@link Ui} class with default parameters.
     */
    public Ui() {
        this.name = "Echo";
        this.separator = "============================================================";
    }

    /**
     * Instantiates {@link Ui} class with custom parameters.
     * @param name name of chatbot
     * @param separator separator between messages
     */
    public Ui(String name, String separator) {
        this.name = name;
        this.separator = separator;
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
     * Sends the input message wrapped by separator defined in {@link Ui#Ui(String, String)}
     * or the default separator in the case the no-arg constructor {@link Ui#Ui()} is called.
     * @param input input message to be wrapped by the separator.
     */
    public String echo(String input) {
        return separator + '\n' + input + '\n' + separator + '\n';
    }

    /**
     * Shows the error message related to an {@link EchoException}.
     * @param e {@link EchoException} which error message to display
     */
    public String showError(EchoException e) {
        return echo(e.getMessage());
    }

}

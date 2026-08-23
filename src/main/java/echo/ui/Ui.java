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
    public void greet() {
        String banner = " _____     _           \n"
                + "| ____|___| |__   ___  \n"
                + "|  _| / __| '_ \\ / _ \\ \n"
                + "| |__| (__| | | | (_) |\n"
                + "|_____\\___|_| |_|\\___/ \n";
        String salutation = "Hello! I'm " + name + ".\n" +
                "How can I help?";
        echo(banner + "\n" + salutation);
    }

    /**
     * Sends the valediction message.
     */
    public void farewell() {
        String valediction = "Bye!";
        echo(valediction);
    }

    /**
     * Sends the input message wrapped by separator defined in {@link Ui#Ui(String, String)}
     * or the default separator in the case the no-arg constructor {@link Ui#Ui()} is called.
     * @param input input message to be wrapped by the separator.
     */
    public void echo(String input) {
        System.out.println(separator);
        System.out.println(input);
        System.out.println(separator);
    }

    /**
     * Shows the error message related to an {@link EchoException}.
     * @param e {@link EchoException} which error message to display
     */
    public void showError(EchoException e) {
        echo(e.getMessage());
    }

}

import java.util.Scanner;

public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";


    private static void greet() {
        String banner = " _____     _           \n"
                      + "| ____|___| |__   ___  \n"
                      + "|  _| / __| '_ \\ / _ \\ \n"
                      + "| |__| (__| | | | (_) |\n"
                      + "|_____\\___|_| |_|\\___/ \n";
        String salutation = "Hello! I'm " + NAME + ".\n" +
                          "How can I help?";
        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println(salutation);
        System.out.println(SEPARATOR);
    }

    private static void farewell() {
        String valediction = "Bye!";
        System.out.println(SEPARATOR);
        System.out.println(valediction);
        System.out.println(SEPARATOR);
    }

    private static void echo(String input) {
        System.out.println(SEPARATOR);
        System.out.println(input);
        System.out.println(SEPARATOR);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Echo.greet();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            Echo.echo(input);
        }
        Echo.farewell();
        scanner.close();
    }
}

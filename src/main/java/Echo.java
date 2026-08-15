import java.util.Scanner;

public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private static final String[] itemArray = new String[100];
    private static int lastIdx = 0;


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

    private static void add(String item) {
        if (lastIdx == itemArray.length) {
            Echo.echo("I can't remember any more! Sorry!");
            return;
        }
        itemArray[lastIdx++] = item;
        Echo.echo("added: " + item);
    }

    private static void list() {
        System.out.println(SEPARATOR);
        for (int i = 1; i <= lastIdx; ++i) {
            String entry = i + ". " + itemArray[i - 1];
            System.out.println(entry);
        }
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
            if (input.equals("list")) {
                Echo.list();
                continue;
            }
            Echo.add(input);
        }
        Echo.farewell();
        scanner.close();
    }
}

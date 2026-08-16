import java.util.Scanner;

public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private static final String[] items = new String[100];
    private static int itemCnt = 0;


    private static void greet() {
        String banner = " _____     _           \n"
                      + "| ____|___| |__   ___  \n"
                      + "|  _| / __| '_ \\ / _ \\ \n"
                      + "| |__| (__| | | | (_) |\n"
                      + "|_____\\___|_| |_|\\___/ \n";
        String salutation = "Hello! I'm " + NAME + ".\n" +
                          "How can I help?";
        Echo.echo(banner + "\n" + salutation);
    }

    private static void farewell() {
        String valediction = "Bye!";
        Echo.echo(valediction);
    }

    private static void echo(String input) {
        System.out.println(SEPARATOR);
        System.out.println(input);
        System.out.println(SEPARATOR);
    }

    private static void add(String item) {
        if (itemCnt == items.length) {
            Echo.echo("I can't remember any more! Sorry!");
            return;
        }
        items[itemCnt++] = item;
        Echo.echo("added: " + item);
    }

    private static void list() {
        StringBuilder listTxt = new StringBuilder();
        for (int i = 1; i <= itemCnt; ++i) {
            listTxt.append(i).append(". ").append(items[i - 1]);
            if (i != itemCnt) listTxt.append("\n");
        }
        Echo.echo(listTxt.toString());
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

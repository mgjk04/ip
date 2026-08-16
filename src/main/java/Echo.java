import java.util.Scanner;

/**
 * Comment by Codex:
 * Runs a simple command-line task list that can add, list, mark, and unmark tasks.
 */
public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private static final String[] items = new String[100];
    /** Comment by Codex: Stores whether the task at each corresponding {@code items} index is complete. */
    private static final boolean[] done = new boolean[100]; //By Codex
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
        // Modification by Codex:
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= itemCnt; ++i) {
            listTxt.append(i).append(".[").append(done[i - 1] ? "X" : " ")
                    .append("] ").append(items[i - 1]);
            if (i != itemCnt) listTxt.append("\n");
        }
        Echo.echo(listTxt.toString());
    }

    /**
     * Method created by Codex:
     * Marks the task at the given one-based task number as complete.
     *
     * @param taskNumberText text supplied after the {@code mark} command
     */
    private static void mark(String taskNumberText) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > itemCnt) {
                Echo.echo("That task number is not in the list.");
                return;
            }
            done[taskNumber - 1] = true;
            Echo.echo("Nice! I've marked this task as done:\n  [X] " + items[taskNumber - 1]);
        } catch (NumberFormatException e) {
            Echo.echo("Please provide a task number to mark.");
        }
    }

    /**
     * Method created by Codex:
     * Marks the task at the given one-based task number as not done.
     *
     * @param taskNumberText text supplied after the {@code unmark} command
     */
    private static void unmark(String taskNumberText) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > itemCnt) {
                Echo.echo("That task number is not in the list.");
                return;
            }
            done[taskNumber - 1] = false;
            Echo.echo("OK, I've marked this task as not done yet:\n  [ ] " + items[taskNumber - 1]);
        } catch (NumberFormatException e) {
            Echo.echo("Please provide a task number to unmark.");
        }
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
            // Created by Codex:
            if (input.equals("unmark") || input.startsWith("unmark ")) {
                Echo.unmark(input.substring(6).trim());
                continue;
            }
            // Created by Codex:
            if (input.equals("mark") || input.startsWith("mark ")) {
                Echo.mark(input.substring(4).trim());
                continue;
            }
            Echo.add(input);
        }
        Echo.farewell();
        scanner.close();
    }
}

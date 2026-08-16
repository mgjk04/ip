import java.util.Scanner;

/**
 * Comment by Codex:
 * Runs a simple command-line task list that can add, list, mark, and unmark tasks.
 */
public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private static final Task[] tasks = new Task[100]; //Modified by Codex
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

    private static void add(Task t) {
        // Method modified by Codex:
        if (itemCnt == tasks.length) {
            Echo.echo("I can't remember any more! Sorry!");
            return;
        }
        tasks[itemCnt++] = t;
        Echo.echo("Got it. I've add this task:\n" + t.toString() + "\nNow you have " + itemCnt + " tasks in the list.");
    }

    private static void list() {
        // Method modified by Codex:
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= itemCnt; ++i) {
            Task task = tasks[i - 1];
            listTxt.append(i).append(". ").append(task.toString());
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
            // Hunk modified by Codex:
            Task task = tasks[taskNumber - 1];
            task.markDone();
            Echo.echo("Nice! I've marked this task as done:\n" + task.toString());
        } catch (NumberFormatException e) {
            Echo.echo("Please provide a task number to mark.");
        }
    }

    /**
     * Method with some Codex contribution:
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
            // Hunk modified by Codex:
            Task task = tasks[taskNumber - 1];
            task.markUnDone();
            Echo.echo("OK, I've marked this task as not done yet:\n" + task.toString());
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
            // Code with Codex contribution:
            if (input.startsWith("unmark ")) {
                Echo.unmark(input.substring(6).trim());
                continue;
            }
            // Code with Codex contribution:
            if (input.startsWith("mark ")) {
                Echo.mark(input.substring(4).trim());
                continue;
            }
            if (input.startsWith("todo ")) {
                Task t = new Todo(input.substring(5).trim());
                Echo.add(t);
                continue;
            }
            Echo.add(new Task(input));
        }
        Echo.farewell();
        scanner.close();
    }
}

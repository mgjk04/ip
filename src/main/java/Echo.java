import java.util.ArrayList;
import java.util.Scanner;

public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private static final ArrayList<Task> tasks = new ArrayList<>();


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

    private static void add(Task t) throws EchoException {
        tasks.add(t);
        Echo.echo("Got it. I've added this task:\n" + t.toString()
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void list() {
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= tasks.size(); ++i) {
            Task task = tasks.get(i - 1);
            listTxt.append(i).append(".").append(task.toString());
            if (i != tasks.size()) listTxt.append("\n");
        }
        Echo.echo(listTxt.toString());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as complete.
     *
     * @param taskNumberText text supplied after the {@code mark} command
     */
    private static void mark(String taskNumberText) throws EchoException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new InvalidTaskNumberException();
            }
            Task task = tasks.get(taskNumber - 1);
            task.markDone();
            Echo.echo("Nice! I've marked this task as done:\n" + task.toString());
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as not done.
     *
     * @param taskNumberText text supplied after the {@code unmark} command
     */
    private static void unmark(String taskNumberText) throws EchoException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new InvalidTaskNumberException();
            }
            Task task = tasks.get(taskNumber - 1);
            task.markUnDone();
            Echo.echo("OK, I've marked this task as not done yet:\n" + task.toString());
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }

    private static void showError(EchoException e) {
        Echo.echo(e.getMessage());
    }

    /** Method created by Codex: Returns whether the input starts with a complete command keyword. */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Method with Codex contribution:
     * Processes one input line, returning whether the user requested the chatbot to exit.
     *
     * @param input command line entered by the user
     * @return true when Echo should stop accepting commands
     * @throws EchoException if the command or its arguments are invalid
     */
    private static boolean processCommand(String input) throws EchoException {
        String trimmedInput = input.trim();
        if (trimmedInput.equals("bye")) {
            return true;
        }
        if (trimmedInput.equals("list")) {
            Echo.list();
            return false;
        }
        if (isCommand(trimmedInput, "unmark")) {
            String taskNumber = trimmedInput.substring("unmark".length()).trim();
            Echo.unmark(taskNumber);
            return false;
        }
        if (isCommand(trimmedInput, "mark")) {
            String taskNumber = trimmedInput.substring("mark".length()).trim();
            Echo.mark(taskNumber);
            return false;
        }
        if (isCommand(trimmedInput, "todo")) {
            String description = trimmedInput.substring("todo".length()).trim();
            if (description.isEmpty()) { throw new TodoFormatException(); }
            Echo.add(new Todo(description));
            return false;
        }
        if (isCommand(trimmedInput, "deadline")) {
            String[] parts = trimmedInput.substring("deadline".length()).trim().split(" /by ", 2);
            if  (parts.length != 2) { throw new DeadlineFormatException(); }
            String description = parts[0].trim();
            String dueDate = parts[1].trim();
            if (description.isEmpty() || dueDate.isEmpty()) {
                throw new DeadlineFormatException();
            }
            Echo.add(new Deadline(description, dueDate));
            return false;
        }
        if (isCommand(trimmedInput, "event")) {
            String[] parts = trimmedInput.substring("event".length()).trim().split(" /from | /to ", 3);
            if  (parts.length != 3) { throw new EventFormatException(); }
            String description = parts[0].trim();
            String startTime = parts[1].trim();
            String endTime = parts[2].trim();
            if (description.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                throw new EventFormatException();
            }
            Echo.add(new Event(description, startTime, endTime));
            return false;
        }
        throw new UnknownCommandException();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Echo.greet();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                if (processCommand(input)) { // Modified by Codex
                    break;
                }
            } catch (EchoException exception) { // Modified by Codex
                showError(exception);
            }
        }
        Echo.farewell();
        scanner.close();
    }
}

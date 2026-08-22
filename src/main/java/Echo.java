import java.util.ArrayList;
import java.util.Scanner;

public class Echo {
    private static final String NAME = "Echo";
    private static final String SEPARATOR = "============================================================";
    private final ArrayList<Task> tasks = new ArrayList<>();

    private void greet() {
        String banner = " _____     _           \n"
                      + "| ____|___| |__   ___  \n"
                      + "|  _| / __| '_ \\ / _ \\ \n"
                      + "| |__| (__| | | | (_) |\n"
                      + "|_____\\___|_| |_|\\___/ \n";
        String salutation = "Hello! I'm " + NAME + ".\n" +
                          "How can I help?";
        echo(banner + "\n" + salutation);
    }

    private void farewell() {
        String valediction = "Bye!";
        echo(valediction);
    }

    private void echo(String input) {
        System.out.println(SEPARATOR);
        System.out.println(input);
        System.out.println(SEPARATOR);
    }

    private void add(Task t) throws EchoException {
        tasks.add(t);
        echo("Got it. I've added this task:\n" + t.toString()
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private void delete(int index) throws EchoException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task t = tasks.remove(index);
        echo("Noted. I've removed this task:\n" + t.toString() +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private void list() {
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= tasks.size(); ++i) {
            Task task = tasks.get(i - 1);
            listTxt.append(i).append(".").append(task.toString());
            if (i != tasks.size()) listTxt.append("\n");
        }
        echo(listTxt.toString());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as complete.
     *
     * @param taskNumberText text supplied after the {@code mark} command
     */
    private void mark(String taskNumberText) throws EchoException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new InvalidTaskNumberException();
            }
            Task task = tasks.get(taskNumber - 1);
            task.markDone();
            echo("Nice! I've marked this task as done:\n" + task.toString());
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
    private void unmark(String taskNumberText) throws EchoException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new InvalidTaskNumberException();
            }
            Task task = tasks.get(taskNumber - 1);
            task.markUnDone();
            echo("OK, I've marked this task as not done yet:\n" + task.toString());
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }

    private void showError(EchoException e) {
        echo(e.getMessage());
    }

    /**
     * Method with Codex contribution:
     * Processes one input line, returning whether the user requested the chatbot to exit.
     *
     * @param input command line entered by the user
     * @return true when Echo should stop accepting commands
     * @throws EchoException if the command or its arguments are invalid
     */
    private boolean processCommand(String input) throws EchoException {
        String trimmedInput = input.trim();
        CommandType cmd = CommandType.fromInput(trimmedInput);
        String args = trimmedInput.substring(cmd.getKeyword().length()).trim();
        switch (cmd) {
            case BYE:
                return true;
            case LIST:
                list();
                return false;
            case MARK:
                mark(args);
                return false;
            case UNMARK:
                unmark(args);
                return false;
            case TODO:
                if (args.isEmpty()) { throw new TodoFormatException(); }
                add(new Todo(args));
                return false;
            case DEADLINE:
                String[] deadlineParts = args.split(" /by ", 2);
                if  (deadlineParts.length != 2) { throw new DeadlineFormatException(); }
                String deadlineDesc = deadlineParts[0].trim();
                String dueDate = deadlineParts[1].trim();
                if (deadlineDesc.isEmpty() || dueDate.isEmpty()) {
                    throw new DeadlineFormatException();
                }
                add(new Deadline(deadlineDesc, dueDate));
                return false;
            case EVENT:
                String[] eventParts = args.split(" /from | /to ", 3);
                if  (eventParts.length != 3) { throw new EventFormatException(); }
                String eventDesc = eventParts[0].trim();
                String startTime = eventParts[1].trim();
                String endTime = eventParts[2].trim();
                if (eventDesc.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    throw new EventFormatException();
                }
                add(new Event(eventDesc, startTime, endTime));
                return false;
            case DELETE:
                try {
                    if (args.isEmpty()) { throw new DeleteFormatException(); }
                    delete(Integer.parseInt(args) - 1);
                    return false;
                } catch (NumberFormatException e) {
                    throw new DeleteFormatException();
                }
            default:
                throw new UnknownCommandException();
        }
    }

    /**
     * Runs the chatbot: greets the user, then reads and processes one command
     * per line until the user enters {@code bye} or input ends. Finally,
     * print farewell.
     */
    private void run() {
        Scanner scanner = new Scanner(System.in);
        greet();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                if (processCommand(input)) {
                    break;
                }
            } catch (EchoException exception) {
                showError(exception);
            }
        }
        farewell();
        scanner.close();
    }

    public static void main(String[] args) {
        new Echo().run();
    }
}

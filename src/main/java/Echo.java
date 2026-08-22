import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Echo {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Ui ui = new Ui();
    private final Storage storage;
    private final Parser parser = new Parser();
    /**
     * Instantiates {@link Echo} chatbot instance with default
     * parameters.
     */
    public Echo() {
        this.storage = new Storage();
    }

    /**
     * Instantiates {@link Echo} chatbot instance with custom
     * file path.
     */
    public Echo(Path fileSavePath) {
        this.storage = new Storage(fileSavePath);
    }

    private void add(Task t) throws EchoException {
        tasks.add(t);
        storage.save(tasks);
        ui.echo("Got it. I've added this task:\n" + t.toString()
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private void delete(int index) throws EchoException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task t = tasks.remove(index);
        storage.save(tasks);
        ui.echo("Noted. I've removed this task:\n" + t.toString() +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private void list() {
        StringBuilder listTxt = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= tasks.size(); ++i) {
            Task task = tasks.get(i - 1);
            listTxt.append(i).append(".").append(task.toString());
            if (i != tasks.size()) listTxt.append("\n");
        }
        ui.echo(listTxt.toString());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as complete.
     *
     * @param taskIndex zero-based index of the task to mark
     */
    private void mark(int taskIndex) throws EchoException {
        Task task = getTask(taskIndex);
        task.markDone();
        storage.save(tasks);
        ui.echo("Nice! I've marked this task as done:\n" + task.toString());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as not done.
     *
     * @param taskIndex zero-based index of the task to unmark
     */
    private void unmark(int taskIndex) throws EchoException {
        Task task = getTask(taskIndex);
        task.markUnDone();
        storage.save(tasks);
        ui.echo("OK, I've marked this task as not done yet:\n" + task.toString());
    }
    /**
     * Returns the task at the parsed index after checking it against the
     * current task list. Parser cannot do this check because it does not own
     * the list.
     *
     * @param taskIndex zero-based task index from a command
     * @return referenced task
     * @throws InvalidTaskNumberException when the index is not in the list
     */
    private Task getTask(int taskIndex) throws InvalidTaskNumberException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        return tasks.get(taskIndex);
    }

    /**
     * Executes a parsed command against Echo's task list.
     * @param command command to execute
     */
    private boolean execute(Command command) throws EchoException {
        switch (command.getType()) {
        case BYE:
            return true;
        case LIST:
            list();
            break;
        case TODO, DEADLINE, EVENT:
            add(command.getTask());
            break;
        case MARK:
            mark(command.getTaskIndex());
            break;
        case UNMARK:
            unmark(command.getTaskIndex());
            break;
        case DELETE:
            delete(command.getTaskIndex());
            break;
        }
        return false;
    }


    /**
     * Runs the chatbot: loads any previously saved tasks, greets the user,
     * then reads and processes one command per line until the user enters
     * {@code bye} or input ends. Finally, print farewell.
     */
    private void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            tasks.addAll(storage.read());
        } catch (StorageException e) {
            ui.showError(e);
        }
        ui.greet();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                if (execute(parser.parse(input))) {
                    break;
                }
            } catch (EchoException exception) {
                ui.showError(exception);
            }
        }
        ui.farewell();
        scanner.close();
    }

    public static void main(String[] args) {
        new Echo().run();
    }
}

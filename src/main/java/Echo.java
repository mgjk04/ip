import java.nio.file.Path;
import java.util.Scanner;

public class Echo {
    private final TaskList taskList = new TaskList();
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
        taskList.add(t);
        storage.save(taskList.getAll());
        ui.echo("Got it. I've added this task:\n" + t.toString()
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private void delete(int index) throws EchoException {
        Task t = taskList.delete(index);
        storage.save(taskList.getAll());
        ui.echo("Noted. I've removed this task:\n" + t.toString() +
                "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private void list() {
        ui.echo(taskList.asListText());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as complete.
     *
     * @param taskIndex zero-based index of the task to mark
     */
    private void mark(int taskIndex) throws EchoException {
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        storage.save(taskList.getAll());
        ui.echo("Nice! I've marked this task as done:\n" + task.toString());
    }

    /**
     * Method with Codex contribution:
     * Marks the task at the given one-based task number as not done.
     *
     * @param taskIndex zero-based index of the task to unmark
     */
    private void unmark(int taskIndex) throws EchoException {
        Task task = taskList.getTask(taskIndex);
        task.markUnDone();
        storage.save(taskList.getAll());
        ui.echo("OK, I've marked this task as not done yet:\n" + task.toString());
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
            taskList.addAll(storage.read());
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

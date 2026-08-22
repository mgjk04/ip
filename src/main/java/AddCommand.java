/**
 * Adds one parsed task to the list and confirms the new task count.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that appends the given task.
     *
     * @param task validated task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Appends the task, saves the list, and confirms the addition.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws EchoException {
        taskList.add(task);
        storage.save(taskList.getAll());
        ui.echo("Got it. I've added this task:\n" + task.toString()
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }
}

package echo.command;

import echo.exception.EchoException;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskList;
import echo.ui.Ui;

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
     * See: {@link Command#execute(TaskList, Ui, Storage)}
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws EchoException {
        taskList.add(task);
        storage.save(taskList.getAll());
        return ui.echo("Got it. I've added this task:\n" + task.toString()
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }
}

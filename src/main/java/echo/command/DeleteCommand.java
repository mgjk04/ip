package echo.command;

import echo.exception.EchoException;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskList;
import echo.ui.Ui;

/**
 * Removes one task from the list by its zero-based index.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command that deletes one task.
     *
     * @param index zero-based index of the task to remove
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Deletes the task, saves the list, and reports which task went away
     * together with the new task count.
     * See: {@link Command#execute(TaskList, Ui, Storage)}
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws EchoException {
        Task removed = taskList.delete(index);
        storage.save(taskList.getAll());
        return ui.echo("Noted. I've removed this task:\n" + removed.toString()
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }
}

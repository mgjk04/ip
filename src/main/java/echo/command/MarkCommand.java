package echo.command;

import echo.exception.EchoException;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskList;
import echo.ui.Ui;
//Codex assistance

/**
 * Updates the completion status of the task at a zero-based index.
 * Marking done and marking not done share this class because they differ
 * only in the status written and the wording of the confirmation.
 */
public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    /**
     * Creates a command that updates one task's completion status.
     *
     * @param index zero-based index of the task to update
     * @param done true to mark as done, false to mark as not done
     */
    public MarkCommand(int index, boolean done) {
        this.index = index;
        this.isDone = done;
    }

    /**
     * Updates the task's status, saves the list, and confirms the change.
     * See: {@link Command#execute(TaskList, Ui, Storage)}
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws EchoException {
        Task task = taskList.getTask(index);
        String response = "";
        if (this.isDone) {
            task.markDone();
            response = ui.joinLines("Nice! I've marked this task as done:", task.toString());
        } else {
            task.markNotDone();
            response = ui.joinLines("OK, I've marked this task as not done yet:", task.toString());
        }
        storage.save(taskList.getAll());
        return response;
    }
}

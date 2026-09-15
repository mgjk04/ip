package echo.command;

import echo.exception.EchoException;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskList;
import echo.ui.Ui;
//Codex assistance

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
        String missionNoun = taskList.size() == 1 ? "mission" : "missions";
        return ui.joinLines("Mission logged:",
                task.toString(),
                "You now have " + taskList.size() + " " + missionNoun + " on the manifest.");
    }
}

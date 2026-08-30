package echo.command;

import echo.storage.Storage;
import echo.task.TaskList;
import echo.ui.Ui;

/**
 * Shows every task currently in the list.
 */
public class ListCommand extends Command {
    /**
     * Prints the numbered listing of all tasks. Nothing is saved because
     * listing does not change any task.
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.echo(taskList.asListText());
    }
}

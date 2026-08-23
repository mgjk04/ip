package echo.command;

import echo.storage.Storage;
import echo.task.TaskList;
import echo.ui.Ui;

/**
 * Shows every task currently in the list that matches the searchText.
 */
public class FindCommand extends Command {
    private final String searchText;

    /**
     * Creates a command that searches the list for task which description matches the searchText.
     *
     * @param searchText Search string to match in the description of tasks.
     */
    public FindCommand(String searchText) {
        this.searchText = searchText;
    }
    /**
     * Prints the numbered listing of all tasks. Nothing is saved because
     * listing does not change any task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.echo(taskList.searchListText(searchText));
    }
}
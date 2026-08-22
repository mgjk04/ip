package echo.command;

import echo.storage.Storage;
import echo.task.TaskList;
import echo.ui.Ui;

/**
 * Ends the chatbot session.
 */
public class ExitCommand extends Command {
    /**
     * Does nothing itself; Echo prints its farewell once the loop ends.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
    }

    /**
     * Exiting is this command's whole purpose.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

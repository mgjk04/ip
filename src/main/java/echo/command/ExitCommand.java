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
     * See: {@link Command#execute(TaskList, Ui, Storage)}
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.farewell();
    }

    /**
     * Exiting is this command's whole purpose.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

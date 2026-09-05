package echo;

import java.nio.file.Path;

import echo.command.Command;
import echo.exception.EchoException;
import echo.exception.StorageException;
import echo.parser.Parser;
import echo.storage.Storage;
import echo.task.TaskList;
import echo.ui.Ui;


/**
 * Entry point of the Echo chatbot application. Wires together the task list,
 * UI, parser, and storage components, then runs the read-evaluate-print loop.
 */
public class Echo {
    private final TaskList taskList = new TaskList();
    private final Ui ui = new Ui();
    private final Storage storage;
    private final Parser parser = new Parser();
    private Command prevCommand = null;

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

    /**
     * Starts the chatbot: loads any previously saved tasks, greets the user.
     */
    public String start() {
        try {
            var loadedTasks = storage.read();
            assert loadedTasks != null : "Storage.read must return a task collection.";
            taskList.addAll(loadedTasks);
        } catch (StorageException e) {
            ui.showError(e);
        }
        return ui.greet();
    }

    public boolean isExit() {
        return prevCommand != null && prevCommand.isExit();
    }

    /**
     * Processes the user input and returns Echo's response
     * @param input user input
     * @return Echo's output
     */
    public String getResponse(String input) {
        try {
            Command cmd = parser.parse(input);
            prevCommand = cmd;
            return cmd.execute(taskList, ui, storage);
        } catch (EchoException exception) {
            return ui.showError(exception);
        }
    }
}

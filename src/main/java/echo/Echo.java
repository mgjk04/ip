package echo;

import echo.command.Command;
import echo.exception.EchoException;
import echo.exception.StorageException;
import echo.parser.Parser;
import echo.storage.Storage;
import echo.task.TaskList;
import echo.ui.Ui;

import java.nio.file.Path;
import java.util.Scanner;

public class Echo {
    private final TaskList taskList = new TaskList();
    private final Ui ui = new Ui();
    private final Storage storage;
    private final Parser parser = new Parser();
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
     * Runs the chatbot: loads any previously saved tasks, greets the user,
     * then parses one command per line and lets each command execute itself
     * against the task list until {@code bye} or input ends. Finally, print
     * farewell.
     */
    private void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            taskList.addAll(storage.read());
        } catch (StorageException e) {
            ui.showError(e);
        }
        ui.greet();
        boolean isExit = false;
        while (!isExit && scanner.hasNextLine()) {
            try {
                Command cmd = parser.parse(scanner.nextLine());
                cmd.execute(taskList, ui, storage);
                isExit = cmd.isExit();
            } catch (EchoException exception) {
                ui.showError(exception);
            }
        }
        ui.farewell();
        scanner.close();
    }

    public static void main(String[] args) {
        new Echo().run();
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the task list to a file on disk.
 * Each task is stored on its own line in a pipe-delimited
 * save format (see {@link Task#toSaveFormat()}), e.g.
 * {@code D | 1 | return book | Sunday}.
 */
public class Storage {
    private static final Path PROJECT_DIR = Path.of(System.getProperty("user.dir"));
    private static final Path SAVE_FILE = Path.of(PROJECT_DIR.toString(), "data", "echo.txt");

    /**
     * Overwrites the save file with one line per task, creating the
     * {@code data} directory first if it does not exist yet.
     *
     * @param tasks list of tasks to write to the save file
     * @throws EchoException when writing to the save file fails
     */
    public void save(List<Task> tasks) throws EchoException {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toSaveFormat());
        }
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.write(SAVE_FILE, lines);
        } catch (IOException e) {
            throw new EchoException("I could not save your tasks to " + SAVE_FILE + ".");
        }
    }
}

package echo.storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import echo.exception.StorageException;
import echo.task.Task;


/**
 * Saves the task list to a file on disk.
 * Each task is stored on its own line in a pipe-delimited
 * save format (see {@link Task#toSaveFormat()}), e.g.
 * {@code D | 1 | return book | Sunday}.
 */
public class Storage {
    private final Path saveFile;

    /**
     * Instantiates the {@link Storage} class with default
     * save file path.
     */
    public Storage() {
        this.saveFile = Path.of("data", "echo.txt");
    }

    /**
     * Instantiates the {@link Storage} class with custom
     * save file path.
     * @param fileSavePath {@link Path} of save file
     */
    public Storage(Path fileSavePath) {
        this.saveFile = fileSavePath;
    }

    /**
     * Overwrites the save file with one line per task, creating the
     * {@code data} directory first if it does not exist yet.
     *
     * @param tasks list of tasks to write to the save file
     * @throws StorageException when writing to the save file fails
     */
    public void save(List<Task> tasks) throws StorageException {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toSaveFormat());
        }
        try {
            Files.createDirectories(saveFile.getParent());
            Files.write(saveFile, lines);
        } catch (IOException e) {
            throw new StorageException("I could not save your tasks to " + saveFile + ".");
        }
    }

    /**
     * Reads the save file line by line and lets each task class reconstruct
     * itself from its own line (see {@link Task#fromSaveFormat(String)}).
     * A missing save file is treated as an empty task list so that the
     * chatbot starts cleanly on first use.
     *
     * @return tasks restored from the save file
     * @throws StorageException when reading fails or a line is malformed
     */
    public List<Task> read() throws StorageException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(saveFile)) {
            return tasks;
        }
        try (Scanner scanner = new Scanner(saveFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    tasks.add(Task.fromSaveFormat(line));
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new StorageException("I could not read your tasks at " + saveFile + ".");
        }
    }
}

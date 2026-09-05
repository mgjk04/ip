package echo.task;
import java.time.format.DateTimeParseException;

import echo.exception.CorruptFormatException;
import echo.exception.StorageException;


/**
 * Represents a task description and whether the task has been completed.
 * Class with Codex contribution.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text that describes the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Gets description of the task.
     * @return description string of the task.
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as complete. */
    public void markDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markUnDone() {
        isDone = false;
    }

    /**
     * Returns this task's details in the pipe-delimited save-file format:
     * a {@code 1}/{@code 0} completion flag followed by the description,
     * e.g., {@code 1 | read book}. Subclasses prepend their type letter
     * and append any extra fields.
     */
    public String toSaveFormat() {
        return (this.isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Reconstructs a task from one of its save-file lines, e.g.,
     * {@code D | 1 | return book | Sunday} becomes a completed Deadline.
     * Dispatches on the leading type letter to the matching subclass,
     * which validates and interprets its remaining fields.
     *
     * @param saveFormat one pipe-delimited line written by {@link #toSaveFormat()}
     * @return the reconstructed task, marked done when its flag is {@code 1}
     * @throws StorageException when the line does not follow the save format
     */
    public static Task fromSaveFormat(String saveFormat) throws StorageException {
        String[] fields = saveFormat.split(" \\| ");
        if (fields.length < 3 || fields[1].isEmpty() || fields[2].isEmpty()
                || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new CorruptFormatException(saveFormat);
        }
        try {
            Task task = switch (fields[0]) {
                case "T" -> Todo.fromSaveFormat(fields);
                case "D" -> Deadline.fromSaveFormat(fields);
                case "E" -> Event.fromSaveFormat(fields);
                default -> throw new CorruptFormatException(saveFormat);
            };
            if (fields[1].equals("1")) {
                task.markDone();
            }
            return task;
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new CorruptFormatException(saveFormat);
        }
    }

    /**
     * Creates String representation of a task.
     * Contains the doneness and description only.
     * @return String representation of a Task
     */
    @Override
    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + description;
    }
}

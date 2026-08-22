/**
 * File with Codex contribution:
 * Represents a task description and whether the task has been completed.
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

    @Override
    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + description;
    }
}

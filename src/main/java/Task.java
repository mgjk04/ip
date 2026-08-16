/**
 * File created by Codex:
 * Represents a task description and whether the task has been completed.
 */
public class Task {
    /** Text that describes the work represented by this task. */
    private final String description;
    /** Tracks whether this task has been marked as complete. */
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
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the character used to show this task's completion state.
     *
     * @return {@code "X"} if complete, otherwise a blank space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task's description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }
}

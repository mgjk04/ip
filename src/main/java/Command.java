/**
 * Represents one validated user command. A command either has no additional
 * data, contains a task to add, or identifies an existing task by its
 * zero-based index.
 */
public class Command {
    private final CommandType type;
    private final Task task;
    private final int taskIndex;

    /**
     * Creates a command that needs no additional data, such as {@code list}
     * or {@code bye}.
     *
     * @param type kind of command
     */
    public Command(CommandType type) {
        this(type, null, -1);
    }

    /**
     * Creates an add-task command.
     *
     * @param type kind of command that created the task
     * @param task validated task to add
     */
    public Command(CommandType type, Task task) {
        this(type, task, -1);
    }

    /**
     * Creates a command that refers to an existing task.
     *
     * @param type kind of command that will use the task index
     * @param taskIndex zero-based index supplied by the user
     */
    public Command(CommandType type, int taskIndex) {
        this(type, null, taskIndex);
    }

    private Command(CommandType type, Task task, int taskIndex) {
        this.type = type;
        this.task = task;
        this.taskIndex = taskIndex;
    }

    /**
     * Returns the kind of user command.
     *
     * @return command type
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Returns the task created while parsing an add-task command.
     *
     * @return task to add
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns the zero-based task index for mark, unmark, or delete commands.
     * The caller must still check that this index is present in its task list.
     *
     * @return task index
     */
    public int getTaskIndex() {
        return taskIndex;
    }
}

package echo.task;

/**
 * A {@link Task} with only a description and the ability to mark/unmark as done.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete Todo.
     * @param description Description of Todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates {@link String} representation of the {@link Todo} instance.
     * @return String representation of {@link Todo}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Creates save {@link String} representation of the {@link Todo} instance.
     * @return String representation of {@link Todo}
     */
    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }

    /**
     * Reconstructs a todo from its save-format fields (type letter,
     * completion flag, description).
     *
     * @param fields the split save-file line for this todo.
     * @return the reconstructed todo.
     */
    public static Todo fromSaveFormat(String[] fields) {
        if (fields.length != 3) {
            throw new IllegalArgumentException("A saved todo must have exactly 3 fields.");
        }
        return new Todo(fields[2]);
    }
}

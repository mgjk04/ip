public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }

    /**
     * Reconstructs a todo from its save-format fields (type letter,
     * completion flag, description).
     *
     * @param fields the split save-file line for this todo
     * @return the reconstructed todo
     */
    public static Todo fromSaveFormat(String[] fields) {
        if (fields.length != 3) {
            throw new IllegalArgumentException("A saved todo must have exactly 3 fields.");
        }
        return new Todo(fields[2]);
    }
}

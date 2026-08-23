package echo.task;

import echo.utils.DateTimeUtility;

import java.time.LocalDateTime;

/**
 * A {@link Task} with a {@link LocalDateTime} as a deadline.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline task.
     * @param description description of deadline.
     * @param by the {@link LocalDateTime} the deadline is due.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates {@link String} representation of the {@link Deadline} instance
     * @return String representation of deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeUtility.DISPLAY) + ")";
    }


    /**
     * Creates save {@link String} representation of the {@link Deadline} instance
     * @return Save string representation of deadline
     */
    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by;
    }

    /**
     * Reconstructs a deadline from its save-format fields (type letter,
     * completion flag, description, due date).
     *
     * @param fields the split save-file line for this deadline
     * @return the reconstructed deadline
     */
    public static Deadline fromSaveFormat(String[] fields)  {
        if (fields.length != 4 || fields[3].isEmpty()) {
            throw new IllegalArgumentException("A saved deadline must have exactly 4 non-empty fields.");
        }
        return new Deadline(fields[2], LocalDateTime.parse(fields[3]));
    }
}


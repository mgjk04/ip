package echo.task;

import echo.utils.DateTimeUtility;

import java.time.LocalDateTime;

/**
 * Represents a task that needs to be done before a specific date-time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description text that describes the deadline
     * @param by date-time by which the deadline must be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeUtility.DISPLAY) + ")";
    }

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
    public static Deadline fromSaveFormat(String[] fields) {
        if (fields.length != 4 || fields[3].isEmpty()) {
            throw new IllegalArgumentException("A saved deadline must have exactly 4 non-empty fields.");
        }
        return new Deadline(fields[2], LocalDateTime.parse(fields[3]));
    }
}


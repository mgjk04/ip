package echo.task;

import echo.utils.DateTimeUtility;

import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(DateTimeUtility.DISPLAY)
                + " to: " + this.to.format(DateTimeUtility.DISPLAY) + ")";
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + this.from + " | " + this.to;
    }

    /**
     * Reconstructs an event from its save-format fields (type letter,
     * completion flag, description, start time, end time).
     *
     * @param fields the split save-file line for this event
     * @return the reconstructed event
     */
    public static Event fromSaveFormat(String[] fields) {
        if (fields.length != 5 || fields[3].isEmpty() || fields[4].isEmpty()) {
            throw new IllegalArgumentException("A saved event must have exactly 5 non-empty fields.");
        }
        return new Event(fields[2], LocalDateTime.parse(fields[3]), LocalDateTime.parse(fields[4]));
    }
}

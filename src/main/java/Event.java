public class Event extends Task {
    private String from = "?";
    private String to = "?";

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
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
        return new Event(fields[2], fields[3], fields[4]);
    }
}

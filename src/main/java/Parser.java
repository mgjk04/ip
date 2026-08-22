import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Converts a line entered by the user into an executable {@link Command}.
 * It validates the input and builds the matching command object; running
 * the command against the task list happens later in {@link Command#execute}.
 */
public class Parser {
    /**
     * Parses one user-input line into a ready-to-run command.
     *
     * @param input command line entered by the user
     * @return a command ready for Echo to execute
     * @throws EchoException if the command or its arguments are invalid
     */
    public Command parse(String input) throws EchoException {
        String trimmedInput = input.trim();
        String keyword = trimmedInput.split(" ", 2)[0];
        String arguments = trimmedInput.substring(keyword.length()).trim();

        return switch (keyword) {
        case "bye" -> new ExitCommand();
        case "list" -> new ListCommand();
        case "todo" -> parseTodo(arguments);
        case "deadline" -> parseDeadline(arguments);
        case "event" -> parseEvent(arguments);
        case "mark" -> new MarkCommand(parseIndex(arguments,
                new TaskNumberFormatException("mark"), new InvalidTaskNumberException()), true);
        case "unmark" -> new MarkCommand(parseIndex(arguments,
                new TaskNumberFormatException("unmark"), new InvalidTaskNumberException()), false);
        case "delete" -> new DeleteCommand(parseIndex(arguments,
                new DeleteFormatException(), new DeleteFormatException()));
        default -> throw new UnknownCommandException();
        };
    }

    private Command parseTodo(String arguments) throws EchoException {
        if (arguments.isEmpty()) {
            throw new TodoFormatException();
        }
        requireSavable(arguments);
        return new AddCommand(new Todo(arguments));
    }

    private Command parseDeadline(String arguments) throws EchoException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length != 2) {
            throw new DeadlineFormatException();
        }

        String description = parts[0].trim();
        String dueDateTime = parts[1].trim();
        if (description.isEmpty() || dueDateTime.isEmpty()) {
            throw new DeadlineFormatException();
        }
        requireSavable(description);
        requireSavable(dueDateTime);

        try {
            LocalDateTime by = LocalDateTime.parse(dueDateTime, DateTimeUtility.INPUT);
            return new AddCommand(new Deadline(description, by));
        } catch (DateTimeParseException exception) {
            throw new DeadlineFormatException();
        }
    }

    private Command parseEvent(String arguments) throws EchoException {
        String[] descriptionAndSchedule = arguments.split(" /from ", 2);
        if (descriptionAndSchedule.length != 2) {
            throw new EventFormatException();
        }
        String[] times = descriptionAndSchedule[1].split(" /to ", 2);
        if (times.length != 2) {
            throw new EventFormatException();
        }

        String description = descriptionAndSchedule[0].trim();
        String startDateTime = times[0].trim();
        String endDateTime = times[1].trim();
        if (description.isEmpty() || startDateTime.isEmpty() || endDateTime.isEmpty()) {
            throw new EventFormatException();
        }
        requireSavable(description);
        requireSavable(startDateTime);
        requireSavable(endDateTime);

        try {
            LocalDateTime from = LocalDateTime.parse(startDateTime, DateTimeUtility.INPUT);
            LocalDateTime to = LocalDateTime.parse(endDateTime, DateTimeUtility.INPUT);
            return new AddCommand(new Event(description, from, to));
        } catch (DateTimeParseException exception) {
            throw new EventFormatException();
        }
    }

    /**
     * Converts a one-based task number into a zero-based index. Mark,
     * unmark, and delete share this method because they differ only in
     * which errors their format messages describe.
     *
     * @param taskNumberText raw text following the command keyword
     * @param emptyError error for a missing number, e.g. {@code mark} alone
     * @param invalidError error for text that is not a whole number
     * @return zero-based index into the task list
     * @throws EchoException using the supplied errors when the text is unusable
     */
    private int parseIndex(String taskNumberText, EchoException emptyError, EchoException invalidError)
            throws EchoException {
        if (taskNumberText.isEmpty()) {
            throw emptyError;
        }
        try {
            return Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException exception) {
            throw invalidError;
        }
    }

    /**
     * Rejects task details containing the pipe character, which is reserved
     * as the field separator in the save file.
     *
     * @param detail one user-supplied task field
     * @throws EchoException when the detail contains a pipe character
     */
    private void requireSavable(String detail) throws EchoException {
        if (detail.contains("|")) {
            throw new EchoException("'|' cannot be used because it separates fields in the save file.");
        }
    }
}

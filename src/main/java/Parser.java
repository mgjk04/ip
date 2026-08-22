import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Converts a line entered by the user into a validated {@link Command}.
 * It only interprets the input; operations on the task list belong to
 * {@link Echo} because only Echo knows which tasks currently exist.
 */
public class Parser {
    /**
     * Parses one user-input line into a command and its validated data.
     *
     * @param input command line entered by the user
     * @return a command ready for Echo to execute
     * @throws EchoException if the command or its arguments are invalid
     */
    public Command parse(String input) throws EchoException {
        String trimmedInput = input.trim();
        CommandType type = CommandType.fromInput(trimmedInput);
        String arguments = trimmedInput.substring(type.getKeyword().length()).trim();

        return switch (type) {
        case BYE, LIST -> new Command(type);
        case MARK, UNMARK -> new Command(type, parseTaskIndex(type, arguments));
        case TODO -> parseTodo(arguments);
        case DEADLINE -> parseDeadline(arguments);
        case EVENT -> parseEvent(arguments);
        case DELETE -> new Command(type, parseDeleteIndex(arguments));
        };
    }

    private Command parseTodo(String arguments) throws EchoException {
        if (arguments.isEmpty()) {
            throw new TodoFormatException();
        }
        requireSavable(arguments);
        return new Command(CommandType.TODO, new Todo(arguments));
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
            return new Command(CommandType.DEADLINE, new Deadline(description, by));
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
            return new Command(CommandType.EVENT, new Event(description, from, to));
        } catch (DateTimeParseException exception) {
            throw new EventFormatException();
        }
    }

    private int parseTaskIndex(CommandType type, String taskNumberText) throws EchoException {
        if (taskNumberText.isEmpty()) {
            throw new TaskNumberFormatException(type);
        }
        try {
            return Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException exception) {
            throw new InvalidTaskNumberException();
        }
    }

    private int parseDeleteIndex(String taskNumberText) throws EchoException {
        if (taskNumberText.isEmpty()) {
            throw new DeleteFormatException();
        }
        try {
            return Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException exception) {
            throw new DeleteFormatException();
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

package echo.parser;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import echo.command.AddCommand;
import echo.command.Command;
import echo.command.DeleteCommand;
import echo.command.ExitCommand;
import echo.command.FindCommand;
import echo.command.ListCommand;
import echo.command.MarkCommand;
import echo.exception.DeadlineFormatException;
import echo.exception.DeleteFormatException;
import echo.exception.EchoException;
import echo.exception.EventFormatException;
import echo.exception.FindFormatException;
import echo.exception.InvalidTaskNumberException;
import echo.exception.TaskNumberFormatException;
import echo.exception.TodoFormatException;
import echo.exception.UnknownCommandException;
import echo.task.Deadline;
import echo.task.Event;
import echo.task.Todo;
import echo.utils.DateTimeUtility;


/**
 * Converts a line entered by the user into an executable {@link Command}.
 * It validates the input and builds the matching command object; running
 * the command against the task list happens later in {@link Command#execute}.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String BY_SEPARATOR = " /by ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";

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
            case BYE_COMMAND -> new ExitCommand();
            case LIST_COMMAND -> new ListCommand();
            case TODO_COMMAND -> parseTodo(arguments);
            case DEADLINE_COMMAND -> parseDeadline(arguments);
            case EVENT_COMMAND -> parseEvent(arguments);
            case MARK_COMMAND -> new MarkCommand(parseIndex(arguments,
                    new TaskNumberFormatException(MARK_COMMAND), new InvalidTaskNumberException()), true);
            case UNMARK_COMMAND -> new MarkCommand(parseIndex(arguments,
                    new TaskNumberFormatException(UNMARK_COMMAND), new InvalidTaskNumberException()), false);
            case DELETE_COMMAND -> new DeleteCommand(parseIndex(arguments,
                    new DeleteFormatException(), new DeleteFormatException()));
            case FIND_COMMAND -> parseFind(arguments);
            default -> throw new UnknownCommandException();
        };
    }

    /**
     * Parses the string arguments according to what is expected
     * of the {@code todo} command and returns a Command.
     * Throws EchoException otherwise.
     * @param arguments string arguments of the command
     * @return a {@link Command} that can be executed
     * @throws EchoException if the arguments do not adhere to the {@code todo} format
     */
    private Command parseTodo(String arguments) throws EchoException {
        if (arguments.isEmpty()) {
            throw new TodoFormatException();
        }
        requireSavable(arguments);
        return new AddCommand(new Todo(arguments));
    }

    /**
     * Parses the string arguments according to what is expected
     * of the deadline command and returns a {@link Command}.
     * Throws EchoException otherwise.
     * @param arguments string arguments of the command
     * @return a {@link Command} that can be executed
     * @throws EchoException if the arguments do not adhere to the deadline format
     */
    private Command parseFind(String arguments) throws EchoException {
        if (arguments.isEmpty()) {
            throw new FindFormatException();
        }
        return new FindCommand(arguments);
    }

    private Command parseDeadline(String arguments) throws EchoException {
        String[] parts = arguments.split(BY_SEPARATOR, 2);
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
    /**
     * Parses the string arguments according to what is expected
     * of the event command and returns a {@link Command}.
     * Throws EchoException otherwise.
     * @param arguments string arguments of the command
     * @return a {@link Command} that can be executed
     * @throws EchoException if the arguments do not adhere to the event format
     */
    private Command parseEvent(String arguments) throws EchoException {
        String[] descriptionAndSchedule = arguments.split(FROM_SEPARATOR, 2);
        if (descriptionAndSchedule.length != 2) {
            throw new EventFormatException();
        }
        String[] times = descriptionAndSchedule[1].split(TO_SEPARATOR, 2);
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

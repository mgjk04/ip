package echo.parser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import echo.command.AddCommand;
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
import echo.exception.TodoFormatException;
import echo.exception.UnknownCommandException;
import echo.storage.Storage;
import echo.task.Deadline;
import echo.task.TaskList;
import echo.task.Todo;
import echo.ui.Ui;


/**
 * Tests {@link Parser#parse(String)}: command routing, argument validation,
 * date parsing, and the one-based to zero-based index conversion.
 */
public class ParserTest {

    @TempDir
    Path tempDir;

    /** Executes a parsed command against a real list, saving to a temp file. */
    private void execute(echo.command.Command cmd, TaskList tasks) throws EchoException {
        cmd.execute(tasks, new Ui(), new Storage(tempDir.resolve("echo.txt")));
    }

    @Test
    public void parse_bye_returnsExitCommand() throws EchoException {
        assertTrue(new Parser().parse("bye") instanceof ExitCommand);
    }

    @Test
    public void parse_listWithWhitespace_returnsListCommand() throws EchoException {
        assertTrue(new Parser().parse("  list  ") instanceof ListCommand);
    }

    @Test
    public void parse_unknownKeyword_exceptionThrown() {
        assertThrows(UnknownCommandException.class, () -> new Parser().parse("frobnicate"));
    }

    @Test
    public void parse_findWithKeyword_returnsFindCommand() throws EchoException {
        assertTrue(new Parser().parse("find book") instanceof FindCommand);
    }

    @Test
    public void parse_findWithoutKeyword_exceptionThrown() {
        assertThrows(FindFormatException.class, () -> new Parser().parse("find"));
    }

    @Test
    public void parse_findWhitespaceOnlyKeyword_exceptionThrown() {
        assertThrows(FindFormatException.class, () -> new Parser().parse("find   "));
    }

    @Test
    public void parse_todoWithoutDescription_exceptionThrown() {
        assertThrows(TodoFormatException.class, () -> new Parser().parse("todo"));
    }

    @Test
    public void parse_todoContainingPipe_rejected() {
        // Pipes are reserved for the save-file format, so they must never
        // reach a task description.
        assertThrows(EchoException.class, () -> new Parser().parse("todo read | book"));
    }

    @Test
    public void parse_validTodo_addsTodoWhenExecuted() throws Exception {
        AddCommand cmd = (AddCommand) new Parser().parse("todo read book");
        TaskList tasks = new TaskList();
        execute(cmd, tasks);

        assertEquals(1, tasks.size());
        assertTrue(tasks.getTask(0) instanceof Todo);
    }

    @Test
    public void parse_deadlineWithoutByClause_exceptionThrown() {
        assertThrows(DeadlineFormatException.class, () -> new Parser().parse("deadline return book"));
    }

    @Test
    public void parse_deadlineInvalidDate_exceptionThrown() {
        assertThrows(DeadlineFormatException.class, () -> new Parser().parse("deadline return book /by tomorrow"));
    }

    @Test
    public void parse_validDeadline_parsesDueDateTime() throws Exception {
        AddCommand cmd = (AddCommand)
                new Parser().parse("deadline return book /by 2025-01-15 1800");
        TaskList tasks = new TaskList();
        execute(cmd, tasks);

        Deadline deadline = (Deadline) tasks.getTask(0);
        assertEquals("[D][ ] return book (by: Jan 15 2025, 6:00 PM)",
                deadline.toString());
    }

    @Test
    public void parse_eventMissingToClause_exceptionThrown() {
        assertThrows(EventFormatException.class, () -> new Parser().parse(
                        "event meeting /from 2025-01-15 1800"));
    }

    @Test
    public void parse_eventInvalidDate_exceptionThrown() {
        assertThrows(EventFormatException.class, () -> new Parser().parse(
                        "event meeting /from 2025-01-15 1800 /to sometime"));
    }

    @Test
    public void parse_markNonNumericIndex_exceptionThrown() {
        // Note: unlike delete (DeleteFormatException for both cases), mark
        // reports a non-numeric index as InvalidTaskNumberException.
        assertThrows(InvalidTaskNumberException.class, () -> new Parser().parse("mark abc"));
    }

    @Test
    public void parse_deleteNonNumericIndex_exceptionThrown() {
        assertThrows(DeleteFormatException.class, () -> new Parser().parse("delete two"));
    }

    @Test
    public void parse_markSecondTask_marksZeroBasedIndexOne() throws Exception {
        // User-visible numbering is 1-based; the command must target index n-1.
        TaskList tasks = new TaskList();
        execute(new Parser().parse("todo first"), tasks);
        execute(new Parser().parse("todo second"), tasks);

        MarkCommand mark = (MarkCommand) new Parser().parse("mark 2");
        execute(mark, tasks);

        assertEquals("[T][X] second", tasks.getTask(1).toString());
        assertEquals("[T][ ] first", tasks.getTask(0).toString());
    }

    @Test
    public void parse_deleteFirstTask_removesZeroBasedIndexZero() throws Exception {
        TaskList tasks = new TaskList();
        execute(new Parser().parse("todo first"), tasks);
        execute(new Parser().parse("todo second"), tasks);

        DeleteCommand del = (DeleteCommand) new Parser().parse("delete 1");
        execute(del, tasks);

        assertEquals(1, tasks.size());
        assertTrue(tasks.getTask(0).toString().endsWith("second"));
    }
}

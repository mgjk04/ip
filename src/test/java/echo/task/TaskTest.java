package echo.task;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import echo.exception.CorruptFormatException;


//Class with Opencode contribution
/**
 * Tests {@link Task#fromSaveFormat(String)}, the single entry point that
 * reconstructs every task subtype when the save file loads.
 */
public class TaskTest {

    private static final String ISO_DATE = "2025-01-15T18:00";
    private static final String ISO_END = "2025-01-16T20:30";

    @Test
    public void fromSaveFormat_todoLine_returnsTodo() throws Exception {
        Task task = Task.fromSaveFormat("T | 0 | read book");
        assertTrue(task instanceof Todo);
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void fromSaveFormat_deadlineLine_returnsDeadline() throws Exception {
        Task task = Task.fromSaveFormat("D | 0 | return book | " + ISO_DATE);
        assertTrue(task instanceof Deadline);
        assertEquals("[D][ ] return book (by: Jan 15 2025, 6:00 PM)", task.toString());
    }

    @Test
    public void fromSaveFormat_eventLine_returnsEvent() throws Exception {
        Task task = Task.fromSaveFormat(
                "E | 0 | project meeting | " + ISO_DATE + " | " + ISO_END);
        assertTrue(task instanceof Event);
        assertEquals("[E][ ] project meeting (from: Jan 15 2025, 6:00 PM"
                        + " to: Jan 16 2025, 8:30 PM)",
                task.toString());
    }

    @Test
    public void fromSaveFormat_flagOne_marksTaskDone() throws Exception {
        Task task = Task.fromSaveFormat("T | 1 | read book");
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void fromSaveFormat_flagZero_leavesTaskUndone() throws Exception {
        Task task = Task.fromSaveFormat("T | 0 | read book");
        assertFalse(task.toString().startsWith("[X]"));
    }

    @Test
    public void toSaveFormat_undoneTask_flagsZero() {
        assertEquals("0 | borrow book", new Task("borrow book").toSaveFormat());
    }

    @Test
    public void toSaveFormat_doneTask_flagsOne() {
        Task task = new Task("borrow book");
        task.markDone();
        assertEquals("1 | borrow book", task.toSaveFormat());
    }

    @Test
    public void fromSaveFormat_unknownTypeLetter_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("X | 0 | mystery"));
    }

    @Test
    public void fromSaveFormat_tooFewFields_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("T | 0"));
    }

    @Test
    public void fromSaveFormat_completionFlagNotZeroOrOne_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("T | 2 | read book"));
    }

    @Test
    public void fromSaveFormat_emptyDescription_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("D | 0 |  | " + ISO_DATE));
    }

    @Test
    public void fromSaveFormat_invalidDeadlineDate_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("D | 0 | return book | not-a-date"));
    }

    @Test
    public void fromSaveFormat_invalidEventDate_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat(
                        "E | 0 | meeting | " + ISO_DATE + " | nope"));
    }
}

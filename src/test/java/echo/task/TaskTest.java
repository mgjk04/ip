package echo.task;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

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
    private static final String ISO_COMPLETION = "2025-01-13T12:45";

    @Test
    public void fromSaveFormat_todoLine_returnsTodo() throws Exception {
        Task task = Task.fromSaveFormat("T |  | read book");
        assertTrue(task instanceof Todo);
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void fromSaveFormat_deadlineLine_returnsDeadline() throws Exception {
        Task task = Task.fromSaveFormat("D | " + ISO_COMPLETION + " | return book | " + ISO_DATE);
        assertTrue(task instanceof Deadline);
        assertEquals("[D][X] return book (by: Jan 15 2025, 6:00 PM)", task.toString());
    }

    @Test
    public void fromSaveFormat_eventLine_returnsEvent() throws Exception {
        Task task = Task.fromSaveFormat(
                "E |  | project meeting | " + ISO_DATE + " | " + ISO_END);
        assertTrue(task instanceof Event);
        assertEquals("[E][ ] project meeting (from: Jan 15 2025, 6:00 PM"
                        + " to: Jan 16 2025, 8:30 PM)",
                task.toString());
    }

    @Test
    public void fromSaveFormat_completionTime_marksTaskDone() throws Exception {
        Task task = Task.fromSaveFormat("T | " + ISO_COMPLETION + " | read book");
        assertEquals("[T][X] read book", task.toString());
        assertEquals(LocalDateTime.parse(ISO_COMPLETION), task.getCompletedAt());
    }

    @Test
    public void fromSaveFormat_blankCompletionTime_leavesTaskUndone() throws Exception {
        Task task = Task.fromSaveFormat("T |  | read book");
        assertFalse(task.isDone());
    }

    @Test
    public void toSaveFormat_undoneTask_hasBlankCompletionTime() {
        assertEquals(" | borrow book", new Task("borrow book").toSaveFormat());
    }

    @Test
    public void toSaveFormat_doneTask_writesMinutePrecisionCompletionTime() {
        Task task = new Task("borrow book");
        task.markDone(LocalDateTime.of(2025, 1, 13, 12, 45, 30, 999));
        assertEquals("2025-01-13T12:45 | borrow book", task.toSaveFormat());
    }

    @Test
    public void fromSaveFormat_unknownTypeLetter_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("X |  | mystery"));
    }

    @Test
    public void fromSaveFormat_tooFewFields_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("T | "));
    }

    @Test
    public void fromSaveFormat_legacyCompletionFlag_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("T | 0 | read book"));
    }

    @Test
    public void fromSaveFormat_emptyDescription_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("D |  |  | " + ISO_DATE));
    }

    @Test
    public void fromSaveFormat_invalidDeadlineDate_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat("D |  | return book | not-a-date"));
    }

    @Test
    public void fromSaveFormat_invalidEventDate_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat(
                        "E |  | meeting | " + ISO_DATE + " | nope"));
    }

    @Test
    public void fromSaveFormat_secondPrecisionCompletionTime_exceptionThrown() {
        assertThrows(CorruptFormatException.class, () -> Task.fromSaveFormat(
                "T | 2025-01-13T12:45:01 | read book"));
    }
}

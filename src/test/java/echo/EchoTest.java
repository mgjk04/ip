package echo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import echo.ui.EchoResponse;
import echo.ui.ResponseType;

/**
 * Tests typed GUI responses while preserving the console-facing response API.
 */
public class EchoTest {
    @TempDir
    Path tempDir;

    @Test
    public void getTypedResponse_validCommand_returnsNormalResponse() {
        Echo echo = new Echo(tempDir.resolve("typed-valid.txt"));

        EchoResponse response = echo.getTypedResponse("todo inspect reactor");

        assertEquals(ResponseType.NORMAL, response.type());
        assertTrue(response.text().contains("[T][ ] inspect reactor"));
    }

    @Test
    public void getTypedResponse_invalidCommand_returnsErrorResponse() {
        Echo echo = new Echo(tempDir.resolve("typed-error.txt"));

        EchoResponse response = echo.getTypedResponse("unknown-command");

        assertEquals(ResponseType.ERROR, response.type());
        assertTrue(response.text().startsWith("OOPS!!!"));
    }

    @Test
    public void getResponse_sameCommand_matchesTypedResponseText() {
        Echo typedEcho = new Echo(tempDir.resolve("typed-compatibility.txt"));
        Echo stringEcho = new Echo(tempDir.resolve("string-compatibility.txt"));

        String typedText = typedEcho.getTypedResponse("todo inspect reactor").text();
        String stringText = stringEcho.getResponse("todo inspect reactor");

        assertEquals(stringText, typedText);
    }

    @Test
    public void getTypedResponse_invalidCommand_doesNotChangeTasks() {
        Echo echo = new Echo(tempDir.resolve("unchanged-tasks.txt"));
        echo.getTypedResponse("todo inspect reactor");

        echo.getTypedResponse("todo");
        String listResponse = echo.getTypedResponse("list").text();

        assertTrue(listResponse.contains("1.[T][ ] inspect reactor"));
        assertFalse(listResponse.contains("2."));
    }
}

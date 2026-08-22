package echo.exception;

public class DeadlineFormatException extends EchoException {
    public DeadlineFormatException(){
        super("Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800");
    }
}

public class DeleteFormatException extends EchoException {
    public DeleteFormatException() {
        super("Invalid delete command. Format: delete <taskNumber>");
    }
}

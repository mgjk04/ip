package echo.exception;

public class InvalidTaskNumberException extends EchoException{
    public InvalidTaskNumberException(){
        super("Please provide a valid task number. Check the list and choose a valid task number.");
    }
}

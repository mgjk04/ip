public class InvalidTaskNumberException extends EchoException{
    public InvalidTaskNumberException(int itemCnt){
        super("Please provide a valid task number. Check the list and choose a valid task number.");
    }
}

package BookShop.demo.Exceptions;

public class UserNotAuthorizedToDoThisActionException extends Exception{
    public UserNotAuthorizedToDoThisActionException(String message) {
        super(message);
    }
}

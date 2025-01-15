package BookShop.demo.Exceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

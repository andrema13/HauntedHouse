package exceptions;

public class EmptyCollectionException extends Exception {

    public EmptyCollectionException(String list) {
        super(list);
    }
}

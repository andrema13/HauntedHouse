package exceptions;

public class ElementNotFoundException extends Exception {

    public ElementNotFoundException(String list) {
        super(list);
    }

}

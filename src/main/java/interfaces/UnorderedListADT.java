package interfaces;

public interface UnorderedListADT<T> extends ListADT<T> {

    /**
     * Add an element to the front of the list
     *
     * @param element to be added to the list
     */
    void addToFront(T element);

    /**
     * Add an element to the rear of the list
     *
     * @param element to be added to the list
     */
    void addToRear(T element);

    /**
     * Add an element after a particular element already in the list
     *
     * @param element to be added
     * @param target  to add the element after this
     */
    void addAfter(T element, T target);
}

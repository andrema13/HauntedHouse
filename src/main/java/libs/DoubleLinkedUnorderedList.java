package libs;

import interfaces.UnorderedListADT;

public class DoubleLinkedUnorderedList<T extends Comparable<T>> extends DoubleLinkedList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {

        if (element != null) {
            DoubleNode<T> doubleNode = new DoubleNode<>(element);
            DoubleNode<T> tempNode;

            if (isEmpty()) {
                setHead(doubleNode);
                setTail(doubleNode);
                getHead().setPrevious(null);
                getTail().setNext(null);
            } else {
                tempNode = getHead();
                getHead().setPrevious(doubleNode);
                setHead(doubleNode);
                getHead().setNext(tempNode);
                getHead().setPrevious(null);
            }
            setCount(getCount() + 1);
            setModCount(getModCount() + 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void addToRear(T element) {

        if (element != null) {
            DoubleNode<T> doubleNode = new DoubleNode<>(element);
            DoubleNode<T> tempNode;

            if (isEmpty()) {
                setHead(doubleNode);
                setTail(doubleNode);
                getHead().setPrevious(null);
                getTail().setNext(null);
            } else {
                tempNode = getTail();
                getTail().setNext(doubleNode);
                setTail(doubleNode);
                getTail().setNext(null);
                getTail().setPrevious(tempNode);
            }
            setCount(getCount() + 1);
            setModCount(getModCount() + 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void addAfter(T element, T target) {

        if (element != null) {
            DoubleNode<T> doubleNode = new DoubleNode<>(element);
            DoubleNode<T> tempNode;

            if (isEmpty()) {
                System.out.println("Empty List");
            } else {
                DoubleNode<T> current = getHead();
                boolean inserted = false;
                while (current != null && !inserted) {

                    if (target.compareTo(current.getElement()) == 0 ) {

                        if(current.getNext() == null){
                            current.setNext(doubleNode);
                            doubleNode.setNext(null);
                            doubleNode.setPrevious(current);
                            setTail(doubleNode);
                        }
                        else {
                            tempNode = current.getNext();
                            current.setNext(doubleNode);
                            doubleNode.setNext(tempNode);
                            doubleNode.setPrevious(current);
                            tempNode.setPrevious(doubleNode);
                        }
                        setCount(getCount() + 1);
                        setModCount(getModCount() + 1);
                        inserted = true;
                    }
                    current = current.getNext();
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

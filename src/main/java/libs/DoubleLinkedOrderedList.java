package libs;

import interfaces.OrderedListADT;

public class DoubleLinkedOrderedList<T extends Comparable<T>> extends DoubleLinkedList<T> implements OrderedListADT<T> {

    @Override
    public void add(T element) {

        if (element != null) {

            DoubleNode<T> doubleNode = new DoubleNode<>(element);

            if (isEmpty()) {
                setHead(doubleNode);
                setTail(doubleNode);
                getHead().setPrevious(null);
                getTail().setNext(null);
                setCount(getCount() + 1);
                setModCount(getModCount() + 1);
            } else {
                DoubleNode<T> tempNode = getTail();
                // Insere na ultima posição disponivel quando é superior ao ultimo elemento
                if (element.compareTo(getTail().getElement()) > 0) {
                    getTail().setNext(doubleNode);//o proximo node a seguir ao tail é no novo node
                    setTail(doubleNode);//o novo node é o novo tail
                    getTail().setPrevious(tempNode);//o previous do tail é o node temp
                    tempNode.setNext(getTail());//o temp define o next como o tail
                    setCount(getCount() + 1);//aumenta o count
                    setModCount(getModCount() + 1);
                } else {
                    DoubleNode<T> current = getHead();
                    DoubleNode<T> temp;
                    boolean inserted = false;

                    while (current != null && !inserted) {
                        if (element.compareTo(current.getElement()) < 0) {
                            if (current.getPrevious() == null) {
                                current.setPrevious(doubleNode);
                                doubleNode.setNext(current);
                                doubleNode.setPrevious(null);
                            } else {
                                temp = current.getPrevious();
                                current.setPrevious(doubleNode);
                                doubleNode.setNext(current);
                                doubleNode.setPrevious(temp);
                                temp.setNext(doubleNode);
                            }

                            if (element.compareTo(getHead().getElement()) < 0) {// define o novo head
                                setHead(doubleNode);
                            }
                            setCount(getCount() + 1);
                            setModCount(getModCount() + 1);
                            inserted = true;
                        }
                        current = current.getNext();// passa ao proximo elemento
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
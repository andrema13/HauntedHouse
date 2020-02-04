package libs;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T extends Comparable<T>> implements ListADT<T> {

    private DoubleNode<T> head, tail;
    private int count;
    private int modCount;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
        this.count = 0;
        this.modCount = 0;
    }

    //region get-set

    protected DoubleNode<T> getHead() {
        return head;
    }

    protected void setHead(DoubleNode<T> head) {
        this.head = head;
    }

    protected DoubleNode<T> getTail() {
        return tail;
    }

    protected void setTail(DoubleNode<T> tail) {
        this.tail = tail;
    }

    protected int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    protected int getModCount() {
        return modCount;
    }

    protected void setModCount(int modCount) {
        this.modCount = modCount;
    }

    //endregion

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("List");
        }
        if (this.head == null) {
            throw new NullPointerException("List");
        }

        DoubleNode<T> tempNode = this.head;

        this.head = this.head.getNext();
        tempNode.setNext(null);
        this.head.setPrevious(null);

        this.count--;
        this.modCount++;

        return tempNode.getElement();
    }

    @Override
    public T removeLast() throws EmptyCollectionException {

        DoubleNode<T> tempNode;

        if (isEmpty()) {
            throw new EmptyCollectionException("List");
        }
        if (this.tail == null) {
            throw new NullPointerException("List");
        }

        tempNode = tail;
        tail = tail.getPrevious();
        tempNode.setPrevious(null);
        tail.setNext(null);
        count--;
        modCount++;

        return tempNode.getElement();
    }

    @Override
    public T remove(T targetElement) throws EmptyCollectionException, ElementNotFoundException {

        if (isEmpty())
            throw new EmptyCollectionException("List");

        boolean found = false;
        DoubleNode<T> previous = null;
        DoubleNode<T> current = head;

        while (current != null && !found)
            if (targetElement.equals(current.getElement()))
                found = true;
            else {
                previous = current;
                current = current.getNext();
            }
        if (!found)
            throw new ElementNotFoundException("List");
        if (size() == 1)
            head = tail = null;
        else if (current.equals(head))
            head = current.getNext();
        else if (current.equals(tail)) {
            tail = previous;
            tail.setNext(null);
        } else
            previous.setNext(current.getNext());
        count--;
        modCount++;
        return current.getElement();
    }

    @Override
    public T first() {
        return this.head.getElement();
    }

    @Override
    public T last() {
        return this.tail.getElement();
    }

    @Override
    public boolean contains(T target) {

        DoubleIterator itr = new DoubleIterator(getHead());
        while (itr.hasNext()) {
            if (itr.next() == target) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public int size() {
        return this.count;
    }

    public DoubleIterator iterator() {
        return new DoubleIterator(getHead());
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        DoubleIterator itr = new DoubleIterator(getHead());

        while (itr.hasNext()) {
            stringBuilder.append(itr.next()).append("\n");
        }
        return String.valueOf(stringBuilder);
    }

    public class DoubleIterator implements java.util.Iterator {

        private DoubleNode<T> current;
        private int expectedModCount;
        private boolean okToRemove = false;

        DoubleIterator(DoubleNode<T> doubleNode) {
            this.current = doubleNode;
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if (this.expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return current != null;
        }

        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T temp = this.current.getElement();
            this.current = this.current.getNext();
            this.okToRemove = true;

            return temp;
        }

        @Override
        public void remove() {
            try {
                if (okToRemove) DoubleLinkedList.this.remove(next());
                this.okToRemove = false;
                current = current.getPrevious();
                expectedModCount = modCount;
            } catch (ElementNotFoundException | EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
    }
}

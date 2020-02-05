package libs;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<T> implements ListADT<T> {

    private static final int DEFAULT_CAPACITY = 50;
    private T[] list;
    private int length;
    private Iterator<T> itr;
    private int modCount;

    ArrayList() {
        this.list = (T[]) new Object[DEFAULT_CAPACITY];
        this.length = 0;
        this.modCount = 0;
    }

    ArrayList(int tam) {
        this.list = (T[]) (new Comparable[tam]);
        this.length = 0;
        this.modCount = 0;
    }

    //region get-set
    public T[] getList() {
        return this.list;
    }

    protected void setList(T[] list) {
        this.list = list;
    }

    protected int getDefaultCapacity() {
        return DEFAULT_CAPACITY;
    }

    protected int getLength() {
        return this.length;
    }

    protected void setLength(int length) {
        this.length = length;
    }

    protected int getModCount() {
        return this.modCount;
    }

    protected void setModCount(int modCount) {
        this.modCount = modCount;
    }

    //endregion

    @Override
    public T removeFirst() {
        if (isEmpty())
            throw new NullPointerException("Empty List");

        T result = this.list[0];
        for (int i = 0; i < this.length; i++) {
            this.list[i] = this.list[i + 1];
        }
        this.length--;
        this.modCount++;
        return result;
    }

    @Override
    public T removeLast() {

        if (isEmpty()) {
            throw new NullPointerException("Empty List");
        }

        T result = this.list[this.length - 1];
        this.list[length - 1] = null;
        this.length--;
        this.modCount++;

        return result;
    }

    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {

        if (isEmpty()) {
            throw new EmptyCollectionException("Empty List");
        }
        if (element == null) {
            throw new ElementNotFoundException("Not found");
        }

        int pos = 0;

        while (element != this.list[pos]) {

            if (this.list[pos].equals(element)) {
                for (int j = pos; j < this.length; j++) {
                    this.list[j] = this.list[j + 1];
                }
                this.list[this.length - 1] = null;
                this.length--;
                this.modCount++;

                pos++;
            }
        }
        return element;
    }

    protected void expandCapacity() {

        T[] temp = (T[]) (new Comparable[list.length + DEFAULT_CAPACITY]);

        for (int i = 0; i < this.list.length; i++) {
            temp[i] = list[i];
        }
        this.list = temp;
    }

    @Override
    public T first() {
        return this.list[0];
    }

    @Override
    public T last() {
        return this.list[this.length - 1];
    }

    @Override
    public boolean contains(T target) {
        if (isEmpty())
            throw new NullPointerException("Empty List");

        boolean found = false;

        for (int i = 0; i < this.list.length; i++) {

            if (target.equals(this.list[i])) {
                found = true;
                break;
            }
        }

        return found;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return getLength();
    }

    @Override
    public BasicIterator iterator() {
        return new BasicIterator(list);
    }

    @Override
    public String toString() {

        /*StringBuilder stringBuilder = new StringBuilder();

        while (itr.hasNext()) {
            stringBuilder.append(itr.next());
            stringBuilder.append("|");
        }
        return String.valueOf(stringBuilder);*/
        return "";
    }

    protected void increaseModCount() {
        setModCount(getModCount() + 1);
    }

    protected void increaseLength() {
        setLength(getLength() + 1);
    }

    public class BasicIterator implements Iterator {

        private T[] list;
        private int currentSize;
        private int currentIndex = 0;
        private int expectedModCount;
        private boolean okToRemove = false;

        BasicIterator(T[] list) {
            this.list = list;
            this.currentSize = list.length;
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {

            if (this.expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return this.currentIndex < this.currentSize && this.list[currentIndex] != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return list[currentIndex++];
        }

        @Override
        public void remove() {
            try {
                ArrayList.this.remove(next());
                this.okToRemove = false;
                currentIndex--;
                expectedModCount = modCount;
            } catch (ElementNotFoundException | EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
    }
}

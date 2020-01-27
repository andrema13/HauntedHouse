package libs;

import interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    private LinearNode<T> rear, front;
    private int count;

    LinkedQueue() {
        rear = null;
        front = null;
        count = 0;
    }

    //region get-set
    public LinearNode<T> getRear() {
        return rear;
    }

    public void setRear(LinearNode<T> rear) {
        this.rear = rear;
    }

    public LinearNode<T> getFront() {
        return front;
    }

    public void setFront(LinearNode<T> front) {
        this.front = front;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //endregion

    @Override
    public void enqueue(T element) {

        LinearNode<T> linearNode = new LinearNode(element);

        if (rear == null) {
            front = linearNode;
            rear = linearNode;
        } else {
            rear.setNext(linearNode);
            rear = rear.getNext();
        }
        count++;
    }

    @Override
    public T dequeue() {

        LinearNode<T> temp;

        if (front != null) {
            temp = front;
            front = front.getNext();
            temp.setNext(null);
            count--;
        } else {
            throw new NullPointerException("Empty List");
        }
        return temp.getElement();
    }

    @Override
    public T first() {
        if (getFront() == null)
            throw new NullPointerException("Empty List");
        return getFront().getElement();
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int size() {
        return getCount();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        LinearNode<T> node;
        node = getFront();

        while (node != null) {
            stringBuilder.append(node.getElement());
            stringBuilder.append("\n");
            node = node.getNext();
        }
        return stringBuilder.toString();
    }
}

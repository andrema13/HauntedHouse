import interfaces.StackADT;

public class LinkedStack<T> implements StackADT<T> {

    private LinearNode<T> top;
    private int count;

    //region get-set
    public LinearNode<T> getTop() {
        return top;
    }

    public void setTop(LinearNode<T> top) {
        this.top = top;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    //endregion

    LinkedStack() {
        top = null;
        count = 0;
    }

    @Override
    public void push(T element) {
        LinearNode<T> node = new LinearNode(element);

        if (top == null) {
            top = node;
        } else {
            node.setNext(top);
            top = node;
            count++;
        }
    }

    @Override
    public T pop() {

        LinearNode<T> temp;

        if (top != null) {
            temp = top;
            top = top.getNext();
            temp.setNext(null);
            count--;
            return temp.getElement();
        }
        return null;
    }

    @Override
    public T peek() {
        return getTop().getElement();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return getCount();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        LinearNode<T> node;
        node = getTop();

        while (node != null) {
            stringBuilder.append(node.getElement());
            stringBuilder.append("\n");
            node = node.getNext();
        }
        return stringBuilder.toString();
    }
}

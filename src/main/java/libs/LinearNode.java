package libs;

public class LinearNode<T> implements Comparable<LinearNode<T>> {

    private String name;

    private DoubleLinkedList<LinearNode<T>> shortestPath = new DoubleLinkedList<>();

    private Integer ghostDivisionPoints = Integer.MAX_VALUE;

    private MapConnection<T> adjacentConnections;

    public void addConnection(LinearNode<T> destination, int ghostPoints) {
        this.adjacentConnections = new MapConnection<>(destination, ghostPoints);
    }

    public LinearNode(String name) {
        this.name = name;
    }


    //region get-set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DoubleLinkedList<LinearNode<T>> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(DoubleLinkedList<LinearNode<T>> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public MapConnection<T> getAdjacentConnections() {
        return adjacentConnections;
    }

    public void setAdjacentConnections(MapConnection<T> adjacentConnections) {
        this.adjacentConnections = adjacentConnections;
    }

    public Integer getGhostDivisionPoints() {
        return ghostDivisionPoints;
    }

    public void setGhostDivisionPoints(Integer ghostDivisionPoints) {
        this.ghostDivisionPoints = ghostDivisionPoints;
    }

    //endregion

    /**
     * reference to next node in list
     */
    private LinearNode<T> next;

    /**
     * element stored at this node
     */
    private T element;

    /**
     * Creates an empty node.
     */
    LinearNode() {
        next = null;
        element = null;
    }

    /**
     * Creates a node storing the specified element.
     *
     * @param elem element to be stored
     */
    LinearNode(T elem) {
        next = null;
        element = elem;
    }

    /**
     * Returns the node that follows this one.
     *
     * @return LinearNode<T> reference to next node
     */
    LinearNode<T> getNext() {
        return next;
    }

    /**
     * Sets the node that follows this one.
     *
     * @param node node to follow this one
     */
    void setNext(LinearNode<T> node) {
        next = node;
    }

    /**
     * Returns the element stored in this node.
     *
     * @return T element stored at this node
     */
    T getElement() {
        return element;
    }

    /**
     * Sets the element stored in this node.
     *
     * @param elem element to be stored at this node
     */
    void setElement(T elem) {
        element = elem;
    }

    @Override
    public int compareTo(LinearNode o) {
        return 0;//TODO
    }
}

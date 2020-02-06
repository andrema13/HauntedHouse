package libs;

import interfaces.GraphADT;

import java.util.Iterator;

/**
 * libs.Graph represents an adjacency matrix implementation of a graph.
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 20;
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices

    /**
     * Creates an empty graph.
     */
    @SuppressWarnings("unchecked")
    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) new Object[DEFAULT_CAPACITY];
    }

    //region get-set
    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public boolean[][] getAdjMatrix() {
        return adjMatrix;
    }

    public T getVertex(int index) {
        return (T) vertices[index];
    }

    //endregion

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param sourceIndexId      the id of the first index
     * @param destinationIndexId the id of the second index
     */
    private void addEdge(int sourceIndexId, int destinationIndexId) {
        if (indexIsValid(sourceIndexId) && indexIsValid(destinationIndexId)) {
            adjMatrix[sourceIndexId][destinationIndexId] = true;
            adjMatrix[destinationIndexId][sourceIndexId] = true;
        }
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Returns the index value of the first occurrence of the vertex.
     * Returns -1 if the key is not found.
     *
     * @param vertex to be tested
     * @return an int
     */
    protected int getIndex(T vertex) {

        try {
            for (int i = 0; i < vertices.length; i++) {
                if (vertices[i].equals(vertex)) {
                    return i;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("libs.Graph is empty...");
        }
        return -1;
    }

    /**
     * Removes an edge between two vertices of this graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * removes an edge between two vertices of the graph.
     *
     * @param index1 the first index
     * @param index2 the second index
     */
    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = false;

        }
    }

    /**
     * Returns an iterator that performs a breadth first search
     * traversal starting at the given vertex.
     *
     * @param startIndex start index to start the search
     * @return an iterator
     */
    @Override
    public Iterator<T> iteratorBFS(T startIndex) {
        int index = this.getIndex(startIndex);

        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if (!indexIsValid(index)) {
            return resultList.iterator();
        }
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(index);
        visited[index] = true;
        while (!traversalQueue.isEmpty()) {
            try {
                x = traversalQueue.dequeue();
                resultList.addToRear((T) vertices[x]);
                /**
                 * Find all vertices adjacent to x that have not been visited
                 * and queue them up
                 */
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[x][i] && !visited[i]) {
                        traversalQueue.enqueue(i);
                        visited[i] = true;
                    }
                }
            } catch (Exception ex) {

            }
        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator that performs a depth first search
     * traversal starting at the given index.
     *
     * @param startIndex start index to start the search
     * @return an iterator
     */
    @Override
    public Iterator<T> iteratorDFS(T startIndex) {
        int index = this.getIndex(startIndex);

        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(index)) {
            return resultList.iterator();
        }
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(index);
        resultList.addToRear((T) vertices[index]);
        visited[index] = true;
        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;
            /**
             * Find a vertex adjacent to x that has not been visited and push it
             * on the stack
             */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear((T) vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator that contains the shortest path between
     * the two vertices.
     *
     * @param startVertex  the starting vertex
     * @param targetVertex the ending vertex
     * @return an iterator
     */
    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        int startIndex = this.getIndex(startVertex);
        int targetIndex = this.getIndex(targetVertex);

        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return resultList.iterator();
        }

        Iterator<Integer> it = iteratorShortestPathIndices(startIndex,
                targetIndex);
        while (it.hasNext()) {
            resultList.addToRear((T) vertices[it.next()]);
        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator that contains the indices of the vertices
     * that are in the shortest path between the two given vertices.
     *
     * @param startIndex  initial vertex
     * @param targetIndex final vertex
     * @return an iterator
     */
    private Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) {

        int index = startIndex;
        int[] pathLength = new int[numVertices];
        int[] predecessor = new int[numVertices];
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)
                || (startIndex == targetIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        pathLength[startIndex] = 0;
        predecessor[startIndex] = -1;

        while (!traversalQueue.isEmpty() && (index != targetIndex)) {
            try {
                index = traversalQueue.dequeue();

                /**
                 * Update the pathLength for each unvisited vertex adjacent to
                 * the vertex at the current index.
                 */
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[index][i] && !visited[i]) {
                        pathLength[i] = pathLength[index] + 1;
                        predecessor[i] = index;
                        traversalQueue.enqueue(i);
                        visited[i] = true;
                    }
                }
            } catch (Exception ex) {

            }
        }
        if (index != targetIndex) // no path must have been found
        {
            return resultList.iterator();
        }

        LinkedStack<Integer> stack = new LinkedStack<>();
        index = targetIndex;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty()) {
            resultList.addToRear(stack.pop());
        }

        return resultList.iterator();
    }

    /**
     * Returns true if the graph is empty and false otherwise.
     *
     * @return a boolean
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Returns true if the graph is connected and false otherwise.
     *
     * @return a boolean
     */
    @Override
    public boolean isConnected() {

        if (isEmpty()) {
            return false;
        }
        for (int i = 0; i < numVertices; i++) {
            Iterator<T> it = iteratorBFS((T) vertices[i]);
            int count = 0;

            while (it.hasNext()) {
                it.next();
                count++;
            }
            if (count != numVertices) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the size fo the graph
     *
     * @return size of the graph
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Returns true if the given index is valid , false otherwise
     *
     * @param index1 to be tested
     * @return a boolean
     */
    protected boolean indexIsValid(int index1) {
        return ((index1 < numVertices) && (index1 >= 0));
    }

    /**
     * Creates new arrays to store the contents of the graph with twice the capacity.
     */
    public void expandCapacity() {
        T[] largerVertices = (T[]) (new Comparable[vertices.length * 2]);

        System.arraycopy(vertices, 0, largerVertices, 0, vertices.length);
        vertices = largerVertices;

        boolean[][] largerAdjMatrix = new boolean[this.numVertices * 2][this.numVertices * 2];
        for (int i = 0; i < this.numVertices; i++) {
            System.arraycopy(this.adjMatrix[i], 0, largerAdjMatrix[i], 0, this.numVertices);
        }
        this.adjMatrix = largerAdjMatrix;
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph
     * if necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();
        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    /**
     * Removes a vertex at the given index from the graph.
     *
     * @param vertex the vertex to be removed from this graph
     */
    @Override
    public void removeVertex(T vertex) {
        int index = this.getIndex(vertex);

        if (indexIsValid(index)) {
            numVertices--;

            for (int i = index; i < numVertices; i++) {
                vertices[i] = vertices[i + 1];
            }

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j <= numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i + 1][j];
                }
            }

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    adjMatrix[j][i] = adjMatrix[j][i + 1];
                }
            }
        }
    }

    public String toString() {
        if (numVertices == 0) {
            return "Graph is empty";
        }

        StringBuilder result = new StringBuilder();

        result.append("Adjacency Matrix\n");
        result.append("----------------\n");
        result.append("index\t");

        for (int i = 0; i < numVertices; i++) {
            result.append(i).append(" ");
        }
        result.append("\n\n");

        for (int i = 0; i < numVertices; i++) {
            result.append(i).append("\t");

            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j]) {
                    result.append("1 ");
                } else {
                    result.append("0 ");
                }
            }
            result.append("\n");
        }

        result.append("\n\nVertex Values");
        result.append("\n-------------\n");
        result.append("index\tvalue\n\n");

        for (int i = 0; i < numVertices; i++) {
            result.append(i).append("\t");
            result.append(vertices[i].toString()).append("\n");
        }
        result.append("\n");
        return result.toString();
    }
}
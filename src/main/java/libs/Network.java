package libs;

import interfaces.NetworkADT;
import java.util.Iterator;

/**
 * libs.Graph represents an adjacency matrix implementation of a graph.
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {
    private Double[][] weightedAdjMatrix; // adjacency matrix

    /**
     * Creates an empty graph.
     */

    public Network() {
        super();
        this.weightedAdjMatrix = new Double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    }

    public Double[][] getWeightedAdjMatrix() {
        return this.weightedAdjMatrix;
    }

    //endregion

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param sourceIndexId      the id of the first index
     * @param destinationIndexId the id of the second index
     */
    private void addEdge(int sourceIndexId, int destinationIndexId, double weight) {
        if (indexIsValid(sourceIndexId) && indexIsValid(destinationIndexId)) {
            weightedAdjMatrix[sourceIndexId][destinationIndexId] = weight;
        }
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    /**
     * Returns the weight of the least weight path in the network.
     * Returns positive infinity if no path is found.
     **/
    @Override
    public double shortestPathWeight(T startVertex, T targetVertex) {
        return shortestPathWeight(getIndex(startVertex), getIndex(targetVertex));
    }

    /**
     * Returns the weight of the least weight path in the network.
     * Returns positive infinity if no path is found.
     *
     * @param startIndex  starting vertex index
     * @param targetIndex final vertex index
     * @return shortest path
     */
    private double shortestPathWeight(int startIndex, int targetIndex) {
        double result = 0;
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return Double.POSITIVE_INFINITY;

        int index1, index2;
        Iterator<Integer> it = iteratorShortestPathIndices(startIndex, targetIndex);

        if (it.hasNext())
            index1 = it.next();
        else
            return Double.POSITIVE_INFINITY;

        while (it.hasNext()) {
            index2 = it.next();
            result += weightedAdjMatrix[index1][index2];
            index1 = index2;
        }

        return result;
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

        Iterator<Integer> it = iteratorShortestPathIndices(startIndex, targetIndex);
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

        int index;
        double weight;
        int[] predecessor = new int[numVertices];
        ArrayOrderedList<Double> weightsOrdered = new ArrayOrderedList<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();

        int[] pathIndex = new int[numVertices];
        double[] pathWeight = new double[numVertices];
        for (int i = 0; i < numVertices; i++)
            pathWeight[i] = Double.POSITIVE_INFINITY;

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) ||
                (startIndex == targetIndex) || isEmpty())
            return resultList.iterator();

        pathWeight[startIndex] = 0;
        predecessor[startIndex] = -1;
        visited[startIndex] = true;
        weight = 0;

        /** Update the pathWeight for each vertex except the
         startVertex. Notice that all vertices not adjacent
         to the startVertex  will have a pathWeight of
         infinity for now. */
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                pathWeight[i] = pathWeight[startIndex] + weightedAdjMatrix[startIndex][i];
                predecessor[i] = startIndex;
                weightsOrdered.add(pathWeight[i]);
            }
        }

        do {
            weight = weightsOrdered.removeFirst();
            //weightsOrdered.clear();
            if (weight == Double.POSITIVE_INFINITY)  // no possible path
                return resultList.iterator();
            else {
                index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
                visited[index] = true;
            }

            /** Update the pathWeight for each vertex that has has not been
             visited and is adjacent to the last vertex that was visited.
             Also, add each unvisited vertex to the heap. */
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i]) {
                    if ((weightedAdjMatrix[index][i] < Double.POSITIVE_INFINITY) &&
                            (pathWeight[index] + weightedAdjMatrix[index][i]) < pathWeight[i]) {
                        pathWeight[i] = pathWeight[index] + weightedAdjMatrix[index][i];
                        predecessor[i] = index;
                    }
                    weightsOrdered.add(pathWeight[i]);
                }
            }
        } while (!weightsOrdered.isEmpty() && !visited[targetIndex]);

        index = targetIndex;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty())
            resultList.addToRear((stack.pop()));

        return resultList.iterator();
    }

    /**
     * Returns the index of the the vertex that that is adjacent to
     * the vertex with the given index and also has a pathWeight equal
     * to weight.
     */
    private int getIndexOfAdjVertexWithWeightOf(boolean[] visited, double[] pathWeight, double weight) {
        for (int i = 0; i < numVertices; i++)
            if ((pathWeight[i] == weight) && !visited[i])
                for (int j = 0; j < numVertices; j++)
                    if ((weightedAdjMatrix[i][j] < Double.POSITIVE_INFINITY) && visited[j])
                        return i;

        return -1;  // should never get to here
    }

    /**
     * Checks if exists a connections between this two points
     * @param connection1  starting connection
     * @param connection2  finish connection
     * @return true if exists false otherwise
     */
    public boolean checkIfConnectionExits(int connection1, int connection2) {
        return this.weightedAdjMatrix[connection1][connection2] != null;
    }

    @Override
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
                if (weightedAdjMatrix[i][j] >= 0) {
                    result.append(weightedAdjMatrix[i][j]).append(" ");
                } else {
                    result.append("|?| ");
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
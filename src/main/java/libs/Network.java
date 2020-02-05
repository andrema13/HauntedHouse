package libs;

import interfaces.NetworkADT;

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

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        return 0;
    }

    /**
     * Checks if exists a connections between this two points
     *
     * @param connection1 starting connection
     * @param connection2 finish connection
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
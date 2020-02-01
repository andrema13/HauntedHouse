import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import libs.*;

public class Graph<T extends Comparable<T>> {

    private DoubleLinkedUnorderedList<T> nodes = new DoubleLinkedUnorderedList<>();

    public void addNode(T nodeA) {
        nodes.addToFront(nodeA);
    }

    //region get-set

    public DoubleLinkedUnorderedList<T> getNodes() {
        return nodes;
    }

    public void setNodes(DoubleLinkedUnorderedList<T> nodes) {
        this.nodes = nodes;
    }

    //endregion

    public Graph<T> calculateShortestPathFromSource(Graph<T> graph, LinearNode<T> source)
            throws ElementNotFoundException, EmptyCollectionException {

        source.setGhostDivisionPoints(0);

        DoubleLinkedUnorderedList<LinearNode<T>> settledNodes = new DoubleLinkedUnorderedList<>();
        DoubleLinkedUnorderedList<LinearNode<T>> unsettledNodes = new DoubleLinkedUnorderedList<>();

        unsettledNodes.addToFront(source);

        while (unsettledNodes.size() != 0) {

            LinearNode<T> currentNode = getLowestDivisionGhostPoints(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for(int i = 0; i < currentNode.getAdjacentConnections().getGhostTakenPoints();i++){
                for(LinearNode<T> node : currentNode.getAdjacentConnections().getDestinyDivision()){
                    LinearNode<T> adjacentNode = adjacencyPair.getKey();//encontrar o node
                    Integer edgeWeight = adjacencyPair.getValue(); //colocar o valor dos pontos do ghost
                    if (!settledNodes.contains(adjacentNode)) {
                        CalculateMinimumGhostPointsPerDivision(adjacentNode, edgeWeight, currentNode);
                        unsettledNodes.addToFront(adjacentNode);
                    }
                }
            }
            settledNodes.addToFront(currentNode);
        }
        return graph;
    }

    private LinearNode<T> getLowestDivisionGhostPoints(DoubleLinkedUnorderedList<LinearNode<T>> unsettledNodes) {

        LinearNode<T> lowestGhostDivisionPoints = null;
        int lowestGhostPoints = Integer.MAX_VALUE;
        for (LinearNode<T> node : unsettledNodes) {
            int ghostDivisionPoints = node.getGhostDivisionPoints();
            if (ghostDivisionPoints < lowestGhostPoints) {
                lowestGhostPoints = ghostDivisionPoints;
                lowestGhostDivisionPoints = node;
            }
        }
        return lowestGhostDivisionPoints;
    }

    private void CalculateMinimumGhostPointsPerDivision(LinearNode<T> evaluationNode,
                                                        Integer ghostPoints, LinearNode<T> sourceNode) {

        Integer sourceGhostPoints = sourceNode.getGhostDivisionPoints();

        if (sourceGhostPoints + ghostPoints < evaluationNode.getGhostDivisionPoints()) {

            evaluationNode.setGhostDivisionPoints(sourceGhostPoints + ghostPoints);
            DoubleLinkedUnorderedList<LinearNode<T>> shortestPath =
                    new DoubleLinkedUnorderedList<>(sourceNode.getShortestPath());

            shortestPath.addToFront(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
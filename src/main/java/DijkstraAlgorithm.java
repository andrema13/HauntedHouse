import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import libs.DoubleLinkedUnorderedList;

public class DijkstraAlgorithm<T extends Comparable<T>> {

    /*public static DoubleLinkedUnorderedList<Division> calculateShortestPathFromSource(Division startPoint)
            throws ElementNotFoundException, EmptyCollectionException {

        DoubleLinkedUnorderedList<Division> settledNodes = new DoubleLinkedUnorderedList<>();
        DoubleLinkedUnorderedList<Division> unsettledNodes = new DoubleLinkedUnorderedList<>();
        unsettledNodes.addToRear(startPoint);

        while (unsettledNodes.size() != 0) {

            Division currentNode = getLowestDivisionGhostPoints(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (MapConnection<Division> mapConn : currentNode.getAdjacentConnections()) {
                Division adjacentNode = mapConn.getDestinyDivision();//encontrar o node
                Integer edgeWeight = mapConn.getGhostTakenPoints(); //colocar o valor dos pontos do ghost

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumGhostPointsPerDivision(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.addToFront(adjacentNode);
                }
            }
            settledNodes.addToRear(currentNode);
        }
        return settledNodes;
    }

 */
/*
    private static void CalculateMinimumGhostPointsPerDivision(
            Division evaluationNode,
            Integer ghostPoints,
            Division sourceNode
    ) {

        Integer sourceGhostPoints = sourceNode.getGhostPoints();

        if (sourceGhostPoints + ghostPoints < evaluationNode.getGhostPoints()) {

            evaluationNode.setGhostPoints(sourceGhostPoints + ghostPoints);
            DoubleLinkedUnorderedList<Division> shortestPath = sourceNode.getShortestPath();//TODO Maybe error here

            shortestPath.addToFront(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
*/
    /* private static Division getLowestDivisionGhostPoints(DoubleLinkedUnorderedList<Division> unsettledNodes) {

        Division lowestGhostDivisionPoints = null;
        int lowestGhostPoints = Integer.MAX_VALUE;

        for (Division node : unsettledNodes) {

            int ghostDivisionPoints = node.getGhostPoints();

            if (ghostDivisionPoints < lowestGhostPoints) {
                lowestGhostPoints = ghostDivisionPoints;
                lowestGhostDivisionPoints = node;
            }
        }
        return lowestGhostDivisionPoints;
    }
     */
}
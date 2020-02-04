import libs.DoubleLinkedList;
import libs.DoubleLinkedUnorderedList;
import libs.Network;

public class DijkstraAlgorithm<T extends Comparable<T>> extends Network<T> {

    public void DijkstraShortestPath(Division start, Division end) {
        // We keep track of which path gives us the shortest path for each node
        // by keeping track how we arrived at a particular node, we effectively
        // keep a "pointer" to the parent node of each node, and we follow that
        // path to the start
        DoubleLinkedUnorderedList<ShortestPathData<Division>> changedAt =
                new DoubleLinkedUnorderedList<>();
        changedAt.addToRear(new ShortestPathData<>(start, 0));//weight null

        // Keeps track of the shortest path we've found so far for every node
        DoubleLinkedUnorderedList<ShortestPathData<Division>> shortestPathData = new DoubleLinkedUnorderedList<>();

        // Setting every node's shortest path weight to positive infinity to start
        // except the starting node, whose shortest path weight is 0
        for (int i = 0; i < getNumVertices(); i++) {
            if (getVertex(i) == start) {
                shortestPathData.addToRear(new ShortestPathData<>(start, 0));
            } else {
                shortestPathData.addToRear(new ShortestPathData<>((Division) getVertex(i), Double.POSITIVE_INFINITY));
            }
        }


        // Now we go through all the nodes we can go to from the starting node
        // (this keeps the loop a bit simpler)
        for (int i = 0; i < getNumVertices(); i++) {
            for (int j = 0; j < getNumVertices(); j++) {

                if (checkIfConnectionExits(start.getId(), getIndex(getVertex(i)))) {
                    shortestPathData.addToRear(new ShortestPathData<>(
                            (Division) getVertex(i),
                            getAdjMatrix()[start.getId()][getIndex(getVertex(i))]));
                    changedAt.addToRear(new ShortestPathData<>((Division) getVertex(i),
                            getAdjMatrix()[getIndex(getVertex(i))][start.getId()]));
                }
            }

        }

        start.visit();

        // This loop runs as long as there is an unvisited node that we can
        // reach from any of the nodes we could till then
        while (true) {
            Division currentNode = closestReachableUnvisited(shortestPathData);
            // If we haven't reached the end node yet, and there isn't another
            // reachable node the path between start and end doesn't exist
            // (they aren't connected)
            if (currentNode == null) {
                System.out.println("There isn't a path between " + start.getName() + " and " + end.getName());
                return;
            }

            // If the closest non-visited node is our destination, we want to print the path
            if (currentNode == end) {
                System.out.println("The path with the smallest weight between "
                        + start.getName() + " and " + end.getName() + " is:");

                Division child = end;

                // It makes no sense to use StringBuilder, since
                // repeatedly adding to the beginning of the string
                // defeats the purpose of using StringBuilder
                String path = end.getName();
                while (true) {
                    try {
                        Division parent = null;
                        DoubleLinkedList<ShortestPathData<Division>>.DoubleIterator iterator = changedAt.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getT().equals(child)) {
                                parent = iterator.next().getT();
                                if (parent == null) {
                                    break;
                                }
                            }
                            assert parent != null;//TODO -PREGO
                            path = parent.getName() + " " + path;
                            child = parent;

                            if (iterator.next().getT().equals(end)) {
                                parent = iterator.next().getT();

                                // Since our changedAt map keeps track of child -> parent relations
                                // in order to print the path we need to add the parent before the child and
                                // it's descendants
                                System.out.println(path);
                                System.out.println("The path costs: " + parent);
                            }
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            currentNode.visit();

            // Now we go through all the unvisited nodes our current node has an edge to
            // and check whether its shortest path value is better when going through our
            // current node than whatever we had before

            for (int i = 0; i < getNumVertices(); i++) {
                for (int j = 0; j < getNumVertices(); j++) {
                    if (((Division) getVertex(j)).isVisited())
                        continue;

                    if (shortestPathData.containsTarget(currentNode) + edge.weight
                            < shortestPathData.get(edge.destination)) {
                        shortestPathData.addToRear((Division) getVertex(j)) (edge.destination,
                                shortestPathMap.get(currentNode) + edge.weight);
                        changedAt.put(edge.destination, currentNode);
                    }
                }
            }
        }
    }


    private Division closestReachableUnvisited(DoubleLinkedUnorderedList<ShortestPathData<Division>> shortestPathData) {

        double shortestDistance = Double.POSITIVE_INFINITY;
        Division closestReachableNode = null;

        for (Division node : nodes) {
            if (node.isVisited())
                continue;

            double currentDistance = shortestPathData.get(node);
            if (currentDistance == Double.POSITIVE_INFINITY)
                continue;

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestReachableNode = node;
            }
        }
        return closestReachableNode;
    }
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
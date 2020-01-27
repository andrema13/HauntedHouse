import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interfaces.GraphADT;
import libs.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class MapGame<T extends Comparable<T>> extends AdjacencyMatrix<T> implements GraphADT<T> {

    private String name;
    private int initialPlayerPoints;
    private DoubleLinkedOrderedList<Player> playersList;
    private GameLevel gameLevel;
    private DoubleLinkedUnorderedList<Division> divisionsList;

    public MapGame() {
        this.playersList = new DoubleLinkedOrderedList<>();
    }

    //region get-set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DoubleLinkedOrderedList<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(DoubleLinkedOrderedList<Player> playersList) {
        this.playersList = playersList;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public DoubleLinkedUnorderedList<Division> getDivisionsList() {
        return divisionsList;
    }

    public void setDivisionsList(DoubleLinkedUnorderedList<Division> divisionsList) {
        this.divisionsList = divisionsList;
    }

    public int getInitialPlayerPoints() {
        return initialPlayerPoints;
    }

    public void setInitialPlayerPoints(int initialPlayerPoints) {
        this.initialPlayerPoints = initialPlayerPoints;
    }

    //endregion

    public boolean addNewPlayer(Player player) {

        try {
            getPlayersList().add(player);
            return true;
        } catch (NullPointerException ex) {
            System.out.println("Player is null");
        }
        return false;
    }

    /*public boolean removePlayer(Player player) throws ElementNotFoundException, EmptyCollectionException {

        DoubleLinkedList<Player>.DoubleIterator itr = getPlayersList().iterator();

        while (itr.hasNext()) {
            if (getPlayersList().contains(player)) {
                getPlayersList().remove(player);
                itr.next();
                return true;
            }
        }
        return false;
    }*/

    protected void changePointsPerDivisionByGameLevel(GameLevel gameLevel) {

        DoubleLinkedList<Division>.DoubleIterator iterator = getDivisionsList().iterator();
        while (iterator.hasNext()) {
            Division str = iterator.next();
            str.setGhostPoints(str.getGhostPoints(), gameLevel);
        }
    }

    private boolean addNewDivision(Division division) {

        try {
            getDivisionsList().addToRear(division);
            return true;
        } catch (NullPointerException ex) {
            System.out.println("Division Exception");
        }
        return false;
    }

    public void readFromJSONFile(String path) {

        AdjacencyMatrix<String> mapAdjacencyMatrixGame = new AdjacencyMatrix<>();
        ArrayUnorderedList<T> tempVertices = new ArrayUnorderedList<>();
        this.divisionsList = new DoubleLinkedUnorderedList<>();

        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(path));

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();//gets the name of the game
            int points = jsonObject.get("points").getAsInt();//gets the starting points of the player
            JsonArray map = jsonObject.get("map").getAsJsonArray();

            this.setName(name);//sets the name of the game
            this.setInitialPlayerPoints(points);//sets the initial Player points

            JsonObject mapObjects;

            //adds the vertices
            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                String divisionName = mapObjects.get("place").getAsString();//gets the actual place in map array
                int ghostTakenPoints = mapObjects.get("ghost").getAsInt();// points taken by the ghost
                mapAdjacencyMatrixGame.addVertex(divisionName);//adds a vertex to the graph
                tempVertices.addToRear((T) divisionName);// adds to the temporary array in the rear
                addNewDivision(new Division(divisionName, ghostTakenPoints));
            }
            boolean exitExistsInGraph = false;//flag to when is find the exit vertex

            //adds the existing connections
            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                JsonArray connections = mapObjects.getAsJsonArray("connections");
                String[] possible_connections = new String[connections.size()];//gets the size of the connections

                for (int j = 0; j < connections.size(); j++) {
                    possible_connections[j] = connections.get(j).getAsString();//gets the connections available
                    if (possible_connections[j].equals("entry")) {//if is find the entry vertex
                        mapAdjacencyMatrixGame.addVertex(possible_connections[j]);//adds the only vertex
                        mapAdjacencyMatrixGame.addEdge(possible_connections[j], (String) tempVertices.getList()[i]);//adds a edge
                        mapAdjacencyMatrixGame.removeEdge((String) tempVertices.getList()[i], possible_connections[j]);
                    } else if (possible_connections[j].equals("exit") && !exitExistsInGraph) {
                        //just to add one vertex exit only one time
                        mapAdjacencyMatrixGame.addVertex(possible_connections[j]);
                        mapAdjacencyMatrixGame.addEdge((String) tempVertices.getList()[i], possible_connections[j]);
                        mapAdjacencyMatrixGame.removeEdge(possible_connections[j], (String) tempVertices.getList()[i]);
                        exitExistsInGraph = true;
                    } else if (possible_connections[j].equals("exit") && exitExistsInGraph) {
                        //adds the vertex to the exit point
                        mapAdjacencyMatrixGame.addEdge((String) tempVertices.getList()[i], possible_connections[j]);
                        mapAdjacencyMatrixGame.removeEdge(possible_connections[j], (String) tempVertices.getList()[i]);
                    } else {
                        for (int l = 0; l < tempVertices.size(); l++) {
                            if (tempVertices.getList()[l].equals(possible_connections[j])) {
                                //adds the edge between the vertexes
                                mapAdjacencyMatrixGame.addEdge((String) tempVertices.getList()[i], (String) tempVertices.getList()[l]);
                                mapAdjacencyMatrixGame.addEdge((String) tempVertices.getList()[l], (String) tempVertices.getList()[i]);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        // System.out.println(mapAdjacencyMatrixGame.toString());
    }

    @Override
    public String toString() {
        return "Name = " + name + "\n" +
                " GameLevel = " + gameLevel + "\n" +
                " PlayersList = " + playersList + "\n" +
                " Divisions = " + divisionsList;
    }
}


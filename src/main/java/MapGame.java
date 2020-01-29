import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interfaces.GraphADT;
import libs.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MapGame<T extends Comparable<T>> extends AdjacencyMatrix<T> implements GraphADT<T> {

    private String name;
    private int playerPoints;
    private DoubleLinkedOrderedList<Player> playersList;
    private GameLevel gameLevel;
    private DoubleLinkedUnorderedList<Division> divisionsList;
    private DoubleLinkedUnorderedList<Connection>[][] mapGameMatrix;
    private int idConnection;
    private int divisionsId;

    public MapGame() {
        this.mapGameMatrix = new DoubleLinkedUnorderedList[super.DEFAULT_CAPACITY][super.DEFAULT_CAPACITY];
        for (int i = 0; i < this.DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < this.DEFAULT_CAPACITY; j++) {
                mapGameMatrix[i][j] = new DoubleLinkedUnorderedList<>();
            }
        }
        this.idConnection = 0;
        this.divisionsId = 0;
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

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

    public DoubleLinkedUnorderedList<Division> getDivisionsList() {
        return divisionsList;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    //endregion

    protected boolean addNewPlayer(Player player) {
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
            str.changeGhostPoints(str.getGhostPoints(), gameLevel);
        }
        setGameLevel(gameLevel);
    }

    private void addNewDivision(Division division) {

        if (super.numVertices == super.vertices.length) {
            super.expandCapacity();
        }
        super.addVertex((T) division);
    }

    public void addNewConnection(T origin, T destiny, Connection connection) {
        super.addEdge(origin, destiny);
        int index1 = super.getIndex(origin);
        int index2 = super.getIndex(destiny);
        this.idConnection++;
        connection.setId(this.idConnection);
        this.mapGameMatrix[index1][index2].addToRear(connection);
      /*  System.out.println(connection.toString());
        System.out.println("Origin Division ->" + origin + "Destiny Division -> " + destiny);*/
    }

    /*public void removeConnection(T origin, T destiny, int connectionId) throws EmptyCollectionException {
        Connection tempConnection;
        int indexOne = super.getIndex(origin);
        int indexTwo = super.getIndex(destiny);
        int numberOfConnectionsBefore = this.mapGameMatrix[indexOne][indexTwo].size();

        for (int i = 0; i < this.mapGameMatrix[indexOne][indexTwo].size(); i++) {
            tempConnection = this.mapGameMatrix[indexOne][indexTwo].removeLast();
            if (tempConnection.getId() != connectionId) {
                this.mapGameMatrix[indexOne][indexTwo].addToFront(tempConnection);
            }
        }
        if (this.mapGameMatrix[indexOne][indexTwo].size() == 0) {
            super.removeEdge(origin, destiny);
        }
        if (numberOfConnectionsBefore == this.mapGameMatrix[indexOne][indexTwo].size()) {
            //TODO throw Exception
            System.out.println("Exception index don't exist");
        }
    }*/

    public void readFromJSONFile(String path) {

        this.divisionsList = new DoubleLinkedUnorderedList<>();
        Division entry = null;
        Division exit = null;

        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(path));

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();//gets the name of the game
            int points = jsonObject.get("points").getAsInt();//gets the starting points of the player
            JsonArray map = jsonObject.get("map").getAsJsonArray();

            this.setName(name);//sets the name of the game
            this.setPlayerPoints(points);//sets the initial Player points

            JsonObject mapObjects;

            //adds the vertices
            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                String divisionName = mapObjects.get("place").getAsString();//gets the actual place in map array
                int ghostTakenPoints = mapObjects.get("ghost").getAsInt();// points taken by the ghost
                Division division = new Division(this.divisionsId, divisionName, ghostTakenPoints);
                this.divisionsId++;
                addNewDivision(division);
                this.divisionsList.addToRear(division);
                // adds a new division to the rear of the list
            }

            boolean exitExistsInGraph = false;//flag to when is find the exit vertex

            DoubleLinkedList<Division>.DoubleIterator iterator = getDivisionsList().iterator();


            while (iterator.hasNext()) {//iterates for the divisions list

                //adds the existing connections
                for (int i = 0; i < map.size(); i++) {

                    mapObjects = map.get(i).getAsJsonObject();
                    JsonArray connections = mapObjects.getAsJsonArray("connections");

                    Division connectionDivision = iterator.next();//goes to the next division

                    for (int j = 0; j < connections.size(); j++) {//for cycle to find the connections between divisions

                        if (connections.get(j).getAsString().equals("entry")) {//if is find the entry vertex
                            entry = new Division(this.divisionsId, connections.get(j).getAsString(),
                                    0);//TODO
                            divisionsId++;
                            addNewDivision(entry);//adds new vertex
                            addNewConnection((T) entry, (T) connectionDivision,
                                    new Connection(connectionDivision.getGhostPoints()));
                            entry.addConnection(connectionDivision);
                        } else if (connections.get(j).getAsString().equals("exit") && !exitExistsInGraph) {
                            //just to add one vertex exit only one time
                            exit = new Division(this.divisionsId, connections.get(j).getAsString(),
                                    0);//TODO
                            this.divisionsId++;
                            addNewDivision(exit);//adds new vertex
                            addNewConnection((T) connectionDivision, (T) exit,
                                    new Connection(exit.getGhostPoints()));
                            connectionDivision.addConnection(exit);
                            exitExistsInGraph = true;
                        } else if (connections.get(j).getAsString().equals("exit") && exitExistsInGraph) {

                            addNewConnection((T) connectionDivision, (T) exit,
                                    new Connection(exit.getGhostPoints()));//TODO
                            connectionDivision.addConnection(exit);
                        } else {
                            DoubleLinkedList<Division>.DoubleIterator divisionsList = getDivisionsList().iterator();
                            while (divisionsList.hasNext()) {
                                Division tempDivision = divisionsList.next();
                                if (tempDivision.getName().equals(connections.get(j).getAsString())) {
                                    addNewConnection((T) connectionDivision, (T) tempDivision,
                                            new Connection(tempDivision.getGhostPoints()));
                                    connectionDivision.addConnection(tempDivision);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        this.divisionsList.addToRear(entry);
        this.divisionsList.addToRear(exit);
    }

    public void printConnections(int origin, int destiny) {
        if (mapGameMatrix[origin][destiny].size() == 0) {
            System.out.println("There's no connections between this vertexes");
        }
        System.out.println(mapGameMatrix[origin][destiny].toString());
    }

   /* @Override
    public Iterator iteratorDFS(T division) {
        return super.iteratorDFS(division);
    }

    @Override
    public Iterator iteratorBFS(T division) {
        return super.iteratorBFS(division);
    }*/

    protected Division getEntry() {

        Division temp = null;
        DoubleLinkedList<Division>.DoubleIterator iterator = getDivisionsList().iterator();

        while (iterator.hasNext()) {
            Division connectionDivision = iterator.next();//goes to the next division

            if (connectionDivision.getName().equals("entry")) {
                temp = connectionDivision;
                break;
            }
        }
        return temp;
    }

    protected void startingPoint(Division startingDivision) {

        Scanner scanner = new Scanner(System.in);
        DoubleLinkedList<Division>.DoubleIterator iterator = getDivisionsList().iterator();

        while (iterator.hasNext()) {
            Division connectionDivision = iterator.next();//goes to the next division

            if (connectionDivision.equals(startingDivision)) {
                System.out.println("Points: " + getPlayerPoints());
                connectionDivision.printConnections();
                System.out.println("Choose the next move: ");
                int playerChoice = scanner.nextInt();
                if (super.adjMatrix[startingDivision.getId()][playerChoice]) { //check if the connection exist
                    moveToAnotherDivision(playerChoice, connectionDivision);
                    setPlayerPoints(getPlayerPoints() - connectionDivision.getGhostPoints());
                    System.out.println("Points: " + getPlayerPoints());
                } else {
                    System.out.println("Wrong choice. Pick a valid connection!");
                    startingPoint(startingDivision);
                }

            }
        }
    }

    protected void moveToAnotherDivision(int divisionId, Division origin) {

        Scanner scanner = new Scanner(System.in);
        DoubleLinkedList<Division>.DoubleIterator iterator = getDivisionsList().iterator();

        while (iterator.hasNext()) {
            Division connectionDivision = iterator.next();//goes to the next division;

            if (connectionDivision.getName().equals("exit")) {
                if (super.adjMatrix[origin.getId()][connectionDivision.getId()]) {
                    System.out.println("Finish Points: " + getPlayerPoints());
                    System.out.println("Finish");
                } else {
                    System.out.println("Wrong choice. Pick a valid connection!");
                    startingPoint(origin);
                }
                break;
            }
            if (connectionDivision.getId() == divisionId) {
                if (super.adjMatrix[origin.getId()][connectionDivision.getId()]) {
                    setPlayerPoints(getPlayerPoints() - connectionDivision.getGhostPoints());
                    System.out.println("Points: " + getPlayerPoints());
                    connectionDivision.printConnections();
                    System.out.println("Choose the next move: ");
                    int playerChoice = scanner.nextInt();
                    moveToAnotherDivision(playerChoice, connectionDivision);
                } else {
                    System.out.println("Wrong choice. Pick a valid connection!");
                    startingPoint(origin);
                }
                break;
            }
        }
    }

    @Override
    public String toString() {
        return " Name = " + name + "\n" +
                " GameLevel = \t" + gameLevel + "\n" +
                " PlayersList: \n" + playersList + "\n" +
                " Divisions: \n" + divisionsList;
    }
}


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import libs.DoubleLinkedOrderedList;
import libs.Network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MapGame {

    private String name;
    private int playerPoints;
    private DoubleLinkedOrderedList<Player> playersList;
    private GameLevel gameLevel;
    private Network<Division> graph;

    public MapGame() {
        this.graph = new Network<>();
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

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    //endregion

    protected void addNewPlayer(Player player) {
        try {
            getPlayersList().add(player);
        } catch (NullPointerException ex) {
            System.out.println("Player is null");
        }
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

    public void readFromJSONFile(String path) {
        int divisionsId = 0;

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
            Division entryDivision = new Division(divisionsId++, "entry");

            this.graph.addVertex(entryDivision);
            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                String divisionName = mapObjects.get("place").getAsString();//gets the actual place in map array
                Division division = new Division(divisionsId++, divisionName);
                this.graph.addVertex(division);
            }
            Division exitDivision = new Division(divisionsId, "exit");
            this.graph.addVertex(exitDivision);

            //adds the existing connections
            for (int i = 0; i < map.size(); i++) {

                mapObjects = map.get(i).getAsJsonObject();
                String sourceDivisionName = mapObjects.get("place").getAsString();//gets the actual place in map array
                JsonArray connections = mapObjects.getAsJsonArray("connections");

                Division sourceDivision = getDivisionByName(sourceDivisionName);

                for (int j = 0; j < connections.size(); j++) {//for cycle to find the connections between divisions
                    String destinationDivisionName = connections.get(j).getAsString();
                    Division destinationDivision = getDivisionByName(destinationDivisionName);
                    int ghostPoints;

                    if (destinationDivisionName.equals("entry")) {
                        ghostPoints = getGhostTakenPoints(map, destinationDivision.getName());//sets the weight of the edge
                        this.graph.addEdge(destinationDivision, sourceDivision,
                                ghostPoints * getGameLevel().getGameLevelValue());//sets the weight of the
                        //edge multiplier by the gameLevel
                    } else {
                        ghostPoints = getGhostTakenPoints(map, destinationDivision.getName());//sets the weight of the edge
                        this.graph.addEdge(sourceDivision, destinationDivision,
                                ghostPoints * getGameLevel().getGameLevelValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        mapGameInConsole();
        System.out.println(
                graph.shortestPathWeight(getDivisionByName("entry"),
                        getDivisionByName("hall")));
    }

    private int getGhostTakenPoints(JsonArray map, String divisionName) {
        for (int i = 0; i < map.size(); i++) {
            JsonObject mapDivisions = map.get(i).getAsJsonObject();
            String sourceDivisionName = mapDivisions.get("place").getAsString();//gets the actual place in map array
            int ghostTakenPoints = mapDivisions.get("ghost").getAsInt();// points taken by the ghost
            if (sourceDivisionName.equals(divisionName)) {
                return ghostTakenPoints;
            }
        }
        return 0;
    }

    private Division getDivisionByName(String divisionName) {
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Division division = this.graph.getVertex(i);
            if (division.getName().equals(divisionName)) {
                return division;
            }
        }
        throw new IndexOutOfBoundsException("Division not found");
    }

    private Division getDivisionById(int divisionId) {
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Division division = this.graph.getVertex(i);
            if (division.getId() == divisionId) {
                return division;
            }
        }
        throw new IndexOutOfBoundsException("Division not found");
    }

    protected void startingPoint() throws ElementNotFoundException, EmptyCollectionException {

        Scanner scanner = new Scanner(System.in);
        Player player = getPlayersList().first();

        Division startingPoint = getDivisionByName("entry");

        System.out.println("     *****************************");
        System.out.println(String.format("%-15s %-45s", " ", "Division"));
        System.out.println(String.format("%-15s %-42s", " ", "|" + startingPoint.getName() + "|" + "\n"));
        System.out.println("     Player Points: " + player.getPoints() + "\n");
        System.out.println("     Possible next Divisions :");
        //print connections
        printConnections(startingPoint);
        System.out.println("\nChoose the next move: ");
        int playerChoice = scanner.nextInt();

        if (this.graph.checkIfConnectionExits(startingPoint.getId(), playerChoice)) { //check if the connection exist
            System.out.println("     *****************************");
            moveToAnotherDivision(startingPoint.getId(), getDivisionById(playerChoice));
        } else {
            System.out.println("MESSAGE: Wrong choice. Pick a valid connection!");
            System.out.println("     *****************************");
            startingPoint();
        }
    }


    protected void moveToAnotherDivision(int originId, Division division)
            throws ElementNotFoundException, EmptyCollectionException {

        Player player = getPlayersList().first();
        int playerChoice;

        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Scanner scanner = new Scanner(System.in);

            if (this.graph.getVertex(i).getName().equals("exit") && player.getPoints() > 0) {
                if (this.graph.checkIfConnectionExits(originId,
                        this.graph.getVertex(i).getId())) {
                    System.out.println("MESSAGE: You Won! Congratulations :)");
                    finishScreen();
                } else if (!this.graph.checkIfConnectionExits(originId, this.graph.getVertex(i).getId())) {
                    System.out.println("MESSAGE: Wrong choice. Pick a valid connection!");
                    moveToAnotherDivision(originId, division);
                } else {
                    System.out.println("MESSAGE: Game Over! :(");
                    gameOverScreen();
                }
                return;

            } else if (this.graph.getVertex(i).getId() == division.getId() && player.getPoints() > 0) {

                if (this.graph.checkIfConnectionExits(originId,
                        this.graph.getVertex(i).getId())) {

                    player.setPoints(player.getPoints() - this.graph.getAdjMatrix()[originId][i]);
                    System.out.println("     *****************************");
                    System.out.println(String.format("%-15s %-45s", " ", "Division"));
                    System.out.println(String.format("%-15s %-42s ", " ",
                            "|" + this.graph.getVertex(i).getName() + "|\n"));
                    System.out.println("     Player Points: " + player.getPoints() + "\n");
                    System.out.println("     Possible next Divisions :");
                    printConnections(division);
                    System.out.println("\nChoose the next move: ");
                    playerChoice = scanner.nextInt();
                    System.out.println("     *****************************");
                    moveToAnotherDivision(division.getId(), getDivisionById(playerChoice));

                } else if (!this.graph.checkIfConnectionExits(division.getId(),
                        this.graph.getVertex(i).getId())) {
                    System.out.println("MESSAGE: Wrong choice. Pick a valid connection!");
                    System.out.println("     *****************************\n");
                    System.out.println(String.format("%-15s %-45s", " ", "Division"));
                    System.out.println(String.format("%-15s %-42s ", " ",
                            "|" + getDivisionById(originId).getName() + "|\n"));
                    System.out.println("     Player Points: " + player.getPoints() + "\n");
                    System.out.println("     Possible next Divisions :");
                    printConnections(getDivisionById(originId));
                    System.out.println("\nChoose the next move: ");
                    playerChoice = scanner.nextInt();
                    System.out.println("     *****************************");
                    moveToAnotherDivision(originId, getDivisionById(playerChoice));
                } else {
                    System.out.println("MESSAGE: Game Over! :(");
                    gameOverScreen();
                }
                return;
            } else if (getPlayerPoints() <= 0) {
                System.out.println("MESSAGE: Game Over! :(");
                gameOverScreen();
                return;
            }
        }
    }

    private void printConnections(Division origin) {

        for (int j = 0; j < this.graph.getNumVertices(); j++) {
            if (this.graph.checkIfConnectionExits(origin.getId(),
                    this.graph.getVertex(j).getId())) {
                System.out.println(this.graph.getVertex(j).toString());
            }
        }
    }

    private void gameOverScreen() throws ElementNotFoundException, EmptyCollectionException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("     *****************************");
        System.out.println("     Game Over  \n");
        System.out.println("     Do you want to try Again ? ");
        System.out.println("     1 - Yes ");
        System.out.println("     2 - No  ");
        switch (scanner.nextInt()) {
            case 1:
                Main main = new Main();
                main.initGame();
                break;
            case 2:
                System.out.println("MESSAGE: Program Closed");
                break;
            default:
                System.out.println("MESSAGE: Wrong choice. Pick a valid option!");
                gameOverScreen();
                break;
        }
        System.out.println("     *****************************");
    }

    private void finishScreen() throws ElementNotFoundException, EmptyCollectionException {
        Main main = new Main();
        System.out.println("     *****************************");
        System.out.println("     Finished Game  \n");
        System.out.println("     Level: " + getGameLevel().toString());
        System.out.println("     Player Name: " + getPlayersList().first().getName());
        System.out.println("     Player Points: " + getPlayersList().first().getPoints() + "\n");
        System.out.println("     *****************************");
        main.initGame();
    }

    protected void simulationMode() throws ElementNotFoundException, EmptyCollectionException {
        // System.out.println(DijkstraAlgorithm.calculateShortestPathFromSource(getEntry()));
    }

    private void mapGameInConsole() {

        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            if (!this.graph.getVertex(i).getName().equals("exit")) {
                System.out.println("\n***************************************");
                System.out.println(String.format("%-12s %-35s", " ", "Division"));
                System.out.println(String.format("%-10s %-20s ", " ", "|" +
                        this.graph.getVertex(i).getName() + "|\n"));
                System.out.println("  Connections:");
            }
            for (int j = 0; j < this.graph.getNumVertices(); j++) {
                if (this.graph.getAdjMatrix()[i][j] >= 0) {
                    System.out.println(String.format("%-25s %-10s", "  ----->|" +
                                    this.graph.getVertex(j).getName() + "|",
                            "Ghost: " + this.graph.getAdjMatrix()[i][j]));
                }
            }
            System.out.println("\n***************************************");

        }
    }

    @Override
    public String toString() {
        return " Name = " + this.name + "\n" +
                " GameLevel = \t" + this.gameLevel + "\n";
    }
}

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import libs.ArrayList;
import libs.ArrayOrderedList;
import libs.Network;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MapGame {

    /**
     * Name of the map
     */
    private String name;
    /**
     * Initial player points in this map
     */
    private int playerPoints;
    /**
     * List of the players in this map
     */
    private Player player;
    /**
     * Game Level of this map
     */
    private GameLevel gameLevel;
    /**
     * Graph with Divisions as Nodes
     */
    private Network<Division> graph;

    /**
     * Constructor that initializes the game
     */
    public MapGame() {
        this.graph = new Network<>();
        readFromJSONFile();
    }

    //region get-set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameLevel getGameLevel() {
        return this.gameLevel;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getPlayerPoints() {
        return this.playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    //endregion

    /**
     * Adds a new Player to list with players in this map
     *
     * @param player to be added
     */
    protected void addNewPlayer(Player player) {
        if (player != null) {
            this.player = player;
        } else {
            throw new NullPointerException("Player is null");
        }
    }

    /**
     * Reads the Json File that contains the map with the divisions
     */
    private void readFromJSONFile() {
        int divisionsId = 0;

        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader("map.json"));

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();//gets the name of the game
            int points = jsonObject.get("points").getAsInt();//gets the starting points of the player
            JsonArray map = jsonObject.get("map").getAsJsonArray();

            this.setName(name);//sets the name of the game
            this.setPlayerPoints(points);//sets the initial Player points

            JsonObject mapObjects;

            //adds the vertices
            Division entryDivision = new Division(divisionsId++, "entry");

            // add the nodes of the map
            this.graph.addVertex(entryDivision);
            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                String divisionName = mapObjects.get("place").getAsString();//gets the actual place in map array
                Division division = new Division(divisionsId++, divisionName);
                this.graph.addVertex(division);
            }
            Division exitDivision = new Division(divisionsId, "exit");
            this.graph.addVertex(exitDivision);

            //adds the existing connections as edges
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
                        this.graph.addEdge(destinationDivision, sourceDivision, ghostPoints);
                        //edge multiplier by the gameLevel
                    } else {
                        ghostPoints = getGhostTakenPoints(map, destinationDivision.getName());//sets the weight of the edge
                        this.graph.addEdge(sourceDivision, destinationDivision, ghostPoints);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * Private method that reads the ghost taken points per division
     *
     * @param map          Json array that contains the divisions
     * @param divisionName to filter the specific division
     * @return the ghost taken points in this division
     */
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

    /**
     * Returns the Division by the name passed in the parameter
     *
     * @param divisionName division name to be searched
     * @return the division or throws an exception
     */
    private Division getDivisionByName(String divisionName) {
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Division division = this.graph.getVertex(i);
            if (division.getName().equals(divisionName)) {
                return division;
            }
        }
        throw new IndexOutOfBoundsException("Division not found");
    }

    /**
     * Returns the Division by the id passed in the parameter
     *
     * @param divisionId division name to be searched
     * @return the division or throws an exception
     */
    private Division getDivisionById(int divisionId) {
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Division division = this.graph.getVertex(i);
            if (division.getId() == divisionId) {
                return division;
            }
        }
        throw new IndexOutOfBoundsException("Division not found");
    }

    /**
     * Searches for the starting point of the game, in this case would be the "entry"
     *
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     * @throws IOException
     */
    protected void startingPoint() throws ElementNotFoundException, EmptyCollectionException, IOException {

        Scanner scanner = new Scanner(System.in);
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

    /**
     * Moves to another division after the starting point, will continue looping, recursively until the player
     * loses or wins the game
     *
     * @param originId origin division before moving
     * @param division destiny division to go forward
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     * @throws IOException
     */
    private void moveToAnotherDivision(int originId, Division division)
            throws ElementNotFoundException, EmptyCollectionException, IOException {

        int playerChoice;
        //runs in the existent nodes, divisions in this case
        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            Scanner scanner = new Scanner(System.in);
            //if the next division is the "exit" and the players still have points >0
            if (this.graph.getVertex(i).getName().equals("exit") && player.getPoints() > 0) {
                //checks if the connection exists
                if (this.graph.checkIfConnectionExits(originId, this.graph.getVertex(i).getId())) {
                    System.out.println("MESSAGE: You Won! Congratulations :)");
                    finishScreen(player);
                    //if the connection doesn't exist
                } else if (!this.graph.checkIfConnectionExits(originId, this.graph.getVertex(i).getId())) {
                    System.out.println("MESSAGE: Wrong choice. Pick a valid connection!");
                    moveToAnotherDivision(originId, division);
                } else {
                    System.out.println("MESSAGE: Game Over! :(");
                    gameOverScreen();
                }
                return;
                //if the next vertex is the given division id and the player still have remaining points
            } else if (this.graph.getVertex(i).getId() == division.getId() && player.getPoints() > 0) {
                //if the connection exist
                if (this.graph.checkIfConnectionExits(originId,
                        this.graph.getVertex(i).getId())) {

                    player.setPoints(player.getPoints() - (this.graph.getWeightedAdjMatrix()[originId][i] * gameLevel.getGameLevelValue()));
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
                    //if the connection doesn't exist
                } else if (!this.graph.checkIfConnectionExits(division.getId(), this.graph.getVertex(i).getId())) {
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

    /**
     * Prints the connections possible from the given division
     *
     * @param origin to show connections from this divisions
     */
    private void printConnections(Division origin) {

        for (int j = 0; j < this.graph.getNumVertices(); j++) {
            if (this.graph.checkIfConnectionExits(origin.getId(), this.graph.getVertex(j).getId())) {
                System.out.println(this.graph.getVertex(j).toString());
            }
        }
    }

    /**
     * Game Over Screen, runs when the player losts the game
     *
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     * @throws IOException
     */
    private void gameOverScreen() throws ElementNotFoundException, EmptyCollectionException, IOException {
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

    /**
     * Finish screen when the player wins the game
     *
     * @param player player that won the game
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     * @throws IOException
     */
    private void finishScreen(Player player) throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Finished Game  \n");
        System.out.println("     Level: " + getGameLevel().toString());
        System.out.println("     Player Name: " + player.getName());
        System.out.println("     Player Points: " + player.getPoints() + "\n");
        System.out.println("     *****************************");
        writeToFile();//writes the player score to a file
    }

    /**
     * Prints the map in the console with the all divisions and his connections
     */
    protected void mapGameInConsole() {

        for (int i = 0; i < this.graph.getNumVertices(); i++) {
            if (!this.graph.getVertex(i).getName().equals("exit")) {
                System.out.println("\n***************************************");
                System.out.println(String.format("%-12s %-35s", " ", "Division"));
                System.out.println(String.format("%-12s %-20s ", " ", "|" +
                        this.graph.getVertex(i).getName() + "|\n"));
                System.out.println("  Connections:");
            }
            for (int j = 0; j < this.graph.getNumVertices(); j++) {
                if (this.graph.getWeightedAdjMatrix()[i][j] != null) {
                    System.out.println(String.format("%-25s %-10s", "  ----->|" +
                                    this.graph.getVertex(j).getName() + "|",
                            "Ghost: " + this.graph.getWeightedAdjMatrix()[i][j]));
                }
            }
            System.out.println("\n***************************************");

        }
    }

    /**
     * Writes to the file the list of the player of this map
     */
    protected void writeToFile() {
        ArrayOrderedList<Player> players = loadPlayers();
        if (players == null) {
            players = new ArrayOrderedList<>();
        }
        players.add(player);
        try {
            FileWriter writer = new FileWriter("data/" + this.name + ".json", false);
            Gson gson = new Gson();
            Player[] playersArray = new Player[players.size()];
            ArrayList<Player>.BasicIterator iterator = players.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Player player = iterator.next();
                playersArray[i++] = player;
            }
            gson.toJson(playersArray, Player[].class, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads from the file the players scores , if exists any and returns a Json array
     *
     * @return Json array with the players
     */
    private ArrayOrderedList<Player> loadPlayers() {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader("data/" + this.name + ".json"));
            Player[] players = gson.fromJson(reader, Player[].class);
            ArrayOrderedList<Player> list = new ArrayOrderedList<>();
            for (Player player : players) {
                list.add(player);
            }
            return list;
        } catch (FileNotFoundException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Screen that shows the High Scores of a specific map
     */
    protected void getHighScores() {
        ArrayOrderedList<Player> players = loadPlayers();
        if (players != null) {
            ArrayList<Player>.BasicIterator playerIterator = players.iterator();
            int i = 1;
            while (playerIterator.hasNext()) {
                Player player = playerIterator.next();
                System.out.println(String.format("%-1s %-20s %-20s", i + ":", player.getName(), "Points: " + player.getPoints()));
                System.out.println("    ");
                i++;
            }
        } else {
            System.out.println("MESSAGE: No high scores at this moment");
        }
    }
}

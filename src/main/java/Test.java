import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test<T extends Comparable<T>> {

    public void readFromJSONFile(String path) {

        Graph<T> mapGraphGame = new Graph<>();
        JsonParser parser = new JsonParser();
        ArrayUnorderedList<T> tempVertices = new ArrayUnorderedList<>();

        try {
            JsonElement jsonElement = parser.parse(new FileReader(path));

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();//gets the name of the game
            int points = jsonObject.get("points").getAsInt();//gets the starting points of the player

            /*System.out.println("Map : " + name);
            System.out.println("Points : " + points);*/

            JsonArray map = jsonObject.get("map").getAsJsonArray();
            JsonObject mapObjects;

            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                String actualPlace = mapObjects.get("place").getAsString();//gets the actual place in map array
                int ghostTakenPoints = mapObjects.get("ghost").getAsInt();// points taken by the ghost
                mapGraphGame.addVertex((T) actualPlace);//adds a vertex to the graph
                /* Prints to console */
                /*System.out.println("Place : " + actualPlace);
                System.out.println("Ghost : " + ghostTakenPoints);*/
                tempVertices.addToRear((T) actualPlace);// adds to the temporary array in the rear
            }
            boolean exitExistsInGraph = false;//flag to when is find the exit vertex

            for (int i = 0; i < map.size(); i++) {
                mapObjects = map.get(i).getAsJsonObject();
                JsonArray connections = mapObjects.getAsJsonArray("connections");
                String[] possible_connections = new String[connections.size()];//gets the size of the connections

                for (int j = 0; j < connections.size(); j++) {
                    possible_connections[j] = connections.get(j).getAsString();//gets the connections available
                    if (possible_connections[j].equals("entry")) {//if is find the entry vertex
                        mapGraphGame.addVertex((T) possible_connections[j]);//adds the only vertex entry
                        mapGraphGame.addEdge((T) possible_connections[j], tempVertices.getList()[i]);//adds a edge
                        mapGraphGame.removeEdge(tempVertices.getList()[i],(T) possible_connections[j]);
                    } else if (possible_connections[j].equals("exit") && !exitExistsInGraph) {
                        //just to add one vertex exit only one time
                        mapGraphGame.addVertex((T) possible_connections[j]);
                        mapGraphGame.addEdge(tempVertices.getList()[i], (T) possible_connections[j]);
                        mapGraphGame.removeEdge((T) possible_connections[j],tempVertices.getList()[i]);
                        exitExistsInGraph = true;
                    } else if (possible_connections[j].equals("exit") && exitExistsInGraph) {
                        //adds the vertex to the exit point
                        mapGraphGame.addEdge(tempVertices.getList()[i], (T) possible_connections[j]);
                        mapGraphGame.removeEdge((T) possible_connections[j],tempVertices.getList()[i]);
                    } else {
                        for (int l = 0; l < tempVertices.size(); l++) {
                            if (tempVertices.getList()[l].equals(possible_connections[j])) {
                                //adds the edge between the vertexes
                                mapGraphGame.addEdge(tempVertices.getList()[i], tempVertices.getList()[l]);
                                mapGraphGame.addEdge(tempVertices.getList()[l], tempVertices.getList()[i]);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        System.out.println(mapGraphGame.toString());
    }
}


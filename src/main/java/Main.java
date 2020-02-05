import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    /**
     * Map Game engine
     */
    private MapGame mapGame;
    /**
     * Name to be filled by the user
     */
    private String playerName;

    //region get-set
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    //endregion

    public static void main(String[] args) throws ElementNotFoundException, EmptyCollectionException, IOException {
        Main main = new Main();
        main.initGame();
    }


    protected void initGame() throws ElementNotFoundException, EmptyCollectionException, IOException {

        this.mapGame = new MapGame();

        Scanner scanner = new Scanner(System.in);
        initialScreen();//init the initial screen

        switch (scanner.next()) {
            case "1":
                newGameScreen(scanner);
                break;
            case "2":
                highScoresScreen();
                break;
            case "0":
                System.out.println("MESSAGE: Program Closed");
                break;
            default:
                System.out.println("MESSAGE: Wrong choice. Pick a valid option!");
                initGame();
                break;
        }
    }

    private void initialScreen() {
        System.out.println("     *****************************");
        System.out.println("                                ");
        System.out.println("        1- Start New Game       ");
        System.out.println("        2- High Scores          ");
        System.out.println("        0- Exit                 ");
        System.out.println("     *****************************");
    }

    private void newGameScreen(Scanner scanner) throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Select Mode:                \n");
        System.out.println("         1- Simulation Mode     ");
        System.out.println("         2- Manual Mode         ");
        System.out.println("         0- Back                ");
        System.out.println("     *****************************");

        switch (scanner.next()) {
            case "1":
                simulationGameScreen();
                break;
            case "2":
                typeNameScreen(scanner);
                break;
            case "0":
                initGame();
            default:
                System.out.println("MESSAGE: Wrong choice. Pick a valid option!");
                newGameScreen(scanner);
                break;
        }
    }

    private void highScoresScreen() throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     HighScores                   ");
        mapGame.getHighScores();
        System.out.println("     *****************************");
        initGame();
    }

    private void simulationGameScreen() throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Simulation Mode             \n");
        mapGame.simulationMode();
        System.out.println("     *****************************");
        initGame();
    }

    private void manualGameScreen(Scanner scanner) throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Manual Mode                 \n");
        System.out.println("         1- Select Level        \n");
        System.out.println("         0- Back                ");
        System.out.println("     *****************************");

        switch (scanner.next()) {
            case "1":
                selectLevelScreen(scanner);
                break;
            case "0":
                newGameScreen(scanner);
                break;
            default:
                System.out.println("MESSAGE: Wrong choice. Pick a valid option!");
                manualGameScreen(scanner);
                break;
        }
    }

    private void selectLevelScreen(Scanner scanner) throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Manual Mode                 \n");
        System.out.println("     Select Level:               \n");
        System.out.println("         1- Basic               ");
        System.out.println("         2- Normal              ");
        System.out.println("         3- Hard                \n");
        System.out.println("         0- Back                ");
        System.out.println("     *****************************");


        switch (scanner.next()) {
            case "1":
                startNewGame(GameLevel.BASIC);
                break;
            case "2":
                startNewGame(GameLevel.NORMAL);
                break;
            case "3":
                startNewGame(GameLevel.HARD);
                break;
            case "0":
                manualGameScreen(scanner);
                break;
            default:
                System.out.println("MESSAGE: Wrong choice. Pick a valid option!");
                selectLevelScreen(scanner);
                break;
        }
    }

    private void startNewGame(GameLevel gameLevel) throws ElementNotFoundException, IOException, EmptyCollectionException {
        mapGame.setGameLevel(gameLevel);
        mapGame.addNewPlayer(new Player(getPlayerName(), mapGame.getPlayerPoints()));
        mapGame.startingPoint();
    }

    private void typeNameScreen(Scanner scanner) throws ElementNotFoundException, EmptyCollectionException, IOException {
        System.out.println("     *****************************");
        System.out.println("     Manual Mode ");
        System.out.println("     Type your name: ");
        String playerName = scanner.next();
        System.out.println("     *****************************");
        setPlayerName(playerName);
        manualGameScreen(scanner);
    }

    private void infoGameScreen(Player player, GameLevel gameLevel) {
        System.out.println("     *****************************");
        System.out.println("     Game Info : \n");
        System.out.println("     Name : " + mapGame.getName());
        System.out.println("     Level: " + gameLevel.toString());
        System.out.println("     Player Name: " + player.getName());
        System.out.println("     Player Points: " + player.getPoints() + "\n");
        System.out.println("     *****************************");
    }
}

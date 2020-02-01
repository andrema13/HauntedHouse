import java.util.Scanner;

public class Main {
    private MapGame<String> mapGame;

    public static void main(String[] args) {
        Main main = new Main();
        main.initGame();
    }

    protected void initGame() {

        Scanner scanner = new Scanner(System.in);
        mapGame = new MapGame<>();
        mapGame.readFromJSONFile("map.json");
        initialScreen(mapGame);//init the initial screen

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

    private void initialScreen(MapGame<String> mapGame) {
        System.out.println("     *****************************");
        System.out.println("     " + mapGame.getName() + "\n  ");
        System.out.println("        1- Start New Game       ");
        System.out.println("        2- High Scores          ");
        System.out.println("        0- Exit                 ");
        System.out.println("     *****************************");
    }

    private void newGameScreen(Scanner scanner) {
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

    private void highScoresScreen() {
        System.out.println("     *****************************");
        System.out.println("     HighScores                   ");
        System.out.println("     *****************************");
        initGame();
    }

    private void simulationGameScreen() {
        System.out.println("     *****************************");
        System.out.println("     Simulation Mode             \n");
        System.out.println("     *****************************");
        initGame();
    }

    private void manualGameScreen(Scanner scanner) {
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

    private void selectLevelScreen(Scanner scanner) {
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
                mapGame.changePointsPerDivisionByGameLevel(GameLevel.BASIC);
                checkIfGameIsValid();
                break;
            case "2":
                mapGame.changePointsPerDivisionByGameLevel(GameLevel.NORMAL);
                //System.out.println(mapGame.toString());
                checkIfGameIsValid();
                break;
            case "3":
                mapGame.changePointsPerDivisionByGameLevel(GameLevel.HARD);
                checkIfGameIsValid();
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
    private void checkIfGameIsValid(){
        if (mapGame.getPlayerPoints() <= 0) {
            System.out.println("Player Points is lower or equals to 0");
            initGame();
        } else {
            infoGameScreen(mapGame.getPlayersList().first(),mapGame.getGameLevel());
            mapGame.startingPoint(mapGame.getEntry());
        }
    }

    private void typeNameScreen(Scanner scanner) {
        System.out.println("     *****************************");
        System.out.println("     Manual Mode ");
        System.out.println("     Type your name: ");
        String playerName = scanner.next();
        System.out.println("     *****************************");
        mapGame.addNewPlayer(new Player(playerName, mapGame.getPlayerPoints()));
        manualGameScreen(scanner);
    }

    private void infoGameScreen(Player player, GameLevel gameLevel) {
        System.out.println("     *****************************");
        System.out.println("     Game Info : \n");
        System.out.println("     Level: " + gameLevel.toString());
        System.out.println("     Player Name: " + player.getName());
        System.out.println("     Player Points: " + player.getPoints() + "\n");
        System.out.println("     *****************************");
    }
}

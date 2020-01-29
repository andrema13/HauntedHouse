import java.util.Scanner;

public class Main {
    private static MapGame<String> mapGame;

    public static void main(String[] args) {
        mapGame = new MapGame<>();
        mapGame.readFromJSONFile("map.json");
        initGameScreen();
    }

    private static void initGameScreen() {
        Scanner scanner = new Scanner(System.in);
        initialScreen(mapGame);//init the initial screen

        switch (scanner.next()) {
            case "1":
                newGameScreen(scanner);
                break;
            case "2":
                highScoresScreen();
                break;
            case "0":
                System.out.println("Program Closed");
                break;
            default:
                initGameScreen();
                break;
        }
    }

    private static void initialScreen(MapGame<String> mapGame) {
        System.out.println("     *****************************");
        System.out.println("     " + mapGame.getName() + "\n  ");
        System.out.println("        1- Start New Game       ");
        System.out.println("        2- High Scores          ");
        System.out.println("        0- Exit                 ");
        System.out.println("     *****************************");
    }

    private static void newGameScreen(Scanner scanner) {
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
                initGameScreen();
            default:
                newGameScreen(scanner);
                break;
        }
    }

    private static void highScoresScreen() {
        System.out.println("     *****************************");
        System.out.println("     HighScores                   ");
        System.out.println("     *****************************");
        initGameScreen();
    }

    private static void simulationGameScreen() {
        System.out.println("     *****************************");
        System.out.println("     Simulation Mode             \n");
        System.out.println("     *****************************");
        initGameScreen();
    }

    private static void manualGameScreen(Scanner scanner) {
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
                manualGameScreen(scanner);
                break;
        }
    }

    private static void selectLevelScreen(Scanner scanner) {
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
                break;
            case "2":
                mapGame.changePointsPerDivisionByGameLevel(GameLevel.NORMAL);
                //System.out.println(mapGame.toString());
                mapGame.startingPoint(mapGame.getEntry());
                initGameScreen();
                break;
            case "3":
                mapGame.changePointsPerDivisionByGameLevel(GameLevel.HARD);
                break;
            case "0":
                manualGameScreen(scanner);
                break;
            default:
                selectLevelScreen(scanner);
                break;
        }
    }

    private static void typeNameScreen(Scanner scanner) {
        System.out.println("     *****************************");
        System.out.println("     Manual Mode \n               ");
        System.out.println("     Type your name:              ");
        String playerName = scanner.next();
        System.out.println("     *****************************");
        mapGame.addNewPlayer(new Player(playerName, mapGame.getPlayerPoints()));
        manualGameScreen(scanner);
    }
}

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapGameTest {

    MapGame mapGame;
    Player player;
    String name;
    int points;

/*    @BeforeAll
    public void setup() {
        player = null;
        name = "saa";
        points = 0;
        mapGame = new MapGame();
        player = new Player(name, points);
    }*/


    @Test
    public void addNewPlayerTest() {
        Assertions.assertThrows(NullPointerException.class, () -> mapGame.addNewPlayer(player),
                "Should throw an exception");
    }
}



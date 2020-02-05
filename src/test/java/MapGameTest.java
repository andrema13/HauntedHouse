import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapGameTest {

    MapGame mapGame;

    @BeforeEach
    public void init() {
        mapGame = new MapGame();
    }


    @Test
    public void addNewPlayerTest1() {
        Assertions.assertThrows(NullPointerException.class, () -> mapGame.addNewPlayer(null),
                "Should throw an exception");
    }

}



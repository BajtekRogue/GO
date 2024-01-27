package GUI;

import GameObjects.Board;
import GameObjects.Coordinates;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.NeighbourManager;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
public class GoGUITEST {

    private static GoGUI goGUI;
    @Before
    public void setUp() {
        this.goGUI = new GoGUI(null);
    }
    @Test
    public void testStonesToBeAddedFromStringToCoordinates1(){
        String message = "Received message: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        Coordinates coordinates = goGUI.stonesToBeAddedFromStringToCoordinates(message);
        assertEquals(2, coordinates.getX());
        assertEquals(0, coordinates.getY());

    }

    @Test
    public void testStonesToBeAddedFromStringToCoordinates2(){
        String message = "Received message: Player 1: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        Coordinates coordinates = goGUI.stonesToBeAddedFromStringToCoordinates(message);
        assertEquals(2, coordinates.getX());
        assertEquals(0, coordinates.getY());

    }

    @Test
    public void testStonesToBeRemovedFromStringToCoordinates1() {
        String message = "Received message: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        List<Coordinates> removedCoordinates = goGUI.stonesToBeRemovedFromStringToCoordinates(message);

        assertEquals(2, removedCoordinates.size());

        Coordinates coordinates1 = removedCoordinates.get(0);
        assertEquals(1, coordinates1.getX());
        assertEquals(0, coordinates1.getY());

        Coordinates coordinates2 = removedCoordinates.get(1);
        assertEquals(0, coordinates2.getX());
        assertEquals(0, coordinates2.getY());
    }

    @Test
    public void testStonesToBeRemovedFromStringToCoordinates2() {
        String message = "Received message: Player 1: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        List<Coordinates> removedCoordinates = goGUI.stonesToBeRemovedFromStringToCoordinates(message);

        assertEquals(2, removedCoordinates.size());

        Coordinates coordinates1 = removedCoordinates.get(0);
        assertEquals(1, coordinates1.getX());
        assertEquals(0, coordinates1.getY());

        Coordinates coordinates2 = removedCoordinates.get(1);
        assertEquals(0, coordinates2.getX());
        assertEquals(0, coordinates2.getY());
    }
    @Test
    public void testStonesToBeRemovedFromStringToCoordinates3() {
        String message = "Received message: OK MOVE 2 0 ";
        List<Coordinates> removedCoordinates = goGUI.stonesToBeRemovedFromStringToCoordinates(message);

        assertEquals(0, removedCoordinates.size());
    }
}

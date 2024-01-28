package ServerTEST;

import GameObjects.Coordinates;
import Server.MessageDecoder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
public class MessageDecoderTEST {

    @Test
    public void testStonesToBeAddedFromStringToCoordinates1(){
        String message = "Received message: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        Coordinates coordinates = MessageDecoder.stonesToBeAddedFromStringToCoordinates(message);
        assertEquals(2, coordinates.getX());
        assertEquals(0, coordinates.getY());

    }

    @Test
    public void testStonesToBeAddedFromStringToCoordinates2(){
        String message = "Received message: Player 1: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        Coordinates coordinates = MessageDecoder.stonesToBeAddedFromStringToCoordinates(message);
        assertEquals(2, coordinates.getX());
        assertEquals(0, coordinates.getY());

    }

    @Test
    public void testStonesToBeRemovedFromStringToCoordinates1() {
        String message = "Received message: OK MOVE 2 0 REMOVED 1 0 0 0 ";
        List<Coordinates> removedCoordinates = MessageDecoder.stonesToBeRemovedFromStringToCoordinates(message);

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
        List<Coordinates> removedCoordinates = MessageDecoder.stonesToBeRemovedFromStringToCoordinates(message);

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
        List<Coordinates> removedCoordinates = MessageDecoder.stonesToBeRemovedFromStringToCoordinates(message);

        assertEquals(0, removedCoordinates.size());
    }

    @Test
    public void testExtractBlackPoints1(){
        String message = "Received message: Player 2: ENDGAME Black points: 11 White points: 23";
        int blackPoints = MessageDecoder.extractBlackPoints(message);
        int whitePoints = MessageDecoder.extractWhitePoints(message);

        assertEquals(blackPoints, 11);
        assertEquals(whitePoints, 23);
    }

    @Test
    public void testExtractBlackPoints2(){
        String message = "Received message: ENDGAME Black points: 11 White points: 23";
        int blackPoints = MessageDecoder.extractBlackPoints(message);
        int whitePoints = MessageDecoder.extractWhitePoints(message);

        assertEquals(blackPoints, 11);
        assertEquals(whitePoints, 23);
    }

}

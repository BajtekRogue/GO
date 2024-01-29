package GameObjectLogicTEST;

import Factories.CoordinatesFactory;
import Factories.StoneFactory;
import GameObjects.*;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.NeighbourManager;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CaptureManagerTEST {

    private static final int boardSize = 10;
    private static BoardManager boardManager;
    private static NeighbourManager neighbourManager;
    private static CaptureManager captureManager;

    @Before
    public void setUp() {
        Board board = new Board(boardSize);
        boardManager = new BoardManager(board);
        neighbourManager = new NeighbourManager(board);
        captureManager = new CaptureManager(board);
    }


    @Test
    public void testCheckForCapture1() {

        assertNotNull(boardManager);
        assertNotNull(neighbourManager);
        assertNotNull(captureManager);

        BoardManager.addStone(2, 1, StoneFactory.createWhiteStone());
        BoardManager.addStone(2, 2, StoneFactory.createWhiteStone());
        BoardManager.addStone(2, 3, StoneFactory.createWhiteStone());
        BoardManager.addStone(2, 4, StoneFactory.createWhiteStone());
        BoardManager.addStone(3, 1, StoneFactory.createWhiteStone());
        BoardManager.addStone(4, 1, StoneFactory.createWhiteStone());
        BoardManager.addStone(4, 2, StoneFactory.createWhiteStone());
        BoardManager.addStone(4, 3, StoneFactory.createWhiteStone());
        BoardManager.addStone(4, 4, StoneFactory.createWhiteStone());

        BoardManager.addStone(3, 3, StoneFactory.createBlackStone());
        BoardManager.addStone(3, 2, StoneFactory.createBlackStone());

        BoardManager.addStone(3, 4, StoneFactory.createWhiteStone());

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(3, 4);

        assertEquals(2, capturedStones.size());
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(3, 3)));
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(3, 2)));
    }


    @Test
    public void testCheckForCapture2() {

        assertNotNull(boardManager);
        assertNotNull(neighbourManager);
        assertNotNull(captureManager);

        BoardManager.addStone(0, 9, StoneFactory.createWhiteStone());
        BoardManager.addStone(1, 8, StoneFactory.createWhiteStone());
        BoardManager.addStone(2, 8, StoneFactory.createWhiteStone());
        BoardManager.addStone(3, 7, StoneFactory.createWhiteStone());
        BoardManager.addStone(4, 9, StoneFactory.createWhiteStone());


        BoardManager.addStone(1, 9, StoneFactory.createBlackStone());
        BoardManager.addStone(2, 9, StoneFactory.createBlackStone());
        BoardManager.addStone(3, 9, StoneFactory.createBlackStone());
        BoardManager.addStone(3, 8, StoneFactory.createBlackStone());

        BoardManager.addStone(4, 8, StoneFactory.createWhiteStone());

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(4, 8);

        assertEquals(4, capturedStones.size());
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(1, 9)));
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(2, 9)));
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(3, 9)));
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(3, 8)));
    }

    @Test
    public void testCheckForCapture3() {


        BoardManager.addStone(8, 9, StoneFactory.createBlackStone());

        BoardManager.addStone(9, 9, StoneFactory.createWhiteStone());

        BoardManager.addStone(9, 8, StoneFactory.createBlackStone());

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(9, 8);

        assertEquals(1, capturedStones.size());
        assertTrue(capturedStones.contains(CoordinatesFactory.createCoordinates(9, 9)));
    }

    @Test
    public void testCheckForCapture4() {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                BoardManager.addStone(i, j, StoneFactory.createWhiteStone());
            }
        }

        BoardManager.removeStone(0, 0);
        BoardManager.addStone(0, 0, StoneFactory.createBlackStone());
        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(0, 0);
        assertEquals(boardSize * boardSize - 1, capturedStones.size());
    }

}

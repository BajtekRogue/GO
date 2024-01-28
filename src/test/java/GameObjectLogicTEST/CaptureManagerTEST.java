package GameObjectLogicTEST;

import GameObjects.Board;
import GameObjects.Coordinates;
import GameObjects.Stone;
import GameObjects.StoneColor;
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
    public void testCheckForCapture1(){

        assertNotNull(boardManager);
        assertNotNull(neighbourManager);
        assertNotNull(captureManager);

        BoardManager.addStone(2, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 4, new Stone(StoneColor.WHITE));

        BoardManager.addStone(3, 3, new Stone(StoneColor.BLACK));
        BoardManager.addStone(3, 2, new Stone(StoneColor.BLACK));

        BoardManager.addStone(3, 4, new Stone(StoneColor.WHITE));

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(3, 4);

        assertEquals(2, capturedStones.size());
        assertTrue(capturedStones.contains(new Coordinates(3, 3)));
        assertTrue(capturedStones.contains(new Coordinates(3, 2)));
    }


    @Test
    public void testCheckForCapture2(){

        assertNotNull(boardManager);
        assertNotNull(neighbourManager);
        assertNotNull(captureManager);

        BoardManager.addStone(0, 9, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 8, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 8, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 7, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 9, new Stone(StoneColor.WHITE));


        BoardManager.addStone(1, 9, new Stone(StoneColor.BLACK));
        BoardManager.addStone(2, 9, new Stone(StoneColor.BLACK));
        BoardManager.addStone(3, 9, new Stone(StoneColor.BLACK));
        BoardManager.addStone(3, 8, new Stone(StoneColor.BLACK));

        BoardManager.addStone(4, 8, new Stone(StoneColor.WHITE));

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(4, 8);

        assertEquals(4, capturedStones.size());
        assertTrue(capturedStones.contains(new Coordinates(1, 9)));
        assertTrue(capturedStones.contains(new Coordinates(2, 9)));
        assertTrue(capturedStones.contains(new Coordinates(3, 9)));
        assertTrue(capturedStones.contains(new Coordinates(3, 8)));
    }

    @Test
    public void testCheckForCapture3(){



        BoardManager.addStone(8, 9, new Stone(StoneColor.BLACK));

        BoardManager.addStone(9, 9, new Stone(StoneColor.WHITE));

        BoardManager.addStone(9, 8, new Stone(StoneColor.BLACK));

        NeighbourManager.updateAllNeighbours();
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(9, 8);

        assertEquals(1, capturedStones.size());
        assertTrue(capturedStones.contains(new Coordinates(9, 9)));
    }

}

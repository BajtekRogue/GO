package GameObjectLogicTEST;

import GameObjects.*;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.ExceptionManager;
import GameObjectsLogic.NeighbourManager;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class ExceptionManagerTEST {

    private static final int BOARD_SIZE = 10;

    @Before
    public void setUp() {
        Board board = new Board(BOARD_SIZE);
        ExceptionManager exceptionManager = new ExceptionManager();
        BoardManager boardManager = new BoardManager(board);
        NeighbourManager neighbourManager = new NeighbourManager(board);
        CaptureManager captureManager = new CaptureManager(board);
    }

    @Test
    public void testSetAndGetKO_coordinates() {
        Coordinates koCoordinates = new Coordinates(1, 1);
        ExceptionManager.setKO_coordinates(koCoordinates);

        assertEquals(koCoordinates, ExceptionManager.getKO_coordinates());
    }

    @Test
    public void testRestKO_coordinates() {
        Coordinates koCoordinates = new Coordinates(1, 1);
        ExceptionManager.setKO_coordinates(koCoordinates);

        ExceptionManager.restKO_coordinates();

        assertEquals(new Coordinates(-1, -1), ExceptionManager.getKO_coordinates());
    }

    @Test
    public void testCheckForSuicide1(){


        BoardManager.addStone(5, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(5, 6, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 5, new Stone(StoneColor.WHITE));
        BoardManager.addStone(6, 5, new Stone(StoneColor.WHITE));
        NeighbourManager.updateAllNeighbours();

        BoardManager.addStone(5, 5, new Stone(StoneColor.BLACK));
        NeighbourManager.addNeighbours(5, 5);
        NeighbourManager.updateNeighbours(5, 5);

        List<Coordinates> capturedStones = CaptureManager.checkForCapture(5, 5);
        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);


        if (numberOfCapturedStones == 0)
            assertTrue(ExceptionManager.checkForSuicide(5, 5));


    }

    @Test
    public void testCheckForSuicide2(){


        BoardManager.addStone(5, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(5, 6, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 5, new Stone(StoneColor.WHITE));
        BoardManager.addStone(6, 5, new Stone(StoneColor.WHITE));
        NeighbourManager.updateAllNeighbours();

        BoardManager.addStone(5, 5, new Stone(StoneColor.BLACK));
        NeighbourManager.addNeighbours(5, 5);
        NeighbourManager.updateNeighbours(5, 5);

        List<Coordinates> capturedStones = CaptureManager.checkForCapture(5, 5);
        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);


        if (numberOfCapturedStones == 0)
            assertFalse(ExceptionManager.checkForSuicide(5, 8));

    }

    @Test
    public void testCheckForSuicide3(){


        BoardManager.addStone(0, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 0, new Stone(StoneColor.WHITE));

        NeighbourManager.updateAllNeighbours();

        BoardManager.addStone(0, 0, new Stone(StoneColor.BLACK));
        NeighbourManager.addNeighbours(0, 0);
        NeighbourManager.updateNeighbours(0, 0);

        List<Coordinates> capturedStones = CaptureManager.checkForCapture(0, 0);
        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);


        if (numberOfCapturedStones == 0)
            assertTrue(ExceptionManager.checkForSuicide(0, 0));


    }

    @Test
    public void testCheckForSuicide4(){


        BoardManager.addStone(0, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(0, 6, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 5, new Stone(StoneColor.WHITE));

        NeighbourManager.updateAllNeighbours();

        BoardManager.addStone(0, 5, new Stone(StoneColor.BLACK));
        NeighbourManager.addNeighbours(0, 5);
        NeighbourManager.updateNeighbours(0, 5);

        List<Coordinates> capturedStones = CaptureManager.checkForCapture(0, 5);
        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);


        if (numberOfCapturedStones == 0)
            assertTrue(ExceptionManager.checkForSuicide(0, 5));


    }

    @Test
    public void testCheckForKO(){

        BoardManager.addStone(0, 1, new Stone(StoneColor.BLACK));
        BoardManager.addStone(1, 0, new Stone(StoneColor.BLACK));
        BoardManager.addStone(1, 2, new Stone(StoneColor.BLACK));
        BoardManager.addStone(1, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 1, new Stone(StoneColor.WHITE));

        NeighbourManager.updateAllNeighbours();

        BoardManager.addStone(2, 1, new Stone(StoneColor.BLACK));
        NeighbourManager.addNeighbours(2, 1);
        NeighbourManager.updateNeighbours(2, 1);
        List<Coordinates> capturedStones = CaptureManager.checkForCapture(2, 1);
        ExceptionManager.checkForKO(2, 1);
        int numberOfCapturedStones = CaptureManager.removeCapturedStones(capturedStones);
        if (numberOfCapturedStones == 1)
            ExceptionManager.setKO_coordinates(capturedStones.get(0));
        else
            ExceptionManager.restKO_coordinates();

        BoardManager.addStone(1, 1, new Stone(StoneColor.WHITE));
        NeighbourManager.addNeighbours(1, 1);
        NeighbourManager.updateNeighbours(1, 1);
        assertTrue(ExceptionManager.checkForKO(1, 1));
        int numberOfCapturedStones2 = CaptureManager.removeCapturedStones(capturedStones);
        if (numberOfCapturedStones2 == 1)
            ExceptionManager.setKO_coordinates(capturedStones.get(0));
        else
            ExceptionManager.restKO_coordinates();
    }


}

package ServerTEST;

import Factories.StoneFactory;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.NeighbourManager;
import Server.GameMaster;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameMasterTEST {

    private static final int boardSize = 10;
    private static GameMaster gameMaster;
    @Before
    public void setUp() {
        gameMaster = new GameMaster(boardSize);
    }
    @Test
    public void testHandleMove1() {
        String message = gameMaster.handleMove(3, 5);
        assertEquals(message, "OK MOVE 3 5");
    }

    @Test
    public void testHandleMove2() {
        BoardManager.addStone(0,0, StoneFactory.createBlackStone());
        BoardManager.addStone(0,1, StoneFactory.createBlackStone());
        BoardManager.addStone(0,2, StoneFactory.createWhiteStone());
        BoardManager.addStone(1,1, StoneFactory.createWhiteStone());
        NeighbourManager.updateAllNeighbours();
        gameMaster.switchPlayer();
        String message = gameMaster.handleMove(1, 0);
        assertTrue(message.equals("OK MOVE 1 0 REMOVED 0 1 0 0 ") || message.equals("OK MOVE 1 0 REMOVED 0 0 0 1 "));
    }

    @Test
    public void testHandleMove3() {
        BoardManager.addStone(5,8, StoneFactory.createBlackStone());
        String message = gameMaster.handleMove(5, 8);
        assertEquals(message, "OCCUPIED");
    }

    @Test
    public void testHandleMove4() {
        BoardManager.addStone(5,2, StoneFactory.createWhiteStone());
        BoardManager.addStone(5,4, StoneFactory.createWhiteStone());
        BoardManager.addStone(4,3, StoneFactory.createWhiteStone());
        BoardManager.addStone(6,3, StoneFactory.createWhiteStone());
        NeighbourManager.updateAllNeighbours();
        String message = gameMaster.handleMove(5, 3);
        assertEquals(message, "SUICIDE");
    }

    @Test
    public void testHandleMove5() {
        BoardManager.addStone(3,4, StoneFactory.createWhiteStone());
        BoardManager.addStone(4,3, StoneFactory.createWhiteStone());
        BoardManager.addStone(4,5, StoneFactory.createWhiteStone());
        BoardManager.addStone(5,5, StoneFactory.createBlackStone());
        BoardManager.addStone(4,4, StoneFactory.createBlackStone());
        BoardManager.addStone(6,4, StoneFactory.createBlackStone());
        BoardManager.addStone(5,3, StoneFactory.createBlackStone());
        NeighbourManager.updateAllNeighbours();
        gameMaster.switchPlayer();
        gameMaster.handleMove(5, 4);
        String message = gameMaster.handleMove(4, 4);
        assertEquals(message, "KO");
    }

}

package GameObjectLogicTEST;

import GameObjects.Board;

import GameObjects.Stone;
import GameObjects.StoneColor;
import GameObjectsLogic.BoardManager;
import MyExceptions.OccupiedTileException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardManagerTEST {

    private static final int BOARD_SIZE = 3;
    private static Board board;
    private static BoardManager boardManager;

    @Before
    public void setUp() {
        board = new Board(BOARD_SIZE);
        boardManager = new BoardManager(board);
    }

    @Test
    public void testGetBoard() {
        assertEquals(board, boardManager.getBoard());
    }

    @Test
    public void testAddAndRemoveStone() {
        Stone stone = new Stone(StoneColor.BLACK);


        try {
            boardManager.addStone(1, 1, stone);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(stone, boardManager.getStone(1, 1));

        try {
            boardManager.removeStone(1, 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull(boardManager.getStone(1, 1));
    }

    @Test
    public void testIsTileFree() {
        assertTrue(boardManager.isTileFree(0, 0));

        try {
            boardManager.addStone(0, 0, new Stone(StoneColor.WHITE));
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertFalse(boardManager.isTileFree(0, 0));
    }

    @Test
    public void testResetBoard() {
        Stone stone = new Stone(StoneColor.WHITE);

        try {
            boardManager.addStone(1, 1, stone);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        boardManager.resetBoard();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                assertNull(boardManager.getStone(i, j));
            }
        }
    }
}


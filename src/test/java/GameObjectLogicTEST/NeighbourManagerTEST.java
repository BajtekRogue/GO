package GameObjectLogicTEST;

import GameObjects.*;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.NeighbourManager;
import MyExceptions.OccupiedTileException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NeighbourManagerTEST {

    private static final int BOARD_SIZE = 3;
    private static Board board;
    private static NeighbourManager neighbourManager;

    @Before
    public void setUp() {
        board = new Board(BOARD_SIZE);
        neighbourManager = new NeighbourManager(board);
    }

    @Test
    public void testGetArrayOfNeighbours() {
        assertNotNull(neighbourManager.getArrayOfNeighbours());
    }

    @Test
    public void testAddNeighbours() {
        board.setTile(1, 1, new Stone(StoneColor.BLACK));
        board.setTile(1, 2, new Stone(StoneColor.WHITE));
        board.setTile(2, 1, new Stone(StoneColor.BLACK));
        board.setTile(2, 2, new Stone(StoneColor.BLACK));

        NeighbourManager.addNeighbours(1, 1);
        StoneNeighbours stoneNeighbours1 = NeighbourManager.getArrayOfNeighbours().getNeighbours(1, 1);

        assertEquals(NeighbourState.ENEMY, stoneNeighbours1.getNorth());
        assertEquals(NeighbourState.ALLY, stoneNeighbours1.getEast());
        assertEquals(NeighbourState.EMPTY, stoneNeighbours1.getSouth());
        assertEquals(NeighbourState.EMPTY, stoneNeighbours1.getWest());

        NeighbourManager.addNeighbours(2, 2);
        StoneNeighbours stoneNeighbours2 = NeighbourManager.getArrayOfNeighbours().getNeighbours(2, 2);

        assertEquals(NeighbourState.WALL, stoneNeighbours2.getNorth());
        assertEquals(NeighbourState.WALL, stoneNeighbours2.getEast());
        assertEquals(NeighbourState.ALLY, stoneNeighbours2.getSouth());
        assertEquals(NeighbourState.ENEMY, stoneNeighbours2.getWest());
    }

    @Test
    public void testUpdateNeighbours() {

        board.setTile(1, 1, new Stone(StoneColor.BLACK));
        board.setTile(1, 2, new Stone(StoneColor.WHITE));
        board.setTile(2, 1, new Stone(StoneColor.BLACK));
        board.setTile(2, 2, new Stone(StoneColor.BLACK));

        NeighbourManager.updateNeighbours(1,2);
        StoneNeighbours stoneNeighbours2 = NeighbourManager.getArrayOfNeighbours().getNeighbours(2, 2);

        assertEquals(NeighbourState.WALL, stoneNeighbours2.getNorth());
        assertEquals(NeighbourState.WALL, stoneNeighbours2.getEast());
        assertEquals(NeighbourState.ALLY, stoneNeighbours2.getSouth());
        assertEquals(NeighbourState.ENEMY, stoneNeighbours2.getWest());
    }

    @Test
    public void testUpdateAllNeighbours() {

        neighbourManager.updateAllNeighbours();
        BoardManager boardManager = new BoardManager(board);
        board.setTile(1, 1, new Stone(StoneColor.BLACK));
        board.setTile(1, 2, new Stone(StoneColor.WHITE));
        board.setTile(2, 1, new Stone(StoneColor.BLACK));
        board.setTile(2, 2, new Stone(StoneColor.BLACK));

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                StoneNeighbours neighbours = neighbourManager.getArrayOfNeighbours().getNeighbours(i, j);

                if(BoardManager.getStone(i, j) != null)
                    assertNull(neighbours);


            }
        }
    }
}


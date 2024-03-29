package GameObjectsTEST;

import GameObjects.Board;
import GameObjects.Stone;
import Factories.StoneFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTEST {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(3);
    }

    @Test
    public void testGetBoardSize() {
        assertEquals(3, board.getBoardSize());
    }

    @Test
    public void testGetAndSetTile() {
        Stone stone = StoneFactory.createWhiteStone();
        board.setTile(1, 1, stone);
        assertEquals(stone, board.getTile(1, 1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetTileOutOfBounds1() {
        board.getTile(4, 4);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetTileOutOfBounds2() {
        board.getTile(1, 4);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetTileOutOfBounds1() {
        Stone stone = StoneFactory.createBlackStone();
        board.setTile(4, 4, stone);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetTileOutOfBounds2() {
        Stone stone = StoneFactory.createBlackStone();
        board.setTile(-8, 0, stone);
    }
}


package GameObjectLogicTEST;

import GameObjects.Board;
import GameObjects.Coordinates;
import GameObjects.Stone;
import GameObjects.StoneColor;
import GameObjectsLogic.BoardManager;
import GameObjectsLogic.CaptureManager;
import GameObjectsLogic.NeighbourManager;

import GameObjectsLogic.TerritoryManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TerritoryManagerTEST {

    private static final int boardSize = 5;
    private static BoardManager boardManager;
    private static TerritoryManager territoryManager;

    @Before
    public void setUp() {
        Board board = new Board(boardSize);
        boardManager = new BoardManager(board);
        territoryManager = new TerritoryManager(board);
    }

    @Test
    public void testCheckForTerritory1(){

        BoardManager.addStone(2, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 0, new Stone(StoneColor.WHITE));

        BoardManager.addStone(4, 3, new Stone(StoneColor.BLACK));


        List<Coordinates> territoryWhite = TerritoryManager.lookForTerritory(StoneColor.WHITE);
        List<Coordinates> territoryBlack = TerritoryManager.lookForTerritory(StoneColor.BLACK);

        assertEquals(0, territoryBlack.size());
        assertEquals(10, territoryWhite.size());
        assertTrue(territoryWhite.contains(new Coordinates(0, 0)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 1)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 2)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 3)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 4)));
        assertTrue(territoryWhite.contains(new Coordinates(1, 0)));
        assertTrue(territoryWhite.contains(new Coordinates(1, 1)));
        assertTrue(territoryWhite.contains(new Coordinates(1, 2)));
        assertTrue(territoryWhite.contains(new Coordinates(1, 4)));
        assertTrue(territoryWhite.contains(new Coordinates(2, 0)));
    }


    @Test
    public void testCheckForTerritory2(){

        BoardManager.addStone(0, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 3, new Stone(StoneColor.WHITE));



        List<Coordinates> territory = TerritoryManager.lookForTerritory(StoneColor.WHITE);

        assertEquals(14, territory.size());
        assertTrue(territory.contains(new Coordinates(0, 0)));
        assertTrue(territory.contains(new Coordinates(0, 2)));
        assertTrue(territory.contains(new Coordinates(0, 3)));
        assertTrue(territory.contains(new Coordinates(0, 4)));
        assertTrue(territory.contains(new Coordinates(1, 0)));
        assertTrue(territory.contains(new Coordinates(1, 2)));
        assertTrue(territory.contains(new Coordinates(2, 2)));
        assertTrue(territory.contains(new Coordinates(2, 4)));
        assertTrue(territory.contains(new Coordinates(3, 4)));
        assertTrue(territory.contains(new Coordinates(4, 0)));
        assertTrue(territory.contains(new Coordinates(4, 1)));
        assertTrue(territory.contains(new Coordinates(4, 2)));
        assertTrue(territory.contains(new Coordinates(4, 4)));
    }

    @Test
    public void testCheckForTerritory3(){

        BoardManager.addStone(2, 1, new Stone(StoneColor.WHITE));

        List<Coordinates> territory = TerritoryManager.lookForTerritory(StoneColor.WHITE);

        assertEquals(24, territory.size());
    }

    @Test
    public void testCheckForTerritory4(){

        BoardManager.addStone(2, 1, new Stone(StoneColor.WHITE));

        BoardManager.addStone(4, 3, new Stone(StoneColor.BLACK));

        List<Coordinates> territoryWhite = TerritoryManager.lookForTerritory(StoneColor.WHITE);
        List<Coordinates> territoryBlack = TerritoryManager.lookForTerritory(StoneColor.BLACK);

        assertEquals(0, territoryWhite.size());
        assertEquals(0, territoryBlack.size());
    }

    @Test
    public void testCheckForTerritory5(){

        BoardManager.addStone(0, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(0, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(0, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(0, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(0, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(4, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(2, 4, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 4, new Stone(StoneColor.WHITE));



        List<Coordinates> territory = TerritoryManager.lookForTerritory(StoneColor.WHITE);

        assertEquals(9, territory.size());

    }

    @Test
    public void testCheckForTerritory6(){

        BoardManager.addStone(0, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 0, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 1, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 2, new Stone(StoneColor.WHITE));
        BoardManager.addStone(1, 3, new Stone(StoneColor.WHITE));
        BoardManager.addStone(3, 0, new Stone(StoneColor.WHITE));

        BoardManager.addStone(2, 4, new Stone(StoneColor.BLACK));
        BoardManager.addStone(3, 2, new Stone(StoneColor.BLACK));
        BoardManager.addStone(3, 3, new Stone(StoneColor.BLACK));
        BoardManager.addStone(4, 1, new Stone(StoneColor.BLACK));


        List<Coordinates> territoryWhite = TerritoryManager.lookForTerritory(StoneColor.WHITE);
        List<Coordinates> territoryBlack = TerritoryManager.lookForTerritory(StoneColor.BLACK);

        assertEquals(4, territoryBlack.size());
        assertEquals(3, territoryWhite.size());
        assertTrue(territoryWhite.contains(new Coordinates(0, 0)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 1)));
        assertTrue(territoryWhite.contains(new Coordinates(0, 2)));
        assertTrue(territoryBlack.contains(new Coordinates(3, 4)));
        assertTrue(territoryBlack.contains(new Coordinates(4, 4)));
        assertTrue(territoryBlack.contains(new Coordinates(4, 3)));
        assertTrue(territoryBlack.contains(new Coordinates(4, 2)));

    }

}

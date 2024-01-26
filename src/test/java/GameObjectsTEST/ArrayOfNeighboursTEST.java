package GameObjectsTEST;

import GameObjects.ArrayOfNeighbours;
import GameObjects.NeighbourState;
import GameObjects.StoneNeighbours;
import MyExceptions.OccupiedTileException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ArrayOfNeighboursTEST {

    private ArrayOfNeighbours arrayOfNeighbours;

    @Before
    public void setUp() {
        arrayOfNeighbours = new ArrayOfNeighbours(3);
    }

    @Test
    public void testGetArraySize() {
        assertEquals(3, arrayOfNeighbours.getArraySize());
    }

    @Test
    public void testGetAndSetNeighbours() {

        StoneNeighbours stoneNeighbours = new StoneNeighbours(NeighbourState.ENEMY, NeighbourState.ALLY, NeighbourState.EMPTY, NeighbourState.WALL);
        arrayOfNeighbours.setNeighbours(1, 1, stoneNeighbours);
        assertEquals(stoneNeighbours, arrayOfNeighbours.getNeighbours(1, 1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetNeighboursOutOfBounds() {
        arrayOfNeighbours.getNeighbours(2, 3);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetNeighboursOutOfBounds() {
        StoneNeighbours stoneNeighbours = new StoneNeighbours(NeighbourState.EMPTY, NeighbourState.ALLY, NeighbourState.WALL, NeighbourState.ENEMY);
        arrayOfNeighbours.setNeighbours(14, 1, stoneNeighbours);
    }

    @Test
    public void testGetNeighboursOccupiedTileException() {
        assertNull(arrayOfNeighbours.getNeighbours(1, 1));
    }
}

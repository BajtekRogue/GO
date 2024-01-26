package GameObjectsTEST;

import GameObjects.NeighbourState;
import GameObjects.StoneNeighbours;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoneNeighboursTEST {

    @Test
    public void testGetAndSetStates() {
        StoneNeighbours stoneNeighbours = new StoneNeighbours(NeighbourState.EMPTY, NeighbourState.WALL, NeighbourState.ENEMY, NeighbourState.ALLY);

        assertEquals(NeighbourState.EMPTY, stoneNeighbours.getNorth());
        assertEquals(NeighbourState.WALL, stoneNeighbours.getEast());
        assertEquals(NeighbourState.ENEMY, stoneNeighbours.getSouth());
        assertEquals(NeighbourState.ALLY, stoneNeighbours.getWest());

        stoneNeighbours.setNorth(NeighbourState.ALLY);
        stoneNeighbours.setEast(NeighbourState.ENEMY);
        stoneNeighbours.setSouth(NeighbourState.WALL);
        stoneNeighbours.setWest(NeighbourState.EMPTY);

        assertEquals(NeighbourState.ALLY, stoneNeighbours.getNorth());
        assertEquals(NeighbourState.ENEMY, stoneNeighbours.getEast());
        assertEquals(NeighbourState.WALL, stoneNeighbours.getSouth());
        assertEquals(NeighbourState.EMPTY, stoneNeighbours.getWest());
    }

    @Test
    public void testNumberOfBreathsAndConnections() {
        StoneNeighbours stoneNeighbours = new StoneNeighbours(NeighbourState.EMPTY, NeighbourState.WALL, NeighbourState.ENEMY, NeighbourState.ALLY);
        assertEquals(1, stoneNeighbours.getNumberOfBreaths());
        assertEquals(1, stoneNeighbours.getNumberOfConnections());

        stoneNeighbours.setNorth(NeighbourState.EMPTY);
        stoneNeighbours.setEast(NeighbourState.ALLY);
        stoneNeighbours.setSouth(NeighbourState.EMPTY);
        stoneNeighbours.setWest(NeighbourState.EMPTY);

        assertEquals(3, stoneNeighbours.getNumberOfBreaths());
        assertEquals(1, stoneNeighbours.getNumberOfConnections());
    }
}


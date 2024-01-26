package GameObjectsTEST;

import GameObjects.Coordinates;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinatesTEST {

    @Test
    public void testGettersAndSetters() {
        Coordinates coordinates = new Coordinates(1, 2);

        assertEquals(1, coordinates.getX());
        assertEquals(2, coordinates.getY());

        coordinates.setCoordinates(3, 4);
        assertEquals(3, coordinates.getX());
        assertEquals(4, coordinates.getY());
    }

    @Test
    public void testEquals() {
        Coordinates coordinates1 = new Coordinates(1, 2);
        Coordinates coordinates2 = new Coordinates(1, 2);
        Coordinates coordinates3 = new Coordinates(3, 4);

        assertTrue(coordinates1.equals(coordinates2));
        assertTrue(coordinates2.equals(coordinates1));

        assertFalse(coordinates1.equals(coordinates3));
        assertFalse(coordinates3.equals(coordinates1));
    }

    @Test
    public void testHashCode() {
        Coordinates coordinates1 = new Coordinates(1, 2);
        Coordinates coordinates2 = new Coordinates(1, 2);
        Coordinates coordinates3 = new Coordinates(3, 4);

        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());
        assertFalse(coordinates1.hashCode() == coordinates3.hashCode());
    }
}


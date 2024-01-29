package GameObjectsTEST;

import GameObjects.Coordinates;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinatesTEST {

    @Test
    public void testGettersAndSetters() {
        Coordinates coordinates = CoordinatesFactory.createCoordinates(1, 2);

        assertEquals(1, coordinates.getX());
        assertEquals(2, coordinates.getY());

        coordinates.setCoordinates(3, 4);
        assertEquals(3, coordinates.getX());
        assertEquals(4, coordinates.getY());
    }

    @Test
    public void testEquals() {
        Coordinates coordinates1 = CoordinatesFactory.createCoordinates(1, 2);
        Coordinates coordinates2 = CoordinatesFactory.createCoordinates(1, 2);
        Coordinates coordinates3 = CoordinatesFactory.createCoordinates(3, 4);

        assertEquals(coordinates1, coordinates2);
        assertEquals(coordinates2, coordinates1);

        assertNotEquals(coordinates1, coordinates3);
        assertNotEquals(coordinates3, coordinates1);
    }

    @Test
    public void testHashCode() {
        Coordinates coordinates1 = CoordinatesFactory.createCoordinates(1, 2);
        Coordinates coordinates2 = CoordinatesFactory.createCoordinates(1, 2);
        Coordinates coordinates3 = CoordinatesFactory.createCoordinates(3, 4);

        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());
        assertNotEquals(coordinates1.hashCode(), coordinates3.hashCode());
    }
}


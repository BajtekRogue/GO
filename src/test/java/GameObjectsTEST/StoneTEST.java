package GameObjectsTEST;

import GameObjects.Stone;
import GameObjects.StoneColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoneTEST {

    @Test
    public void testGetStoneColor() {
        Stone stone = new Stone(StoneColor.BLACK);
        assertEquals(StoneColor.BLACK, stone.getStoneColor());
    }

    @Test
    public void testCreateStoneWithWhiteColor() {
        Stone stone = new Stone(StoneColor.WHITE);
        assertEquals(StoneColor.WHITE, stone.getStoneColor());
    }

    @Test
    public void testCreateStoneWithBlackColor() {
        Stone stone = new Stone(StoneColor.BLACK);
        assertEquals(StoneColor.BLACK, stone.getStoneColor());
    }


}


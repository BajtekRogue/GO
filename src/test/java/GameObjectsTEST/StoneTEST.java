package GameObjectsTEST;

import GameObjects.Stone;
import GameObjects.StoneColor;
import Factories.StoneFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoneTEST {

    @Test
    public void testGetStoneColor() {
        Stone stone = StoneFactory.createBlackStone();
        assertEquals(StoneColor.BLACK, stone.getStoneColor());
    }

    @Test
    public void testCreateStoneWithWhiteColor() {
        Stone stone = StoneFactory.createWhiteStone();
        assertEquals(StoneColor.WHITE, stone.getStoneColor());
    }

    @Test
    public void testCreateStoneWithBlackColor() {
        Stone stone = StoneFactory.createBlackStone();
        assertEquals(StoneColor.BLACK, stone.getStoneColor());
    }


}


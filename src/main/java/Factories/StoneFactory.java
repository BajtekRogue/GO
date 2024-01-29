package Factories;

import GameObjects.Stone;
import GameObjects.StoneColor;

public class StoneFactory {

    public static Stone createBlackStone(){
        return new Stone(StoneColor.BLACK);
    }

    public static Stone createWhiteStone(){
        return new Stone(StoneColor.WHITE);
    }

    public static Stone createStoneInColor(StoneColor stoneColor){
        if(stoneColor == StoneColor.WHITE)
            return createWhiteStone();
        else
            return createBlackStone();
    }
}

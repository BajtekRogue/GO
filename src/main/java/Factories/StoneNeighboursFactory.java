package Factories;

import GameObjects.NeighbourState;
import GameObjects.StoneNeighbours;

public class StoneNeighboursFactory {

    public static StoneNeighbours createStoneNeighbours(NeighbourState north, NeighbourState east, NeighbourState south, NeighbourState west){
        return new StoneNeighbours(north,east,south,west);
    }
}

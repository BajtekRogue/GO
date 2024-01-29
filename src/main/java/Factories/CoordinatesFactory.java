package Factories;

import GameObjects.Coordinates;

public class CoordinatesFactory {
    
    public static Coordinates createCoordinates(int x, int y){
        return new Coordinates(x,y);
    }
}

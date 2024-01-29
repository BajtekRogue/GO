package GameObjectsLogic;

import Factories.CoordinatesFactory;
import GameObjects.Coordinates;

import java.util.List;

public class ExceptionManager {

    private static Coordinates KO_coordinates = CoordinatesFactory.createCoordinates(-1, -1);

    public static void setKO_coordinates(Coordinates KO_position) {
        KO_coordinates = KO_position;
    }

    public static Coordinates getKO_coordinates() {
        return KO_coordinates;
    }

    public static void restKO_coordinates(){
        KO_coordinates = CoordinatesFactory.createCoordinates(-1, -1);
    }

    public static boolean checkForSuicide(int x, int y) {
        List<Coordinates> stonesToBeCaptured = CaptureManager.lookForChain(x, y);

        //if no stones can be captured return. If something breaks remove isEmpty()
        if (stonesToBeCaptured == null || stonesToBeCaptured.isEmpty())
            return false;


        BoardManager.removeStone(x, y);
        NeighbourManager.updateNeighbours(x, y);
        return true;

    }

    public static boolean checkForKO(int x, int y){
        if(x == KO_coordinates.getX() && y == KO_coordinates.getY()){
            BoardManager.removeStone(x, y);
            NeighbourManager.updateNeighbours(x, y);
            return true;
        }
        return false;
    }
}

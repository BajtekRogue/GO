package GameObjectsLogic;

import GameObjects.Coordinates;
import MyExceptions.KOException;
import MyExceptions.SuicideException;

import java.util.List;

public class ExceptionManager {

    private static Coordinates KO_coordinates = new Coordinates(-1, -1);

    public static void setKO_coordinates(Coordinates KO_position) {
        KO_coordinates = KO_position;
    }

    public static Coordinates getKO_coordinates() {
        return KO_coordinates;
    }

    public static void restKO_coordinates(){
        KO_coordinates = new Coordinates(-1, -1);
    }

    public static void checkForSuicide(int x, int y) throws SuicideException {
        List<Coordinates> stonesToBeCaptured = CaptureManager.lookForChain(x, y);

        //if no stones can be captured return. If something breaks remove isEmpty()
        if (stonesToBeCaptured == null || stonesToBeCaptured.isEmpty())
            return;


        if(stonesToBeCaptured != null){
            BoardManager.removeStone(x, y);
            NeighbourManager.updateNeighbours(x, y);
            throw new SuicideException("Cannot commit suicide!");
        }
    }

    public static void checkForKO(int x, int y) throws KOException {
        if(x == KO_coordinates.getX() && y == KO_coordinates.getY()){
            BoardManager.removeStone(x, y);
            NeighbourManager.updateNeighbours(x, y);
            throw new KOException("Cannot commit KO");
        }
    }
}

package MyExceptions;

public class OccupiedTileException extends Exception{

    public OccupiedTileException(String message) {
        super(message);
    }
}

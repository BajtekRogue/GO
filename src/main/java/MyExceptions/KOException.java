package MyExceptions;

public class KOException extends Exception{

    public KOException(String message) {
        super(message);
    }
    //jezeli zbity jest jeden i jezeli chcesz psotawic na polu zbitego to throw exception
}

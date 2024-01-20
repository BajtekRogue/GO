package Server;

// GameRequest.java

import java.io.Serializable;

public class GameRequest implements Serializable {
    private String playerType;

    public GameRequest(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerType() {
        return playerType;
    }
}

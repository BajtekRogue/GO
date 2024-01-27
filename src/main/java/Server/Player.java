package Server;

import GameObjects.StoneColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Player {
    private final int playerID;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private boolean initialMessagesSent = false;
    private StoneColor stoneColor;

    public Player(int playerID, Socket socket) throws IOException {
        this.playerID = playerID;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public int getPlayerID() {
        return playerID;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }
    public boolean areInitialMessagesSent() {
        return initialMessagesSent;
    }

    public void setInitialMessagesSent() {
        this.initialMessagesSent = true;
    }

    public void setStoneColor(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }
}
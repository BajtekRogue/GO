package Server;

import GameObjects.StoneColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Player {
    private int playerID;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
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

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }
}
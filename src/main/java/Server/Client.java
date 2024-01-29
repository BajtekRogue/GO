package Server;

import ClientCommands.*;
import GUI.GoGUI;
import GameObjects.StoneColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Client extends Application {

    private final static int PORT = 1161;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private boolean isPlayerReady = false;
    private boolean firstPlayer = false;
    private boolean myTurn = false;
    private StoneColor playerColor;
    private GoGUI goGUI;
    private Map<String, Command> commandMap = new HashMap<>();
    public Client() {
        commandMap.put("GameAccepted", new GameAcceptedCommand());
        commandMap.put("BLACK", new BlackCommand());
        commandMap.put("WHITE", new WhiteCommand());
        commandMap.put("KO", new KOCommand());
        commandMap.put("OK", new LegalMoveCommand());
        commandMap.put("SUICIDE", new SuicideCommand());
        commandMap.put("OCCUPIED", new OccupiedCommand());
        commandMap.put("PASS", new PassCommand());
        commandMap.put("SURRENDER", new SurrenderCommand());
        commandMap.put("ENDGAME", new EndgameCommand());
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void switchTurn(){
        myTurn = !myTurn;
    }
    public StoneColor getPlayerColor(){
        return playerColor;
    }

    public void setPlayerColor(StoneColor playerColor) {
        this.playerColor = playerColor;
    }

    public boolean isPlayerReady() {
        return isPlayerReady;
    }

    public void setPlayerReady() {
        isPlayerReady = true;
    }

    public GoGUI getGoGUI() {
        return goGUI;
    }

    public void run() {
        try {
            Socket socket = new Socket("localhost", PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            String message = receiveMessage();
            goGUI = new GoGUI(this);
            System.out.println(message);

            if (message.startsWith("You have successfully connected to the server as player")) {
                // Extract player ID from the notification
                int playerID = Integer.parseInt(message.replaceAll("[^0-9]", ""));
                System.out.println("Player ID: " + playerID);
                System.out.println("Waiting for other players to join...");

                CountDownLatch guiLatch = new CountDownLatch(1);
                firstPlayer = playerID == 1;

                Platform.runLater(() -> {
                    try {
                        goGUI.start(new Stage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    guiLatch.countDown();
                });

                // Start a new thread to handle messages from the server
                new Thread(this::handleMessagesFromTheServer).start();
            }
        } catch (IOException e) {
            System.out.println("Connection error has occurred");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception");
        }
    }

    private void handleMessagesFromTheServer(){
        try {
            while (true) {
                String message = receiveMessage();
                System.out.println("Received message: " + message);

                for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
                    if (message.contains(entry.getKey())) {
                        entry.getValue().execute(this, message);
                        break; // Only handle the first matching command
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while receiving messages: " + e.getMessage());
        }
    }
    public String receiveMessage() throws IOException, ClassNotFoundException {
        return (String) inputStream.readObject();
    }

    private void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }
    public void sendMove(int x, int y) throws IOException {
        sendMessage("Move " + x + " " + y);
    }
    public void sendSurrender() throws IOException {
        sendMessage("Surrender");
    }
    public void sendPass() throws IOException {
        sendMessage("Pass");
    }
    public void sendGameWithHuman() throws IOException {
        sendMessage("Human");
    }
    public void sendGameWithBot() throws IOException {
        sendMessage("Bot");
    }

    @Override
    public void start(Stage stage){

    }
}

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Client extends Application {

    private final static int PORT = 1151;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private boolean isPlayerReady = false;
    private boolean firstPlayer = false;
    private boolean myTurn = false;
    private StoneColor playerColor;
    private GoGUI goGUI;
    private final Map<String, Command> commandMap = new HashMap<>();
    private List<String> availableGames;
    private Boolean updatedGames = false;
    private boolean hasBoardSize = false;
    private Socket socket;

    public Client() {
        commandMap.put("GameAccepted", new GameAcceptedCommand());
        commandMap.put("BLACK", new BlackCommand());
        commandMap.put("WHITE", new WhiteCommand());
        commandMap.put("KO", new KOCommand());
        commandMap.put("NOT", new OppositeLegalMoveCommand());
        commandMap.put("OK", new LegalMoveCommand());
        commandMap.put("SUICIDE", new SuicideCommand());
        commandMap.put("OCCUPIED", new OccupiedCommand());
        commandMap.put("PASS", new PassCommand());
        commandMap.put("SURRENDER", new SurrenderCommand());
        commandMap.put("ENDGAME", new EndgameCommand());
        commandMap.put("GAMES", new ListOfGamesCommand());
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

    public boolean isFirstPlayer(){return firstPlayer;}
    public void setPlayerReady() {
        isPlayerReady = true;
    }

    public GoGUI getGoGUI() {
        return goGUI;
    }

    public void run() {
        try {
            socket = new Socket("localhost", PORT);
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
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Error while starting GUI");
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
                if(!firstPlayer && message.contains("PlayerID: 2")) {
                    hasBoardSize = true;
                    goGUI.setBoardSize(MessageDecoder.extractBoardSize(message));
                }
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
    public void sendGameWithHuman(int boardSize) throws IOException {
        sendMessage("Human " + boardSize);
    }
    public void sendGameWithBot(int boardSize) throws IOException {
        sendMessage("Bot " + boardSize);
    }

    @Override
    public void start(Stage stage){

    }

    public void setUpdatedGamesTrue(){
        updatedGames = true;
    }
    public void askForAvailableGames() throws IOException {
        sendMessage("DBGames");
    }

    public boolean isGameListUpToDate(){
        return updatedGames;
    }
    public boolean hasBoardSize(){return hasBoardSize;}
    public void setAvailableGames(List<String> games){
        availableGames = games;
    }
    public List<String> getAvailableGames(){
        return availableGames;
    }

    public void loadMove(String selectedGame, int moveNumber) throws IOException {
        sendMessage("LOAD " + selectedGame + " " + moveNumber);
    }

    public void deloadMove(String selectedGame, int moveNumber) throws IOException {
        sendMessage("DLOAD " + selectedGame + " " + moveNumber);
    }
    public void requestLoadedGameBoardSize(String game){
        goGUI.setBoardSize(MessageDecoder.parseGameSize(game));
    }
    public List<String> requestAvailableGames(String games) {
        return MessageDecoder.parseGames(games);
    }

    public void disconnect() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            System.out.println("Error while disconnecting: " + e.getMessage());
        }
    }

}
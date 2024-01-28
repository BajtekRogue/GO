package Server;

import GUI.GoGUI;
import GameObjects.StoneColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    public Client() {
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void switchTurn(){
        myTurn = !myTurn;
    }
    public StoneColor getPlayerColor(){
        return playerColor;
    }
    public boolean isPlayerReady() {
        return isPlayerReady;
    }

    public void run() {
        try {
            Socket socket = new Socket("localhost", PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            String message1 = receiveMessage();
            goGUI = new GoGUI(this);
            System.out.println(message1);

            if (message1.startsWith("You have successfully connected to the server as player")) {
                // Extract player ID from the notification
                int playerID = Integer.parseInt(message1.replaceAll("[^0-9]", ""));
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

                // react accordingly to differently messages
                if (message.equals("GameAccepted")) {
                    isPlayerReady = true;
                    System.out.println("The other player also wants to play!");
                }
                else if(message.equals("BLACK")){
                    playerColor = StoneColor.BLACK;
                    myTurn = true;
                    System.out.println("You are playing " + message);
                    goGUI.toggleActivatePassButton();
                    goGUI.toggleFFPassButton();
                    goGUI.setGUITitle(playerColor);
                }
                else if(message.equals("WHITE")){
                    playerColor = StoneColor.WHITE;
                    myTurn = false;
                    System.out.println("You are playing " + message);
                    goGUI.setGUITitle(playerColor);
                }
                else if(message.contains("KO")){
                    goGUI.showAlert("Cannot commit KO");
                }
                else if(message.contains("OK")){
                    switchTurn();
                    goGUI.refreshGUIAfterSuccessfulMove(message);
                }
                else if(message.contains("SUICIDE")){
                    goGUI.showAlert("Cannot commit suicide");
                }
                else if(message.contains("OCCUPIED")){
                    goGUI.showAlert("This tile is already occupied");
                }
                else if(message.contains("PASS")){
                    switchTurn();
                    goGUI.refreshGUIAfterPass();
                }
                else if(message.contains("SURRENDER")){
                    goGUI.showSurrenderDialog();
                }
                else if(message.contains("ENDGAME")){
                    goGUI.endGame(message);
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

    public void playAgainstBot() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            System.out.println("Bot");
        }
    }

    @Override
    public void start(Stage stage){

    }
}

package Server;

import GUI.GoGUI;
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
    private static Socket socket;
    private boolean isPlayerReady = false;
    private boolean firstPlayer = false;
    private int playerID; // Player ID received from the server
    private String playerColor;

    public Client() {
    }

    public boolean isPlayerReady() {
        return isPlayerReady;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void run() {
        try {
            socket = new Socket("localhost", PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            String message1 = receiveMessage();
            GoGUI goGUI = new GoGUI(this);
            System.out.println(message1);

            if (message1.startsWith("You have successfully connected to the server as player")) {
                // Extract player ID from the notification
                playerID = Integer.parseInt(message1.replaceAll("[^0-9]", ""));
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
                    guiLatch.countDown();  // This counts down the latch
                });



                // Dodaj pętlę do odbierania wiadomości i wypisywania ich
                new Thread(() -> {
                    try {
                        while (true) {
                            String message = receiveMessage();
                            System.out.println("Received message: " + message);
                            // Możesz dodać odpowiednie akcje związane z otrzymaniem wiadomości
                            if (message.equals("GameAccepted")) {
                                isPlayerReady = true;
                                System.out.println("The other player also wants to play!");
                                playAgainstHuman();
                            }
                            if(message.equals("BLACK")){
                                playerColor = message;
                                System.out.println("You are playing " + message);
                            }
                            if(message.equals("WHITE")){
                                playerColor = message;
                                System.out.println("You are playing " + message);
                            }
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error while receiving messages: " + e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Connection error has occurred");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception");
        }
    }

    public String receiveMessage() throws IOException, ClassNotFoundException {
        String notification = (String) inputStream.readObject();
        return notification;
    }

    private void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);

    }
    public void sendMove(int x, int y) throws IOException {
        sendMessage("M " + x + " " + y);
    }
    public void sendSurrender() throws IOException {
        sendMessage("S");
    }
    public void sendPass() throws IOException {
        sendMessage("P");
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

    public void playAgainstHuman() throws InterruptedException, IOException, ClassNotFoundException {
        // Your game logic here
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Implement if needed
    }
}

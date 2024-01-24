package Server;

import GUI.GameModeSelectionGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Client1 extends Application{

    private final static int PORT = 1161;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static Socket socket;

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        try {
            socket = new Socket("localhost", PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            String notification1 = receiveMessage();
            GameModeSelectionGUI gameModeSelectionGUI = new GameModeSelectionGUI();
            System.out.println(notification1);

            if (notification1.equals("You have successfully connected to the server as player1")) {
                CountDownLatch guiLatch = new CountDownLatch(1);

                Platform.runLater(() -> {
                    gameModeSelectionGUI.start(new Stage());
                    guiLatch.countDown();  // This counts down the latch
                });

                // Wait for the GUI to be shown on the JavaFX application thread
                guiLatch.await();
                while (gameModeSelectionGUI.isWindowOpen()) {
                    Thread.sleep(1000);  // Adjust the sleep time as needed
                }

                String gameMode = gameModeSelectionGUI.getSelectedGameMode();
                System.out.println("Selected game mode: " + gameMode);
                sendMessage(gameMode);

                if(gameMode.equals("Human")){
                    System.out.println("Waiting for player2 to join...");
                    String notification2 = receiveMessage();
                    System.out.println(notification2);
                    if(notification2.equals("Player2 has successfully connected")){
                        playAgainstHuman();
                    }
                }else{
                    playAgainstBot();
                }
            }
            else if (notification1.equals("You have successfully connected to the server as player2")) {
                playAgainstHuman();
            }







        }catch(IOException e) {
            System.out.println("Connection error has occurred");
        }catch(ClassNotFoundException e){
            System.out.println("Class not found exception");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for user selection");
        }
    }




    private static String receiveMessage() throws IOException, ClassNotFoundException {
        String notification = (String) inputStream.readObject();
        return notification;
    }

    private static void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    public static void playAgainstBot() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
            System.out.println("Bot");
        }
        //add
    }

    public static void playAgainstHuman() throws InterruptedException, IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        String message = receiveMessage();
        System.out.println(message);
        boolean isGameOngoing = true;
        String currentMove = "";

        while(isGameOngoing){

            message = receiveMessage();
            System.out.println(message);

            if(message.equals("Your move") || message.equals("Your move was incorrect"))
                currentMove = scanner.nextLine();

            sendMessage(currentMove);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}

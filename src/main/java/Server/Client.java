package Server;

import GUI.GameModeSelectionGUI;
import GUI.GoGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Client extends Application{

    private final static int PORT = 1161;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static Socket socket;
    private boolean isPlayerReady = false;

    public Client(){

    }
    public boolean isPlayerReady(){
        return isPlayerReady;
    }

    public void run() {
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
                        isPlayerReady = true;
                        playAgainstHuman();
                    }
                }else{
                    playAgainstBot();
                }
            }
            else if (notification1.equals("You have successfully connected to the server as player2")) {
                isPlayerReady = true;
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




    public String receiveMessage() throws IOException, ClassNotFoundException {
        String notification = (String) inputStream.readObject();
        return notification;
    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }

    public void playAgainstBot() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
            System.out.println("Bot");
        }
        //add
    }

    public void playAgainstHuman() throws InterruptedException, IOException, ClassNotFoundException {


        Scanner scanner = new Scanner(System.in);
        String message = receiveMessage();
        System.out.println(message);
        boolean isGameOngoing = true;
        String currentMove = "";


        CountDownLatch guiLatch = new CountDownLatch(1);

        // Launch the first JavaFX application in a separate thread
        new Thread(() -> {
            Platform.runLater(() -> {
                GoGUI goGUI = new GoGUI(this);
                try {
                    goGUI.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Count down the latch when the GUI is launched
                    guiLatch.countDown();
                }
            });
        }).start();

        // Wait for the first GUI to be launched
        guiLatch.await();

        // Launch the second JavaFX application in a separate thread
        new Thread(() -> {
            Platform.runLater(() -> {
                GoGUI secondGoGUI = new GoGUI(this);
                try {
                    secondGoGUI.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();

//        while(isGameOngoing){
//
//            message = receiveMessage();
//            System.out.println(message);
//
//            if(message.equals("Your move") || message.equals("Your move was incorrect"))
//                currentMove = scanner.nextLine();
//
//            sendMessage(currentMove);
//        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }


}

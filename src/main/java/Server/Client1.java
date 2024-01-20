package Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Client1 extends Application implements PlayerTypeSelectionListener {

    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private Socket socket;
    private CountDownLatch playerReadyLatch = new CountDownLatch(1);
    private boolean isThereAnotherPlayer = false;
    private boolean isThereABot = false;

    public static void main(String[] args) {
        new Client1().run();
    }

    public void run() {
        try {
            socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            String notification1 = (String) in.readObject();

            if (notification1.equals("You are the first player. Please choose player type.")) {
                Platform.runLater(() -> {
                    GameModeSelectionGUI gameModeSelectionGUI = new GameModeSelectionGUI(this);
                    gameModeSelectionGUI.start(new Stage());
                });
            }

            // Wait until the latch is counted down in onPlayerTypeSelected
            playerReadyLatch.await();
            System.out.println("Player ready");

            String notification2 = (String) in.readObject();
            System.out.println(notification2);
            String notification3 = (String) in.readObject();

            if(notification3.equals("Player 2 has joined")){
                System.out.println(notification3);
                System.out.println("The game begins");
                playingTheGame();
            }


        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerTypeSelected(String playerType) {
        try {
            // Now you can use 'out' here to send the player type to the server
            if ("Human".equals(playerType)) {
                isThereAnotherPlayer = true;
            } else if ("Bot".equals(playerType)) {
                isThereABot = true;
            }
            out.writeObject(new GameRequest(playerType));
            playerReadyLatch.countDown(); // Count down the latch to release the waiting thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        // Implement the start method as needed
    }

    public void playingTheGame(){

//        while(true){
//            try{
//                Thread.sleep(2000);
//                System.out.println("A");
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//
//
//        }
        int i = 0;

        while (true){
            try{
                String notification = (String) in.readObject();
                System.out.println(notification);

                switch (notification){
                    case "Correct move":
                        break;
                    case "Incorrect move":
                        break;
                    default:
                        break;
                }

                out.writeObject(new GameRequest(Integer.toString(i)));
                i++;

            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }

        }
    }
}

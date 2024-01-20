package Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Client2 extends Application {

    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private CountDownLatch playerReadyLatch = new CountDownLatch(1);
    private boolean isThereAnotherPlayer = false;
    private boolean isThereABot = false;

    public static void main(String[] args) {
        new Client2().run();
    }

    public void run() {
        try {
            Socket socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            String notification1 = (String) in.readObject();
            System.out.println(notification1);
            if (notification1.equals("You are the second player.")) {
                out.writeObject(new GameRequest("Player 2 ready"));
            }

            System.out.println("The game begins");
            playingTheGame();


        } catch (IOException | ClassNotFoundException  e) {
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
//                System.out.println("B");
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
                i--;

            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }

        }
    }
}

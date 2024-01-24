package Server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client2{

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

            if (notification1.equals("You have successfully connected to the server as player2")) {
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
}

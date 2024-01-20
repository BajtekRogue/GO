package Server;// Server.java

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private static boolean isThereABot = false;
    private static boolean isFirstPlayerReady = false;
    private static boolean isSecondPlayerReady = false;
    private static AtomicBoolean client2Ready = new AtomicBoolean(false);
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server is running...");

            Socket clientSocket1 = serverSocket.accept();
            new Thread(() -> handleClient1(clientSocket1)).start();

            // Wait for the first player to make a choice
            while (!isFirstPlayerReady) {
                // Sleep for a short duration to avoid busy-waiting
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if (isFirstPlayerReady && !isThereABot) {
                Socket clientSocket2 = serverSocket.accept();
                new Thread(() -> handleClient2(clientSocket2)).start();
            } else {
                // If the first player is a bot or no second player allowed, print a message
                System.out.println("No second player allowed and first player plays against bot.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendNotification(Socket clientSocket, ObjectOutputStream out, String message) {
        try {

            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient1(Socket clientSocket) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            sendNotification(clientSocket, out, "You are the first player. Please choose player type.");
            // Handle the client connection
            GameRequest gameRequest = (GameRequest) in.readObject();

            // Set the flag based on the player type of the first client
            if ("Human".equals(gameRequest.getPlayerType())) {
                isFirstPlayerReady = true;
                isThereABot = false;
                System.out.println("Player1 selected playing against another player");
                System.out.println("Waiting for Player 2 to join...");
                sendNotification(clientSocket, out, "Waiting for Player 2 to join...");

                // Wait for Player 2 to signal readiness
                while (!client2Ready.get()) {
                    Thread.sleep(100);
                }

                sendNotification(clientSocket, out, "Player 2 has joined");
                System.out.println("Game begins");
                while (true){}

            } else if ("Bot".equals(gameRequest.getPlayerType())) {
                isFirstPlayerReady = true;
                isThereABot = true;
                System.out.println("Player1 selected playing against a bot");
            }



            // Continue with the game logic as needed

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // The rest of your code for handling client2 remains unchanged
    private static void handleClient2(Socket clientSocket) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            sendNotification(clientSocket, out,"You are the second player.");
            client2Ready.set(true);
            System.out.println("Player 2 has joined");
            while (true){}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Server;


import GameObjects.StoneColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 1161;
    private static Socket clientSocket1;
    private static Socket clientSocket2;
    private static ServerSocket serverSocket;
    private static ObjectOutputStream outputStream1;
    private static ObjectInputStream inputStream1;
    private static ObjectOutputStream outputStream2;
    private static ObjectInputStream inputStream2;
    private static String gameMode;
    private static StoneColor player1color;
    private static StoneColor player2color;


    public static void main(String[] args){
        startServer();
    }

    public static void startServer(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running");

            connectPlayer1();
            selectGameMode();

            if (gameMode.equals("Human")) {
                connectPlayer2();
                sendNotification(outputStream1,"Player2 has successfully connected");
                startGameBetweenTwoPlayers();
            }
            else if(gameMode.equals("Bot")){
                startGameBetweenPlayerAndBot();
            }



        }catch(IOException e) {
            System.out.println("Connection error has occurred");
        }catch(ClassNotFoundException e){
            System.out.println("Class not found exception");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for user selection");
        }
    }

    public static void connectPlayer1() throws IOException{
        clientSocket1 = serverSocket.accept();
        outputStream1 = new ObjectOutputStream(clientSocket1.getOutputStream());
        inputStream1 = new ObjectInputStream(clientSocket1.getInputStream());
        sendNotification(outputStream1, "You have successfully connected to the server as player1");
    }

    public static void connectPlayer2() throws IOException{

        if (gameMode.equals("Human")) {
            clientSocket2 = serverSocket.accept();
            outputStream2 = new ObjectOutputStream(clientSocket2.getOutputStream());
            inputStream2 = new ObjectInputStream(clientSocket2.getInputStream());
            sendNotification(outputStream2, "You have successfully connected to the server as player2");
        }
    }


    private static void sendNotification(ObjectOutputStream out, String message) throws IOException{
        out.writeObject(message);
        out.flush();
    }

    public static void sendNotification(StoneColor stoneColor, String message) throws IOException{

        if(stoneColor == player1color)
            sendNotification(outputStream1, message);
        else
            sendNotification(outputStream2, message);
    }

    private static String receiveNotification(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String message = (String) in.readObject();
        return message;
    }

    public static String receiveNotification(StoneColor stoneColor) throws IOException, ClassNotFoundException {

        if(stoneColor == player1color)
            return receiveNotification(inputStream1);
        else
            return receiveNotification(inputStream2);
    }

    private static void selectGameMode() throws IOException, ClassNotFoundException {

        gameMode = receiveNotification(inputStream1);

        if (gameMode.equals("Human")) {

            System.out.println("Player1 selected playing against another player");
            System.out.println("Waiting for Player 2 to join...");


        } else if (gameMode.equals("Bot")) {
            System.out.println("Player1 selected playing against a bot");
        }

        //maybe later add something else

    }

    private static void startGameBetweenTwoPlayers() throws InterruptedException, IOException, ClassNotFoundException {

        GameMaster gameMaster = new GameMaster(13);
        player1color = StoneColor.BLACK;
        player2color = StoneColor.WHITE;
        sendNotification(outputStream1, "The game has started");
        sendNotification(outputStream2, "The game has started");
        gameMaster.handleGameBetweenTwoPlayers();
    }

    private static void startGameBetweenPlayerAndBot() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
            System.out.println("S");
        }
    }



}

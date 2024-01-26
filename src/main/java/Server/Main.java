package Server;

import Server.Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args){

        Client client = new Client();
        client.run();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}

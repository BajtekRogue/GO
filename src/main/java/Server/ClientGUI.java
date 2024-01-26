package Server;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    public static void main(String args[]) {

        Client client = new Client();
        client.run();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}

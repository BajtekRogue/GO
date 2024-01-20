package Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameModeSelectionGUI extends Application {

    private  Button humanButton;
    private Button botButton;
    private VBox vbox;
    private Scene scene;
    private PlayerTypeSelectionListener listener;

    // Constructor with the listener parameter
    public GameModeSelectionGUI(PlayerTypeSelectionListener listener) {
        this.listener = listener;
    }
    // This method allows you to launch the application from another class

    // Default constructor
    public GameModeSelectionGUI() {
        // Empty constructor
    }
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Player Type Selection");

        humanButton = new Button("Human Player");
        botButton = new Button("Bot Player");

        humanButton.setOnAction(e -> handlePlayerTypeSelection("Human"));
        botButton.setOnAction(e -> handlePlayerTypeSelection("Bot"));

        vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(humanButton, botButton);

        scene = new Scene(vbox, 300, 150);
        primaryStage.setScene(scene);

        primaryStage.show();
    }



    private void handlePlayerTypeSelection(String playerType) {

            // Send the player type to the server
            listener.onPlayerTypeSelected(playerType);

            // Implement game logic for the chosen player type
            if ("Human".equals(playerType)) {
                System.out.println("Selected human");
            } else if ("Bot".equals(playerType)) {
                System.out.println("Selected bot");
            }

            Platform.runLater(() -> {
                Stage stage = (Stage) vbox.getScene().getWindow();
                stage.close();
            });




    }
}


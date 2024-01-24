package GUI;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameModeSelectionGUI extends Application {

    private  Button humanButton;
    private Button botButton;
    private VBox vbox;
    private Scene scene;
    private String selectedGameMode;

    public String getSelectedGameMode() {
        return selectedGameMode;
    }

    public GameModeSelectionGUI() {
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

        selectedGameMode = playerType;
        Platform.runLater(() -> {
            Stage stage = (Stage) vbox.getScene().getWindow();
            stage.close();
        });
    }


    public boolean isWindowOpen() {
        Stage stage = (Stage) vbox.getScene().getWindow();
        return stage.isShowing();
    }

}



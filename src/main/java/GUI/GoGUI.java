package GUI;

import GameObjects.Board;
import GameObjects.Coordinates;
import GameObjects.Stone;
import GameObjects.StoneColor;
import Server.Client;
import Server.MessageDecoder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class GoGUI extends Application {

    private static int BOARD_SIZE = 0;
    private static final int TILE_SIZE = 40;
    private static final double STONE_SIZE = 15;
    private static final double DOT_SIZE = 5;
    private int whitePoints = 0;
    private int blackPoints = 0;
    private Label turnLabel;
    private Label pointsLabel;

    private StoneColor currentPlayer;
    private Canvas canvas;
    private Button[][] buttons;
    private final Client client;
    private Board board;
    private final BooleanProperty activatePassButton = new SimpleBooleanProperty(false);
    private final BooleanProperty activateFFButton = new SimpleBooleanProperty(false);
    private Stage primaryStage;
    private String selectedGame;
    private Pane infoPane;
    private Button passButton;
    private Button surrenderButton;
    private int moveNumber;
    private Pane root;
    private boolean loadGame;

    public GoGUI(Client client) {
        this.board = new Board(BOARD_SIZE);
        this.currentPlayer = StoneColor.BLACK;
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Go Game");
        //Set icon on the application bar
        var appIcon = new Image("/images/gogameicon.png");
        primaryStage.getIcons().add(appIcon);

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/images/gogameicon.png"));
                taskbar.setIconImage(dockIcon);
            }

        }
        primaryStage.setResizable(false);
        Pane root = new Pane();
        root.setPadding(new Insets(10));
        Alert gameTypeDialog = new Alert(Alert.AlertType.CONFIRMATION);
        gameTypeDialog.setTitle("Choose Game Type");
        gameTypeDialog.setHeaderText(null);
        gameTypeDialog.setContentText("Select the game type:");
        this.root = root;
        ButtonType humanButton = new ButtonType("Human");
        ButtonType botButton = new ButtonType("Bot");
        ButtonType loadButton = new ButtonType("Load Game");

        if(client.isFirstPlayer()) {
            gameTypeDialog.getButtonTypes().setAll(humanButton, botButton, loadButton);
            Optional<ButtonType> result = gameTypeDialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == humanButton) {
                    client.sendGameWithHuman(showSizeSelectionDialog());
                } else if (result.get() == botButton) {
                    client.sendGameWithBot(showSizeSelectionDialog());
                } else if (result.get() == loadButton) {
                    handleLoadGameButtonClick();
                } else {
                    primaryStage.close();
                }
            }

        }else{
            client.sendGameWithHuman(BOARD_SIZE);
        }
        if(!client.isFirstPlayer()) {
            while (!client.hasBoardSize()) {
                sleep(1);
            }

        }
        board = new Board(BOARD_SIZE);

        System.out.println("PO " + BOARD_SIZE);

        if(!loadGame) {
            continueInitializingGUI();
        }

    }
    private void continueInitializingGUI(){
        // Board
        canvas = new Canvas((BOARD_SIZE) * TILE_SIZE, (BOARD_SIZE + 1) * TILE_SIZE);
        root.getChildren().add(canvas);

        // Separator Line
        Pane separatorLine = new Pane();
        separatorLine.setLayoutX((BOARD_SIZE) * TILE_SIZE-3);
        separatorLine.setMinWidth(3);
        separatorLine.setMinHeight(1000);
        separatorLine.setStyle("-fx-background-color: BLACK;");

        // Right side information
        infoPane = new Pane();
        infoPane.setLayoutX((BOARD_SIZE) * TILE_SIZE);
        infoPane.setMinWidth(300);
        infoPane.setMinHeight(1000);
        infoPane.setStyle("-fx-background-color: rgb(196,161,118);");

        // Player Turn Label
        turnLabel = new Label("Player Turn: BLACK");
        turnLabel.setLayoutY((((((double) BOARD_SIZE /2) - 1) * TILE_SIZE)));
        turnLabel.setLayoutX(80);
        turnLabel.setStyle("-fx-font-size: 100;");
        turnLabel.setStyle("-fx-font-weight: bold;");
        turnLabel.setTextFill(Color.BLACK);

        pointsLabel = new Label("Black Points: 0\nWhite Points: 0");
        pointsLabel.setLayoutY((((double) (BOARD_SIZE /2)* TILE_SIZE)));
        pointsLabel.setLayoutX(90);
        pointsLabel.setStyle("-fx-font-size: 80;");
        pointsLabel.setStyle("-fx-font-weight: bold;");
        pointsLabel.setStyle("-fx-text-fill: rgb(63,93,49);");



        // Pass Button
        passButton = new Button("Pass Turn");
        passButton.setLayoutY((((double) (BOARD_SIZE /2 + 1)* TILE_SIZE)));
        passButton.setLayoutX(90);
        passButton.setOnAction(e -> {
            try {
                handlePassButtonClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        passButton.disableProperty().bind(Bindings.not(activatePassButton));

        // FF Button
        surrenderButton = new Button("Forfeit");
        surrenderButton.setLayoutY(((((double) BOARD_SIZE /2 + 1.25) * TILE_SIZE)));
        surrenderButton.setLayoutX(100);
        surrenderButton.setOnAction(e -> {
            try {
                handleSurrenderButtonClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        surrenderButton.disableProperty().bind(Bindings.not(activateFFButton));

        infoPane.getChildren().addAll(turnLabel, pointsLabel, passButton, surrenderButton);
        root.getChildren().addAll(separatorLine, infoPane);

        // Buttons
        buttons = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int y = BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Button button = createButton(x, y);
                buttons[x][y] = button;
                root.getChildren().add(button);
            }
        }


        drawGoBoard();

        Scene scene = new Scene(root, (BOARD_SIZE + 1) * TILE_SIZE + 220, BOARD_SIZE * TILE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try {
                client.sendSurrender();
            } catch (IOException ignored) {}
            client.disconnect();
        });
        primaryStage.show();

    }
    private int showSizeSelectionDialog() {
        Alert sizeDialog = new Alert(Alert.AlertType.CONFIRMATION);
        sizeDialog.setTitle("Choose Board Size");
        sizeDialog.setHeaderText(null);
        sizeDialog.setContentText("Select the board size:");

        ButtonType size9x9 = new ButtonType("9x9");
        ButtonType size13x13 = new ButtonType("13x13");
        ButtonType size19x19 = new ButtonType("19x19");

        sizeDialog.getButtonTypes().setAll(size9x9, size13x13, size19x19);

        Optional<ButtonType> sizeResult = sizeDialog.showAndWait();

        if (sizeResult.isPresent()) {
            if (sizeResult.get() == size9x9) {
                BOARD_SIZE = 9;
                return 9;
            } else if (sizeResult.get() == size13x13) {
                BOARD_SIZE = 13;
                return 13;
            } else if (sizeResult.get() == size19x19) {
                BOARD_SIZE = 19;
                return 19;
            }
        }
        return -1;
    }


    public void toggleActivatePassButton(){
        activatePassButton.set(!activatePassButton.get());
    }

    public void toggleFFPassButton(){
        activateFFButton.set(!activateFFButton.get());
    }

    private void updateInfo() {
        turnLabel.setText("Player Turn: " + currentPlayer.toString());
        turnLabel.setTextFill((currentPlayer == StoneColor.BLACK) ? Color.BLACK : Color.WHITE);
        pointsLabel.setText("Black Points: " + blackPoints + "\nWhite Points: " + whitePoints);
    }

    private Button createButton(int x, int y) {
        Button button = new Button();
        button.setMinSize(STONE_SIZE * 2, STONE_SIZE * 2);
        button.setMaxSize(STONE_SIZE * 2, STONE_SIZE * 2);
        // when the program starts the board is empty
        button.setStyle("-fx-background-color: transparent;");
        // + 20 to add margins
        button.setLayoutX(x * TILE_SIZE - STONE_SIZE + 20);
        // + 20 to add margins
        button.setLayoutY(y * TILE_SIZE - STONE_SIZE + 20);
        button.setOnAction(e -> {
            try {
                handleButtonClick(x, y);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Add highlighting effect on mouse enter for empty buttons
        button.setOnMouseEntered(event -> {
            if (board.getTile(x, y) == null && client.isMyTurn() && !loadGame) {
                StoneColor stoneColor = client.getPlayerColor();
                Color highlightColor = (stoneColor == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                highlightColor = highlightColor.deriveColor(0, 1, 1, 0.5);
                button.setStyle("-fx-background-color: " + highlightColor.toString().replace("0x", "#") + ";");
            }
        });

        // Remove highlighting effect on mouse exit
        button.setOnMouseExited(event -> {
            Stone stone = board.getTile(x, y);
            if (stone != null) {
                Color stoneColor = (stone.getStoneColor() == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                button.setStyle("-fx-background-color: " + stoneColor.toString().replace("0x", "#") + ";");
            } else {
                button.setStyle("-fx-background-color: transparent;");
            }
        });
        return button;
    }

    private void handleButtonClick(int x, int y) throws IOException {
        if(client.isMyTurn())
            client.sendMove(x,y);
    }
    private void handleSurrenderButtonClick() throws IOException {
        if(client.isMyTurn())
            client.sendSurrender();
    }
    private void handlePassButtonClick() throws IOException {
        if(client.isMyTurn())
            client.sendPass();
    }
    private void handleLoadGameButtonClick() throws IOException, InterruptedException {
        loadGame = true;
        // Request the list of available games from the server
        client.askForAvailableGames();
        while(!client.isGameListUpToDate()){
            sleep(1);
        }
        // Display the list of available games in a dialog
        ListView<String> gameList = new ListView<>();
        gameList.getItems().addAll(client.getAvailableGames());

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Load Game");
        client.askForAvailableGames();
        alert.getDialogPane().setContent(gameList);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();

        // Check if the user selected a game and load it
        if (alert.getResult() == ButtonType.OK) {
            selectedGame = gameList.getSelectionModel().getSelectedItem();
            client.requestLoadedGameBoardSize(selectedGame);
            board = new Board(BOARD_SIZE);
            continueInitializingGUI();
            resetGame();
            updateStones();
            updateInfo();
            if (selectedGame != null) {
                client.loadMove(selectedGame,1);
                switchToNavigationMode();
            }
        }
    }

    private void switchToNavigationMode() {
        activatePassButton.unbind();
        activateFFButton.unbind();
        activatePassButton.set(false);
        activateFFButton.set(false);
        Button prevButton = new Button("←");
        Button nextButton = new Button("→");
        HBox navigationBox = new HBox(10, prevButton, nextButton);
        navigationBox.setAlignment(Pos.CENTER);
        navigationBox.setLayoutY((((double) (BOARD_SIZE /2 + 1)* TILE_SIZE)));
        navigationBox.setLayoutX(95);
        infoPane.getChildren().removeAll(surrenderButton, passButton);
        infoPane.getChildren().add(navigationBox);

        // Set actions for navigation buttons
        prevButton.setOnAction(e -> {
            try {
                handleNavigationButtonClick(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if(moveNumber<=0){
                prevButton.setVisible(false);
            }
        });
        nextButton.setOnAction(e -> {
            try {
                handleNavigationButtonClick(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if(moveNumber>=0){
                prevButton.setVisible(true);
            }
        });
    }

    private void handleNavigationButtonClick(boolean forward) throws IOException {
        if(forward){
            moveNumber++;
            client.loadMove(selectedGame,moveNumber);
        }else{
            client.deloadMove(selectedGame, moveNumber);
            moveNumber--;

        }
    }

    public void resetGame(){
        moveNumber = 1;
        blackPoints = 0;
        whitePoints = 0;
        resetBoard();
    }

    public void resetBoard() {
        for(int j = board.getBoardSize() - 1; j >= 0; j--) {
            for (int i = 0; i < board.getBoardSize(); i++) {
                board.setTile(i, j, null);
            }
        }
    }

    public void showWinnerDialog(StoneColor winner) {
        Platform.runLater(() -> {
            String message =  winner.toString() + " won the game! Black had " + blackPoints + " points and White had " + whitePoints + " points";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.initOwner(primaryStage);
            alert.setOnCloseRequest(event -> closeGUI());
            alert.setHeaderText("Game over");
            alert.showAndWait();
        });


    }
    private void showDrawDialog() {

        Platform.runLater(() -> {
            String message =  "Draw! Both players had " + blackPoints + " points";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.initOwner(primaryStage);
            alert.setOnCloseRequest(event -> closeGUI());
            alert.setHeaderText("Game over");
            alert.showAndWait();
        });

    }

    public void showSurrenderDialog() {
        Platform.runLater(() -> {
            StoneColor loser = currentPlayer;
            StoneColor winner;
            if(loser == StoneColor.WHITE)
                winner = StoneColor.BLACK;
            else
                winner = StoneColor.WHITE;

            String message =  loser.toString() + " has surrendered! " + winner.toString() + " has won";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.initOwner(primaryStage);
            alert.setOnCloseRequest(event -> closeGUI());
            alert.setHeaderText("Game over");
            alert.showAndWait();
        });


    }
    public void endGame(String message) {
        whitePoints = MessageDecoder.extractWhitePoints(message);
        blackPoints = MessageDecoder.extractBlackPoints(message);
        if(blackPoints > whitePoints) {
            showWinnerDialog(StoneColor.BLACK);
        } else if (whitePoints > blackPoints) {
            showWinnerDialog(StoneColor.WHITE);
        } else{
            showDrawDialog();
        }
    }


    private void drawGoBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BURLYWOOD);
        // Filling the background
        gc.fillRect(0, 0, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        // Creating the grid
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            for (int j = 0; j < BOARD_SIZE - 1; j++) {
                // + 20 to add margins
                double x = j * TILE_SIZE + 20;
                // + 20 to add margins
                double y = i * TILE_SIZE + 20;
                gc.strokeRect(x, y, TILE_SIZE, TILE_SIZE);

                if (i > 0) {
                    gc.strokeLine(x, y, x + TILE_SIZE, y);
                }

                if (j > 0) {
                    gc.strokeLine(x, y, x, y + TILE_SIZE);
                }
            }
        }
        //Decorative dots
        // 19x19
        gc.setFill(Color.BLACK);
        if (BOARD_SIZE == 19) {
            for (int i = 3; i < BOARD_SIZE - 1; i += 6) {
                for (int j = 3; j < BOARD_SIZE - 1; j += 6) {
                    double x = j * TILE_SIZE - DOT_SIZE / 2 + 20;
                    double y = i * TILE_SIZE - DOT_SIZE / 2 + 20;
                    gc.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
            }
        } else {
            // 9x9 and 13x13
            for (int i = 2; i < BOARD_SIZE - 1; i += 4) {
                for (int j = 2; j < BOARD_SIZE - 1; j += 4) {
                    double x = j * TILE_SIZE - DOT_SIZE / 2 + 20;
                    double y = i * TILE_SIZE - DOT_SIZE / 2 + 20;
                    gc.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
            }
        }

    }

    private void updateStones() {
        for (int y = BOARD_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                Stone stone = board.getTile(x, y);
                if (stone != null) {
                    Color stoneColor = (stone.getStoneColor() == StoneColor.WHITE) ? Color.WHITE : Color.BLACK;
                    buttons[x][y].setStyle("-fx-background-color: " + stoneColor.toString().replace("0x", "#") + ";");
                } else {
                    buttons[x][y].setStyle("-fx-background-color: transparent;");
                }
            }
        }
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == StoneColor.WHITE) ? StoneColor.BLACK : StoneColor.WHITE;
        toggleActivatePassButton();
        toggleFFPassButton();
    }

    public void showAlert(String message) {
        if(currentPlayer == client.getPlayerColor()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
                alert.initOwner(primaryStage);
                alert.setHeaderText("Illegal move");
                alert.showAndWait();
            });
        }
    }

    public void addStoneToBoard(Coordinates coordinates){
        board.setTile(coordinates.getX(), coordinates.getY(), new Stone(currentPlayer));
    }
    public void addStonesToBoard(List<Coordinates> listOfStones){
        switchPlayer();
        int points = listOfStones.size();
        for(Coordinates coordinates: listOfStones)
            board.setTile(coordinates.getX(), coordinates.getY(), new Stone(currentPlayer));
        whitePoints -= points;
        blackPoints -= points;

        switchPlayer();
    }

    public void removeStonesFromBoard(List<Coordinates> listOfStones){
        int points = listOfStones.size();
        for(Coordinates coordinates: listOfStones)
            board.setTile(coordinates.getX(), coordinates.getY(), null);

        if(currentPlayer == StoneColor.WHITE)
            whitePoints += points;
        else
            blackPoints += points;
    }

    public void removeStoneFromBoard(Coordinates coordinates){
        try {
            board.setTile(coordinates.getX(), coordinates.getY(), null);
        }catch (ArrayIndexOutOfBoundsException ignored){}

    }


    public void refreshGUIAfterSuccessfulMove(String message){
        Platform.runLater(() -> {
            Coordinates stoneToBeAdded = MessageDecoder.stonesToBeAddedFromStringToCoordinates(message);
            List<Coordinates> stonesToBeRemoved = MessageDecoder.stonesToBeRemovedFromStringToCoordinates(message);
            addStoneToBoard(stoneToBeAdded);
            removeStonesFromBoard(stonesToBeRemoved);
            updateStones();
            switchPlayer();
            updateInfo();
        });
    }
    public void refreshGUIAfterReverseArrow(String notmessage) {
        Platform.runLater(() -> {
            String message = MessageDecoder.removeFirstWord(notmessage);
            Coordinates stoneToBeRemoved= MessageDecoder.stonesToBeAddedFromStringToCoordinates(message);
            List<Coordinates> stonesToBeAdded = MessageDecoder.stonesToBeRemovedFromStringToCoordinates(message);
            addStonesToBoard(stonesToBeAdded);
            removeStoneFromBoard(stoneToBeRemoved);
            updateStones();
            updateInfo();
        });
    }

    public void refreshGUIAfterPass(){
        Platform.runLater(() -> {
            switchPlayer();
            updateInfo();
        });
    }

    private void closeGUI() {
        client.disconnect();
        Platform.runLater(() -> primaryStage.close());
    }

    public void setGUITitle(StoneColor stoneColor){
        Platform.runLater(() -> this.primaryStage.setTitle("PLAYER " + stoneColor.toString()));
    }


    public void setBoardSize(int boardSize) {
        BOARD_SIZE = boardSize;
    }

}

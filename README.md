# Compiling
Make sure you are in the `../GO` directory. Then run the command `mvn clean install`.

# Runing
First run the server with `java -cp target/classes:slf4j-api-1.7.36.jar:slf4j-simple-1.7.36.jar Server.Server`. Then run the client gui with
`java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp target/classes:slf4j-api-1.7.36.jar:slf4j-simple-1.7.36.jar Server.ClientMain`. You can connect up to 2 clients. Replace `/usr/share/openjfx/lib` with the path to your java fx library.

# Gamplay
First select if you want to play with another player, with a bot or to load previous game from data base. Then select board size between 9x9, 13x13 and 19x19. After that enjoy the game.
package game.checkers;

import javafx.scene.Scene;

import static game.ui.GlobalVariables.gameWindow;

public class Entry {

    //create window, add checkerboard, start game loop
    public Scene getWindowContent() {
        gameWindow = new GameWindow();
        Scene scene = new Scene(gameWindow.createGameWindows(), 650, 500);
        return scene;
    }
}

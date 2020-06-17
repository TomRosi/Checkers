package game.checkers;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static game.ui.GlobalVariables.*;

/**
 * This panel lets two users play game.game.game.checkers.checkers.game.checkers.checkers against each other.
 * Red always starts the game.  If a player can jump an opponent's
 * piece, then the player must jump.  When a player can make no more
 * moves, the game ends.
 */
public class GameWindow {
    /**
     * The constructor creates the Board (which in turn creates and manages
     * the buttons and message label), adds all the components, and sets
     * the bounds of the components.  A null layout is used.  (This is
     * the only thing that is done in the main game.game.game.checkers.checkers.game.checkers.checkers.Checkers class.)
     */
    public Parent createGameWindows() {
        BorderPane root = new BorderPane();

        /* Create the label that will show messages. */

        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(100, 255, 100)); // Light green.
        message.setFont(Font.font(null, FontWeight.BOLD, 18));

        /* Create the buttons and the board.  The buttons MUST be
         * created first, since they are used in the CheckerBoard
         * constructor! */


        checkersBoard = new CheckersBoard(); // a subclass of Canvas, defined below
        if(reconnected == false) {
            legalMoves = checkersData.getLegalMoves(currentPlayer);
        }
        checkersBoard.drawBoard();  // draws the content of the checkerboard

        /* Set up ActionEvent handlers for the buttons and a MousePressed handler
         * for the board.  The handlers call instance methods in the board object. */

        //TODO:
//        checkersBoard.doNewGame();
        checkersBoard.setOnMousePressed(e -> checkersBoard.mousePressed(e));

        /* Set the location of each child by calling its relocate() method */

        checkersBoard.relocate(20, 20);
        message.relocate(20, 370);

        /* Set the sizes of the buttons.  For this to have an effect, make
         * the butons "unmanaged."  If they are managed, the Pane will set
         * their sizes. */



        /* Create the Pane and give it a preferred size.  If the
         * preferred size were not set, the unmanaged buttons would
         * not be included in the Pane's computed preferred size. */

//        root.setPrefWidth(500);
//        root.setPrefHeight(420);

        /* Add the child nodes to the Pane and set up the rest of the GUI */

        root.getChildren().addAll(checkersBoard, message);
        root.setStyle("-fx-background-color: darkgreen; "
                + "-fx-border-color: darkred; -fx-border-width:3");

        return root;
    }
}

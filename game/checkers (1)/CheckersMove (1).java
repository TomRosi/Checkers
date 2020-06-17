package game.checkers;

import java.sql.SQLOutput;

import static game.checkers.CheckersData.*;
import static game.ui.GlobalVariables.*;

/**
 * A CheckersMove object represents a move in the game of Checkers.
 * It holds the row and column of the piece that is to be moved
 * and the row and column of the square to which it is to be moved.
 * (This class makes no guarantee that the move is legal.)
 */
public class CheckersMove {
    int fromRow, fromCol;  // Position of piece to be moved.
    int toRow, toCol;      // Square it is to move to.

    public CheckersMove(int r1, int c1, int r2, int c2) {
        // Constructor.  Just set the values of the instance variables.
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    public static boolean isMoveAllowed(CheckersMove move, int playerNumber) {
        int matrixFromNumber = matrix[move.fromRow][move.fromCol];
        int matrixToNumber = matrix[move.toRow][move.toCol];

        if (matrixFromNumber == RED_KING || matrixFromNumber == BLACK_KING) {
            //TODO
            System.out.println("Královna dodělat");
            if (matrixToNumber == EMPTY) {
                return true;
            }
            return false;
        }
        if(matrixFromNumber == playerNumber && matrixToNumber == EMPTY) {
            return true;
        } else {
            return false;
        }
    }

    boolean isJump() {
        // Test whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }
}



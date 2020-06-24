package com.example.checkers;

/**
 * Třída CheckersMove reprezentuje pohyb kamenu
 */

public class CheckersMove {
    int fromRow, fromCol;
    int toRow, toCol;

    /**
     * Konstruktor
     * @param r1 start row
     * @param c1 start column
     * @param r2 end row
     * @param c2 end column
     */
    public CheckersMove(int r1, int c1, int r2, int c2) {
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    /**
     * Ověří jestli je daný pohyb skokem
     * @return boolean
     */
    boolean isJump() {
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }

    public static boolean isMoveAllowed(CheckersMove move, int playerNumber) {
        int matrixFromNumber = CheckersData.matrix[move.fromRow][move.fromCol];
        int matrixToNumber = CheckersData.matrix[move.toRow][move.toCol];

        if (matrixFromNumber == CheckersData.RED_KING || matrixFromNumber == CheckersData.BLACK_KING) {
            if (matrixToNumber == CheckersData.EMPTY) {
                return true;
            }
            return false;
        }
        if(matrixFromNumber == playerNumber && matrixToNumber == CheckersData.EMPTY) {
            return true;
        } else {
            return false;
        }
    }
}
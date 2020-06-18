package com.example.checkers;

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

//    public static boolean isMoveAllowed(CheckersMove move, int playerNumber) {
//        int matrixFromNumber = matrix[move.fromRow][move.fromCol];
//        int matrixToNumber = matrix[move.toRow][move.toCol];
//
//        if (matrixFromNumber == RED_KING || matrixFromNumber == BLACK_KING) {
//            //TODO
//            System.out.println("Královna dodělat");
//            if (matrixToNumber == EMPTY) {
//                return true;
//            }
//            return false;
//        }
//        if(matrixFromNumber == playerNumber && matrixToNumber == EMPTY) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    boolean isJump() {
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }
}
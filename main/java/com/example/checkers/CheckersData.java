package com.example.checkers;

import java.util.ArrayList;

public class CheckersData {

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;

    public CheckersData() {
        setUpGame();
    }

    public static int matrix[][] = {{0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0},
                                    {0,0,0,0,0,0,0,0}};

    void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3)
                        matrix[row][col] = BLACK;
                    else if (row > 4)
                        matrix[row][col] = RED;
                    else
                        matrix[row][col] = EMPTY;
                }
                else {
                    matrix[row][col] = EMPTY;
                }
            }
        }
    }

    int pieceAt(int row, int col) {
        return matrix[row][col];
    }

//    public static void changePlayer() {
//        if(currentPlayer == RED) currentPlayer = BLACK;
//        else if(currentPlayer == BLACK) currentPlayer = RED;
//    }


    void makeMove(CheckersMove move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }


    public static void makeMove(int rowS, int colS, int rowE, int colE) {
        matrix[rowE][colE] = matrix[rowS][colS];
        matrix[rowS][colS] = EMPTY;
        if (rowS - rowE == 2 || rowS - rowE == -2) {
            // The move is a jump.  Remove the jumped piece from the board.
            int jumpRow = (rowS + rowE) / 2;  // Row of the jumped piece.
            int jumpCol = (colS + colE) / 2;  // Column of the jumped piece.

            matrix[jumpRow][jumpCol] = EMPTY;
        }
        if (rowE == 0 && matrix[rowE][colE] == RED)
            matrix[rowE][colE] = RED_KING;
        if (rowE == 7 && matrix[rowE][colE] == BLACK)
            matrix[rowE][colE] = BLACK_KING;
    }


    CheckersMove[] getLegalMoves(int player) {

        if (player != RED && player != BLACK)
            return null;

        int playerKing;
        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (matrix[row][col] == player || matrix[row][col] == playerKing) {
                    if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                        moves.add(new CheckersMove(row, col, row+2, col+2));
                    if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                        moves.add(new CheckersMove(row, col, row-2, col+2));
                    if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                        moves.add(new CheckersMove(row, col, row+2, col-2));
                    if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                        moves.add(new CheckersMove(row, col, row-2, col-2));
                }
            }
        }

        if (moves.size() == 0) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (matrix[row][col] == player || matrix[row][col] == playerKing) {
                        if (canMove(player,row,col,row+1,col+1))
                            moves.add(new CheckersMove(row,col,row+1,col+1));
                        if (canMove(player,row,col,row-1,col+1))
                            moves.add(new CheckersMove(row,col,row-1,col+1));
                        if (canMove(player,row,col,row+1,col-1))
                            moves.add(new CheckersMove(row,col,row+1,col-1));
                        if (canMove(player,row,col,row-1,col-1))
                            moves.add(new CheckersMove(row,col,row-1,col-1));
                    }
                }
            }
        }

        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }

    }


    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        if (player != RED && player != BLACK)
            return null;
        int playerKing;  // The constant representing a King belonging to player.
        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;
        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // The legal jumps will be stored in this list.
        if (matrix[row][col] == player || matrix[row][col] == playerKing) {
            if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                moves.add(new CheckersMove(row, col, row+2, col+2));
            if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                moves.add(new CheckersMove(row, col, row-2, col+2));
            if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                moves.add(new CheckersMove(row, col, row+2, col-2));
            if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                moves.add(new CheckersMove(row, col, row-2, col-2));
        }
        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }


    private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false;  // (r3,c3) is off the board.

        if (matrix[r3][c3] != EMPTY)
            return false;  // (r3,c3) already contains a piece.

        if (player == RED) {
            if (matrix[r1][c1] == RED && r3 > r1)
                return false;  // Regular red piece can only move up.
            if (matrix[r2][c2] != BLACK && matrix[r2][c2] != BLACK_KING)
                return false;  // There is no black piece to jump.
            return true;  // The jump is legal.
        }
        else {
            if (matrix[r1][c1] == BLACK && r3 < r1)
                return false;  // Regular black piece can only move downn.
            if (matrix[r2][c2] != RED && matrix[r2][c2] != RED_KING)
                return false;  // There is no red piece to jump.
            return true;  // The jump is legal.
        }

    }

    private boolean canMove(int player, int r1, int c1, int r2, int c2) {

        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false;  // (r2,c2) is off the board.

        if (matrix[r2][c2] != EMPTY)
            return false;  // (r2,c2) already contains a piece.

        if (player == RED) {
            if (matrix[r1][c1] == RED && r2 > r1)
                return false;  // Regular red piece can only move down.
            return true;  // The move is legal.
        } else {
            if (matrix[r1][c1] == BLACK && r2 < r1)
                return false;  // Regular black piece can only move up.
            return true;  // The move is legal.
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

}





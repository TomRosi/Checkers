package com.example.checkers;

import java.util.ArrayList;

/**
 * Třída CheckersData reprezentuje dámu po datové stránce
 */
public class CheckersData {

    // jednotlivé druhy kamenů reprezované jako čísla
    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;

    // matice reprezentující board
    public static int matrix[][] =
                    {{0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0}};

    /**
     * Konstruktor
     */
    public CheckersData() {
        setUpGame();
    }

    /**
     * Metoda pro pohyb reprezentovaný jako třídou CheckersMove
     * @param move pohyb reprezující třídou
     */
    void makeMove(CheckersMove move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }


    /**
     * Metoda pro pohyb kamene - upravuje hodnoty v matici
     * @param rowS from (start) row
     * @param colS from (start) column
     * @param rowE to (end) row
     * @param colE to (end) column
     */
    public static void makeMove(int rowS, int colS, int rowE, int colE) {
        matrix[rowE][colE] = matrix[rowS][colS];
        matrix[rowS][colS] = EMPTY;

        if (rowS - rowE == 2 || rowS - rowE == -2) {
            int jumpRow = (rowS + rowE) / 2;
            int jumpCol = (colS + colE) / 2;

            matrix[jumpRow][jumpCol] = EMPTY;
        }
        if (rowE == 0 && matrix[rowE][colE] == RED)
            matrix[rowE][colE] = RED_KING;
        if (rowE == 7 && matrix[rowE][colE] == BLACK)
            matrix[rowE][colE] = BLACK_KING;
    }


    /**
     * Metoda vrátí povolené místa, kam se daný kámen může pohnout
     * @param player hráč
     * @return pole povolených pohybů (míst)
     */
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


    /**
     * Metoda vrátí povolené skoky
     * @param player hráč
     * @param row from row
     * @param col from column
     * @return pole možných míst
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {

        if (player != RED && player != BLACK)
            return null;

        int playerKing;

        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

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

    /**
     * Může daný kamen skočit?
     * @param player hráč
     * @param r1 from row
     * @param c1 from column
     * @param r2 through row
     * @param c2 through column
     * @param r3 to row
     * @param c3 to column
     * @return boolean
     */
    private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false;

        if (matrix[r3][c3] != EMPTY)
            return false;

        if (player == RED) {
            if (matrix[r1][c1] == RED && r3 > r1)
                return false;
            if (matrix[r2][c2] != BLACK && matrix[r2][c2] != BLACK_KING)
                return false;
            return true;
        }
        else {
            if (matrix[r1][c1] == BLACK && r3 < r1)
                return false;
            if (matrix[r2][c2] != RED && matrix[r2][c2] != RED_KING)
                return false;
            return true;
        }

    }

    /**
     * Může se daný kámen pohnout?
     * @param player hráč
     * @param r1 from row
     * @param c1 from column
     * @param r2 to row
     * @param c2 to column
     * @return boolean
     */
    private boolean canMove(int player, int r1, int c1, int r2, int c2) {

        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false;

        if (matrix[r2][c2] != EMPTY)
            return false;

        if (player == RED) {
            if (matrix[r1][c1] == RED && r2 > r1)
                return false;
            return true;
        } else {
            if (matrix[r1][c1] == BLACK && r2 < r1)
                return false;
            return true;
        }
    }

    /**
     * Metoda nastaví matici do základní podoby - reprezentuje postavení kamenu na boardu
     *  	      0  1  2  3  4  5  6  7
     *
     *     	 0    3  0  3  0  3  0  3  0
     *     	 1    0  3  0  3  0  3  0  3
     *     	 2    3  0  3  0  3  0  3  0
     *     	 3    0  0  0  0  0  0  0  0
     *     	 4    0  0  0  0  0  0  0  0
     *     	 5    0  1  0  1  0  1  0  1
     *     	 6    1  0  1  0  1  0  1  0
     *     	 7    0  1  0  1  0  1  0  1
     */
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

    /**
     * metoda pro vytisknutí matice
     * @param matrix
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Metoda pro zjištění hodnoty v matici
     * @param row
     * @param col
     * @return int
     */
    int pieceAt(int row, int col) {
        return matrix[row][col];
    }

    /**
     * změna hráče
     */
    public static void changePlayer() {
//        if(currentPlayer == RED) currentPlayer = BLACK;
//        else if(currentPlayer == BLACK) currentPlayer = RED;
    }
}





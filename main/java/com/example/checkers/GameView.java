package com.example.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

public class GameView extends View {
    Paint paint = new Paint();
    Canvas canvas = new Canvas();
    CheckersData board;
    CheckersMove[] legalMoves;
    int currentPlayer;
    int selectedRow, selectedCol;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context) {
        super(context);
        board = new CheckersData();
        doNewGame();
    }

//    GameView() {
//        super(324,324);  // canvas is 324-by-324 pixels
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void doNewGame() {
        board.setUpGame();   // Set up the pieces.
        currentPlayer = CheckersData.RED;   // RED moves first.
        legalMoves = board.getLegalMoves(CheckersData.RED);  // Get RED's legal moves.
        selectedRow = -1;   // RED has not yet selected a piece to move.

//        onDraw(canvas);
        draw(canvas);
    }


    void doClickSquare(int row, int col) {
        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == CheckersData.RED) {
//                    message.setText("RED:  Make your move.");
                } else {
//                    message.setText("BLACK:  Make your move.");
                }
                draw(canvas);
//                drawBoard();
                return;
            }

        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                    && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                doMakeMove(legalMoves[i]);
                return;
            }
    }


    void doMakeMove(CheckersMove move) {
        board.makeMove(move);
        if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer,move.toRow,move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == CheckersData.RED) {
//                    message.setText("RED:  You must continue jumping.");
                } else {
//                    message.setText("BLACK:  You must continue jumping.");
                }
                selectedRow = move.toRow;  // Since only one piece can be moved, select it.
                selectedCol = move.toCol;
                draw(canvas);
                return;
            }
        }

        if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
//            if (legalMoves == null)
//                gameOver("BLACK has no moves.  RED wins.");
//            else if (legalMoves[0].isJump())
//                message.setText("BLACK:  Make your move.  You must jump.");
//            else
//                message.setText("BLACK:  Make your move.");
        }
        else {
            currentPlayer = CheckersData.RED;
            legalMoves = board.getLegalMoves(currentPlayer);
//            if (legalMoves == null)
//                gameOver("RED has no moves.  BLACK wins.");
//            else if (legalMoves[0].isJump())
//                message.setText("RED:  Make your move.  You must jump.");
//            else
//                message.setText("RED:  Make your move.");
        }

        selectedRow = -1;

        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }

        draw(canvas);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawRect(1, 1, 322, 322, paint);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ( row % 2 == col % 2 )
                    paint.setColor(Color.BLACK);
                else
                    paint.setColor(Color.GRAY);
                canvas.drawRect(2 + col*40, 2 + row*40, 40, 40, paint);
                switch (board.pieceAt(row, col)) {
                    case CheckersData.RED:
                        paint.setColor(Color.RED);
                        canvas.drawOval(8 + col*40, 8 + row*40, 28, 28, paint);
                        break;
                    case CheckersData.BLACK:
                        paint.setColor(Color.BLACK);
                        canvas.drawOval(8 + col*40, 8 + row*40, 28, 28, paint);
                        break;
//                    case CheckersData.RED_KING:
//                        paint.setColor(Color.RED);
//                        canvas.fillOval(8 + col*40, 8 + row*40, 28, 28, paint);
//                        paint.setColor(Color.WHITE);
//                        paint.fillText("K", 15 + col*40, 29 + row*40, paint);
//                        break;
//                    case CheckersData.BLACK_KING:
//                        paint.setColor(Color.BLACK);
//                        canvas.fillOval(8 + col*40, 8 + row*40, 28, 28, paint);
//                        paint.setColor(Color.WHITE);
//                        canvas.fillText("K", 15 + col*40, 29 + row*40, paint);
//                        break;
//                }
            }
        }

            /* If a game is in progress, highlight the legal moves.   Note that legalMoves
             is never null while a game is in progress. */

//        if (gameInProgress) {
//            /* First, draw a 4-pixel cyan border around the pieces that can be moved. */
//            paint.setStroke(Color.CYAN);
//            paint.setLineWidth(4);
////            for (int i = 0; i < legalMoves.length; i++) {
////                g.strokeRect(4 + legalMoves[i].fromRow*40, 4 + legalMoves[i].fromRow *40, 36, 36);
////            }
//                /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
//                    draw a yellow border around that piece and draw green borders
//                    around each square that that piece can be moved to. */
//            if (selectedRow >= 0) {
//                paint.setStroke(Color.YELLOW);
//                paint.setLineWidth(4);
//                paint.strokeRect(4 + selectedCol*40, 4 + selectedRow*40, 36, 36);
//                paint.setStroke(Color.LIME);
//                paint.setLineWidth(4);
//                for (int i = 0; i < legalMoves.length; i++) {
//                    if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
//                        paint.strokeRect(4 + legalMoves[i].toCol*40, 4 + legalMoves[i].toRow*40, 36, 36);
//                    }
//                }
//            }
        }
    }
}

package com.example.checkers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Třída GameActivity startuje hru
 */
public class GameActivity extends AppCompatActivity {

    private int[] buttons_id;
    private Button[][] buttonBoard;
    private static int duration = Toast.LENGTH_SHORT;
    CheckersData board;
    CheckersMove[] legalMoves;
    int currentPlayer;
    int selectedRow, selectedCol;

    /**
     * Metoda vytvoří novou hru a zjistí jestli se hraje v portrait nebo landscape modu
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setUpBoardForPortrait();
        }

        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setUpBoardForLandscape();
        }
        doNewGame();
    }

    /**
     * Metoda pro vytvoření nové hry, vytvoří matici, matici buttonu, naplní hrací plochu,
     * nastaví prvního hráče, tvoří pole povolených pohybů
     */
    void doNewGame() {
        board = new CheckersData();
        buttons_id = getButtonArray();
        buttonBoard = new Button[8][8];
        fillBoard(listener);
        updateBoard(buttonBoard);

        board.setUpGame();
        currentPlayer = CheckersData.RED;
        legalMoves = board.getLegalMoves(CheckersData.RED);
        selectedRow = -1;
    }


    /**
     * Metoda pro zjištění a zajištění informací o kliknutí
     * index kliknutí buttonu uloží jako selected a poté zjistí jestli kamen může provézt move
     * vykreslí možné pohyby daného kamenu a zavolá metodu doMakeMove, která vykoná pohyb
     * @param row řádek vybraného buttonu (kamenu)
     * @param col slopec vybraného buttonu (kamenu)
     */
    void doClickButton(int row, int col) {
        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == CheckersData.RED) {
                    Toast.makeText(this, "RED: Make your move.", duration).show();
                } else {
                    Toast.makeText(this, "BLACK: Make your move.", duration).show();
                }

                if(CheckersData.matrix[selectedRow][selectedCol] == CheckersData.RED) {
                    updateBoard(buttonBoard);
                    buttonBoard[selectedRow][selectedCol].setBackgroundResource(R.drawable.light_piece_pressed);
                    for (int j = 0; j < legalMoves.length; j++) {
                        if (legalMoves[j].fromCol == selectedCol && legalMoves[j].fromRow == selectedRow) {
                            buttonBoard[legalMoves[j].toRow][legalMoves[j].toCol].setBackgroundResource(R.drawable.light_piece_highlighted);
                        }
                    }
                }
                if(CheckersData.matrix[selectedRow][selectedCol] == CheckersData.RED_KING) {
                    updateBoard(buttonBoard);
                    buttonBoard[selectedRow][selectedCol].setBackgroundResource(R.drawable.light_king_piece_pressed);
                    for (int j = 0; j < legalMoves.length; j++) {
                        if (legalMoves[j].fromCol == selectedCol && legalMoves[j].fromRow == selectedRow) {
                            buttonBoard[legalMoves[j].toRow][legalMoves[j].toCol].setBackgroundResource(R.drawable.light_king_highlighted);
                        }
                    }
                }
                if(CheckersData.matrix[selectedRow][selectedCol] == CheckersData.BLACK) {
                    updateBoard(buttonBoard);
                    buttonBoard[selectedRow][selectedCol].setBackgroundResource(R.drawable.dark_piece_pressed);
                    for (int j = 0; j < legalMoves.length; j++) {
                        if (legalMoves[j].fromCol == selectedCol && legalMoves[j].fromRow == selectedRow) {
                            buttonBoard[legalMoves[j].toRow][legalMoves[j].toCol].setBackgroundResource(R.drawable.dark_piece_highlighted);
                        }
                    }
                }
                if(CheckersData.matrix[selectedRow][selectedCol] == CheckersData.BLACK_KING) {
                    updateBoard(buttonBoard);
                    buttonBoard[selectedRow][selectedCol].setBackgroundResource(R.drawable.dark_king_piece_pressed);
                    for (int j = 0; j < legalMoves.length; j++) {
                        if (legalMoves[j].fromCol == selectedCol && legalMoves[j].fromRow == selectedRow) {
                            buttonBoard[legalMoves[j].toRow][legalMoves[j].toCol].setBackgroundResource(R.drawable.dark_king_highlighted);
                        }
                    }
                }
                return;
            }

        if (selectedRow < 0) {
            Toast.makeText(this, "Click the piece you want to move.", duration).show();
            return;
        }


        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                doMakeMove(legalMoves[i]);
                return;
            }
    }

    /**
     * Metoda zajistí pohyb kamenu na cílovou pozici,
     * dá vědět hráči jestli se jedná o jump nebo obyčejný pohyb,
     * nastaví nového current hráče,
     * zároveň zjistí, jestli už hra neskočila a vypíše dialog
     * @param move pohyb kamenu (starRow, starCol, endRow, endCol)
     */
    void doMakeMove(CheckersMove move) {
        board.makeMove(move);

        if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == CheckersData.RED) {
                    Toast.makeText(this, "RED:  You must continue jumping.", duration).show();
                } else {
                    Toast.makeText(this, "BLACK:  You must continue jumping.", duration).show();
                }
                selectedRow = move.toRow;
                selectedCol = move.toCol;
                updateBoard(buttonBoard);
                return;
            }
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                doNewGame();
            }
        });

        dialog.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null) {
                dialog.setMessage("BLACK has no moves. RED wins.");
                dialog.show();
            }
            else if (legalMoves[0].isJump())
                Toast.makeText(this, "BLACK: Make your move. You must jump.", duration).show();
            else
                Toast.makeText(this, "BLACK: Make your move.", duration).show();
        } else {
            currentPlayer = CheckersData.RED;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null) {
                dialog.setMessage("RED has no moves. BLACK wins.");
                dialog.show();
            }
            else if (legalMoves[0].isJump())
                Toast.makeText(this, "RED: Make your move. You must jump.", duration).show();
            else
                Toast.makeText(this, "RED: Make your move.", duration).show();
        }

        selectedRow = -1;

        if (legalMoves != null) {
            boolean sameStartSquare = true;

            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }
        updateBoard(buttonBoard);
    }

    /**
     * Naplnění matice buttonů, na černých pozicích boardu přidá button_id
     *  	      0   1  2   3  4   5  6   7
     *
     *     	 0    0   _  2   _  4   _  6   _
     *     	 1    _   9  _  11  _  13  _  15
     *     	 2    16  _  18  _  20  _  22 _
     *     	 3    _  25  _  27  _  29  _  31
     *     	 4    32  _  34  _  36  _  38  _
     *     	 5    _  41  _  43  _  45  _  47
     *     	 6    48  _  50  _  52  _  54
     *     	 7    _  57  _  59  _  61  _  63
     * @param listener
     */
    public void fillBoard(View.OnClickListener listener) {
        int index = 0;
        for (int i = 0; i < CheckersData.matrix.length; i++) {
            for (int j = 0; j < CheckersData.matrix.length; j++) {
                if ((i + j) % 2 == 0) {
                    buttonBoard[i][j] = (Button) findViewById(buttons_id[index]);
                    index++;
                    buttonBoard[i][j].setTag(i * 10 + j);
                    buttonBoard[i][j].setOnClickListener(listener);
                }
            }
        }
    }

    /**
     * Metoda pro update hrací plochy podle matice
     * prochází matici buttonů a matici hrací plochy
     * @param button
     */
    public void updateBoard(Button[][] button) {
        for (int i = 0; i < CheckersData.matrix.length; i++) {
            for (int j = 0; j < CheckersData.matrix.length; j++) {
                if ((i + j) % 2 == 0) {
                    if (CheckersData.matrix[i][j] == CheckersData.EMPTY) {
                        button[i][j].setBackgroundResource(R.drawable.blank_square);
                    }
                    else if (CheckersData.matrix[i][j] == CheckersData.RED) {
                        button[i][j].setBackgroundResource(R.drawable.light_piece);
                    }
                    else if (CheckersData.matrix[i][j] == CheckersData.RED_KING) {
                        button[i][j].setBackgroundResource(R.drawable.light_king_piece);
                    }
                    else if (CheckersData.matrix[i][j] == CheckersData.BLACK) {
                        button[i][j].setBackgroundResource(R.drawable.dark_piece);
                    }
                    else if (CheckersData.matrix[i][j] == CheckersData.BLACK_KING) {
                        button[i][j].setBackgroundResource(R.drawable.dark_king_piece);
                    }
                }
            }
        }
    }

    /**
     * Listener pro klikání + ověření řádků a sloupců
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (Integer) v.getTag();
            int row = tag / 10; //řádek 0-7
            int col = tag % 10; //sloupec 0-7

            if (col >= 0 && col < 8 && row >= 0 && row < 8) {
                doClickButton(row, col);
            }
        }
    };

    /**
     * pole button Id pro černé čtverečky
     * @return pole button_id
     */
    public int[] getButtonArray(){
        int[] buttons_id = {
                R.id.button0, R.id.button2, R.id.button4, R.id.button6,
                R.id.button9, R.id.button11, R.id.button13, R.id.button15,
                R.id.button16, R.id.button18, R.id.button20, R.id.button22,
                R.id.button25, R.id.button27, R.id.button29, R.id.button31,
                R.id.button32, R.id.button34, R.id.button36, R.id.button38,
                R.id.button41, R.id.button43, R.id.button45, R.id.button47,
                R.id.button48, R.id.button50, R.id.button52, R.id.button54,
                R.id.button57, R.id.button59, R.id.button61, R.id.button63};
        return buttons_id;
    }

    /**
     * Upraví a nastaví board pro Portrait mode
     * přepočítání šířky a výšky
     */
    public void setUpBoardForPortrait() {
        WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        double width = metrics.widthPixels;

        ImageView boardImageView = (ImageView) findViewById(R.id.boardImageView);
        ViewGroup.LayoutParams imageParams = boardImageView.getLayoutParams();
        imageParams.width =  (int) (width * 1.0028);
        imageParams.height = (int) (width * 1.0085);
        boardImageView.setLayoutParams(imageParams);

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
        ViewGroup.LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();
        buttonLayoutParams.width =  (int) (width * 0.967);
        buttonLayoutParams.height = (int) (width * 0.9723);
        buttonLayout.setLayoutParams(buttonLayoutParams);
    }

    /**
     * Upraví a nastaví board pro landscape mode
     * přepočítání šířky a výšky
     */
    public void setUpBoardForLandscape() {
        final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        double height = metrics.heightPixels - (actionBarHeight * 1.75);

        ImageView imageView = (ImageView) findViewById(R.id.boardImageView);
        LayoutParams imageParams = imageView.getLayoutParams();
        imageParams.width =  (int) (height * 1.0028);
        imageParams.height = (int) (height * 1.0085);
        imageView.setLayoutParams(imageParams);

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
        LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();
        buttonLayoutParams.width =  (int) (height * 0.967);
        buttonLayoutParams.height = (int) (height * 0.9723);
        buttonLayout.setLayoutParams(buttonLayoutParams);
    }
}
package game.ui;

import game.checkers.CheckersBoard;
import game.checkers.CheckersData;
import game.checkers.CheckersMove;
import game.checkers.GameWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class GlobalVariables {
    // the game by resigning.

    public static Label message;  // Label for displaying messages to the user.

    public static boolean playerRepeat = false;

    public static boolean enabledTurn;

    public static long waitingTime = 0;

    public static ReceivingThread receivingThread = null;

    public static boolean connected = false;

    public static String playerName = null;

    public static String ipAddress = null;

    public static String port = null;

    public static ObservableList<String> rooms;

    public static long createdMillis;

    public static String reconnectMessage = null;

    public static String gameName = null;

    public static String roomName = null;

    public static int colS, rowS, colE, rowE;

    public static String getOpponentName() {
        return opponentName;
    }

    public static void setOpponentName(String opponentName) {
        GlobalVariables.opponentName = opponentName;
    }

    public static String opponentName = null;

    public static Alert getAlert() {
        return alert;
    }

    public static void setAlert(Alert alert) {
        GlobalVariables.alert = alert;
    }

    public static Alert alert = null;

    public static CheckersBoard checkersBoard;

    public static GameWindow gameWindow;

    public static CheckersData checkersData;

    public static CheckersMove[] legalMoves;

    public static int currentPlayer;

    public static int selectedRow;

    public static int selectedCol;

    public static boolean gameInProgress;

    public static boolean reconnected = false;

//    public static GridPane container;

    public static PingThread pingThread = null;

    public static String color = null;

//    public static int matrix[][];

//    public static int matrix[][] = {{3,0,3,0,3,0,3,0},
//                                    {0,3,0,3,0,3,0,3},
//                                    {3,0,3,0,3,0,3,0},
//                                    {0,0,0,0,0,0,0,0},
//                                    {0,0,0,0,0,0,0,0},
//                                    {0,1,0,1,0,1,0,1},
//                                    {1,0,1,0,1,0,1,0},
//                                    {0,1,0,1,0,1,0,1}};

    public static int matrix[][] = {{0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}};

    public static String messagePattern(int messageSize) {
        String pattern;
        if (messageSize < 10){
            pattern = "00" + messageSize;
        } else if(messageSize < 100){
            pattern = "0" + messageSize;
        } else {
            pattern = "" + messageSize;
        }
        return pattern;
    }

    public static String getGameName() {
        return gameName;
    }

    public static void setGameName(String gameName) {
        GlobalVariables.gameName = gameName;
    }

    public static ReceivingThread getReceivingThread() {
        return receivingThread;
    }

    public static void setReceivingThread(ReceivingThread receivingThread) {
        GlobalVariables.receivingThread = receivingThread;
    }
}
package game.ui;

import game.checkers.CheckersData;
import game.main.Connection;
import game.checkers.CheckersMove;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLOutput;

import static game.checkers.CheckersData.*;
import static game.ui.Controller.*;
import static game.ui.Controller.primaryStage;
import static game.ui.GlobalVariables.*;

public class ReceivingThread extends Thread {

    public static Connection connection;
    public static Alert a;
    private final Socket socket;
    private boolean isRunning = false;

    public ReceivingThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        isRunning = true;
        int wrongMessage = 0;
        try {
            //char[] inputArray = new char[5000];
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (isRunning) {
                String message = bufferedReader.readLine();

                int sec = getAgeInSeconds();

                //TODO: SOCKET CUT
//                System.out.println("wrong message " +wrongMessage);
                if(sec > 15) System.out.println("Sekundy od pingu " +sec);

                if (message == null || sec > 20) {
                    System.out.println("Lost connection to server!");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Lost connection to server!");
                        a.showAndWait();
                    });
                    Thread.sleep(2000);
                    GlobalVariables.connected = false;
                    socket.close();
                    isRunning = false;
//                    Platform.runLater(() -> Controller.showLoginWindow());
                    disconnectClient();
                    break;

                }
//                System.out.println("From server: " + message);
                //String tmp = new String(inputArray);
                String[] command = message.split(";");
                if (command.length > 0 && !command[0].equals("ping")){
                    System.out.println("From server: " + message);
                }

                //response for server ping
                if (command[0].equals("ping")){
                    createdMillis = System.currentTimeMillis();
                    wrongMessage = 0;
                    Connection.sendMessage(MsgType.PING_TYPE.type_string);

                } else if (command[0].equals("rooms")){ //parse rooms response
                    if (command.length > 1) {
                        Platform.runLater(() -> {
                            System.out.println("roomky "+command[1]);
                            rooms.clear();
                            for (int i = 1; i < command.length; i++) {
                                GlobalVariables.rooms.add(command[i]);
                            }
                        });
//                        Platform.runLater(() ->controllerRoomPickerScene.fillList());
                    } else {
                        Platform.runLater(() -> {
                            Alert a = Dialogs.dialogError("Vytvořte roomku");
                            a.showAndWait();
                        });
                    }
                } else if (command[0].equals("logErr")){ //if waiting for opponent placing
                    Platform.runLater(() -> {
                        System.out.println("Login error");
                        Alert alert = Dialogs.dialogError("Login error");
                        alert.showAndWait();
                    });
                } else if (command[0].equals("joinERR")){ //if waiting for opponent placing
                    Platform.runLater(() -> {
                        System.out.println("Join error");
                        Alert alert = Dialogs.dialogError("Join error");
                        alert.showAndWait();
//                        Controller.showRoomWindow();
                    });
                } else if (command[0].equals("roomERR")){ //if waiting for opponent placing
                    Platform.runLater(() -> {
                        System.out.println("Room error");
                        Alert alert = Dialogs.dialogError("Room error");
                        alert.showAndWait();
                    });
                } else if (command[0].equals("roomCreat")) {
                    Platform.runLater(() -> {
                        System.out.println("Waiting for opponent");
                        a = new Alert(Alert.AlertType.NONE);
                        a.setTitle("Waiting for opponent");
                        a.setHeaderText("Please wait... ");
                        ProgressIndicator progressIndicator = new ProgressIndicator();
                        a.setGraphic(progressIndicator);
                        a.showAndWait();
                        //TODO:
//                        a.setOnCloseRequest();
                    });
                } else if (command[0].equals("joined")){ //zobrazí se druhému hráči, který se do roomky připojil
                    System.out.println("Druhý hráč se připojil. Máš černou barvu.");
                    gameName = command[1];
                    enabledTurn = false;
                    Platform.runLater(() -> {
                        playCheckers();
                        System.out.println("Čekej než dohraje červený hráč!");
                        Alert alert = Dialogs.infoDialog("Máš černou barvu, čekej než dohraje červený hráč!");
                        alert.showAndWait();
                    });
                } else if (command[0].equals("gameStarted")){ //zobrazí se prvnímu hráči, který roomku založil
//                    //game.checkers.ui2.GlobalVariables.setOpponentName(command[1]);
                    System.out.println("Hra začala. Jsi červený.");
                    gameName = command[1];
                    enabledTurn = true;

                    Platform.runLater(() ->  {
                        playCheckers();
                        a.getButtonTypes().add(ButtonType.CLOSE);
                        a.close();
//                        Alert alert = Dialogs.infoDialog("Jsi na řadě! Máš červenou barvu.");
//                        alert.showAndWait();
                    });
                } else if (command[0].equals("setGame")){ //start game
                    System.out.println("Set game");
                    currentPlayer = 1;
                } else if (command[0].equals("my")) {
                    System.out.println("Pohnul jsem se na "+ command[4] + " " + command[5]);
                    reconnected = false;

                } else if (command[0].equals("enemy")){ //response for enemy shooting
                    //enemy;move;cS;rS;cE;rE
//                    System.out.println(turn);
                    System.out.println("Protihráč se pohnul na "+ command[4] + " " + command[5]);

                    GlobalVariables.colS = Integer.parseInt(command[2]);
                    GlobalVariables.rowS = Integer.parseInt(command[3]);
                    GlobalVariables.colE = Integer.parseInt(command[4]);
                    GlobalVariables.rowE = Integer.parseInt(command[5]);

                    CheckersMove move = new CheckersMove(rowS, colS, rowE, colE);

                    Platform.runLater(() ->  {
                        boolean canHe = checkersData.canMove(currentPlayer, rowS, colS, rowE, colE);
                        if(canHe) {
                            checkersBoard.doMakeMove(move);

                            checkersBoard.whoIsNext();
//                            legalMoves = checkersData.getLegalMoves(currentPlayer);
//                            checkersData.getLegalJumpsFrom(currentPlayer, rowS, colS);
//                            checkersBoard.drawBoard();
//                            checkersBoard.doClickSquare(rowE, colE);
                        }
                    });
                } else if (command[0].equals("my")) {
                    System.out.println("Pohnul jsem se na "+ command[4] + " " + command[5]);

                } else if (command[0].equals("reconnect")){
                    System.out.println("Připojil ses zpět do hry");
                //reconnect;roomka;barva nebo číslo připojícíhu se hráče;64čísel;kdo je na tahu
                    gameName = command[1];
                    String color = command[2];
                    reconnected = true;
                    gameInProgress = true;
                    String numbers = command[3];

                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            matrix[i][j] = numbers.charAt(i*8 + j) - 48;
                        }
                    }

                    currentPlayer = Integer.parseInt(command[4]);
//                    System.out.println(color);
//                    System.out.println(currentPlayer);

                    if(color.equals("BLACK") && currentPlayer == 1) {
                        enabledTurn = false;
                        System.out.println("Máš černou, nejsi na řadě");
                        reconnectMessage = "Úspěšně ses připojil do hry. Máš černou, nejsi na řadě.";
                    }
                    if(color.equals("BLACK") && currentPlayer == 3) {
                        enabledTurn = true;
                        System.out.println("Máš černou, jsi na řadě");
                        reconnectMessage = "Úspěšně ses připojil do hry. Máš černou, jsi na řadě.";
                    }
                    if(color.equals("RED") && currentPlayer == 1) {
                        enabledTurn = true;
                        System.out.println("Máš červenou, jsi na řadě");
                        reconnectMessage =  "Úspěšně ses připojil do hry. Máš červenou, jsi na řadě.";
                    }
                    if(color.equals("RED") && currentPlayer == 3) {
                        enabledTurn = false;
                        System.out.println("Máš červenou, nejsi na řadě");
                        reconnectMessage = "Úspěšně ses připojil do hry. Máš červenou, nejsi na řadě.";
                    }
//                    System.out.println(gameName);
//                    System.out.println("matrix");
//                    System.out.println(enabledTurn);

//                    printMatrix(matrix);
//                    System.out.println(currentPlayer);


                    Platform.runLater(() -> {
                        checkersData = new CheckersData();

                        legalMoves = checkersData.getLegalMoves(currentPlayer);
                        Controller.playCheckers();
                        Alert a = Dialogs.infoDialog(reconnectMessage);
                        a.showAndWait();
                    });

                } else if (command[0].equals("activeGame")){
//                    game.checkers.ui2.GlobalVariables.opponentName = command[1];
//                    game.checkers.ui2.GlobalVariables.game = new GameController();
//                    game.checkers.ui2.GlobalVariables.game.initializeMyPane(command[2]);
//                    game.checkers.ui2.GlobalVariables.game.initializeEnemyPane(command[3]);
//                    if (command[4].equals("1")){
//                        Platform.runLater(() -> controllerGameScene.statusText.setText("Your turn!"));
//                        Platform.runLater(() -> controllerGameScene.setEnemyGridPaneEnable());
//                    } else if (command[4].equals("0")){
//                        Platform.runLater(() -> controllerGameScene.statusText.setText("Enemy turn!"));
//                        Platform.runLater(() -> controllerGameScene.setEnemyGridPaneDisable());
//                    }
//                    Platform.runLater(() -> game.checkers.ui2.GlobalVariables.sceneChanger.changeToGameScene());
//                    //send game state (activeGame;roomname;myplacing;enemyplacing;turn\n)
//
                } else if (command[0].equals("logged")){
                    System.out.println("Logged");
                    Platform.runLater(() -> Controller.showRoomWindow());

                } else if (command[0].equals("win")){
                    System.out.println("You won");
                    Platform.runLater(() -> {
                        Alert alert = Dialogs.infoDialog("You WON");
                        alert.showAndWait();
                        gameInProgress = false;
                        if(alert.getResult() == ButtonType.OK) {
                            Connection.sendMessage(MsgType.LEAVE_ROOM_TYPE.type_string +";"+ gameName);
//                            primaryStage.close();
//                            primaryStage = new Stage();
                            Controller.showRoomWindow();
                        }
                    });
                } else if (command[0].equals("lose")){
                    System.out.println("You lost");
                    Platform.runLater(() -> {
                        Alert alert = Dialogs.infoDialog("You LOST");
                        alert.showAndWait();
                        gameInProgress = false;
                        if(alert.getResult() == ButtonType.OK) {
                            Connection.sendMessage(MsgType.LEAVE_ROOM_TYPE.type_string +";"+ gameName);
                            primaryStage.close();
//                            primaryStage = new Stage();
                            Controller.showRoomWindow();
                        }
                    });
                } else if (command[0].equals("nickTaken")){
                    System.out.println("nick taken");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Name already exist");
                        a.showAndWait();
                    });
//                    disconnectClient();
                } else if (command[0].equals("wrongMove")){
                    System.out.println("Hráč " +command[1] +" špatně pohlul.");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Hráč " + command[1] + " špatně pohlul.");
                        a.showAndWait();
                    });
                } else if (command[0].equals("wrong move")){
                    System.out.println("Hráč špatně pohlul.");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Hráč špatně pohlul.");
                        a.showAndWait();
                    });
                } else if (command[0].equals("logout")){
                    System.out.println("Druhý hráč zavřel hru. Konec hry. Ukončete hru.");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Druhý hráč zavřel hru. Konec hry.");
                        a.showAndWait();
                        disconnectClient();
                    });
                } else if (command[0].equals("disconnected")) {
                    System.out.println("Druhý hráč se odpojil.");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Druhý hráč se odpojil.");
                        a.showAndWait();
                    });
                } else if (command[0].equals("playerRec")) {
                    System.out.println("Druhý hráč se připojil.");
                    Platform.runLater(() -> {
                        Alert a = Dialogs.dialogError("Druhý hráč se připojil.");
                        a.showAndWait();
                    });
//                } else if (playerRepeat){
//                    if (command[0].equals("repeat")){
//                        game.checkers.ui2.GlobalVariables.waitingTime = 0;
//                        Platform.runLater(() -> controllerGameScene.resetGame());
//                        Platform.runLater(() -> game.checkers.ui2.GlobalVariables.sceneChanger.changeToPlacingScene());
//                    } else if (game.checkers.ui2.GlobalVariables.waitingTime < GameConfiguration.waitForOpponent){
//                        game.checkers.ui2.GlobalVariables.waitingTime++;
//                    } else if (game.checkers.ui2.GlobalVariables.waitingTime == GameConfiguration.waitForOpponent){
//                        Platform.runLater(() -> game.checkers.ui2.GlobalVariables.sceneChanger.changeToRoomScene());
//                        //send endGame
//                        //game.checkers.ui2.GlobalVariables.getSendingThread().sendMessage("00214");
//                    }

                } else if (command[0].equals("endGame")){
                    Platform.runLater(() -> {
                        System.out.println("Konec hry");
                        Alert a = Dialogs.infoDialog("Konec hry");
                        a.showAndWait();
                    });
                } else if (wrongMessage > 3){
                    System.out.println("Wrong message");
//                    Platform.runLater(() -> Controller.showRoomWindow());
                    disconnectClient();
                } else {
                    wrongMessage++;
                }
            }
            int sec = getAgeInSeconds();
//            System.out.println("Sekundy od pingu mimo " +sec);
//            if (sec > 10) {
//                System.out.println("Lost connection to server!");
//                Platform.runLater(() -> {
//                    Alert a = Dialogs.dialogError("Lost connection to server!");
//                    a.showAndWait();
//                });
//            }
        } catch (IOException | InterruptedException g){
            System.out.println("Receiver: IE");
        }
    }

    public int getAgeInSeconds() {
        if(createdMillis == 0) return 0;
        long nowMillis = System.currentTimeMillis();
        return (int)((nowMillis - createdMillis) / 1000);
    }

    public void disconnectClient() {
//        try {
            System.out.println("Client disconnected! Server is not consistent!");
            Platform.runLater(() -> {
                Alert a = Dialogs.dialogError("Disconnecting from server!");
                a.showAndWait();
            });
//            Thread.sleep(2000);
            GlobalVariables.connected = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
//            Platform.runLater(() -> Controller.showLoginWindow());
//        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
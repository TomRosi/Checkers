package game.main;

import game.ui.Dialogs;
import game.ui.GlobalVariables;
import game.ui.PingThread;
import game.ui.ReceivingThread;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;


public class Connection {

    public static Socket socket;

    public static boolean connect(String ip, String port) {

//        if (!validateIP(ip) || ip != null) {
//            Alert a = Dialogs.dialogError("Input IP is not valid!");
//            a.showAndWait();
//            return true;
//        }

        if (!validatePort(port)) {
            Alert a = Dialogs.dialogError("Input port is not valid!");
            a.showAndWait();
            return false;
        }

        try {
            socket = new Socket(ip, Integer.parseInt(port));
//            socket.setSoTimeout(1500);
        } catch (IOException e) {
            Alert a = Dialogs.dialogError("Server is not running!");
            a.showAndWait();

            return false;
        }

//        statusTextField.setText("CONNECTED");
//        //inicializace senderu a recieveru
//        sendingThread = new SendingThread(socket);
//
        ReceivingThread receivingThread = new ReceivingThread(socket);
//        receivingThread.setControllerLoginScene(this);
        receivingThread.start();
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
//        LocalDateTime start = LocalDateTime.now();
//        System.out.println("Client was connected to server at " + dtf.format(start));
//        System.out.println("Server address: " + game.ui.GlobalVariables.ipAddress + " port: " + game.ui.GlobalVariables.port);
//
//        game.checkers.ui2.GlobalVariables.setSendingThread(sendingThread);
          GlobalVariables.setReceivingThread(receivingThread);
//        game.checkers.ui2.GlobalVariables.ipAddress = serverIPAddress.getText();
//        game.checkers.ui2.GlobalVariables.port = serverPort.getText();
          GlobalVariables.connected = true;
//        game.checkers.ui2.GlobalVariables.playerName = name;
//
//          game.ui.GlobalVariables.pingThread = new PingThread(game.ui.GlobalVariables.ipAddress);
//          game.ui.GlobalVariables.pingThread.start();
//
//
//        int messageSize = 2 + game.checkers.ui2.GlobalVariables.playerName.length();
//        game.checkers.ui2.GlobalVariables.getSendingThread().sendMessage(game.checkers.ui2.GlobalVariables.messagePattern(messageSize) + "01" + game.checkers.ui2.GlobalVariables.playerName);
//        gui.setDisable(true);
//        //Platform.runLater(() -> game.checkers.ui2.GlobalVariables.sceneChanger.changeToRoomScene());
        return true;
    }

    public static void sendMessage(String message) {
        int divisor = 100;
        int length = message.length();
        String finalMessage = "";

        while(divisor > 0) {
            int res = length/divisor;

            if(res > 0) {
                finalMessage = finalMessage + res;
                length = length - (res * divisor);
            }
            else
                finalMessage = finalMessage + "0";

            divisor /= 10;
        }

        finalMessage = finalMessage + message;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
//            System.out.println("To server: " +finalMessage);
            outputStreamWriter.write(finalMessage);
            if (!message.isEmpty() && !message.equals("00214")){
                System.out.println("Send to server: " + finalMessage);
            }
            outputStreamWriter.flush();
        } catch (SocketException f){
            System.out.println("ERROR: Socket is closed");
        } catch (IOException e) {
            System.out.println("ERROR: Sender");
        }
    }


    public static boolean validatePort(String inputPort){
        int port = Integer.parseInt(inputPort);
        return port >= 1 && port <= 65535;
    }

    public static boolean validateIP(String inputIP){
//        if (inputIP == null || inputIP.isEmpty()) {
//            return false;
//        }
//        inputIP = inputIP.trim();
//        if ((inputIP.length() < 7) || (inputIP.length() > 16)){
//            return false;
//        }
        /*
        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher matcher = pattern.matcher(inputIP);
        return matcher.matches();*/
        //return InetAddressUtils.isValidInet4Address(inputIP);
        return true;
    }

//    @FXML
//    public void initialize() {
//        SceneChanger sceneChanger = new SceneChanger();
//        gui.setStyle("-fx-background-image: url('/main/resources/img/background.png'); -fx-background-position: center center;");
//        game.checkers.ui2.GlobalVariables.sceneChanger = sceneChanger;
//      /*  if(game.checkers.ui2.GlobalVariables.playerOneRepeat && game.checkers.ui2.GlobalVariables.playerTwoRepeat){
//            initAfterGame();
//        }*/
//    }
}
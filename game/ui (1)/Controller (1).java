package game.ui;

import game.main.Connection;
import game.checkers.Entry;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    public static Stage primaryStage;

    public static void showRoomWindow() {
        primaryStage.hide();
        Room roomsF = new Room();
        primaryStage.setScene(new Scene(roomsF.getWindowContent()));
        primaryStage.show();
    }

    public static void showLoginWindow() {
        primaryStage.hide();
        Game game = new Game();
        game.start(primaryStage);
        primaryStage.show();
    }

    public static void playCheckers() {
        primaryStage.hide();
        Entry entry = new Entry();
//        entry.start(primaryStage);
        primaryStage.setScene(entry.getWindowContent());
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Client was terminated.");
            Connection.sendMessage(MsgType.LOGOUT_TYPE.type_string + GlobalVariables.playerName);
//            if(GlobalVariables.pingThread != null){
//                if (GlobalVariables.pingThread.threadActive){
//                    GlobalVariables.pingThread.close();
//                }
//            }
            if (GlobalVariables.connected) {
                ReceivingThread receivingThread = GlobalVariables.getReceivingThread();
                receivingThread.disconnectClient();
            }
//            System.exit(0);
        });
    }
}

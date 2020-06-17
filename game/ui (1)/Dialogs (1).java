package game.ui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class Dialogs {

    private static boolean joined;
    private static Label lbl;
    private static Stage primaryStage = new Stage();

    public static TextInputDialog dialogEntry(String msg, String msg2) {
        TextInputDialog dialog = new TextInputDialog(msg2);
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText(msg);
        dialog.setContentText(msg);

        return dialog;
    }

    public static Alert dialogError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(msg);

        return alert;
    }

    public static Alert dialogConfirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        return alert;
    }

    public static Alert dialogWait(String msg) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
//                Alert.AlertType.INFORMATION,
                msg
//                ButtonType.CANCEL
        );
        alert.setTitle(msg);
        alert.setHeaderText("Please wait... ");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        alert.setGraphic(progressIndicator);
        
        return alert;
    }

    public static Alert infoDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(" Info Dialog");
        alert.setContentText(msg);

        return alert;
    }

    public static void waitingWindow(String userName) {
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.setTitle("Checkers - Waiting - " + userName);
        primaryStage.setHeight(200);
        primaryStage.setWidth(400);
        primaryStage.setResizable(false);
        FlowPane fp = new FlowPane();
        fp.getChildren().add(addContent());
        fp.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(fp));

        primaryStage.setOnCloseRequest(Event::consume);

        primaryStage.show();
    }

    /**
     * prida obsah okna
     * @return VBox s obsahem
     */
    public static Node addContent() {
        Button start = new Button("Start");
        if(joined) {
            start.setDisable(true);
        }
        start.setOnAction(e -> Controller.playCheckers());

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> cancelWait(true));

        VBox vbx = new VBox(20);
        vbx.getChildren().addAll(start, cancel, lbl);
        vbx.setPadding(new Insets(20));

        return vbx;
    }

    public static void cancelWait(boolean answer) {
        if(answer) {
            primaryStage.close();
        }
        else {
            Alert alert2 = new Alert(Alert.AlertType.WARNING);
            alert2.setTitle("Error while quiting game!");
            alert2.setContentText("Try it again later!");
            alert2.showAndWait();
        }
    }

    /**
     * spusti hru
     * @param answer odpoved serveru
     */
    public static void startGame(boolean answer) {
        if(answer) {
//            CheckersBoard.createCheckerBoard();
            try {
//                gw.open(network, userName, roomName, network.getNrcv().getIl().getGameField(), network.getNrcv().getIl().getPNames(), network.getNrcv().getIl().getPScores(), true, lw, 18);
                primaryStage.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        else {
            dialogError("Error while starting game!");
        }
    }
}

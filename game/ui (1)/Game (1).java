package game.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import game.main.Connection;

import java.util.List;

public class Game extends Application {

    String regex = "^[a-zA-Z0-9]{4,16}$";

    @Override
    public void start(Stage primaryStage) {
        game.ui.Controller.primaryStage = primaryStage;
        primaryStage.setTitle("Checkers");
        BorderPane root = new BorderPane();

        Label labelName = new Label("Name:");
        TextField nameTextF = new TextField ();
        VBox vb = new VBox();
        HBox hb = new HBox();

        Button connectButton = new Button("Connect");

        connectButton.setOnAction(event -> {
            if(nameTextF.getText() == null || !nameTextF.getText().matches(regex)) {
                Alert a = game.ui.Dialogs.dialogError("Invalid name!");
                a.showAndWait();
            } else {
                game.ui.GlobalVariables.playerName = nameTextF.getText();
                Connection.sendMessage(game.ui.MsgType.LOGIN_TYPE.type_string + nameTextF.getText());
          //      primaryStage.hide();
          //      Room r = new Room();
          //      r.showRoomWindow(primaryStage);
            }
        });

        labelName.setMinSize(30, 10);
        connectButton.setMinSize(50, 20);


        vb.getChildren().addAll(labelName, nameTextF, connectButton);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        hb.getChildren().add(vb);
        hb.setAlignment(Pos.CENTER);

        root.setCenter(hb);
        primaryStage.setScene(new Scene(root, 360, 180));

        primaryStage.setOnCloseRequest( e -> {
            System.out.println("Client was terminated.");
            Connection.sendMessage(MsgType.LOGOUT_TYPE.type_string + " ");

        });
        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();
        final String ipAdress = !parameters.isEmpty() ? parameters.get(0) : "127.0.0.1";
//        GlobalVariables.ipAddress = ipAdress;
        final String port = !parameters.isEmpty() ? parameters.get(1) : "6666";
//        GlobalVariables.port = port;

        if (!Connection.connect(ipAdress, port)) { //192.168.1.117  192.168.140.201
//        if (!Connection.connect("192.168.1.108", "6666")) { //192.168.1.117  192.168.140.201  192.168.1.108
            Alert a = Dialogs.dialogError("Cannot connect to server!");
            a.showAndWait();
            System.exit(1);
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package game.ui;

import game.main.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static game.ui.GlobalVariables.rooms;

public class Room  {

    public String regex = "^[a-zA-Z0-9]{4,16}$";

    public Parent getWindowContent() {
        BorderPane root = new BorderPane();

        Label labelName = new Label("Rooms:");
        VBox vb = new VBox(10);
        HBox hb = new HBox(10);
        Button refresh = new Button("Refresh");
        Button createButton = new Button("Create");
        Button joinButton = new Button("Join");

        ListView<String> listView = new ListView<String>();
//        ObservableList<String> roomList = FXCollections.observableArrayList();
        rooms = FXCollections.observableArrayList();

        refresh.setOnAction(event -> {
//            rooms.add(roomName);
            listView.getItems().clear();
            Connection.sendMessage(MsgType.REQUEST_ROOM_TYPE.type_string + GlobalVariables.playerName +";"+rooms);
        });

        listView.setItems(rooms);

        createButton.setOnAction(event -> {
            System.out.println("Vytvořil jsi roomku se svým jménem " + GlobalVariables.playerName);
            Connection.sendMessage(MsgType.CREATE_ROOM_TYPE.type_string + GlobalVariables.playerName);
//            TextInputDialog roomsName = Dialogs.dialogEntry("Entry room name", "Room1");
//            roomsName.setHeaderText("Room name must be characters long.\nNumbers and letters only.");
////            roomsName.setContentText();
//            Optional<String> name = roomsName.showAndWait();
//            if(!name.isPresent())
//                return;
//            if(!name.get().matches(regex) || !name.get().isEmpty()) {
//                System.out.println("Jsi hráč " + GlobalVariables.playerName);
//                Connection.sendMessage(MsgType.CREATE_ROOM_TYPE.type_string + name.get() +";" +GlobalVariables.playerName);
//                //kdyz ok tak super jinak nic
//                // pridani do listview
//            } else {
////                roomsName.setGraphic(); // nastaveni barvy textu nebo iconu vykricniku
//            }
        });


        joinButton.setOnAction(event -> {
            ObservableList<String> string = listView.getSelectionModel().getSelectedItems();
            if(!string.isEmpty()) {
                if (!string.get(0).isEmpty()) {
                System.out.println("Joinul jsi roomku "+string.get(0)+" a jsi "+GlobalVariables.playerName);
                System.out.println(MsgType.JOIN_ROOM_TYPE.type_string + GlobalVariables.playerName  +";" + string.get(0));
                Connection.sendMessage(MsgType.JOIN_ROOM_TYPE.type_string + GlobalVariables.playerName  +";" + string.get(0));
                } else {
                    Alert a = Dialogs.dialogError("Není žádná aktivní roomka");
                    a.showAndWait();
                }
            } else {
                Alert a = Dialogs.dialogError("Není žádná aktivní roomka");
                a.showAndWait();
            }

//            game.checkers.Connection.sendMessage(MsgType.SET_BOARD_TYPE.type_string + GlobalVariables.playerName  +";" + listView.getSelectionModel().getSelectedItems().get(0));
        });

        labelName.setMinSize(30, 10);
        listView.setMaxHeight(100);
        createButton.setMinSize(50, 20);

        hb.getChildren().addAll(refresh, createButton, joinButton);

        vb.getChildren().addAll(labelName, listView, hb);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        hb.setAlignment(Pos.CENTER);

        root.setCenter(vb);

        return root;
    }
}

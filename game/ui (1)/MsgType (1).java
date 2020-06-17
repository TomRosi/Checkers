package game.ui;

public enum MsgType {
    LOGIN_TYPE("01"),
    LOGOUT_TYPE("02"),
    CREATE_ROOM_TYPE("03"),
    REQUEST_ROOM_TYPE("04"),
    JOIN_ROOM_TYPE("05"),
    LEAVE_ROOM_TYPE("06"),
    MOVE_TYPE("07"),
    SET_BOARD_TYPE("08"),
    PING_TYPE("14");

    public String type_string;

    private MsgType(String type_string) {
        this.type_string = type_string;
    }
}

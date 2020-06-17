package com.example.checkers;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

public class Board extends Activity {
    GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setBackgroundColor(Color.WHITE);
        setContentView(gameView);

    }

}

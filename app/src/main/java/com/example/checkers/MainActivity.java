package com.example.checkers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Třída MainActivity je třída reprezentující "loading" screen
 * tlačítko play přepne activity
 */
public class MainActivity extends AppCompatActivity {
    Button button;

    /**
     * Tlačítko start přepne aktivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }

    /**
     * Otevře novou aktivitu
     */
    public void openNewActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
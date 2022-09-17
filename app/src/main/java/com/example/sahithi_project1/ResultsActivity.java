package com.example.sahithi_project1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);
        Intent intent = getIntent();
        TextView message = findViewById(R.id.resultsMsg);
        Boolean won = intent.getBooleanExtra("com.example.sahithi_project1.won", true);
        if (won) {
            int clock = intent.getIntExtra("com.example.sahithi_project1.clock", 0);
            String msg = String.format("Used %d seconds. You won! Good job!", clock);
            message.setText(msg);
        } else {
            message.setText("Try again.");
        }

    }


}

package com.example.sahithi_project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.Random;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 8;
    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    ArrayList<Cell> cells = new ArrayList<Cell>();
    private int clock = 0;
    private boolean running = false;
    ArrayList<Integer> minesList = new ArrayList<Integer>();
    TextView flagPick;
    TextView flagCount;

//    private int dpToPixel(int dp) {
//        float density = Resources.getSystem().getDisplayMetrics().density;
//        return Math.round(dp * density);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();
        flagPick = findViewById(R.id.textView12);

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);

        flagCount = findViewById(R.id.textView10);


        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<=9; i++) {
            for (int j=0; j<=7; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
//                cells.add(new Cell());
                Cell cell = new Cell();
                cells.add(cell);
                //tv.setText(String.valueOf(i)+String.valueOf(j));
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }

        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }
        placingMines(4);

        runTimer();

        flagPick.setText(R.string.pick);
        flagPick.setOnClickListener(this::onClickflagPick);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView11);
        final Handler handler = new Handler();
        running = true;

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours =clock/3600;
                int minutes = (clock%3600) / 60;
                int seconds = clock%60;
                String time = String.format("%d:%02d:%02d", hours, minutes, seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickflagPick(View view) {
        TextView flagPick = (TextView) view;
        if (flagPick.getText().equals(getString(R.string.pick))) {
            flagPick.setText(R.string.flag);
        }else {
            flagPick.setText(R.string.pick);
        }

    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int count = Integer.parseInt(flagCount.getText().toString());
//        tv.setText(String.valueOf(n));
        if(flagPick.getText().equals(getString(R.string.flag))){
            if (cells.get(n).getFlag()) {
                tv.setText("");
                count++;
                flagCount.setText(String.valueOf(count));
                cells.get(n).setFlag();
            } else{
                tv.setText(this.getString(R.string.flag));
                count--;
                flagCount.setText(String.valueOf(count));
                cells.get(n).setFlag();
            }
        } else { //if it's on the pickaxe and they click it, dot he BFS ere
//            parameter of the bfs function would be the index of the cell, then call

          }

        if (tv.getCurrentTextColor() == Color.GRAY) {
            tv.setTextColor(Color.GREEN);
            tv.setBackgroundColor(Color.parseColor("lime"));
        }else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.GREEN);
        }
    }

    private void setAdjacentValues(int originalCell){
        int i = originalCell/COLUMN_COUNT; //row
        int j = originalCell%COLUMN_COUNT; //column

        //left diagonal
        int leftDiagonalRow = i - 1;
        int leftDiagonalColumn = j - 1;
        if (inBounds(leftDiagonalRow, leftDiagonalColumn)){
            cells.get(leftDiagonalRow * COLUMN_COUNT + leftDiagonalColumn).incCount();
        }

        //right diagonal
        int rightDiagonalRow = i - 1;
        int rightDiagonalColumn = j + 1;
        if (inBounds(rightDiagonalRow, rightDiagonalColumn)){
            cells.get(rightDiagonalRow * COLUMN_COUNT + rightDiagonalColumn).incCount();
        }

        //top
        int topRow = i -1;
        int topColumn = j;
        if (inBounds(topRow, topColumn)){
            cells.get(topRow * COLUMN_COUNT + topColumn).incCount();
        }

        //direct left
        int leftRow = i;
        int leftColumn = j - 1;
        if (inBounds(leftRow, leftColumn)){
            cells.get(leftRow * COLUMN_COUNT + leftColumn).incCount();
        }

        //direct right
        int rightRow = i + 1;
        int rightColumn = j;
        if (inBounds(rightRow, rightColumn)){
            cells.get(rightRow * COLUMN_COUNT + rightColumn).incCount();
        }

        //bottom
        int bottomRow = i + 1;
        int bottomColumn = j;
        if (inBounds(bottomRow, bottomColumn)){
            cells.get(bottomRow * COLUMN_COUNT + bottomColumn).incCount();
        }

        //left bottom diagonal
        int leftBotRow = i + 1;
        int leftBotColumn = j - 1;
        if (inBounds(leftBotRow, leftBotColumn)){
            cells.get(leftBotRow * COLUMN_COUNT + leftBotColumn).incCount();
        }

        //right bottom diagonal
        int rightBotRow = i + 1;
        int rightBotColumn = j + 1;
        if (inBounds(rightBotRow, rightBotColumn)){
            cells.get(rightBotRow * COLUMN_COUNT + rightBotColumn).incCount();
        }

    }

//    only push a neighbor if it's an adjacent cell
//    if it's a mine, don't look at its neighbors

    public void placingMines(int numberMines){
        for(int i = 0; i <numberMines; i++) {
            int randNum = new Random().nextInt(79);
            if (!cells.get(randNum).getMine()) {
                cells.get(randNum).setMine();
                minesList.add(randNum);
                setAdjacentValues(randNum);
            } else {
                i--;
            }
        }
    }

    public boolean inBounds(int row, int col) {
        if ( (row < 0) || (row > 10)) {
            return false;
        }

        if( (col > COLUMN_COUNT) || (col < 0)) {
            return false;
        }

        return true;
    }
}


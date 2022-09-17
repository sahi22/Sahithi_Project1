package com.example.sahithi_project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
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
import java.util.LinkedList;
import java.util.Queue;

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
    private int revealed;
    private boolean gameStatus = false;

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
        revealed = 0;
        gameStatus = true;
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<=9; i++) {
            for (int j=0; j<=7; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
//                cells.add(new Cell());
                Cell cell = new Cell(i * COLUMN_COUNT + j);
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
//                int hours = clock/3600;
//                int minutes = (clock%3600) / 60;
                String time = String.format("%02d", clock);
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
        if (gameStatus == false) {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("com.example.sahithi_project1.won", true);
            intent.putExtra("com.example.sahithi_project1.clock", clock);
            startActivity(intent);
            return;
        }
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
            if (cells.get(n).getMine()) {
//               if they click on a cell with a mine, reveal all the mines
                for (int i = 0; i < minesList.size(); i++) {
                    tv.setText(this.getString(R.string.mine));
                }

                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("com.example.sahithi_project1.won", true);
                intent.putExtra("com.example.sahithi_project1.clock", clock);

                startActivity(intent);

            } else {
                tv.setBackgroundColor(Color.GRAY);
                ++revealed;
                Queue<Cell> queue = new LinkedList<>();
//                cell_tvs.get(n).setBackgroundColor(Color.GRAY);

                queue.add(cells.get(n));
                cells.get(n).setVisited();

                boolean noMine = true;
                while(queue.size() > 0 && noMine ) {
                    Cell curr_cell = queue.remove();
                    int row = curr_cell.getNumber()/COLUMN_COUNT; //row
                    int col = curr_cell.getNumber()%COLUMN_COUNT;
                    for (int k = row-1; k < row+2; ++k) {
                        for (int l = col-1; l < col + 2; ++l) {
                            int curr_idx = k * COLUMN_COUNT + l;
                            if (k == row && l == col) {
                                continue;
                            }
                            else if (inBounds(k, l) && cells.get(curr_idx).getMine()) {
                                noMine = false;
//                                k = row + 2;
//                                break;
                            } else if (inBounds(k, l) && cells.get(curr_idx).getCount() > 0
                                    && cells.get(curr_idx).getVisited() == false) {
                                cell_tvs.get(curr_idx).setBackgroundColor(Color.GRAY);
                                cell_tvs.get(curr_idx).setText(Integer.toString(cells.get(curr_idx).getCount()));
                                cell_tvs.get(curr_idx).setTextColor(Color.GREEN);
                                cells.get(curr_idx).setVisited();
                                ++revealed;
                                continue;
                            }
                            else if (inBounds(k, l) && cells.get(curr_idx).getVisited() == false) {
                                ++revealed;
                                queue.add(cells.get(curr_idx));
                                cells.get(curr_idx).setVisited();
                                cell_tvs.get(curr_idx).setBackgroundColor(Color.GRAY);
                            }
                            System.out.println(revealed);
                        }
                    }
                }
            }
            if (revealed >= 76) {
                for (int i = 0; i < minesList.size(); i++) {
                    cell_tvs.get(minesList.get(i)).setBackgroundColor(Color.BLUE);
                    cell_tvs.get(minesList.get(i)).setText(this.getString(R.string.mine));
                }
                gameStatus = false;
                running = false;

            }
          }
    }

    private void setAdjacentValues(int originalCell){
        int i = originalCell/COLUMN_COUNT; //row
        int j = originalCell%COLUMN_COUNT; //column

        for (int row = i-1; row < i+2; row++) {
            for (int col = j-1; col < j + 2; ++col) {
                if (i == row && j == col) {
                    continue;
                }
                if  (inBounds(row, col) && !cells.get(row * COLUMN_COUNT + col).getMine()) {
                    cells.get(row * COLUMN_COUNT + col).incCount();
                    cell_tvs.get(row * COLUMN_COUNT + col).setText(Integer.toString(cells.get(row * COLUMN_COUNT + col).getCount()));


                }
            }
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
                cell_tvs.get(randNum).setText(getString(R.string.mine));
                setAdjacentValues(randNum);
            } else {
                i--;
            }
        }
    }

    public boolean inBounds(int row, int col) {
        if ( (row < 0) || (row > 9)) {
            return false;
        }

        if( (col > COLUMN_COUNT - 1) || (col < 0)) {
            return false;
        }

        return true;
    }
}


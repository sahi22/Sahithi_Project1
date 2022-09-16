package com.example.sahithi_project1;

public class Cell {
    private boolean hasFlag = false;
    private boolean hasMine = false;
    private int mineCount = 0;

    public int getCount() {
        return mineCount;
    }

    public void incCount() {
        mineCount++;
    }

    public boolean getMine() {
        return hasMine;
    }

    public boolean getFlag() {
        return hasFlag;
    }

    public void setFlag() {
        if (hasFlag == true) {
            hasFlag = false;
        } else {
            hasFlag = true;
        }
    }

    public void setMine() {
        hasMine = true;
    }
}

package com.example.sahithi_project1;

public class Cell {
    private boolean hasFlag;
    private boolean hasMine;
    private int mineCount;
    private boolean visited;
    private int number;

    public Cell() {
        hasFlag = false;
        hasMine = false;
        mineCount = 0;
        visited = false;
    }

    public Cell(int n) {
        hasFlag = false;
        hasMine = false;
        mineCount = 0;
        visited = false;
        number = n;

    }

    public int getNumber() {
        return number;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

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

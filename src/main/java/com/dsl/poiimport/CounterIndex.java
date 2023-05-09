package com.dsl.poiimport;

public class CounterIndex {

    private int row;
    private int cell;

    public CounterIndex() {
    }

    public CounterIndex(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int addRowIndex(){
        return ++row;
    }

    public int addCellIndex(){
        return ++cell;
    }

    public void clearIndex(){
        this.row = 0;
        this.cell = 0;
    }

    public void clearCell(){
        this.cell = 0;
    }

}

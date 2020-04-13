package com.andris.battleship;

import java.util.ArrayList;

public class ShipModel {
    private ArrayList<Integer> locationCells;

    /** getters & setters **/
    public void setLocationCells(ArrayList<Integer> locationCells) {
        this.locationCells = locationCells;
    }

    /** check if hit/miss/kill method **/
    public String checkInput(Integer userInput) {
        String result = "Miss!";
        int index = locationCells.indexOf(userInput);
        //returns the index of element or -1 if element is not present

        if(index>=0) {
            locationCells.remove(index);
            if(locationCells.isEmpty()) {
                result = "Kill!";
            } else {
                result = "Hit!";
            }
        }
        return result;
    }
}

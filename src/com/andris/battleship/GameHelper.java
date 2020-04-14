package com.andris.battleship;

import java.util.ArrayList;

public class GameHelper {
    int gridWidth;
    int gridHeight;
    int shipSize;
    // cell value: 0 if free, 1 if taken
    private int[] cellValues;

    public GameHelper(int gridWidth, int gridHeight, int shipSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.shipSize = shipSize;
        cellValues = new int[gridHeight*gridWidth];
    }

    // place ship on grid
    public ArrayList<Integer> placeShip() {
        // list to hold the cell values of the ship (i.e. "c1", "c2", "c3")
        ArrayList<Integer> shipCellPosition = new ArrayList<>();
        // coordinates for storing the the position of ship cells (0-48)
        int[] gridPosition = new int[shipSize];
        // attempts to place ship
        int attempts = 0;
        boolean success = false;
        // starting location to place ship
        int location = 0;


        // set the increment step: 1 for horizontal ship OR 7 (gridWidth) for vertical ship
        int rand = (int)(Math.random()*10);
        int incrStep;
        if ((rand % 2) == 1) {   // should be random!!!
            incrStep = gridWidth;
        } else {
            incrStep = 1;
        }

        while ( !success & attempts++ < 200 ) {
            // get random starting point to place ship
            location = (int) (Math.random() * (gridHeight*gridWidth));
//            System.out.print(" try " + location);
            // ship cell number
            int x = 0;
            System.out.println(cellValues.length);
            // assume success
            success = true;

            // place ship by finding a number (=shipSize) of adjacent free spots
            while (success && x < shipSize) {
                // if cell not already used
                if (cellValues[location] == 0) {
                    // save ship cell position to grid location (0-48)
                    // and move to next ship cell
                    gridPosition[x++] = location;
                    // move to adjacent position
                    location += incrStep;
                    if (location >= (gridHeight*gridWidth)){ // if true, we are below the bottom of the grid
                        success = false;
                    }
                    if (x>0 && (location % gridWidth == 0)) {  // went over the right edge, valid right edge locations are n*7-1
                        success = false;
                    }
                    // if location == 1, cell is already used
                } else {
                    System.out.print(" used " + location);
                    success = false;
                }
            }
        }

        // turn cell number (0-48) into grid position (i.e. "c1")
        int x = 0;

        while (x < shipSize) {
            // mark ship cell position as occupied
            cellValues[gridPosition[x]] = 1;
            // save ship cell position
            shipCellPosition.add(gridPosition[x]);
            x++;
        }
        System.out.println("\n");
        return shipCellPosition;
    }
}

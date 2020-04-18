package com.andris.minesweeper;

import java.util.ArrayList;

public class Cell {
    Integer location;

    public int[] calculateAdjacentLocations(int gridWidth, int gridHeight) {
        int cellNumber = gridHeight*gridWidth - 1;

        int[] adLoc = new int[8];
        adLoc[0] = location - gridWidth; // north
        adLoc[1] = location - gridWidth + 1; // north-east
        adLoc[2] = location + 1; // east
        adLoc[3] = location + gridWidth + 1; // south-east
        adLoc[4] = location + gridWidth; // south
        adLoc[5] = location + gridWidth - 1; // south-west
        adLoc[6] = location - 1; // west
        adLoc[7] = location - gridWidth - 1; // north-west

        for(int i = 0; i<adLoc.length; i++ ) {
            if(adLoc[i] >= cellNumber || adLoc[i] < 0) { // adjacent cell above or below the grid
                adLoc[i]=-100;
            }
        }

        if(location%gridWidth == gridWidth-1) { // right border cell - no right adjacent cells
            adLoc[1] = -100; // north-east
            adLoc[2] = -100; // east
            adLoc[3] = -100; // south-east
        }

        if(location%gridWidth == 0) { // left border cell - no left adjacent cells
            adLoc[5] = -100; // south-west
            adLoc[6] = -100; // west
            adLoc[7] = -100; // north-west
        }
        return adLoc;
    }
}

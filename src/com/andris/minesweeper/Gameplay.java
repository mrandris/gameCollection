package com.andris.minesweeper;

import java.util.ArrayList;

public class Gameplay {
    private int gridWidth = 7;
    private int gridHeight = 7;
    private int bombNumber = 5;

    private Cell cell;

    public ArrayList<Integer> startGame() {
        int[] cellValues = new int[gridWidth*gridHeight];
        ArrayList<Integer> bombs = new ArrayList<>();
        for(int i = 0; i<bombNumber; i++) {
            boolean success = false;
            while(!success) {
                int bombLocation = (int) (Math.random()*(gridWidth*gridHeight));
                if(cellValues[bombLocation] == 0) { //cell not taken
                    bombs.add(bombLocation);
                    cellValues[bombLocation] = 1; // mark as occupied
                    success = true;
                }
            }
            System.out.println("Bomb " + i + ", location: " + bombs.get(i));
        }
        return bombs;
    }

    public ArrayList<Integer> playGame(Integer index) {
        ArrayList<Integer> adjacent = new ArrayList<>();
        cell = new Cell();
        cell.location = index;
        int[] adLoc = cell.calculateAdjacentLocations(7,7);
//        System.out.println("\nAdjacent cells for cell no. " + index);
//        for(int i=0; i<adLoc.length; i++) {
//            System.out.print(adLoc[i] + "\t");
//        }
        for(int i = 0; i<adLoc.length; i++) {
            adjacent.add(adLoc[i]);
        }
        return adjacent;
    }
}

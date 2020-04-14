package com.andris.battleship;

import java.util.ArrayList;

public class Gameplay {
    private int numberOfShips = 3;
    private int sizeOfShips = 3;

    int guesses = 0;

    private ArrayList<ShipModel> shipList = new ArrayList<>();
    GameHelper helper = new GameHelper(7,7,3);

    private int gridWidth = 7;
    private int gridHeight = 7;

    public ArrayList<Integer> startGame() {
        ShipModel sbs1 = new ShipModel();
        ShipModel sbs2 = new ShipModel();
        ShipModel sbs3 = new ShipModel();

        shipList.add(sbs1);
        shipList.add(sbs2);
        shipList.add(sbs3);

        ArrayList<Integer> locations = new ArrayList<>();
        for(ShipModel ship : shipList) {
            ArrayList<Integer> shipLocation = helper.placeShip();
            System.out.println("Ship: " + shipList.indexOf(ship));
            System.out.println("Location: ");
            for(Integer i : shipLocation) {
                System.out.print(i + " ");
                locations.add(i);
            }
            ship.setLocationCells(shipLocation);
        }
        return locations;
    }

    public ArrayList<String> playGame(Integer userGuess) {
        ArrayList<String> messages = new ArrayList<>();

        while(!shipList.isEmpty()){
            messages.add(checkUserGuess(userGuess));
            if(shipList.isEmpty()) {
                return endGame();
            }
            return messages;
        }
        return endGame();
    }

    private String checkUserGuess(Integer userGuess) {
        guesses++;
        String result = "Miss!";

        for(ShipModel ship : shipList) {
            result = ship.checkInput(userGuess);
            if(result.equals("Hit!")) {
                System.out.println(result);
                break;
            }
            if(result.equals("Kill!")) {
                shipList.remove(ship);
                System.out.println(result);
                break;
            }
        }

        if(result.equals("Miss!")) {
            System.out.println(result);
        }
        return result;
    }

    private ArrayList<String> endGame() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("All ships sunk! Game over!");
        messages.add("It took you " + guesses + " guesses.");
        if(guesses<20) {
            messages.add("Well done! Good aiming!");
        } else if(guesses<35) {
            messages.add("Took you long enough...");
        } else {
            messages.add("Unlucky? Try again!");
        }
        return messages;
    }
}

package com.andris;

public class App {
    public static void main(String[] args) {
        GameService.setup();
        new Appearence().buildGUI();
    }
}

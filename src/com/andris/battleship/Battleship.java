package com.andris.battleship;

import com.andris.Game;
import com.andris.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Battleship implements Game {
    private String gameName = "Battleship";
    Gameplay game;

    String message;

    ButtonListener buttonListener = new ButtonListener();
    ArrayList<JButton> battlefieldCells;
    ArrayList<Boolean> battlefieldValues; // true if ship is on that location

    JPanel mainPanel;
    JPanel battlefieldPanel;
    JPanel rightPanel;
    Box messageBox;
    JLabel messageLabel;

    /*** just to show name of the game ***/
    @Override
    public String getGameName() {
        return gameName;
    }

    /*** this is called when the game starts ***/
    @Override
    public JPanel launch() {
        mainPanel = new JPanel(new BorderLayout());

        buildTopPanel();
        buildBattlefieldPanel();
        buildRightPanel();

        return mainPanel;
    }

    /*** build top panel ***/
    private void buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,15,5));

        JLabel title = new JLabel("B A T T L E S H I P");
        topPanel.add(title);
        mainPanel.add(BorderLayout.NORTH, topPanel);
    }

    /*** build battlefield panel ***/
    private void buildBattlefieldPanel() {
        GridLayout grid = new GridLayout(7, 7);  // list should be parametrized!!! not hard-coded 49 buttons
        grid.setVgap(0);
        grid.setHgap(0);

        battlefieldPanel = new JPanel(grid);
        battlefieldPanel.setBorder(BorderFactory.createBevelBorder(1));
        mainPanel.add(BorderLayout.CENTER, battlefieldPanel);

        makeBattlefield(); // set up battlefield
    }

    /*** set up battlefield ***/
    private void makeBattlefield() {
        game = new Gameplay();
        battlefieldCells = new ArrayList<>();
        battlefieldValues = new ArrayList<>();
        ArrayList<Integer> locations = game.startGame(); // startGame() places ships on the battlefield
        for (Integer i = 0; i < 49; i++) { // list should be parametrized!!! not hard-coded 49 buttons
            JButton b = new JButton();
            b.setBorder(BorderFactory.createBevelBorder(0));
            b.addActionListener(buttonListener);
            b.setBackground(Color.WHITE);
            b.setPreferredSize(new Dimension(50, 50));
            battlefieldCells.add(b);
            if(locations.contains(i)) {
                battlefieldValues.add(true);
            } else {
                battlefieldValues.add(false);
            }
            battlefieldPanel.add(b);
        }
    }

    /*** build right panel ***/
    private void buildRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));

        buildGameDescription();
        buildGameButtons();
        buildMessageBox();

        mainPanel.add(BorderLayout.EAST, rightPanel);
    }

    /*** build game description - for right panel ***/
    private void buildGameDescription() {
        Box textBox = new Box(BoxLayout.Y_AXIS);
        rightPanel.add(BorderLayout.NORTH, textBox);

        JLabel text1 = new JLabel("Try to sink the ships in");
        JLabel text11 = new JLabel("as few clicks as you can!");
        JLabel text2 = new JLabel("Size of the ships: 3 squares");
        JLabel text3 = new JLabel("Number of ships: 3");

        textBox.add(text1);
        textBox.add(text11);
        textBox.add(new JLabel(" "));
        textBox.add(text2);
        textBox.add(text3);
        textBox.add(new JLabel(" "));
    }

    /*** build game buttons - for right panel ***/
    private void buildGameButtons() {
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        rightPanel.add(BorderLayout.CENTER, buttonBox);
        JButton reset = new JButton("Start New Game");
        reset.addActionListener(new ResetListener());
        JButton show = new JButton("   Reveal Ships   ");
        show.addActionListener(new ShowListener());
        buttonBox.add(reset);
        buttonBox.add(new JLabel(" "));
        buttonBox.add(show);
    }

    /*** build message box - for right panel ***/
    private void buildMessageBox() {
        messageBox = new Box(BoxLayout.Y_AXIS);
        message = "Game Started!";
        messageLabel = new JLabel(message);
        messageBox.add(messageLabel);
        rightPanel.add(BorderLayout.SOUTH, messageBox);
    }

    /*** ACTION: button clicked on battlefield ***/
    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(JButton button : battlefieldCells) {
                if(e.getSource().equals(button)) {
                    int index = battlefieldCells.indexOf(button);
                    refreshMessages(index);

                    battlefieldPanel.remove(index);
                    if(battlefieldValues.get(index)) {
                        JButton b = new JButton("X");
                        b.setBorder(BorderFactory.createBevelBorder(1));
                        b.setBackground(Color.RED);
                        b.setOpaque(true);
                        battlefieldPanel.add(b, index);
                    } else {
                        battlefieldPanel.add(new JPanel(),index);
                    }
                    battlefieldPanel.validate();
                    battlefieldPanel.repaint();
                }
            }
            System.out.println(e.getModifiers());
        }
    }

    public void refreshMessages(Integer index) {
        messageBox.removeAll();

        for(String message : game.playGame(index)) {
            messageBox.add(new JLabel(message));
            System.out.println(message);
        }

        messageBox.validate();
        messageBox.repaint();
        rightPanel.validate();
        rightPanel.repaint();
    }

    /*** ACTION: reset battlefield (new game) ***/
    public class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Reset battlefield");
            battlefieldPanel.removeAll();
            makeBattlefield();
            battlefieldPanel.validate();
            battlefieldPanel.repaint();
        }
    }

    /*** ACTION: reveal ships location ***/
    public class ShowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(JButton button : battlefieldCells) {
                    int index = battlefieldCells.indexOf(button);
                    battlefieldPanel.remove(index);
                    if(battlefieldValues.get(index)) {
                        JButton b = new JButton("X");
                        b.setBorder(BorderFactory.createBevelBorder(1));
                        b.setBackground(Color.RED);
                        b.setOpaque(true);
                        battlefieldPanel.add(b, index);
                    } else {
                        battlefieldPanel.add(new JPanel(),index);
                    }
                    battlefieldPanel.validate();
                    battlefieldPanel.repaint();
            }
        }
    }
}

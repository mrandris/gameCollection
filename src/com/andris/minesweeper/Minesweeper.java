package com.andris.minesweeper;

import com.andris.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Minesweeper implements Game {
    private String gameName = "Minesweeper";
    Gameplay game;

    String message;

    MouseListener buttonListener = new ButtonListener();
    ArrayList<JButton> gameFieldCells;
    ArrayList<Integer> gameFieldValues; // true if object is on that location

    JPanel mainPanel;
    JPanel gameFieldPanel;
    JPanel rightPanel;
    Box messageBox;
    JLabel messageLabel;

    boolean flagged = false;

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public JPanel launch(){
        mainPanel = new JPanel(new BorderLayout());

        buildTopPanel();
        buildGameFieldPanel();
        buildRightPanel();

        return mainPanel;
    }

    /*** build top panel ***/
    private void buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,15,5));

        JLabel title = new JLabel(gameName);
        topPanel.add(title);
        mainPanel.add(BorderLayout.NORTH, topPanel);
    }

    /*** build gameField panel ***/
    private void buildGameFieldPanel() {
        GridLayout grid = new GridLayout(7, 7);  // list should be parametrized!!! not hard-coded 49 buttons
        grid.setVgap(0);
        grid.setHgap(0);

        gameFieldPanel = new JPanel(grid);
        gameFieldPanel.setBorder(BorderFactory.createBevelBorder(1));
        mainPanel.add(BorderLayout.CENTER, gameFieldPanel);

        makeGameField(); // set up gameField
    }

    /*** set up gameField ***/
    private void makeGameField() {
        game = new Gameplay();
        gameFieldCells = new ArrayList<>();
        gameFieldValues = new ArrayList<>();
        ArrayList<Integer> locations = game.startGame(); // startGame() places ships on the battlefield
        for (Integer i = 0; i < 49; i++) {  // list should be parametrized!!! not hard-coded 49 buttons
            JButton b = new JButton();
            b.setBorder(BorderFactory.createBevelBorder(0));
            b.addMouseListener(buttonListener);
            b.setBackground(Color.WHITE);
            b.setPreferredSize(new Dimension(50, 50));
            gameFieldCells.add(b);
            Integer index = gameFieldCells.indexOf(b);
            if(locations.contains(i)) {
                gameFieldValues.add(-1); // mark button as bomb
            } else {
                // get adjacent cells and count how many bombs are between them
                ArrayList<Integer> adjacent = game.playGame(i);
                int counter = 0;
                for(Integer x : adjacent) {
                    if(locations.contains(x)) {
                        counter++;
                    }
                }
                gameFieldValues.add(counter); // set cell value to adjacent bomb number
            }
            gameFieldPanel.add(b);
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

        JLabel text1 = new JLabel("Find all the mines on the field");
        JLabel text11 = new JLabel("without stepping on one!");
        JLabel text2 = new JLabel("Size of filed: H x L");
        JLabel text3 = new JLabel("Number of mines: 3");

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
        JButton show = new JButton("   Reveal Mines   ");
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
    public class ButtonListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                for(JButton button : gameFieldCells) {
                    if (e.getSource().equals(button)) {
                        int index = gameFieldCells.indexOf(button);
                        gameFieldPanel.remove(index);
                        JButton b = gameFieldCells.get(index);
                        if (!b.isSelected()) {
                            b.setText("X");
                            b.setBorder(BorderFactory.createBevelBorder(0));
                            b.setBackground(Color.GREEN);
                            b.setOpaque(true);
                            b.setSelected(true);
                            gameFieldPanel.add(b, (int) index);
                            gameFieldPanel.validate();
                            gameFieldPanel.repaint();

                            // get message on field of remaining bombs
                        } else {
                            b.setText("");
                            b.setBackground(Color.WHITE);
                            b.setSelected(false);
                            b.setBorder(BorderFactory.createBevelBorder(0));
                            gameFieldPanel.add(b, (int) index);
                            gameFieldPanel.validate();
                            gameFieldPanel.repaint();
                            // get message on field of remaining bombs
                        }
                    }
                }
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                for(JButton button : gameFieldCells) {
                    if(!button.isSelected()) {
                        if (e.getSource().equals(button)) {
                            int index = gameFieldCells.indexOf(button);

                            if (gameFieldValues.get(index) == -1) {
                                revealMines();
                                gameFieldPanel.remove(index);
                                JButton b = new JButton("X");
                                b.setBorder(BorderFactory.createBevelBorder(1));
                                b.setBackground(Color.RED);
                                b.setOpaque(true);
                                gameFieldPanel.add(b, (int) index);
                                gameFieldPanel.validate();
                                gameFieldPanel.repaint();
                                // game over - reveal board
                                // get message
                            } else if (gameFieldValues.get(index) == 0) {
                                // reveal adjacent buttons
                                revealAdjacentCells(index);
                            } else {
                                // reveal cell value
                                gameFieldPanel.remove(index);
                                String s = gameFieldValues.get(index).toString();
                                JButton b = new JButton(s);
                                b.setBackground(Color.LIGHT_GRAY);
                                b.setOpaque(true);
                                b.setBorder(BorderFactory.createBevelBorder(1));
                                gameFieldPanel.add(b, (int) index);
                                gameFieldPanel.validate();
                                gameFieldPanel.repaint();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /*** reveal adjacent cells ***/
    public void revealAdjacentCells(int index) {
        // reveal this button
        gameFieldPanel.remove(index);
        String s = gameFieldValues.get(index).toString() + "+";
        gameFieldValues.set(index, -99);
        JButton b = new JButton();
        b.setBackground(Color.LIGHT_GRAY);
        b.setOpaque(true);
//        b.setBorder(BorderFactory.createBevelBorder(0));
        gameFieldPanel.add(b, index);
        gameFieldPanel.validate();
        gameFieldPanel.repaint();

        // get adjacent cells
        ArrayList<Integer> adjacentCells = game.playGame(index);
        System.out.println("Index: " + index);

        // invalid address x = -100
        for(Integer x : adjacentCells) {
            if(x >= 0) {
                if(gameFieldValues.get(x) == 0) {
                    revealAdjacentCells((int) x);
//                    System.out.println("Cell: " + x + " - set value to -99");
//                    gameFieldValues.set(adjacentCells.indexOf(x), -99);

                } else if (gameFieldValues.get(x) > 0){
                    System.out.println("Location: " + x + "\t Value: " + gameFieldValues.get(x) + "\t");
                    gameFieldPanel.remove(x);
                    String str = gameFieldValues.get(x).toString();
                    JButton butt = new JButton(str);
                    butt.setBackground(Color.LIGHT_GRAY);
                    butt.setOpaque(true);
                    butt.setBorder(BorderFactory.createBevelBorder(1));
                    gameFieldPanel.add(butt, (int) x);
                    gameFieldPanel.validate();
                    gameFieldPanel.repaint();
                }
            }
        }

    }

    /*** ACTION: reset minefield (new game) ***/
    public class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Reset battlefield");
            gameFieldPanel.removeAll();
            makeGameField();
            gameFieldPanel.validate();
            gameFieldPanel.repaint();
        }
    }

    /*** ACTION: reveal mines location ***/
    public class ShowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            revealMines();
        }
    }

    /*** reveal mines ***/
    public void revealMines() {
        for(JButton button : gameFieldCells) {
            int index = gameFieldCells.indexOf(button);
            if(gameFieldValues.get(index) == -1) { // bomb
                gameFieldPanel.remove(index);
                JButton b = new JButton("X");
                b.setBackground(Color.BLACK);
                b.setOpaque(true);
                b.setBorder(BorderFactory.createBevelBorder(0));
                gameFieldPanel.add(b, (int) index);
                gameFieldPanel.validate();
                gameFieldPanel.repaint();
            } else if (gameFieldValues.get(index) == 0){
                gameFieldPanel.remove(index);
                JButton b = new JButton();
                b.setBackground(Color.LIGHT_GRAY);
                b.setOpaque(true);

                gameFieldPanel.add(b, index);
                gameFieldPanel.validate();
                gameFieldPanel.repaint();
            } else if (gameFieldValues.get(index) != -99){
                gameFieldPanel.remove(index);
                String str = gameFieldValues.get(index).toString();
                JButton b = new JButton(str);
                b.setBackground(Color.LIGHT_GRAY);
                b.setOpaque(true);
                b.setBorder(BorderFactory.createBevelBorder(1));
                gameFieldPanel.add(b, index);
                gameFieldPanel.validate();
                gameFieldPanel.repaint();
            }
        }
    }
}

package com.andris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Appearence {
    JFrame theFrame;
    JPanel mainPanel;
    JComboBox gameList;


    public void buildGUI() {
        theFrame = new JFrame("GameService");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JLabel label = new JLabel("Select game from drop-down list");
        mainPanel.add(label);

        theFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        Object [] games = GameService.getGameList();
        gameList = new JComboBox(games);

        theFrame.getContentPane().add(BorderLayout.NORTH, gameList);
        // listener
        gameList.addActionListener(new MyGameListListener());

        theFrame.setSize(600,500);
        theFrame.setVisible(true);
    }

    class MyGameListListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // take the selection the user made and load the appropriate service
            Object selected = gameList.getSelectedItem();
            Game game = GameService.getGame(selected);
            mainPanel.removeAll();
            mainPanel.add(game.launch());
            mainPanel.validate();
            mainPanel.repaint();
        }
    }


}

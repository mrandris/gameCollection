package com.andris;

import com.andris.Game;
import com.andris.battleship.Battleship;
import com.andris.minesweeper.Minesweeper;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {
    static HashMap gameservice = new HashMap();

    public static void setup() {
        addGame(new Battleship());
        addGame(new Minesweeper());
    }

    public static void addGame(Game game) {
        gameservice.put(game.getGameName(), game);
    }

    public static Object[] getGameList() {
        Object[] gameList = gameservice.keySet().toArray();
        return gameList;
    }

    public static Game getGame(Object gameKey) {
        Game game = (Game) gameservice.get(gameKey);
        return game;
    }
}

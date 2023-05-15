package com.example.obstaclecoursegame.Models;

import java.util.ArrayList;

public class PlayerList {
    private String name = "";
    private ArrayList<Player> players = new ArrayList<>();

    public PlayerList() {
    }

    public String getName() {
        return name;
    }

    public PlayerList setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public PlayerList setPlayers(ArrayList<Player> players) {
        this.players = players;
        return this;
    }

    @Override
    public String toString() {
        return "PlayerList{" +
                "name='" + name + '\'' +
                ", players=" + players +
                '}';
    }
}

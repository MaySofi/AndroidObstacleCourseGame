package com.example.obstaclecoursegame.Utilities;

import com.example.obstaclecoursegame.Models.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Player> getPlayers() {
        String fromSP = MySPv.getInstance().getString("players","");
        ArrayList<Player> playerListFromJson = new Gson().fromJson(fromSP, new TypeToken<ArrayList<Player>>() {}.getType());
        if(playerListFromJson != null)
            return playerListFromJson;

        ArrayList<Player> playerArrayList = new ArrayList<>();
        playerArrayList.add(new Player().setName("May").setScore(5));
        playerArrayList.add(new Player().setName("Maya").setScore(10));
        playerArrayList.add(new Player().setName("Mai").setScore(15));

        return playerArrayList;
    }
}

package com.example.obstaclecoursegame;

import static com.example.obstaclecoursegame.Utilities.Constants.KEY_NAME;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_SCORE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.obstaclecoursegame.Fragments.ListFragment;
import com.example.obstaclecoursegame.Fragments.MapFragment;
import com.example.obstaclecoursegame.Interfaces.CallBack_SendRecord;
import com.example.obstaclecoursegame.Models.Player;
import com.example.obstaclecoursegame.Utilities.DataManager;
import com.example.obstaclecoursegame.Utilities.MySPv;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private String player = "";
    private int score = -1;

    private final CallBack_SendRecord callBack_sendRecord = new CallBack_SendRecord() {
        @Override
        public void playerChosen(String name) {
            Player player;
            ArrayList<Player> players = DataManager.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                if(players.get(i).getName().equals(name)) {
                    player = players.get(i);
                    mapFragment.zoomOnRecord(player.getLatitude(), player.getLongitude());
                    return;
                }
            }
            showLocationOnMap();
        }
    };

    private void showLocationOnMap() {
        double latitude = 32.4999;
        double longitude = 34.5599;
        mapFragment.zoomOnRecord(latitude, longitude);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent previousIntent = getIntent();
        player = previousIntent.getStringExtra(KEY_NAME);
        score = previousIntent.getIntExtra(KEY_SCORE, -1);

        initRecordFragments();
        beginTransactions();

        handleNewPlayer();
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.record_FRAME_map, mapFragment).commit();
    }

    private void initRecordFragments() {
        listFragment = new ListFragment();
        listFragment.setCallBack_SendRecord(callBack_sendRecord);
        mapFragment = new MapFragment();
    }

    private void handleNewPlayer() {
        ArrayList<Player> playerArrayList = DataManager.getPlayers();
        if(player == null)
            player = "";
        int playerIndex = playerArrayList.indexOf(
                playerArrayList
                        .stream()
                        .filter(
                                player1 -> player
                                        .equals(player1
                                                .getName()))
                        .findFirst()
                        .orElse(null));

        if(playerIndex != -1) {
            LatLng latLng = mapFragment.getCurrentPlayerCoordinates();
            playerArrayList.get(playerIndex).setLatitude(latLng.latitude).setLongitude(latLng.longitude);
        }
        else {
            playerArrayList
                    .add(
                            new Player()
                                    .setName(player)
                                    .setScore(score)
                                    .setLatitude(32.0879315)
                                    .setLongitude(34.797246));
        }
        String playerListJson = new Gson().toJson(playerArrayList);
        MySPv.getInstance().putString("players", playerListJson);
    }
}

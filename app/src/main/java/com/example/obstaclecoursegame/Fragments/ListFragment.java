package com.example.obstaclecoursegame.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.obstaclecoursegame.Adapters.PlayerAdapter;
import com.example.obstaclecoursegame.Interfaces.CallBack_SendRecord;
import com.example.obstaclecoursegame.R;
import com.example.obstaclecoursegame.Utilities.DataManager;
import com.example.obstaclecoursegame.Utilities.SignalGenerator;

public class ListFragment extends Fragment {
    private RecyclerView record_LST_players;
    private CallBack_SendRecord callBack_sendRecord;

    public void setCallBack_SendRecord(CallBack_SendRecord callBack_sendRecord) {
        this.callBack_sendRecord = callBack_sendRecord;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        PlayerAdapter playerAdapter = new PlayerAdapter(DataManager.getPlayers());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        record_LST_players.setAdapter(playerAdapter);
        record_LST_players.setLayoutManager(linearLayoutManager);

        playerAdapter.setPlayerCallback((player, position) -> {
            SignalGenerator.getInstance().toast(player.getName(), Toast.LENGTH_SHORT);
            callBack_sendRecord.playerChosen(player.getName());
        });

        return view;
    }

    private void findViews(View view) {
        record_LST_players = view.findViewById(R.id.record_LST_players);
    }
}

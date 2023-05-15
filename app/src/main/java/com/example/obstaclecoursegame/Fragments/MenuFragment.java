package com.example.obstaclecoursegame.Fragments;

import static com.example.obstaclecoursegame.Interfaces.CallBack_SendClick.Mode.FAST;
import static com.example.obstaclecoursegame.Interfaces.CallBack_SendClick.Mode.SLOW;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obstaclecoursegame.Interfaces.CallBack_SendClick;
import com.example.obstaclecoursegame.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Objects;

public class MenuFragment extends Fragment {
    private AppCompatEditText menu_ET_name;
    private MaterialButton menu_BTN_play;
    private AppCompatToggleButton menu_TB_sensors;
    private CallBack_SendClick callBack_sendClick;
    private MaterialButtonToggleGroup menu_BTG_mode;
    private MaterialButton menu_BTN_slow;
    private MaterialButton menu_BTN_fast;
    private MaterialButton menu_BTN_open_records;
    public void setCallBack_sendClick(CallBack_SendClick callBack_sendClick) {
        this.callBack_sendClick = callBack_sendClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(view);
        menu_TB_sensors.setText(getString(R.string.sensors));
        menu_TB_sensors.setTextOn("Sensors On");
        menu_TB_sensors.setTextOff("Sensors Off");
        menu_BTN_play.setOnClickListener(v -> sendClicked());
        menu_BTN_open_records.setOnClickListener(view1 -> callBack_sendClick.openRecords());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        Objects.requireNonNull(menu_ET_name.getText()).clear();
        menu_TB_sensors.setText(getString(R.string.sensors));
        menu_BTG_mode.clearChecked();
    }

    private void sendClicked() {
        if(callBack_sendClick != null) {
            String name = menu_ET_name.getText() == null ? "" : menu_ET_name.getText().toString();
            callBack_sendClick.userNameChosen(name);

            int id = menu_BTG_mode.getCheckedButtonId();
            CallBack_SendClick.Mode mode;
            if(id == menu_BTN_slow.getId())
                mode = SLOW;
            else if(id == menu_BTN_fast.getId())
                mode = FAST;
            else
                mode = null;
            callBack_sendClick.gameMode(mode);

            Boolean sensorsOn = menu_TB_sensors.isChecked();
            callBack_sendClick.sensorsMode(sensorsOn);

            callBack_sendClick.destroy();
        }
    }

    private void findViews(View view) {
        menu_ET_name = view.findViewById(R.id.menu_ET_name);
        menu_BTN_play = view.findViewById(R.id.menu_BTN_play);
        menu_TB_sensors = view.findViewById(R.id.menu_TB_sensors);
        menu_BTG_mode = view.findViewById(R.id.menu_BTG_mode);
        menu_BTN_slow = view.findViewById(R.id.menu_BTN_slow);
        menu_BTN_fast = view.findViewById(R.id.menu_BTN_fast);
        menu_BTN_open_records = view.findViewById(R.id.menu_BTN_open_records);
    }
}
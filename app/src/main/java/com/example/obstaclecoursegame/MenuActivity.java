package com.example.obstaclecoursegame;

import static com.example.obstaclecoursegame.Utilities.Constants.KEY_DELAY;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_NAME;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_SENSOR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.obstaclecoursegame.Fragments.MenuFragment;
import com.example.obstaclecoursegame.Interfaces.CallBack_SendClick;

public class MenuActivity extends AppCompatActivity {
    private MenuFragment menuFragment;
    private String playerName;
    private Boolean sensors = false;
    private static final int DELAY_SLOW = 1500;
    private static final int DELAY_FAST = 500;
    private int DELAY = 1000;

    private final CallBack_SendClick callBack_sendClick = new CallBack_SendClick() {
        @Override
        public void userNameChosen(String name) {
            playerName = name;
        }

        @Override
        public void gameMode(Mode mode) {
            if(mode != null) {
                switch (mode) {
                    case SLOW:
                        DELAY = DELAY_SLOW;
                        break;
                    case FAST:
                        DELAY = DELAY_FAST;
                        break;
                }
            }
        }

        @Override
        public void sensorsMode(Boolean sensor) {
            sensors = sensor;
        }

        @Override
        public void destroy() {
            openGameScreen(playerName, DELAY, sensors);
        }

        @Override
        public void openRecords() {
            openRecordScreen();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initFragments();
        beginTransactions();
    }

    private void openGameScreen(String player, int delay, Boolean sensor) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_NAME, player);
        intent.putExtra(KEY_DELAY, delay);
        intent.putExtra(KEY_SENSOR, sensor);
        startActivity(intent);
    }

    private void openRecordScreen() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    private void initFragments() {
        menuFragment = new MenuFragment();
        menuFragment.setCallBack_sendClick(callBack_sendClick);
    }

    private void beginTransactions() {
        if(menuFragment != null)
            getSupportFragmentManager().beginTransaction().add(R.id.menu_FRAME_menu, menuFragment).commit();
    }
}

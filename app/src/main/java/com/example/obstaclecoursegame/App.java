package com.example.obstaclecoursegame;

import android.app.Application;

import com.example.obstaclecoursegame.Utilities.MySPv;
import com.example.obstaclecoursegame.Utilities.SignalGenerator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySPv.init(this);
        SignalGenerator.init(this);
    }

}

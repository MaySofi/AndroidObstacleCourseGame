package com.example.obstaclecoursegame.Interfaces;

public interface CallBack_SendClick {
    public static enum Mode{
        SLOW, FAST;
    }
    void userNameChosen(String name);
    void gameMode(Mode mode);
    void sensorsMode(Boolean sensor);
    void destroy();
    void openRecords();
}

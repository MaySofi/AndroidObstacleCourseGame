package com.example.obstaclecoursegame.Logic;

import static com.example.obstaclecoursegame.MainActivity.Type.COIN;

import com.example.obstaclecoursegame.MainActivity;
import com.example.obstaclecoursegame.Utilities.SignalGenerator;

public class GameManager {
    private static final int COIN_SCORE = 10;
    private static final int AVOID_OBSTACLE_SCORE = 1;

    private final int GAME_OVER;
    private int score;
    private int failures;

    public GameManager(int GAME_OVER) {
        this.GAME_OVER = GAME_OVER;
        this.score = 0;
        this.failures = 0;
    }

    public int getScore() {
        return score;
    }

    public int getFailures() {
        return failures;
    }

    public boolean isLose() {
        return GAME_OVER == failures;
    }

    public void checkCollision(int position, int lastRowRockIndex, MainActivity.Type type) {
        if(position == lastRowRockIndex) {
            if(type == COIN) {
                score += COIN_SCORE;
                return;
            }
            String fail = "Failed: " + ++failures;
            SignalGenerator.getInstance().toast(fail, fail.length());
        }
        else
            score += AVOID_OBSTACLE_SCORE;
    }



}

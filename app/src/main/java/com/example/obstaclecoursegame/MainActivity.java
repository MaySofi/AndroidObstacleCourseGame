package com.example.obstaclecoursegame;

import static com.example.obstaclecoursegame.Utilities.Constants.KEY_DELAY;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_NAME;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_SCORE;
import static com.example.obstaclecoursegame.Utilities.Constants.KEY_SENSOR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obstaclecoursegame.Interfaces.RotateCallback;
import com.example.obstaclecoursegame.Logic.GameManager;
import com.example.obstaclecoursegame.Models.Player;
import com.example.obstaclecoursegame.Utilities.DataManager;
import com.example.obstaclecoursegame.Utilities.MySPv;
import com.example.obstaclecoursegame.Utilities.RotationDetector;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String player;
    private long DELAY = 1000;
    private static final int CENTER = 2;
    private static final int LEFT = 0;
    private static final int RIGHT = 4;
    private static final int STOP_TIMER = 1;
    private static final int SUCCESS_REFRESH = 0;
    private static int pos = CENTER;
    private final int COLS = 5;
    private final int ROWS = 8;
    private final ImageView[][] imageViewMatrix = new ImageView[ROWS][COLS];

    private final int[][] visibilityMatrix = new int[ROWS][COLS];
    private final ImageView[][] coinsMatrix = new ImageView[ROWS][COLS];
    private final int[][] coinVisibilityMatrix = new int[ROWS][COLS];
    private ImageButton leftButton, rightButton;
    private int failures = 0;
    private ShapeableImageView[] main_IMG_hearts;
    private ImageView[] imageViewCar;
    private TextView scoreView;
    private GridLayout gridLayout;
    private GridLayout coinsGridLayout;
    private GameManager gameManager;
    private Timer timer;
    private MediaPlayer mediaPlayer;
    private RotationDetector rotationDetector;
    private boolean sensor;
    public enum Type{
        ROCK, COIN
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        Intent previousIntent = getIntent();
        player = previousIntent.getStringExtra(KEY_NAME);
        Log.d("CallBack_SendClick", "userNameChosen: "+player);

        DELAY = previousIntent.getIntExtra(KEY_DELAY, (int) DELAY);
        Log.d("MainActivity", "KEY_DELAY = " + DELAY);

        // TODO: add sensor option.
        sensor = previousIntent.getBooleanExtra(KEY_SENSOR, false);

        gameManager = new GameManager(main_IMG_hearts.length);

        initImageViewMatrix();
        initCoinsMatrix();

        if(sensor)
            activateSensors();
        else {
            leftButton.setOnClickListener(v -> {
                if (!(pos == LEFT)) {
                    pos--;
                    SetActiveLane(imageViewCar, pos);
                }
            });

            rightButton.setOnClickListener(v -> {
                if (!(pos == RIGHT)) {
                    pos++;
                    SetActiveLane(imageViewCar, pos);
                }
            });
        }
    }

    private void activateSensors() {
        // Buttons useless
        leftButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);

        rotationDetector = new RotationDetector(this, new RotateCallback() {
            @Override
            public void rotateRight() {
                if(!(pos == RIGHT)) {
                    pos++;
                    SetActiveLane(imageViewCar, pos);
                }
            }

            @Override
            public void rotateLeft() {
                if(!(pos == LEFT)) {
                    pos--;
                    SetActiveLane(imageViewCar, pos);
                }
            }
        });
    }

    private void savePlayerToSharedPreferences(int score) {
        ArrayList<Player> playerList = DataManager.getPlayers();
        playerList.add(new Player().setName(player).setScore(score));

        String playerListJson = new Gson().toJson(playerList);
        MySPv.getInstance().putString("players", playerListJson);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SetActiveLane(imageViewCar, CENTER);
    }
    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        moveObstacles();

        mediaPlayer = MediaPlayer.create(this, R.raw.crash_sound);
        mediaPlayer.setVolume(1.0f,1.0f);
        if(sensor)
            rotationDetector.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        mediaPlayer.stop();
        if(sensor)
            rotationDetector.stop();
    }

    private void moveObstacles() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    // Check the rock location in the last row
                    int lastRowRockIndex = -1;
                    for (int i = 0; i < COLS; i++) {
                        if(visibilityMatrix[ROWS-1][i] == View.VISIBLE)
                            lastRowRockIndex = i;
                    }
                    gameManager.checkCollision(pos, lastRowRockIndex, Type.ROCK);

                    // The same for coins
                    lastRowRockIndex = -1;
                    for (int i = 0; i < COLS; i++) {
                        if(coinVisibilityMatrix[ROWS-1][i] == View.VISIBLE)
                            lastRowRockIndex = i;
                    }
                    if(lastRowRockIndex != -1)
                        gameManager.checkCollision(pos, lastRowRockIndex, Type.COIN);

                    // Move row down
                    for (int i = (ROWS-1); i > 0; i--) {
                        for (int j = 0; j < COLS; j++) {
                            imageViewMatrix[i][j].setVisibility(visibilityMatrix[i-1][j]);
                            visibilityMatrix[i][j] = visibilityMatrix[i-1][j];

                            coinsMatrix[i][j].setVisibility(coinVisibilityMatrix[i-1][j]);
                            coinVisibilityMatrix[i][j] = coinVisibilityMatrix[i-1][j];
                        }
                    }

                    // Create new row
                    for (ImageView image : imageViewMatrix[0])
                        image.setVisibility(View.INVISIBLE);
                    Arrays.fill(visibilityMatrix[0], View.INVISIBLE);

                    for (ImageView image : coinsMatrix[0])
                        image.setVisibility(View.INVISIBLE);
                    Arrays.fill(coinVisibilityMatrix[0], View.INVISIBLE);

                    Random random = new Random();
                    int randomLane = random.nextInt(COLS);

                    imageViewMatrix[0][randomLane].setVisibility(View.VISIBLE);
                    visibilityMatrix[0][randomLane] = View.VISIBLE;

                    if((gameManager.getScore() % 10) == 1) {
                        int newRandomLane = (randomLane+1) % COLS;
                        coinsMatrix[0][newRandomLane].setVisibility(View.VISIBLE);
                        coinVisibilityMatrix[0][newRandomLane] = View.VISIBLE;
                    }

                    if(STOP_TIMER == refreshUI())
                        timer.cancel();

                });
            }
        }, 0, DELAY);
    }

    private int refreshUI() {
        if(gameManager.isLose()) {
            main_IMG_hearts[main_IMG_hearts.length - gameManager.getFailures()].setVisibility(View.INVISIBLE);
            mediaPlayer.start();
            openRecordScreen(player == null ? "" : player, gameManager.getScore());
            return STOP_TIMER;
        } else if(0 != gameManager.getFailures() && gameManager.getFailures() != failures) {
            failures = gameManager.getFailures();
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                mediaPlayer = MediaPlayer.create(this, R.raw.crash_sound);
            mediaPlayer.start();
            main_IMG_hearts[main_IMG_hearts.length - gameManager.getFailures()].setVisibility(View.INVISIBLE);
            return SUCCESS_REFRESH;
        }
        String scoreSetText = "" + gameManager.getScore();
        scoreView.setText(scoreSetText);
        return SUCCESS_REFRESH;
    }

    private void openRecordScreen(String player, int score) {
        savePlayerToSharedPreferences(score);

        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(KEY_NAME,player);
        intent.putExtra(KEY_SCORE ,score);
        startActivity(intent);
        finish();
    }

    private void SetActiveLane(@NonNull ImageView[] imageViews, int position) {
        for (int pos = 0; pos < imageViews.length; pos++) {
            imageViews[pos].setVisibility(pos != position ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private void findViews() {
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        imageViewCar = new ImageView[]{
                findViewById(R.id.car1),
                findViewById(R.id.car2),
                findViewById(R.id.car3),
                findViewById(R.id.car4),
                findViewById(R.id.car5)};

        gridLayout = findViewById(R.id.road);
        coinsGridLayout = findViewById(R.id.coins);
        leftButton = findViewById(R.id.button_left_bottom);
        rightButton = findViewById(R.id.button_right_bottom);
        scoreView = findViewById(R.id.main_Score);
    }

    private void initImageViewMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                int viewId = gridLayout.getChildAt(index).getId();
                imageViewMatrix[i][j] = findViewById(viewId);
                imageViewMatrix[i][j].setVisibility(View.INVISIBLE);
                visibilityMatrix[i][j] = View.INVISIBLE;
            }
        }
    }

    private void initCoinsMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                int viewId = coinsGridLayout.getChildAt(index).getId();
                coinsMatrix[i][j] = findViewById(viewId);
                coinsMatrix[i][j].setVisibility(View.INVISIBLE);
                coinVisibilityMatrix[i][j] = View.INVISIBLE;
            }
        }
    }
}

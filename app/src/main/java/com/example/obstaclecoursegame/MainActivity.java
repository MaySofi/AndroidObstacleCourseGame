package com.example.obstaclecoursegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obstaclecoursegame.Utilities.SignalGenerator;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int GAME_OVER = 3;
    private static final long DELAY = 1500;
    private static final int CENTER = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 2;
    private static int pos = CENTER;
    private final int COLS = 3;
    private final int ROWS = 6;
    // Create a matrix of ImageView objects
    @SuppressWarnings("FieldMayBeFinal")
    private ImageView[][] imageViewMatrix = new ImageView[ROWS][COLS];
    @SuppressWarnings("FieldMayBeFinal")
    private int[][] visibilityMatrix = new int[ROWS][COLS];

    private static int failures = 0;
    private static int score = 0;
    private ShapeableImageView[] main_IMG_hearts;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridLayout gridLayout;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton leftButton, rightButton;


        leftButton = findViewById(R.id.button_left_bottom);
        rightButton = findViewById(R.id.button_right_bottom);

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        scoreView = findViewById(R.id.main_Score);

        // get reference to the ImageView
        ImageView[] imageViewCar = new ImageView[COLS];
        imageViewCar[0] =  findViewById(R.id.car0);
        imageViewCar[1] =  findViewById(R.id.car);
        imageViewCar[2] =  findViewById(R.id.car2);
        SetActiveLane(imageViewCar, CENTER);

        gridLayout = getWindow().findViewById(R.id.road); // Get road view

        // Initial procedure
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                int viewId = gridLayout.getChildAt(index).getId();
                imageViewMatrix[i][j] = findViewById(viewId);
                imageViewMatrix[i][j].setVisibility(View.INVISIBLE);
                visibilityMatrix[i][j] = View.INVISIBLE;
            }
        }

        leftButton.setOnClickListener(v -> {
            if((pos == CENTER) || (pos == RIGHT)) {
                pos--;
                SetActiveLane(imageViewCar, pos);
            }
            Log.d("BUTTONS", "User tapped the leftButton");
        });

        rightButton.setOnClickListener(v -> {
            if((pos == CENTER) || (pos == LEFT)) {
                pos++;
                SetActiveLane(imageViewCar, pos);
            }
            Log.d("BUTTONS", "User tapped the rightButton");
        });
        moveObstacles();
    }

    private void moveObstacles() {
        Timer timer = new Timer();
        // run by timer
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    // move row down
                    for (int i = (ROWS-1); i > 0; i--) {
                        // first row deleted - invisible
                        // for each element in sub array, copy from the upper sub
                        for (int j = 0; j < COLS; j++) {
                            imageViewMatrix[i][j].setVisibility(visibilityMatrix[i-1][j]);
                            visibilityMatrix[i][j] = visibilityMatrix[i-1][j];
                        }
                    }

                    // create new row
                    for (ImageView image : imageViewMatrix[0])
                        image.setVisibility(View.INVISIBLE);

                    Arrays.fill(visibilityMatrix[0], View.INVISIBLE);

                    // choose random lane
                    Random random = new Random();
                    int randomLane = random.nextInt(COLS);

                    // make the obj in this lane visible
                    imageViewMatrix[0][randomLane].setVisibility(View.VISIBLE);

                    // Update the visibilityMatrix
                    visibilityMatrix[0][randomLane] = View.VISIBLE;

                    // Check the rock location in the last row
                    int lastRowRockIndex = -1;
                    for (int i = 0; i < COLS; i++) {
                        if(visibilityMatrix[ROWS-1][i] == View.VISIBLE)
                            lastRowRockIndex = i;
                    }

                    if(pos == lastRowRockIndex) {
                        String fail = "Failed: " + ++failures;
                        SignalGenerator.getInstance().toast(fail, fail.length());
                        if(failures <= GAME_OVER)
                            main_IMG_hearts[main_IMG_hearts.length - failures].setVisibility(View.INVISIBLE);
                        if(GAME_OVER == failures)
                            timer.cancel();
                    }
                    else
                    {
                        score++;
                        String scoreSetText = "" + score;
                        scoreView.setText(scoreSetText);
                    }

                });
            }
        }, 0, DELAY);

    }

    private void SetActiveLane(@NonNull ImageView[] imageViews, int position) {
        for (int pos = 0; pos < imageViews.length; pos++) {
            imageViews[pos].setVisibility(pos != position ? View.INVISIBLE : View.VISIBLE);
        }
    }
}

package com.example.obstaclecoursegame.Utilities;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;


public class SignalGenerator {
    public static SignalGenerator instance;
    private final Context context;
    private static Vibrator vibrator;

    private SignalGenerator(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SignalGenerator(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static SignalGenerator getInstance() {
        return instance;
    }

    public void toast(String text, int length) {
        Toast
                .makeText(context, text, length)
                .show();
    }

    public void vibrate() {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}

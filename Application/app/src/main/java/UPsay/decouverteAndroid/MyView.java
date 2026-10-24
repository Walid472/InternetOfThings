package UPsay.decouverteAndroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

public class MyView extends View {
    float taille = 0;
    float xText = 600, yText = 600;
    float x1, x2, y1, y2;

    // Accéléromètre
    private int pencheH, pencheV, penche;
    Sensor accelerometre;

    Handler timerHandler = new Handler();
    Runnable updateTimerThread = new Runnable() { public void run() { timerHandler.postDelayed(this, 100); invalidate(); } };

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        timerHandler.postDelayed(updateTimerThread, 10);

        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xText = event.getX();
                yText = event.getY();

                float dx, dy;
                String direction;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        Log.i("monAPP", "appuyé");
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        dx = x2 - x1;
                        dy = y2 - y1;
                        if (Math.abs(dx) > Math.abs(dy)) {
                            direction = (dx > 0) ? "right" : "left";
                        } else {
                            direction = (dy > 0) ? "down" : "up";
                        }
                        Log.i("monAPP", "laché " + direction);
                        Log.i("monAPP", "dx = " + dx + "; dy = " + dy);
                        break;
                }

                invalidate();
                return true;
            }
        };
        setOnTouchListener(onTouchListener);

        // Accéléromètre : récupération du capteur
        SensorManager m = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometre = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Accéléromètre : réaction aux changements
        final SensorEventListener mSensorEventListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                pencheH = -(int) (sensorEvent.values[0]);
                pencheV = (int) (sensorEvent.values[1]);
                penche = pencheH * pencheH + pencheV * pencheV;
                invalidate();
            }
        };
        m.registerListener(mSensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint p = new Paint();

        // 1. Fond noir
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        // 2. Image par-dessus le fond
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bunny_holding_carrot);
        canvas.drawBitmap(b, 200, 200, null);

        // 3. Texte par-dessus tout, qui grandit, suit le toucher et réagit à l'inclinaison
        if (taille < 300) {
            taille++;
        }
        p.setColor(Color.GREEN);
        p.setTextSize(taille);
        p.setTextAlign(Paint.Align.CENTER);
        String texte = "Bonjour MONDE";
        canvas.drawText(texte, xText + pencheH * 20, yText + pencheV * 20, p);
    }
}
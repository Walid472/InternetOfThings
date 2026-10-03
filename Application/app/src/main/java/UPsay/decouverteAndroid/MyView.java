package UPsay.decouverteAndroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;

public class MyView extends View {
    float taille = 0;
    Handler timerHandler = new Handler();
    Runnable updateTimerThread = new Runnable() { public void run() { timerHandler.postDelayed(this, 100); invalidate(); } };
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        timerHandler.postDelayed(updateTimerThread, 10);
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

        // 3. Texte par-dessus tout, qui grandit à chaque tick du timer
        if (taille < 300) {
            taille++;
        }
        p.setColor(Color.GREEN);
        p.setTextSize(taille);
        p.setTextAlign(Paint.Align.CENTER);
        String texte = "Bonjour MONDE";
        canvas.drawText(texte, (float) getWidth() / 2, (float) getHeight() / 2, p);
    }
}
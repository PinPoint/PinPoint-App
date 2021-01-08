package de.pinpoint.app.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import de.pinpoint.app.PinPoint;
import de.pinpoint.app.R;

public class MarkerIconCreator {
    private String name;
    private String color = "#f44336";
    private int size = 150;

    public BitmapDrawable createIcon(Context context) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(42);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.LEFT);

        final int width = (int) (textPaint.measureText(name) + 0.5f);


        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_person_pin);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color));


        wrappedDrawable.setBounds(0, 20, size-20, size);

        Bitmap image = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        wrappedDrawable.draw(canvas);
        canvas.drawText(name, 0, 30, textPaint);

        Paint markerPaint = new Paint();
        return new BitmapDrawable(context.getResources(), image);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

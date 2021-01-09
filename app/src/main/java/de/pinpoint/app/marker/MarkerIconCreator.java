package de.pinpoint.app.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import de.pinpoint.app.R;

public class MarkerIconCreator {
    private String name;
    private String color = "#f44336";
    private int size = 125;

    public BitmapDrawable createIcon(Context context) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(42);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.LEFT);

        /* Bounds of the text */
        int textHeight = 30;
        int textWidth = (int) textPaint.measureText(name);

        /* Bounds of the marker image */
        int imageHeight = size;
        int imageWidth = size;
        int imageX = Math.max(0, (textWidth - imageWidth) / 2);

        /* Calculate Bounds of the total image*/
        int totalHeight = textHeight + imageHeight;
        int totalWidth = Math.max(imageWidth, textWidth);

        /* Create icon Bitmap Canvas */
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        /* Draw colored marker image on Canvas */
        Drawable coloredImage = this.getColoredMarker(context);
        coloredImage.setBounds(imageX, textHeight, imageX + size, size + textHeight);
        coloredImage.draw(canvas);

        /* Draw text on canvas*/
        int textX = Math.max(0, (imageWidth - textWidth) / 2);
        canvas.drawText(name, textX, textHeight, textPaint);

        /* Return Bitmap as BitMap Drawable*/
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private Drawable getColoredMarker(Context context) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_person_pin);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color));
        return wrappedDrawable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

package com.teched.smartread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.graphics.Palette;

public class Utils {
    public static int calculateColorBase(String name) {
        String opacity = "#ff";
        String hexColor = String.format(
                opacity + "%06X", (0xeeeeee & name.hashCode()));

        return Color.parseColor(hexColor);
    }

    public static int calculateColor(String name) {

        ShapeDrawable drawable = new ShapeDrawable(new RectShape());

        drawable.getPaint().setColor(calculateColorBase(name));
        drawable.setIntrinsicHeight(2);
        drawable.setIntrinsicWidth(2);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        Palette palette = Palette.generate(bitmap);

        return palette.getVibrantColor(0xff00bcd4);
    }
    public static String firstChar(String str) {
        for(int i = 0; i<str.length();i++) {
            if(Character.isLetter(str.charAt(i))) {
                return str.substring(i,i+1);
            }
        }
        return " ";
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 360;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}

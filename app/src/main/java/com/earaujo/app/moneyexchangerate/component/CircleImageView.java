package com.earaujo.app.moneyexchangerate.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Eduardo on 12/10/2016.
 */

public class CircleImageView extends ImageView {

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable d = getDrawable();

        if(d == null) {
            return;
        }

        if(d.getIntrinsicHeight() == 0 || d.getIntrinsicWidth() == 0) {
            return;
        }

        int radius = getWidth();
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

        Bitmap b = createCroppedBitmap(radius, bitmap);
        canvas.drawBitmap(b, 0, 0, null);
    }

    private Bitmap createCroppedBitmap(int radius, Bitmap bitmap) {

        if(radius != bitmap.getWidth() || radius != bitmap.getHeight()) {
            bitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        }

        Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cvs = new Canvas(b);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setFilterBitmap(true);

        cvs.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cvs.drawBitmap(bitmap, rect, rect, paint);
        return b;
    }

}

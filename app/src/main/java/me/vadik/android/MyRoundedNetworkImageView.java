package me.vadik.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;

import com.android.volley.toolbox.NetworkImageView;

/**
 * User: vadik
 * Date: 4/12/16
 */
public class MyRoundedNetworkImageView extends NetworkImageView {
    public MyRoundedNetworkImageView(Context context) {
        super(context);
    }

    public MyRoundedNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRoundedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable != null && drawable instanceof BitmapDrawable) {

            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();

            if (b != null) { //TODO understand this check
                Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

                int w = getWidth(), h = getHeight();

                Bitmap roundBitmap = RoundedImageView.getRoundedCroppedBitmap(bitmap, w);
                canvas.drawBitmap(roundBitmap, 0, 0, null);
            } else {
                Log.w("me", "bitmap is null");
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }
}

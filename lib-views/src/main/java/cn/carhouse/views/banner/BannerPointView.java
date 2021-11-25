package cn.carhouse.views.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 轮播的点
 */

public class BannerPointView extends View {
    private Paint mPaint = new Paint();
    private int mColor;
    private float mRadius = 1;

    public BannerPointView(Context context) {
        this(context, null);
    }

    public BannerPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mColor != 0) {
            RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            mPaint.setAntiAlias(true);
            mPaint.setColor(mColor);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
    }


    public void setColorAndRadius(int color, float radius) {
        this.mColor = color;
        this.mRadius = radius;
        invalidate();
    }


}

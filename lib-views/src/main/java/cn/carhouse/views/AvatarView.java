package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义头像圆角View
 */
public class AvatarView extends View {
    private Bitmap mAvatarBitmap;
    private int mWidth;
    private Paint mPaint;
    private PorterDuffXfermode mXDuffMode;
    // 图片资源ID
    private int mAvatarResId = 0;
    // 边的颜色
    private int mStoreColor = Color.WHITE;
    // 边的宽度
    private int mStoreWidth = 0;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarView);
        mAvatarResId = array.getResourceId(R.styleable.AvatarView_avatarResId, mAvatarResId);
        mStoreColor = array.getColor(R.styleable.AvatarView_avatarStoreColor, mStoreColor);
        mStoreWidth = (int) array.getDimension(R.styleable.AvatarView_avatarStoreWidth, mStoreWidth);
        array.recycle();
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXDuffMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mPaint.setColor(mStoreColor);
        if (mAvatarResId != 0) {
            mAvatarBitmap = getAvatarBitmap(mAvatarResId);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAvatarBitmap == null) {
            return;
        }
        // 1. 如果要边，先绘制一个圆
        if (mStoreWidth > 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        }
        // 2. 离屏缓冲
        int saveLayer = canvas.saveLayer(new RectF(0, 0, getWidth(), getHeight()), mPaint, Canvas.ALL_SAVE_FLAG);

        // 3. 绘制圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mStoreWidth, mPaint);

        // 4. 设置Xfermode
        mPaint.setXfermode(mXDuffMode);

        // 5. 绘制Bitmap
        canvas.drawBitmap(mAvatarBitmap, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(saveLayer);
    }


    /**
     * 设置图片
     */
    public void setAvatarBitmap(final Bitmap avatarBitmap) {
        if (avatarBitmap == null) {
            return;
        }
        mAvatarBitmap = fitBitmap(avatarBitmap, mWidth);
        postInvalidate();
    }

    /**
     * 设置图片
     */
    public void setAvatarResId(int resId) {
        this.mAvatarResId = resId;
        mAvatarBitmap = getAvatarBitmap(mAvatarResId);
        postInvalidate();
    }

    /**
     * 根据资源ID缩放Bitmap
     */
    private Bitmap getAvatarBitmap(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, opts);
        opts.inJustDecodeBounds = false;
        opts.inDensity = opts.outWidth;
        opts.inTargetDensity = mWidth;
        return BitmapFactory.decodeResource(getResources(), resId, opts);
    }

    /**
     * 根据宽来缩放图片
     */
    private Bitmap fitBitmap(Bitmap target, int newWidth) {
        if (target == null) {
            return null;
        }
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix, true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;
    }
}

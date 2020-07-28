package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 这个是宽度平分控件
 */

public class WidthEqualsLayout extends FrameLayout {

    /**
     * 默认是占一份
     */
    private int mCount = 1;
    // 调度是平分宽度后的调度
    private boolean heightEquals;


    public WidthEqualsLayout(@NonNull Context context) {
        this(context, null);
    }

    public WidthEqualsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WidthEqualsLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WidthEqualsLayout);
        mCount = array.getInt(R.styleable.WidthEqualsLayout_width_counts, mCount);
        heightEquals = array.getBoolean(R.styleable.WidthEqualsLayout_heightEquals, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) / mCount;
        if (heightEquals) {
            setMeasuredDimension(width, width);
        } else {
            setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
        }
    }
}

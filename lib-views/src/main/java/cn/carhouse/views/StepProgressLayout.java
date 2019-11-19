package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 物流进度的View
 */
public class StepProgressLayout extends ConstraintLayout {
    private int mFirstImage;
    private int mFirstImageSize;
    private int mNormalImage;
    private int mNormalImageSize;
    private int mLineSize;
    private int mLineColor = Color.parseColor("#e6e6e6");
    private int mFirstLineHeight, mNormalLineHeight;
    private int mStepStyle = STYLE_NORMAL;// 进度样式
    private static final int STYLE_FIRST = 0;//  第一个条目样式
    private static final int STYLE_NORMAL = 1;//  正常条目样式
    private static final int STYLE_END = 2;//  最后一条条目样式

    private View mTopLine, mBottomLine;
    private ImageView mIcon;

    public StepProgressLayout(@NonNull Context context) {
        this(context, null);
    }

    public StepProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 加载布局
        inflate(context, R.layout.layout_view_step, this);
        // 加载自定义的属性
        obtainStyledAttributes(context, attrs);
        // 初始化View
        initViews();
    }


    private void obtainStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepProgressLayout);
        mFirstImage = array.getResourceId(R.styleable.StepProgressLayout_step_first_image, 0);
        mFirstImageSize = (int) array.getDimension(R.styleable.StepProgressLayout_step_first_image_size, dip2px(20));
        mNormalImage = array.getResourceId(R.styleable.StepProgressLayout_step_normal_image, 0);
        mNormalImageSize = (int) array.getDimension(R.styleable.StepProgressLayout_step_normal_image_size, dip2px(11));
        mLineSize = (int) array.getDimension(R.styleable.StepProgressLayout_step_line_size, 1);
        mLineColor = array.getColor(R.styleable.StepProgressLayout_step_line_color, mLineColor);
        mFirstLineHeight = (int) array.getDimension(R.styleable.StepProgressLayout_step_first_line_height, dip2px(17));
        mNormalLineHeight = (int) array.getDimension(R.styleable.StepProgressLayout_step_normal_line_height, dip2px(17));
        array.recycle();
    }

    private void initViews() {
        mTopLine = findViewById(R.id.step_top_line);
        mBottomLine = findViewById(R.id.step_bottom_line);
        mIcon = findViewById(R.id.iv_icon);

        if (mStepStyle == STYLE_FIRST) {
            setImageResource(mFirstImage);
            setImageSize(mFirstImageSize);
            mTopLine.setVisibility(INVISIBLE);
        } else if (mStepStyle == STYLE_NORMAL || mStepStyle == STYLE_END) {
            setImageResource(mNormalImage);
            setImageSize(mNormalImageSize);
            if (mStepStyle == STYLE_END) {
                mBottomLine.setVisibility(INVISIBLE);
            }
        }
        setTopLineHeight(mFirstLineHeight);
        setLineColor(mLineColor);
        setLineSize(mLineSize);

    }

    public void setLineColor(int color) {
        if (color != 0) {
            mBottomLine.setBackgroundColor(color);
            mTopLine.setBackgroundColor(color);
        }
    }

    /**
     * 设置第一条线第一个条目的高度
     */
    public void setFirstLineHeight(int height) {
        this.mFirstLineHeight = height;
    }

    /**
     * 设置第一条线正常样式的高度
     */
    public void setNormalLineHeight(int height) {
        this.mNormalLineHeight = height;
    }

    public void setTopLineHeight(int height) {
        if (height != 0) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mTopLine.getLayoutParams();
            params.height = height;
            mTopLine.setLayoutParams(params);
        }
    }

    /**
     * 设置线的大小
     */
    public void setLineSize(int width) {
        this.mLineSize = width;
        if (this.mLineSize != 0) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mTopLine.getLayoutParams();
            params.width = width;
            mTopLine.setLayoutParams(params);

            params = (ConstraintLayout.LayoutParams) mBottomLine.getLayoutParams();
            params.width = width;
            mBottomLine.setLayoutParams(params);
        }
    }


    /**
     * 设置第一个图片资源
     */
    public void setHeadImageSize(int size) {
        this.mFirstImageSize = size;
        setImageSize(this.mFirstImageSize);
    }


    private void setImageResource(int resource) {
        if (resource != 0) {
            mIcon.setImageResource(resource);
        }
    }

    private void setImageSize(int size) {
        if (size >= 0) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mIcon.getLayoutParams();
            params.height = size;
            params.width = size;
            mIcon.setLayoutParams(params);
        }
    }

    /**
     * 变成第一个条目样式
     */
    public void changeFirstStyle() {
        if (mStepStyle != STYLE_FIRST) {
            mStepStyle = STYLE_FIRST;
            mTopLine.setVisibility(INVISIBLE);
            setImageResource(mFirstImage);
            setHeadImageSize(mFirstImageSize);
            setTopLineHeight(mFirstLineHeight);
        }
    }

    /**
     * 变成正常条目样式
     */
    public void changeNormalStyle() {
        if (mStepStyle != STYLE_NORMAL) {
            mStepStyle = STYLE_NORMAL;
            mBottomLine.setVisibility(VISIBLE);
            mTopLine.setVisibility(VISIBLE);
            setImageResource(mNormalImage);
            setImageSize(mNormalImageSize);
            setTopLineHeight(mFirstLineHeight);
        }
    }

    /**
     * 最后一种样式
     */
    public void changeEndStyle() {
        if (mStepStyle != STYLE_END) {
            mStepStyle = STYLE_END;
            mTopLine.setVisibility(VISIBLE);
            mBottomLine.setVisibility(INVISIBLE);
            setImageResource(mNormalImage);
            setImageSize(mNormalImageSize);
            setTopLineHeight(mFirstLineHeight);
        }
    }

    public void setBottomLineVisible(int visible) {
        mBottomLine.setVisibility(visible);
    }

    private int dip2px(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()) + 0.5);
    }
}

package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 自定义布局
 */
public class XTextLayout extends FrameLayout {
    private TextView mTvLeft, mTvRight;
    private View mViewLine;
    private Drawable mLeftIcon, mRightIcon;
    private int mLeftTextColor, mRightTextColor;
    private int mLeftTextSize, mRightTextSize;
    private int mLeftMargin, mRightMargin;
    private int mLeftPadding, mRightPadding;
    private int mLineColor, mLineHeight, mLineLeftMargin, mLineRightMargin;
    private boolean mShowLine;
    private String mLeftText, mRightText;


    public XTextLayout(Context context) {
        this(context, null);
    }

    public XTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
        initAttrs(context, attrs);
        initViews();
    }

    private final void initLayout(Context context) {
        inflate(context, R.layout.layout_view_text, this);
        mTvLeft = findViewById(R.id.tvLeft);
        mTvRight = findViewById(R.id.tvRight);
        mViewLine = findViewById(R.id.viewLine);
    }

    private final void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTextLayout);
        mLeftIcon = array.getDrawable(R.styleable.XTextLayout_textLeftIcon);
        mRightIcon = array.getDrawable(R.styleable.XTextLayout_textLeftIcon);
        mLeftTextColor = array.getColor(R.styleable.XTextLayout_textLeftTextColor, Color.parseColor("#333333"));
        mRightTextColor = array.getColor(R.styleable.XTextLayout_textRightTextColor, Color.parseColor("#999999"));
        mLeftTextSize = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftTextSize, dp2px(16));
        mRightTextSize = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightTextSize, dp2px(16));
        mLeftMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftMargin, 0);
        mRightMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightMargin, 0);
        mLeftPadding = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftIconPadding, 0);
        mRightPadding = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightIconPadding, 0);
        mLeftText = array.getString(R.styleable.XTextLayout_textLeftText);
        mRightText = array.getString(R.styleable.XTextLayout_textRightText);

        mLineColor = array.getColor(R.styleable.XTextLayout_textLineColor, Color.parseColor("#cccccc"));
        mLineHeight = array.getDimensionPixelSize(R.styleable.XTextLayout_textLineHeight, 0);
        mLineLeftMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textLineLeftMargin, 0);
        mLineRightMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textLineRightMargin, 0);
        mShowLine = array.getBoolean(R.styleable.XTextLayout_textShowLine, false);
        array.recycle();
    }

    private final void initViews() {
        // 左边
        if (mLeftIcon != null) {
            mTvLeft.setCompoundDrawablesWithIntrinsicBounds(mLeftIcon, null, null, null);
        }
        if (mLeftPadding > 0) {
            mTvLeft.setCompoundDrawablePadding(mLeftPadding);
        }
        if (mLeftTextSize > 0) {
            mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        }
        mTvLeft.setTextColor(mLeftTextColor);
        if (mLeftMargin > 0) {
            FrameLayout.LayoutParams params = (LayoutParams) mTvLeft.getLayoutParams();
            params.leftMargin = mLeftMargin;
            mTvLeft.setLayoutParams(params);
        }
        setLeftText(mLeftText);
        // 右边
        if (mRightIcon != null) {
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightIcon, null);
        }
        if (mRightPadding > 0) {
            mTvRight.setCompoundDrawablePadding(mRightPadding);
        }
        if (mRightTextSize > 0) {
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        }
        mTvRight.setTextColor(mRightTextColor);
        if (mRightMargin > 0) {
            FrameLayout.LayoutParams params = (LayoutParams) mTvRight.getLayoutParams();
            params.rightMargin = mRightMargin;
            mTvRight.setLayoutParams(params);
        }
        setRightText(mRightText);
        // 线
        if (mShowLine) {
            mViewLine.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (LayoutParams) mViewLine.getLayoutParams();
            params.height = mLineHeight;
            params.leftMargin = mLineLeftMargin;
            params.rightMargin = mLineRightMargin;
            mViewLine.setBackgroundColor(mLineColor);
        }
    }

    public final void setLeftText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTvLeft.setText(text);
    }

    public final void setLeftTextColor(int color) {
        if (color != 0) {
            mTvLeft.setTextColor(color);
        }
    }

    public final void setLeftTextSize(int size) {
        if (size > 0) {
            mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    public final void setLeftIcon(int drawable) {
        Drawable icon = getResources().getDrawable(drawable);
        if (icon != null) {
            mTvLeft.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        }
    }

    public final void setLeftIconPadding(int padding) {
        if (padding > 0) {
            mTvLeft.setCompoundDrawablePadding(padding);
        }
    }


    public final void setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTvRight.setText(text);
    }

    public final void setRightTextColor(int color) {
        if (color != 0) {
            mTvRight.setTextColor(color);
        }
    }

    public final void setRightTextSize(int size) {
        if (size > 0) {
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    public final void setRightIcon(int drawable) {
        Drawable icon = getResources().getDrawable(drawable);
        if (icon != null) {
            mTvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        }
    }

    public final void setRightIconPadding(int padding) {
        if (padding > 0) {
            mTvRight.setCompoundDrawablePadding(padding);
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

}

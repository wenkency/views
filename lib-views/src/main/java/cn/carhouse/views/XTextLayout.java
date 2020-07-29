package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义布局
 */
public class XTextLayout extends FrameLayout {
    private ImageView mIvLeft, mIvRight, mIvRightTwo;
    private TextView mTvLeft, mTvRight;
    private View mViewLine;
    private int mLeftIcon, mRightIcon, mRightTwoIcon;
    private int mLeftIconWidth, mRightIconWidth, mRightIconTwoWidth;
    private int mLeftTextColor, mRightTextColor;
    private int mLeftTextSize, mRightTextSize;
    private int mLeftMargin, mRightMargin;
    private int mLeftPadding, mRightPadding, mRightTwoPadding;
    private int mLineColor, mLineHeight, mLineLeftMargin, mLineRightMargin;
    private int mTextLeftTextLines;
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
        mIvLeft = findViewById(R.id.ivLeft);
        mIvRight = findViewById(R.id.ivRight);
        mIvRightTwo = findViewById(R.id.ivRightTwo);
    }

    private final void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTextLayout);
        mLeftIcon = array.getResourceId(R.styleable.XTextLayout_textLeftIcon, 0);
        mRightIcon = array.getResourceId(R.styleable.XTextLayout_textRightIcon, 0);
        mRightTwoIcon = array.getResourceId(R.styleable.XTextLayout_textRightTwoIcon, 0);
        mLeftTextColor = array.getColor(R.styleable.XTextLayout_textLeftTextColor, Color.parseColor("#333333"));
        mRightTextColor = array.getColor(R.styleable.XTextLayout_textRightTextColor, Color.parseColor("#999999"));
        mLeftTextSize = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftTextSize, dp2px(16));
        mTextLeftTextLines = array.getInt(R.styleable.XTextLayout_textLeftTextLines, 1);
        mRightTextSize = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightTextSize, dp2px(16));
        mLeftIconWidth = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftIconWidth, LayoutParams.WRAP_CONTENT);
        mRightIconWidth = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightIconWidth, LayoutParams.WRAP_CONTENT);
        mRightIconTwoWidth = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightIconTwoWidth, LayoutParams.WRAP_CONTENT);

        mLeftMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftMargin, 0);
        mRightMargin = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightMargin, 0);
        mLeftPadding = array.getDimensionPixelSize(R.styleable.XTextLayout_textLeftIconPadding, 0);
        mRightPadding = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightIconPadding, 0);
        mRightTwoPadding = array.getDimensionPixelSize(R.styleable.XTextLayout_textRightIconTwoPadding, 0);
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
        setLeftIcon(mLeftIcon);
        // 设置图片大小和左边距
        setLeftIconWidthPadding();
        if (mLeftTextSize > 0) {
            mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        }
        mTvLeft.setTextColor(mLeftTextColor);
        mTvLeft.setLines(mTextLeftTextLines);
        if (mLeftMargin > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvLeft.getLayoutParams();
            params.leftMargin = mLeftMargin;
            mTvLeft.setLayoutParams(params);
        }
        setLeftText(mLeftText);
        // 右边
        if (mRightIcon != 0) {
            mIvRight.setImageResource(mRightIcon);
        }
        setRightIconWidthPadding();
        if (mRightTextSize > 0) {
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        }
        mTvRight.setTextColor(mRightTextColor);
        if (mRightMargin > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvRight.getLayoutParams();
            params.rightMargin = mRightMargin;
            mTvRight.setLayoutParams(params);
        }
        setRightText(mRightText);
        // 右边第二个图片
        if (mRightTwoIcon != 0) {
            mIvRightTwo.setImageResource(mRightTwoIcon);
        }
        if (mRightTwoPadding > 0 || mRightIconTwoWidth > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvRightTwo.getLayoutParams();
            params.rightMargin = mRightTwoPadding;
            params.width = mRightIconTwoWidth;
            mIvRightTwo.setLayoutParams(params);
        }
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

    public final void setLeftIcon(int resId) {
        mLeftIcon = resId;
        if (mLeftIcon != 0) {
            mIvLeft.setImageResource(mLeftIcon);
        }
    }

    public final void setLeftIconWidth(int width) {
        mLeftIconWidth = width;
        setLeftIconWidthPadding();
    }

    public final void setLeftIconPadding(int padding) {
        mLeftPadding = padding;
        setLeftIconWidthPadding();
    }

    private final void setLeftIconWidthPadding() {
        if (mLeftIconWidth > 0 || mLeftPadding > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvLeft.getLayoutParams();
            params.leftMargin = mLeftPadding;
            params.width = mLeftIconWidth;
            mIvLeft.setLayoutParams(params);
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

    public final void setRightIcon(int resId) {
        mRightIcon = resId;
        if (mRightIcon != 0) {
            mIvRight.setImageResource(resId);
        }
    }

    public final void setRightIconWidth(int width) {
        mRightIconWidth = width;
        setRightIconWidthPadding();
    }

    public final void setRightIconPadding(int padding) {
        mRightPadding = padding;
        setRightIconWidthPadding();
    }

    private final void setRightIconWidthPadding() {
        if (mRightPadding > 0 || mRightIconWidth > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvRight.getLayoutParams();
            params.rightMargin = mRightPadding;
            params.width = mRightIconWidth;
            mIvRight.setLayoutParams(params);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    public ImageView getIvLeft() {
        return mIvLeft;
    }

    public ImageView getIvRight() {
        return mIvRight;
    }

    public ImageView getIvRightTwo() {
        return mIvRightTwo;
    }

    public TextView getTvLeft() {
        return mTvLeft;
    }

    public TextView getTvRight() {
        return mTvRight;
    }
}

package cn.carhouse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 从左到右：ImageView EditText ImageView ImageView ImageView
 */
public class XEditLayout extends ConstraintLayout implements TextWatcher, View.OnFocusChangeListener {
    public static int CLEAR_ICON = R.drawable.x_edit_clear_icon;
    private ImageView mIvLeftIcon, mIvRightClearIcon, mIvRightIcon, mIvRightTwoIcon;
    private EditText mEtContent;
    private View mViewLine;
    private int mLeftIcon, mLeftWidth;
    private int mRightClearIcon, mRightClearWidth;
    private int mRightIcon, mRightWidth;
    private int mRightTwoIcon, mRightTwoWidth;
    private String mText, mHint;
    private int mTextColor, mTextHintColor;
    private float mTextSize;
    private boolean mLineVisible;
    private int mLineSize, mLineColor, mLineFocusColor;

    public XEditLayout(Context context) {
        this(context, null);
    }

    public XEditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 加载布局
        inflate(context, R.layout.layout_view_edit, this);
        // 加载自定义的属性
        initAttributes(context, attrs);
        initViews();
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XEditLayout);
        mLeftIcon = array.getResourceId(R.styleable.XEditLayout_edit_left_icon, 0);
        mLeftWidth = (int) array.getDimension(R.styleable.XEditLayout_edit_left_width, 0);
        mRightClearIcon = array.getResourceId(R.styleable.XEditLayout_edit_right_clear_icon, CLEAR_ICON);
        mRightClearWidth = (int) array.getDimension(R.styleable.XEditLayout_edit_right_clear_width, dip2px(35));
        mRightIcon = array.getResourceId(R.styleable.XEditLayout_edit_right_icon, 0);
        mRightWidth = (int) array.getDimension(R.styleable.XEditLayout_edit_right_width, 0);
        mRightTwoIcon = array.getResourceId(R.styleable.XEditLayout_edit_right_two_icon, 0);
        mRightTwoWidth = (int) array.getDimension(R.styleable.XEditLayout_edit_right_two_width, 0);
        mText = array.getString(R.styleable.XEditLayout_editText);
        mHint = array.getString(R.styleable.XEditLayout_edit_hint);
        mTextSize = array.getDimension(R.styleable.XEditLayout_edit_size, dip2px(16));
        mTextColor = array.getColor(R.styleable.XEditLayout_edit_color, Color.parseColor("#333333"));
        mTextHintColor = array.getColor(R.styleable.XEditLayout_edit_hint_color, Color.parseColor("#cccccc"));
        mLineVisible = array.getBoolean(R.styleable.XEditLayout_edit_line_visible, false);
        mLineSize = array.getDimensionPixelSize(R.styleable.XEditLayout_edit_line_height, 0);
        mLineColor = array.getColor(R.styleable.XEditLayout_edit_line_color, Color.parseColor("#999999"));
        mLineFocusColor = array.getColor(R.styleable.XEditLayout_edit_line_color, Color.parseColor("#dd2828"));
        array.recycle();
    }

    private void initViews() {
        mIvLeftIcon = findViewById(R.id.iv_left_icon);
        mIvRightClearIcon = findViewById(R.id.iv_right_clear_icon);
        mIvRightIcon = findViewById(R.id.iv_right_icon);
        mIvRightTwoIcon = findViewById(R.id.iv_right_two_icon);
        mEtContent = findViewById(R.id.et_content);
        mViewLine = findViewById(R.id.v_line);
        mEtContent.addTextChangedListener(this);
        mEtContent.setOnFocusChangeListener(this);
        // 1. 重新设置左边Icon宽度
        if (mLeftWidth > 0) {
            resetView(mIvLeftIcon, mLeftWidth);
        }
        if (mLeftIcon != 0) {
            mIvLeftIcon.setImageResource(mLeftIcon);
        }
        // 2. 重新设置清除Icon宽度
        mIvRightClearIcon.setImageResource(mRightClearIcon);
        resetView(mIvRightClearIcon, mRightClearWidth);
        // 3. 重新设置右边第一个Icon宽度
        if (mRightWidth > 0) {
            resetView(mIvRightIcon, mRightWidth);
        }
        if (mRightIcon != 0) {
            mIvRightIcon.setImageResource(mRightIcon);
        }
        // 4. 重新设置右边第二个Icon宽度
        if (mRightTwoWidth > 0) {
            resetView(mIvRightTwoIcon, mRightTwoWidth);
        }
        if (mRightTwoIcon != 0) {
            mIvRightTwoIcon.setImageResource(mRightTwoIcon);
        }

        mIvRightClearIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditText("");
            }
        });


        // 设置EditText信息
        setEditText(mText);
        setEditHint(mHint);
        setEditTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        setEditTextColor(mTextColor);
        setEditTextHintColor(mTextHintColor);

        // 设置线的信息
        setLineVisible(mLineVisible);
        setLineSize(mLineSize);
        setLineColor(mLineColor);
    }

    public final void setLineVisible(boolean visible) {
        if (visible) {
            mViewLine.setVisibility(View.VISIBLE);
        } else {
            mViewLine.setVisibility(View.GONE);
        }
    }

    public final void setLineSize(int lineSize) {
        if (lineSize <= 0) {
            return;
        }
        ViewGroup.LayoutParams params = mViewLine.getLayoutParams();
        params.height = lineSize;
        mViewLine.setLayoutParams(params);
    }

    public final void setLineColor(int lineColor) {
        if (lineColor == 0) {
            return;
        }
        mViewLine.setBackgroundColor(lineColor);
    }

    public final void setEditTextColor(int color) {
        if (color == 0) {
            return;
        }
        mEtContent.setTextColor(color);
    }

    public final void setEditTextHintColor(int color) {
        if (color == 0) {
            return;
        }
        mEtContent.setHintTextColor(color);
    }

    public final void setEditTextSize(int unit, float textSize) {
        mEtContent.setTextSize(unit, textSize);
    }

    public final void setEditTextSize(float textSize) {
        setEditTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }


    public final void setEditHint(String hint) {
        if (hint != null) {
            mEtContent.setHint(hint);
        }
    }

    private void resetView(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }


    private int dip2px(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()) + 0.5);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        changeClearIcon(charSequence.length() > 0);
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean focus) {
        if (focus) {
            changeClearIcon(mEtContent.getText().toString().length() > 0);
        } else {
            changeClearIcon(false);
        }
        changeLineFocus(focus);
    }

    /**
     * 清除按钮是否可见
     */
    public final void changeClearIcon(boolean isVisible) {
        if (isVisible && mEtContent.isFocused()) {
            mIvRightClearIcon.setVisibility(View.VISIBLE);
            mIvRightClearIcon.setEnabled(true);
        } else {
            mIvRightClearIcon.setVisibility(View.INVISIBLE);
            mIvRightClearIcon.setEnabled(false);
        }
    }

    /**
     * 线的状态
     */
    public final void changeLineFocus(boolean focus) {
        if (!mLineVisible) {
            return;
        }
        if (focus) {
            setLineColor(mLineFocusColor);
        } else {
            setLineColor(mLineColor);
        }
    }

    /**
     * 统一设置图片背景，点击效果用
     */
    public final void setImageBackground(int resId) {
        if (mLeftWidth > 0) {
            mIvLeftIcon.setBackgroundResource(resId);
        }
        mIvRightClearIcon.setBackgroundResource(resId);
        if (mRightWidth > 0) {
            mIvRightIcon.setBackgroundResource(resId);
        }
    }

    /**
     * 右边第一个按钮点击事件
     */
    public final void setOnRightClick(View.OnClickListener listener) {
        mIvRightIcon.setOnClickListener(listener);
    }

    /**
     * 右边第二个按钮点击事件
     */
    public final void setOnRightTwoClick(View.OnClickListener listener) {
        mIvRightTwoIcon.setOnClickListener(listener);
    }

    public ImageView getRightIcon() {
        return mIvRightIcon;
    }

    public ImageView getRightClearIcon() {
        return mIvRightClearIcon;
    }

    public ImageView getRightTwoIcon() {
        return mIvRightTwoIcon;
    }

    public EditText getEditContent() {
        return mEtContent;
    }

    public String getEditText() {
        return mEtContent.getText().toString().trim();
    }

    public final void setEditText(String text) {
        String editText = getEditText();
        if (editText.equals(text)) {
            return;
        }
        if (text != null) {
            mEtContent.setText(text);
        }
    }
}

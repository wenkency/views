package cn.carhouse.views.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Tab的容器，包含ItemView和底部跟踪的指示器
 *
 * @author 刘付文
 */
class XTabContainer extends FrameLayout {
    private final int mScreenWidth;
    /**
     * 1. 每个TAB固定宽
     */
    private int mItemWidth = 0;
    /**
     * 2. 每个Tab平分
     */
    private boolean mTabEqual = false;
    /**
     * 线平分
     */
    private boolean mLineEqual = false;
    /**
     * 3. 一屏幕显示的TAB个数
     */
    private int mTabCount = 0;
    private LinearLayout mLLTabContainer;
    // 记录每个条目的宽度
    private SparseArray<Integer> mItemsWidth;
    private View mLineView;
    private LayoutParams mLineLayoutParams;
    private int mLineWidth;
    private int mLineHeight;
    private int mTabLineBottomMargin = 0;

    public XTabContainer(@NonNull Context context) {
        this(context, null);
    }

    public XTabContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTabContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 添加容器
        mLLTabContainer = new LinearLayout(context);
        mLLTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLLTabContainer);
        mItemsWidth = new SparseArray<>();
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 添加ItemView
     */
    public void addItemView(int position, View itemView) {
        // 在这里计算Item的宽度
        mLLTabContainer.addView(itemView, position, getItemLayoutParams());
    }

    /**
     * 计算每个条目的宽度
     *
     * @return
     */
    private LinearLayout.LayoutParams getItemLayoutParams() {
        // 1. 如果指定了条目的宽度
        if (mItemWidth > 0) {
            return new LinearLayout.LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
        }
        if (mTabEqual) {
            return new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        }
        // 2. 如果指定了一屏条目个数
        if (mTabCount > 0) {
            XTabLayout parent = (XTabLayout) getParent();
            int width = mScreenWidth - parent.getPaddingLeft() - parent.getPaddingRight();
            return new LinearLayout.LayoutParams(width / mTabCount, LayoutParams.MATCH_PARENT);
        }
        return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 获取ItemView
     *
     * @param position
     * @return
     */
    public View getItemView(int position) {
        return mLLTabContainer.getChildAt(position);
    }

    /**
     * 移除所有的ItemView
     */
    public void removeAllItemViews() {
        mLLTabContainer.removeAllViews();
        if (mLineView != null) {
            removeView(mLineView);
            mLineView = null;
        }
    }

    public void setTabCount(int tabCount) {
        this.mTabCount = tabCount;
    }

    public void setItemWidth(int itemWidth) {
        this.mItemWidth = itemWidth;
    }

    public void setTabEqual(boolean tabEqual) {
        this.mTabEqual = tabEqual;
    }

    public void setLineEqual(boolean lineEqual) {
        this.mLineEqual = lineEqual;
    }

    public void addLineView(View lineView) {
        if (lineView == null) {
            return;
        }
        mLineView = lineView;
        // 添加线
        addView(lineView, getLineLayoutParams());
        // 设置线的偏移
        //updateLine(0, 0);
    }

    private LayoutParams getLineLayoutParams() {
        ViewGroup.LayoutParams layoutParams = mLineView.getLayoutParams();
        // 用户设置了宽高
        if (mLineWidth > 0 && mLineHeight > 0) {
            mLineLayoutParams = new LayoutParams(mLineWidth, mLineHeight);
        } else if (layoutParams != null) {
            // 设置线的LayoutParams
            mLineLayoutParams = new LayoutParams(layoutParams.width, layoutParams.height);
        } else {
            if (mTabCount > 0 && mLineEqual) {
                XTabLayout parent = (XTabLayout) getParent();
                int width = mScreenWidth - parent.getPaddingLeft() - parent.getPaddingRight();
                mLineLayoutParams = new LayoutParams(width / mTabCount, mLineHeight > 0 ? mLineHeight : dip2px(2));
            } else {
                // 默认线的宽高
                mLineLayoutParams = new LayoutParams(getItemWidth(0), mLineHeight > 0 ? mLineHeight : dip2px(2));
            }
        }
        mLineLayoutParams.gravity = Gravity.BOTTOM;
        mLineLayoutParams.bottomMargin = mTabLineBottomMargin;
        return mLineLayoutParams;
    }

    public void resetLineWidth(int position) {
        if (mLineView == null) {
            return;
        }
        View child = mLLTabContainer.getChildAt(position);
        if (child == null) {
            return;
        }
        int measuredWidth = getItemWidth(position);
        // 与条目一样宽
        if (mTabCount == 0 && mLineEqual) {
            mLineLayoutParams.width = measuredWidth
                    - child.getPaddingLeft()
                    - child.getPaddingRight();
        }

    }

    /**
     * 更新线的位置
     */
    public void updateLine(int position, int offset) {
        if (mLineView == null) {
            return;
        }
        View child = mLLTabContainer.getChildAt(position);
        if (child == null) {
            return;
        }
        int measuredWidth = getItemWidth(position);

        int leftMargin = child.getLeft() + offset + (measuredWidth - mLineLayoutParams.width) / 2;


        mLineLayoutParams.leftMargin = leftMargin;


        mLineView.setLayoutParams(mLineLayoutParams);
    }

    public int getItemWidth(int position) {
        if (getChildCount() <= 0) {
            return 0;
        }
        View child = mLLTabContainer.getChildAt(position);
        int measuredWidth = child.getMeasuredWidth();
        if (measuredWidth <= 0) {
            child.measure(0, 0);
            measuredWidth = child.getMeasuredWidth();
        }
        return measuredWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.mLineWidth = lineWidth;
    }

    public void setLineHeight(int lineHeight) {
        this.mLineHeight = lineHeight;
    }

    private int dip2px(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()) + 0.5);
    }

    public void setTabLineBottomMargin(int tabLineBottomMargin) {
        this.mTabLineBottomMargin = tabLineBottomMargin;
    }
}

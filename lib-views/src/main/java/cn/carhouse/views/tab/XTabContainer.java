package cn.carhouse.views.tab;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
    private LinearLayout mLLTabContainer;
    private View mLineView;
    /**
     * 一个条目的宽度
     */
    private int mItemWidth;
    private LayoutParams mTabLineParams;
    private int mInitLeftMargin;

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
    }

    public LinearLayout getLLTabContainer() {
        return mLLTabContainer;
    }

    /**
     * 添加ItemView
     *
     * @param itemView
     */
    public void addItemView(View itemView) {
        mLLTabContainer.addView(itemView);
    }

    /**
     * 添加ItemView
     *
     * @param itemView
     */
    public void addItemView(View itemView, LinearLayout.LayoutParams layoutParams) {
        mLLTabContainer.addView(itemView, layoutParams);
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
    }

    /**
     * 根据位置获取ItemView
     *
     * @param position
     * @return
     */
    public View getItemAt(int position) {
        return mLLTabContainer.getChildAt(position);
    }

    public void addTabBottomLineView(View lineView, int itemWidth) {
        this.mLineView = lineView;
        this.mItemWidth = itemWidth;
        if (mLineView == null) {
            return;
        }
        // 添加线
        addView(mLineView);
        // 设置线的的位置和宽度
        mTabLineParams = (LayoutParams) mLineView.getLayoutParams();
        mTabLineParams.gravity = Gravity.BOTTOM;

        int tabWidth = mTabLineParams.width;
        // 没有设置宽度
        if (mTabLineParams.width == ViewGroup.LayoutParams.WRAP_CONTENT
                || mTabLineParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            tabWidth = mItemWidth;
        }
        // 大于Item的宽度
        if (tabWidth > mItemWidth) {
            tabWidth = mItemWidth;
        }

        mTabLineParams.width = tabWidth;

        // 确保在最中间
        mInitLeftMargin = (mItemWidth - tabWidth) / 2;
        mTabLineParams.leftMargin = mInitLeftMargin;
    }

    private boolean isPositionOffset;

    /**
     * 滚动底部的指示器-->LeiftMargin来设置
     */
    public void scrollBottomLine(int position, float positionOffset) {
        isPositionOffset = true;
        if (mLineView == null) {
            return;
        }
        int leftMargin = (int) ((position + positionOffset) * mItemWidth + mInitLeftMargin);
        int maxMargin = (position + 1) * mItemWidth + mInitLeftMargin;
        mTabLineParams.leftMargin = Math.min(leftMargin, maxMargin);
        mLineView.setLayoutParams(mTabLineParams);
    }

    public void scrollBottomLine(int position) {
        if (isPositionOffset) {
            isPositionOffset = false;
            return;
        }
        if (mLineView == null) {
            return;
        }
        // 总共要移动的距离
        int totalMargin = position * mItemWidth + mInitLeftMargin;
        // 当前的距离
        int currentMargin = mTabLineParams.leftMargin;
        // 移动的距离
        int distance = totalMargin - currentMargin;

        // 用动画来执行移动
        ValueAnimator animator = ObjectAnimator.ofInt(currentMargin, totalMargin);
        animator.setDuration((long) Math.abs(distance * 0.2f));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTabLineParams.leftMargin = value;
                mLineView.setLayoutParams(mTabLineParams);
            }
        });
        animator.start();

    }
}

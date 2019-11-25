package cn.carhouse.views.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import cn.carhouse.views.R;


/**
 * 可以单独或者和ViewPager结合使用的View
 *
 * @author 刘付文
 */

public class XTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private boolean mTabScroll;
    /**
     * 一屏幕显示的TAB个数
     */
    private int mTabCount = 0;
    /**
     * 是否用全屏
     */
    private boolean isFullScreen = true;
    private boolean isLooper = false;

    private XTabContainer mTabContainer;

    private XTabAdapter mAdapter;
    private int mItemWidth;
    private ViewPager mViewPager;

    /**
     * 当前位置
     */
    private int mCurrentPosition = -1;
    /**
     * 解决抖动的问题
     */
    private boolean isScroll;
    /**
     * 点击Item的时候是否滚动ViewPager
     */
    private boolean smoothScroll = false;
    private int mScreenWidth;


    public XTabLayout(Context context) {
        this(context, null);
    }

    public XTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMotionEventSplittingEnabled(false);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTabLayout);
        mTabCount = array.getInt(R.styleable.XTabLayout_tabCount, mTabCount);
        mTabScroll = array.getBoolean(R.styleable.XTabLayout_tabIsScroll, true);
        isFullScreen = array.getBoolean(R.styleable.XTabLayout_tabIsFullScreen, true);
        array.recycle();
        mTabContainer = new XTabContainer(context);
        addView(mTabContainer);
    }

    public void setAdapter(XTabAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("XTabAdapter is null");
        }
        mAdapter = adapter;
        int count = mAdapter.getCount();
        mTabContainer.removeAllItemViews();
        for (int i = 0; i < count; i++) {
            View itemView = mAdapter.getView(i, mTabContainer);
            itemView.setFocusable(true);
            itemView.requestFocus();
            mTabContainer.addItemView(itemView);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getItemWidth(), LayoutParams.MATCH_PARENT);
            itemView.setLayoutParams(lp);
            setItemClick(itemView, i);
        }
        // 默认选中第一个位置
        setCurrentPosition(0);
    }

    /**
     * 获取TabItemView
     *
     * @param position 位置
     * @return
     */
    public View getTabItemView(int position) {
        return mTabContainer.getItemView(position);
    }

    public int getTabCount() {
        return mTabContainer.getChildCount();
    }


    private void setItemClick(final View itemView, final int position) {

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentPosition(position);
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 用户点击时，不断滚动到当前的位置
     */
    private void smoothScrollTab(int position) {
        if (!mTabScroll) {
            return;
        }
        // 总的滚动距离
        float totalScroll = (position) * mItemWidth;
        // 左边的偏移
        float offsetScroll = (getWidth() - mItemWidth) / 2;
        // 最终要滚的距离
        float finalScroll = totalScroll - offsetScroll;
        smoothScrollTo((int) finalScroll, 0);
    }


    /**
     * 关联ViewPager
     *
     * @param adapter
     * @param viewPager
     */
    public void setAdapter(XTabAdapter adapter, ViewPager viewPager) {
        if (viewPager == null) {
            throw new NullPointerException("viewPager is null");
        }
        this.mViewPager = viewPager;
        setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mAdapter == null) {
            return;
        }
        if (changed) {
            // 单个Item的宽度
            mItemWidth = getItemWidth();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                try {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
                    mTabContainer.getItemAt(i).setLayoutParams(layoutParams);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            // 如果没有指定个数，就取最宽的
            // 如果所有宽度不够一屏，就显示成一屏
            // 添加底部的指示器
            mTabContainer.addTabBottomLineView(mAdapter.getTabBottomLineView(mTabContainer), mItemWidth);
        }
    }

    public int getItemWidth() {
        // 整个布局的宽度
        int width = getMeasuredWidth();
        if (width <= 0) {
            width = mScreenWidth;
        }
        // 如果指定了
        if (mTabCount > 0) {
            return width / mTabCount;
        }
        // 没有指定
        int itemWidth;
        // 取最宽的
        int maxWidth = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View child = mTabContainer.getItemAt(i);
            if (child == null) {
                continue;
            }
            int currentWidth = child.getMeasuredWidth();
            if (currentWidth <= 0) {
                child.measure(0, 0);
                currentWidth = child.getMeasuredWidth();
            }
            // 取条目最大的宽度
            maxWidth = Math.max(currentWidth, maxWidth);
        }
        itemWidth = maxWidth;
        if (isFullScreen) {
            // 如果所有宽度不够一屏
            if (maxWidth * mAdapter.getCount() < width) {
                itemWidth = width / mAdapter.getCount();
            }
        }

        return itemWidth;
    }


    /**
     * 滚动底部的指示器
     */
    private void scrollBottomLine(int position, float positionOffset) {
        if (isScroll) {
            if (!mTabScroll) {
                return;
            }
        }
        mTabContainer.scrollBottomLine(position, positionOffset);
    }

    /**
     * 划动时，不断滚动当前的位置
     */
    private void scrollTab(int position, float positionOffset) {
        if (isScroll) {
            if (!mTabScroll) {
                return;
            }
        }
        // 总的滚动距离
        float totalScroll = (position + positionOffset) * mItemWidth;
        // 左边的偏移
        float offsetScroll = (getWidth() - mItemWidth) / 2;
        // 最终要滚的距离
        float finalScroll = totalScroll - offsetScroll;
        scrollTo((int) finalScroll, 0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 划动ViewPager就执行，用户点击ItemView不执行
        if (isScroll) {
            if (!mTabScroll) {
                return;
            }
            // 不断滚动当前的指示器
            scrollTab(position, positionOffset);
            // 滚动底部的指示器
            scrollBottomLine(position, positionOffset);
        }
    }


    /**
     * 设置当前的位置
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (position < 0) {
            return;
        }
        if (mCurrentPosition != position && position >= 0) {
            // 设置选中
            setItemSelected(position);
            // 用户ItemView点击时，不断滚动到当前的位置
            smoothScrollTab(position);
        }
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(position, getTabItemView(position));
        }
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, smoothScroll);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (isLooper && mAdapter != null && mAdapter.getCount() > 0) {
            position = position % mAdapter.getCount();
        }
        setItemSelected(position);
    }

    /**
     * 设置Item选中
     */
    public void setItemSelected(int position) {
        // 重置上一个位置
        if (mCurrentPosition >= 0) {
            mAdapter.onTabReset(mTabContainer.getItemAt(mCurrentPosition), mCurrentPosition);
        }
        mCurrentPosition = position;
        if (mCurrentPosition >= 0) {
            // 选中当前位置
            mAdapter.onTabSelected(mTabContainer.getItemAt(mCurrentPosition), mCurrentPosition);
        }
        // 用户点击时，滚动底部的指示器
        mTabContainer.scrollBottomLine(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 用户点击ItemView状态不会有 1
        //isScroll=state==1;
        // 点击：2--->0
        // 划动：1-->2-->0
        if (state == 1) {
            isScroll = true;
        }
        if (state == 0) {
            isScroll = false;
        }
    }

    /**
     * 点击Item的时候是否滚动ViewPager
     *
     * @param smoothScroll 切换Item时设置是否滚动
     */
    public void setViewPagerSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }

    public void setLooper(boolean looper) {
        isLooper = looper;
    }

    public XTabAdapter getAdapter() {
        return mAdapter;
    }
}

package cn.carhouse.views.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.viewpager.widget.ViewPager;

import cn.carhouse.views.R;
import cn.carhouse.views.adapter.XBaseAdapter;


/**
 * 可以单独或者和ViewPager结合使用的View
 */

public class XTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {


    // Tab是否可以和ViewPager联动
    private boolean mTabScroll;
    private XTabContainer mTabContainer;
    private XBaseAdapter mAdapter;
    private ViewPager mViewPager;
    // 防止手动点击Tab时滚动抖动
    private boolean isScroll;
    private DataSetObserver mObserver;
    private boolean isRegister = false;
    private int mPosition = -1;

    public XTabLayout(Context context) {
        this(context, null);
    }

    public XTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);// 设置滚动视图是否可以伸缩其内容以填充视口
        setMotionEventSplittingEnabled(false);
        // 初始化属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTabLayout);
        int tabCount = array.getInt(R.styleable.XTabLayout_tabCount, 0);
        int itemWidth = (int) array.getDimension(R.styleable.XTabLayout_tabWidth, 0f);
        mTabScroll = array.getBoolean(R.styleable.XTabLayout_tabIsScroll, true);
        boolean tabEqual = array.getBoolean(R.styleable.XTabLayout_tabEqual, false);
        int lineWidth = (int) array.getDimension(R.styleable.XTabLayout_tabLineWidth, 0f);
        int lineHeight = (int) array.getDimension(R.styleable.XTabLayout_tabLineHeight, 0f);
        int tabLineBottomMargin = (int) array.getDimension(R.styleable.XTabLayout_tabLineBottomMargin, 0f);
        array.recycle();

        // 设置配置
        mTabContainer = new XTabContainer(context);
        mTabContainer.setTabCount(tabCount);
        mTabContainer.setItemWidth(itemWidth);
        mTabContainer.setTabEqual(tabEqual);
        mTabContainer.setLineWidth(lineWidth);
        mTabContainer.setLineHeight(lineHeight);
        mTabContainer.setTabLineBottomMargin(tabLineBottomMargin);
        addView(mTabContainer);
    }

    public void setAdapter(XBaseAdapter adapter) {
        setAdapter(adapter, null);
    }

    public void setAdapter(XBaseAdapter adapter, ViewPager viewPager) {
        // 移除监听
        unRegisterAdapter();
        if (adapter == null) {
            throw new NullPointerException("XTabAdapter is null");
        }
        mAdapter = adapter;
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
        }

        // 注册监听
        mObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                XTabLayout.this.notifyDataSetChanged();
            }
        };
        registerAdapter();

        XTabLayout.this.notifyDataSetChanged();

    }

    @Override
    protected void onDetachedFromWindow() {
        unRegisterAdapter();
        super.onDetachedFromWindow();
    }

    private void unRegisterAdapter() {
        // 移除监听
        if (mAdapter != null && mObserver != null && isRegister) {
            isRegister = false;
            mAdapter.unregisterDataSetObserver(mObserver);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerAdapter();
    }

    private void registerAdapter() {
        // 添加监听
        if (mAdapter != null && mObserver != null && !isRegister) {
            mAdapter.registerDataSetObserver(mObserver);
            isRegister = true;
        }
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        if (mAdapter == null) {
            return;
        }
        int count = mAdapter.getCount();
        mTabContainer.removeAllItemViews();
        for (int i = 0; i < count; i++) {
            View itemView = mAdapter.getView(i, mTabContainer);
            final int position = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(position, mTabScroll);
                    } else {
                        onPageSelected(position);
                    }
                }
            });
            itemView.setFocusable(true);
            itemView.requestFocus();
            mTabContainer.addItemView(i, itemView);
        }
        View lineView = mAdapter.getTabBottomLineView(mTabContainer);
        if (lineView != null) {
            mTabContainer.addLineView(lineView);
        }

        // 默认选中第一个位置
        this.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(0);
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isScroll) {
            float totalScroll = (positionOffset) * mTabContainer.getItemWidth(position);
            mTabContainer.updateLine(position, (int) totalScroll);
        }
    }

    @Override
    public void onPageSelected(int position) {

        if (mPosition == position) {
            return;
        }
        isScroll = false;
        // 1. 重置
        if (mPosition >= 0) {
            mAdapter.onTabReset(mTabContainer.getItemView(mPosition), mPosition);
        }
        mPosition = position;
        // 2. 重置选中
        if (mPosition >= 0) {
            mAdapter.onTabSelected(mTabContainer.getItemView(mPosition), mPosition);
        }
        mTabContainer.updateLine(position, 0);
        smoothScrollTab(position);
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
     * 用户点击时，不断滚动到当前的位置
     */
    private void smoothScrollTab(int position) {
        // 总的滚动距离
        float totalScroll = (position) * mTabContainer.getItemWidth(position);
        // 左边的偏移
        float offsetScroll = (getWidth() - mTabContainer.getItemWidth(position)) / 2;
        // 最终要滚的距离
        float finalScroll = totalScroll - offsetScroll;
        smoothScrollTo((int) finalScroll, 0);
    }

    public void setTabCount(int tabCount) {
        mTabContainer.setTabCount(tabCount);
    }

    public void setItemWidth(int itemWidth) {
        mTabContainer.setItemWidth(itemWidth);
    }

    public void setTabEqual(boolean tabEqual) {
        mTabContainer.setTabEqual(tabEqual);
    }

    public void setLineWidth(int lineWidth) {
        mTabContainer.setLineWidth(lineWidth);
    }

    public void setLineHeight(int lineHeight) {
        mTabContainer.setLineHeight(lineHeight);
    }

    public void setTabLineBottomMargin(int tabLineBottomMargin) {
        mTabContainer.setTabLineBottomMargin(tabLineBottomMargin);
    }


}

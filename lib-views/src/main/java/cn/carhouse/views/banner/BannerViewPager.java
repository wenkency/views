package cn.carhouse.views.banner;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;


/**
 * 轮播图的ViewPager
 */

public class BannerViewPager extends ViewPager implements View.OnTouchListener {
    private BannerPagerAdapter mAdapter;
    private long mRollTime = 3500;// 轮播的时间
    private boolean isLoop = true;
    private BannerScroller mBannerScroller;
    private int downX;
    private Runnable mTask = null;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            // 选用反射来改变滚动的速度
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mBannerScroller = new BannerScroller(context, new LinearInterpolator());
            mScroller.setAccessible(true);
            mScroller.set(this, mBannerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTask = new Runnable() {
            @Override
            public void run() {
                // 切换到下一页
                int mCurrentItem = getCurrentItem() + 1;
                if (getAdapter() != null && mCurrentItem == getAdapter().getCount() - 1) {
                    mCurrentItem = 0;
                    setCurrentItem(mCurrentItem, false);
                } else {
                    setCurrentItem(mCurrentItem);
                }
                // 不断的轮播
                startRoll();
            }
        };

        // 设置触摸监听
        setOnTouchListener(this);
    }


    /**
     * 设置Banner的adapter
     *
     * @param adapter
     */
    public void setAdapter(BannerPagerAdapter adapter) {
        mAdapter = adapter;
        // 调用父类的
        super.setAdapter(adapter);
    }

    /**
     * 开启自动轮播
     */
    public void startRoll() {
        if (mTask != null) {
            // 清除消息
            removeCallbacks(mTask);
            if (isLoop) {
                postDelayed(mTask, mRollTime);
            }
        }

    }

    public void stopRoll() {
        if (mTask != null) {
            removeCallbacks(mTask);
        }
    }

    /**
     * 处理一些错误
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Throwable e) {
        }
        return false;
    }

    int moveX;

    /**
     * 兼容低版本
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopRoll();
                downX = (int) ev.getX();
                moveX = (int) ev.getX();
                //当前viewpager对应的夫控件不能去拦截事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getX();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startRoll();
                int diff = moveX - downX;
                if (!isLoop && diff < -100 && mAdapter != null && getCurrentItem() == mAdapter.getData().size() - 1) {
                    //夫控件拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopRoll();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 清除消息
                startRoll();
                break;
        }
        return false;
    }

    /**
     * 设置滚动的时间
     */
    public void setRollTime(long rollTime) {
        this.mRollTime = rollTime;
    }

    /**
     * 设置是否轮播
     *
     * @param isLoop
     */
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        if (mAdapter != null) {
            mAdapter.setLooper(isLoop);
        }
        if (!isLoop) {
            stopRoll();
        }
    }

    /**
     * ViewPager划动速度时间
     */
    public void setDuration(int duration) {
        if (mBannerScroller != null) {
            mBannerScroller.setDuration(duration);
        }
    }


}

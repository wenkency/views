package cn.carhouse.views.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * ViewPager滚动的时间控制
 */

public class BannerScroller extends Scroller {

    private int mDuration = 300;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * ViewPager划动速度时间
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}

package cn.carhouse.views.banner;


import java.util.List;

import cn.carhouse.adapter.XQuickPagerAdapter;


/**
 * BannerView的Adapter
 */
public abstract class BannerPagerAdapter<T> extends XQuickPagerAdapter<T> {
    public BannerPagerAdapter(List<T> data, int layoutId, boolean isLopper) {
        super(data, layoutId, isLopper);
    }

    public BannerPagerAdapter(List<T> data, int layoutId, boolean isLopper, boolean isCache) {
        super(data, layoutId, isLopper, isCache);
    }

    public BannerPagerAdapter(List<T> data, int layoutId) {
        super(data, layoutId);
    }

    public BannerPagerAdapter(int layoutId) {
        super(null, layoutId);
    }

    public void setLooper(boolean isLooper) {
        this.isLooper = isLooper;
    }

    public int getDataSize() {
        return getData().size();
    }

    public boolean isLooper() {
        return this.isLooper;
    }

    public String getBannerDesc(int position) {
        return null;
    }
}

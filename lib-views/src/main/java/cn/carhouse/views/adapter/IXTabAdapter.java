package cn.carhouse.views.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface IXTabAdapter<V extends View> {
    /**
     * 选中Tab回调
     */
    void onTabSelected(V view, int position);

    /**
     * 重置Tab回调
     */
    void onTabReset(V view, int position);

    /**
     * 添加底部的跟踪指示器
     */
    View getTabBottomLineView(ViewGroup parent);
}

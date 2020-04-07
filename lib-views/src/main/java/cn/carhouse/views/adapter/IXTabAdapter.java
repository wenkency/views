package cn.carhouse.views.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface IXTabAdapter<V extends View> {
    /**
     * Tab选中
     */
    void onTabSelected(V view, int position);

    /**
     * 要重置的Tab
     */
    void onTabReset(V view, int position);

    /**
     * 添加底部的跟踪指示器
     *
     * @return
     */
    View getTabBottomLineView(ViewGroup parent);
}

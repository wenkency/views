package cn.carhouse.views.tab;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

/**
 * TabLayout 的ada0pter
 *
 * @author 刘付文
 */

public abstract class XTabAdapter<V extends View> {
    /**
     * 获取Tab的数量
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获取一个TAb
     *
     * @param position 位置
     * @param parent   Tab的父类
     * @return 一个Tab
     */
    public abstract View getView(int position, ViewGroup parent);

    /**
     * Tab选中
     */
    public void onTabSelected(V view, int position) {

    }

    /**
     * 要重置的Tab
     */
    public void onTabReset(V view, int position) {

    }

    /**
     * 添加底部的跟踪指示器
     *
     * @return
     */
    public View getTabBottomLineView(ViewGroup parent) {
        return null;
    }

}

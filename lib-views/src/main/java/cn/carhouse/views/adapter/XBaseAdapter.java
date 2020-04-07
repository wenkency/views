package cn.carhouse.views.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * 适配器
 */

public abstract class XBaseAdapter implements IXTabAdapter<View> {
    private DataSetObservable mObservable = new DataSetObservable();

    /**
     * 数量
     */
    public abstract int getCount();

    /**
     * 条目的布局
     */
    public abstract View getView(int position, ViewGroup parent);

    /**
     * 注册数据监听
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        try {
            mObservable.registerObserver(observer);
        } catch (Throwable e) {
        }
    }

    /**
     * 移除数据监听
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        try {

            mObservable.unregisterObserver(observer);
        } catch (Throwable e) {
        }
    }


    /**
     * 内容改变
     */
    public void notifyDataSetChanged() {
        mObservable.notifyChanged();
    }


    // =========XTabLayout专用=======================================================

    /**
     * XTabLayout专用
     *
     * @param view
     * @param position
     */
    @Override
    public  void onTabReset(View view, int position) {

    }

    /**
     * XTabLayout专用
     *
     * @param view
     * @param position
     */
    @Override
    public  void onTabSelected(View view, int position) {

    }

    /**
     * XTabLayout专用
     */
    @Override
    public View getTabBottomLineView(ViewGroup parent) {
        return null;
    }
    // =========XTabLayout专用=======================================================
}

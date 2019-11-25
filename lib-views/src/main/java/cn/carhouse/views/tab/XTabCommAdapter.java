package cn.carhouse.views.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.views.adapter.XViewHolder;

/**
 * TAB公用的Adapter
 *
 * @param <T>
 * @param <V>
 */
public abstract class XTabCommAdapter<T, V extends View> extends XTabAdapter<V> {
    protected int mLayoutId;
    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public XTabCommAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    public XTabCommAdapter(Context context, int layoutId) {
        this(context, null, layoutId);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        XViewHolder xHolder = new XViewHolder(view);
        convert(xHolder, mData.get(position), position);
        return view;
    }

    public abstract void convert(XViewHolder holder, T item, int position);

    @Override
    public final void onTabReset(V view, int position) {
        if (view.getTag() == null) {
            return;
        }
        XViewHolder holder = (XViewHolder) view.getTag();
        convertTabReset(holder, mData.get(position), position);
    }

    @Override
    public final void onTabSelected(V view, int position) {
        if (view.getTag() == null) {
            return;
        }
        XViewHolder holder = (XViewHolder) view.getTag();
        convertTabSelected(holder, mData.get(position), position);
    }


    public void convertTabSelected(XViewHolder holder, T item, int position) {

    }

    public void convertTabReset(XViewHolder holder, T item, int position) {

    }

//    @Override
//    public View getTabBottomLineView(ViewGroup parent) {
//        View view = new View(parent.getContext());
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
//        view.setBackgroundColor(Color.RED);
//        return view;
//    }
}

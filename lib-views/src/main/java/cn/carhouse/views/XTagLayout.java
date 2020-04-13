package cn.carhouse.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.views.adapter.XBaseAdapter;


/**
 * 流式布局
 */

public class XTagLayout extends ViewGroup {
    private XBaseAdapter mAdapter;
    private DataSetObserver mObserver;
    private boolean isRegister = false;
    private List<Rect> mChildBounds = new ArrayList<>();
    public XTagLayout(Context context) {
        this(context, null);
    }

    public XTagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //  宽高使用了的长度
        int widthUsed = getPaddingLeft();
        int heightUsed = 0;
        // 宽高
        int width = 0;
        int height = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // 1. 测量
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);

            // 加上Margin值
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 换行判断
            int lineUsed = widthUsed + child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + getPaddingRight();
            if (lineUsed > widthSize) {
                widthUsed = getPaddingLeft();
                // 累加高度
                height += heightUsed;
                heightUsed = 0;
                // 重新测量
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }
            // 记录子View的位置信息
            Rect childBounds;
            if (mChildBounds.size() <= i) {
                childBounds = new Rect();
                mChildBounds.add(childBounds);
            } else {
                childBounds = mChildBounds.get(i);
            }

            childBounds.left = widthUsed + lp.leftMargin;
            childBounds.right = widthUsed + child.getMeasuredWidth() + lp.rightMargin;
            childBounds.top = height + lp.topMargin;
            childBounds.bottom = height + child.getMeasuredHeight() + lp.bottomMargin;


            widthUsed += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            heightUsed = Math.max(heightUsed, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            // 宽取最长的
            width = Math.max(width, widthUsed);
        }
        // 换行后，最后一行要加上
        height += heightUsed + getPaddingBottom();
        width += getPaddingRight();
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 布局的摆放
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect bounds = mChildBounds.get(i);
            child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setAdapter(XBaseAdapter adapter) {
        // 移除监听
        unRegisterAdapter();
        if (adapter == null) {
            throw new NullPointerException("FlowBaseAdapter is null");
        }
        mAdapter = adapter;
        mObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }
        };
        // 注册监听
        registerAdapter();
        // 添加布局
        notifyDataSetChanged();
    }

    /**
     * 重新添加布局
     */
    private void notifyDataSetChanged() {
        if (mAdapter == null) {
            return;
        }
        removeAllViews();
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, this);
            addView(view);
        }
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

    public XBaseAdapter getAdapter() {
        return mAdapter;
    }

}

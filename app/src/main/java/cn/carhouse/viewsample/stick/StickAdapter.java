package cn.carhouse.viewsample.stick;

import android.app.Activity;
import android.view.View;

import cn.carhouse.adapter.XQuickAdapter;
import cn.carhouse.adapter.XQuickSupport;
import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.views.stick.IStick;
import cn.carhouse.viewsample.R;

/**
 * 1. 简单的多条目
 * 2. 有悬浮粘附的View
 */
public class StickAdapter extends XQuickAdapter<StickBean> implements IStick {
    private OnTabItemClick onTabItemClick;
    // 多条目支持 ，简单的写法
    XQuickSupport<StickBean> support = new XQuickSupport<StickBean>() {
        @Override
        public int getViewTypeCount() {
            // 这个是随便写，只要大于实际条目就行
            return 100;
        }

        @Override
        public int getLayoutId(StickBean item, int position) {
            int viewType = item.getItemViewType();
            switch (viewType) {
                case 1:
                    return R.layout.item_type1;
                case 2:
                    return R.layout.item_type2;
                case 3:
                    return R.layout.item_type3;
                case 4:
                    return R.layout.item_type4;
            }
            return R.layout.item_empty;
        }

        @Override
        public int getItemViewType(StickBean item, int position) {
            // 条目类型
            return item.getItemViewType();
        }

        @Override
        public boolean isSpan(StickBean item) {
            return true;
        }

        @Override
        public int getSpanSize(StickBean item, int position) {
            if (item.getItemViewType() == 4) {
                return 1;
            }
            return 3;
        }
    };

    public StickAdapter(Activity context) {
        super(context);
        setMultiSupport(support);
    }

    @Override
    protected void convert(XQuickViewHolder holder, StickBean item, int position) {
        // 根据ViewType做相应的操作
        switch (item.getItemViewType()) {
            case 3:
                holder.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onTabItemClick != null) {
                            onTabItemClick.onTabItemClick(v, position);
                        }
                    }
                });
                break;
        }
    }

    // 悬浮的View的布局位置
    @Override
    public int getStickPosition() {
        // 假设这个是粘附在顶的View，位置是4 position 是3
        return 3;
    }

    // =====悬浮的View的布局类型
    @Override
    public int getStickViewType() {
        return getItemViewType(getStickPosition());
    }

    public interface OnTabItemClick {
        void onTabItemClick(View view, int position);
    }

    // 点击事件
    public void setOnTabItemClick(OnTabItemClick onTabItemClick) {
        this.onTabItemClick = onTabItemClick;
    }
}

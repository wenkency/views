package cn.carhouse.viewsample.stick;

/**
 * 模拟多条目的Bean
 */
public class StickBean implements IViewType {
    private int viewType = -1;

    public StickBean() {
    }

    public StickBean(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

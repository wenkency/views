package cn.carhouse.viewsample.stick;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.viewsample.R;

/**
 * 1. 更新 implementation "com.github.wenkency:quickadapter:2.0.0"
 * 2. 更新 StickFrameLayout 这个类
 * 3. 还可以参考连接：https://github.com/wenkency/CommAdapter 多条目的写法
 * 4. 多条目博客：https://www.jianshu.com/p/548f556c63f5
 */
public class StickActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private StickAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        initViews();
        initNet();
    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        // 创建Adapter
        mAdapter = new StickAdapter(this);
        // 设置粘附的ViewID，要和StickFrameLayout里面的那个ID一样
        mAdapter.setStickViewId(R.id.stick_view_position);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // 模拟Tab条目点击事件
        mAdapter.setOnTabItemClick(new StickAdapter.OnTabItemClick() {
            @Override
            public void onTabItemClick(View view, int position) {
                // 2. 滚动到指定位置
                mLayoutManager.scrollToPositionWithOffset(position, 0);
            }
        });


    }

    private void initNet() {
        // 1. 组装一些数据
        List<StickBean> data = new ArrayList<>();
        data.add(new StickBean(1));
        data.add(new StickBean(2));
        data.add(new StickBean(1));
        // 假设这个是粘附在顶的View，位置是4 position 是3
        // 对应Adapter里面getStickPosition 设置为3
        data.add(new StickBean(3));
        for (int i = 0; i < 100; i++) {
            data.add(new StickBean(4));
        }
        // 加载完数据后
        mAdapter.replaceAll(data);
    }
}

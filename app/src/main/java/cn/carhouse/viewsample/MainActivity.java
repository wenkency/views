package cn.carhouse.viewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.adapter.XQuickPagerAdapter;
import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.views.adapter.XCommAdapter;
import cn.carhouse.views.adapter.XViewHolder;
import cn.carhouse.views.tab.XTabLayout;

public class MainActivity extends AppCompatActivity {

    private XCommAdapter<String> tabCommAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 底部的Tab
        XTabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("");
        }
        viewPager.setAdapter(new XQuickPagerAdapter<String>(data, R.layout.item_view_pager, false) {
            @Override
            protected void convert(XQuickViewHolder holder, String data, int position) {
                holder.setText(R.id.tv, "ViewPager" + position);
            }
        });

        tabCommAdapter = new XCommAdapter<String>(this, data, R.layout.item_tab_layout) {
            @Override
            public void convert(XViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, "TabTab" + position);
                String url = "https://img.car-house.cn/Upload/activity/20200226/TZ5izRtnAzWGHSYKWeBBx6yhPFR62aX2.png";
                if (position % 3 == 1) {
                    url = "https://img.car-house.cn/Upload/activity/20200219/EEAmKbASEh3T3ECXFwT8D2e8m56BGhBe.jpg";
                }
                if (position % 3 == 2) {
                    url = "https://img.car-house.cn/Upload/activity/20200304/sKFHyzZwTPBMzbjGyFTzMKsyYQEAZF6h.jpg";
                }
                holder.displayImage(R.id.iv_icon, url);
            }

            @Override
            public void convertTabReset(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, false);
            }

            @Override
            public void convertTabSelected(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, true);
            }

            @Override
            public View getTabBottomLineView(ViewGroup parent) {
                View view = new View(parent.getContext());
                view.setBackgroundColor(Color.RED);
                return view;
            }
        };
        // 设置底部线宽高和距离底部高度
        tabLayout.setLineWidth(100);
        tabLayout.setLineHeight(10);
        tabLayout.setTabLineBottomMargin(5);
        // 设置一屏显示5个
        tabLayout.setTabCount(5);
        // 设置点击回调
        tabLayout.setOnItemClickListener(new XTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View item) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        // 设置默认位置为2下标，也就是3
        tabLayout.setAdapter(tabCommAdapter, viewPager, 2);

    }
}

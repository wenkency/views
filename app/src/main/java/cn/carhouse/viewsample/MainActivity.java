package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.adapter.XQuickPagerAdapter;
import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.views.adapter.XViewHolder;
import cn.carhouse.views.tab.XTabCommAdapter;
import cn.carhouse.views.tab.XTabLayout;

public class MainActivity extends AppCompatActivity {

    private XTabCommAdapter<String, View> tabCommAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        tabCommAdapter = new XTabCommAdapter<String, View>(this, data, R.layout.item_tab_layout) {
            @Override
            public void convert(XViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, "Tab" + position);
            }

            @Override
            public void convertTabReset(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, false);
            }

            @Override
            public void convertTabSelected(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, true);
            }
        };
        tabLayout.setAdapter(tabCommAdapter, viewPager);
        tabLayout.setViewPagerSmoothScroll(true);


    }
}

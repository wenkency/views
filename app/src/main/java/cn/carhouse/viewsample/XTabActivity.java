package cn.carhouse.viewsample;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.printservice.PrintService;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

public class XTabActivity extends AppCompatActivity {

    private XCommAdapter<String> tabCommAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 底部的Tab
        XTabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        final List<String> data = new ArrayList<>();
        data.add("abc");
        data.add("abdedec");
        data.add("abdddddddc");
        data.add("ab");
        data.add("abasdfasdc");
        data.add("abcfff");
        data.add("abcasdfasdfas");
        viewPager.setAdapter(new XQuickPagerAdapter<String>(data, R.layout.item_view_pager, false) {
            @Override
            protected void convert(XQuickViewHolder holder, String data, int position) {
                holder.setText(R.id.tv, "ViewPager " + data + position);

            }
        });

        tabCommAdapter = new XCommAdapter<String>(this, data, R.layout.item_tab_layout) {
            @Override
            public void convert(XViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, item);
                String url = "http://lmg.jj20.com/up/allimg/tp05/19100209401614Q-0-lp.jpg";
                if (position % 3 == 1) {
                    url = "http://lmg.jj20.com/up/allimg/tp01/1ZZQ20QJS6-0-lp.jpg";
                }
                if (position % 3 == 2) {
                    url = "https://img4.orsoon.com:8901/pic/201912/09110428_45fbbfb018.png";
                }
                if (position % 3 == 1){
                    holder.displayCircleImage(R.id.iv_icon, url);
                }else {
                    holder.displayImage(R.id.iv_icon, url);
                }

            }

            @Override
            public void convertTabReset(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, false);
                TextView tv=holder.getView(R.id.tv);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16f);
            }

            @Override
            public void convertTabSelected(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, true);
                TextView tv=holder.getView(R.id.tv);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f);
            }

            @Override
            public View getTabBottomLineView(ViewGroup parent) {

                /*// 这里可以创建自已的View
                ImageView view = new ImageView(parent.getContext());
                // 这里可以设置自己的宽高：动态宽高（适配屏幕的）
                view.setLayoutParams(new ViewGroup.LayoutParams(80, 80));
                // 设置图片
                view.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher));*/

                View view = new View(parent.getContext());
                view.setBackgroundColor(Color.parseColor("#ff0000"));

                return view;
            }
        };
        // 设置底部线宽高和距离底部高度
        tabLayout.setTabLineBottomMargin(4);
        // 设置一屏显示5个
        // 设置点击回调
        tabLayout.setOnItemClickListener(new XTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View item) {
                //Toast.makeText(XTabActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        // 设置默认位置为2下标，也就是3
        tabLayout.setAdapter(tabCommAdapter, viewPager, 0);

    }
}

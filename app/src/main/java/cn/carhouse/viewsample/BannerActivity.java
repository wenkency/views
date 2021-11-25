package cn.carhouse.viewsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.imageloader.ImageLoaderFactory;
import cn.carhouse.views.banner.BannerPagerAdapter;
import cn.carhouse.views.banner.BannerView;

public class BannerActivity extends AppCompatActivity {
    // 轮播图片
    private BannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mBannerView = findViewById(R.id.banner_view);
        List<String> images = new ArrayList<>();
        images.add("http://lmg.jj20.com/up/allimg/tp05/19100209401614Q-0-lp.jpg");
        images.add("http://lmg.jj20.com/up/allimg/tp01/1ZZQ20QJS6-0-lp.jpg");
        images.add("https://img4.orsoon.com:8901/pic/201912/09110428_45fbbfb018.png");
        images.add("https://www.2008php.com/2011_Website_appreciate/2011-03-05/20110305173219.jpg");
        BannerPagerAdapter<String> adapter = new BannerPagerAdapter<String>(images, R.layout.item_banner) {
            @Override
            protected void convert(XQuickViewHolder holder, String imageUrl, int position) {
                View view = holder.getView(R.id.iv_icon);
                holder.displayImage(R.id.iv_icon, imageUrl, view.getWidth(), view.getHeight());
            }
        };
        mBannerView.setAdapter(adapter);

    }
}
